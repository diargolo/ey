package com.ey.desafiotecnico.service;

import com.ey.desafiotecnico.dto.PhoneDto;
import com.ey.desafiotecnico.dto.ResponseCreateUserDto;
import com.ey.desafiotecnico.dto.UserDto;
import com.ey.desafiotecnico.dto.UserTable;
import com.ey.desafiotecnico.exception.UserException;
import com.ey.desafiotecnico.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class UserServiceFacade {

    public static final String MSG_DOMINIO_CL = "Email en formato o dominio incorrecto. [ej: aaaaaaa@dominio.cl]";
    public static final String MSG_PW = "Contraseña en formato incorrecto. [ej: Minimo una letra mayuscula, varias letras minúsculas, y minimo dos numeros]";
    public static final String MSG_EMAIL_EXISTE = "El correo ya registrado";
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceFacade.class);

    @Autowired
    private Tool tool;

    @Autowired
    private DBService dbService;

    public boolean createTables() {

        LOGGER.info("Starting creating Tables");
        return dbService.createTables();

    }

    public ArrayList<UserTable> listUsers() {

        LOGGER.info("Starting looking for users in db");

        return dbService.listUsers();

    }

    public UserTable getUserById(String id) {

        LOGGER.info("Starting looking for user in db");

        UserTable user = dbService.getUserById(id);

        if(!Objects.isNull(user)) {
            user.setPhones(dbService.getPhonesByUserId(id));
        } else {
            throw new UserException("Usuario no existe");
        }

        return user;

    }

    public Boolean deleteUserById(String id) {

        LOGGER.info("Starting looking for user in db");

        dbService.deletePhonesByUserId(id);
        dbService.deleteUserById(id);

        return true;
    }

    public ResponseCreateUserDto createUser(UserDto request) {

        LOGGER.info("Starting createUser");

        runValidations(request.getEmail(), request.getPassword());
        runDBValidations(request);

        ResponseCreateUserDto responseCreateUser = buildResponse(request);
        UserTable user = tool.fill(request, responseCreateUser);

        dbService.createUser(user);
        this.addPhones(user.getId(), request.getPhones());

        return responseCreateUser;
    }

    public boolean updateUser(UserTable user) {

        LOGGER.info("Starting update User");

        this.runValidations(user.getEmail(), user.getPassword());
        this.isEmailInUse(user.getId(), user.getEmail());

        dbService.updateUser(user);
        dbService.deletePhonesByUserId(user.getId());
        this.addPhones(user.getId(), user.getPhones());

        return true;
    }

    private void addPhones(String idUser,  ArrayList<PhoneDto> phones) {

        for (PhoneDto phone: phones) {

            phone.setId(tool.generateUUID());
            phone.setIdUser(idUser);

            dbService.createPhone(phone);
        }

    }

    private void runValidations (String email, String password) {

        if(!tool.validateEmail(email)) {
            throw new UserException(MSG_DOMINIO_CL);
        }

        if(!tool.validatePW(password)) {
            throw new UserException(MSG_PW);
        }
    }

    private void runDBValidations (UserDto user) {

        if(dbService.existEmail(user.getEmail())) {
            throw new UserException(MSG_EMAIL_EXISTE);
        }

    }

    private boolean isEmailInUse(String id, String email) {

        UserTable userTable = this.dbService.loadUserByEmail(email);

        if (Objects.isNull(userTable)) {
            // no existe email
            return true;
        } else if (id.equals(userTable.getId())) {
            // pertenece al usuario que se quiere modificar
            return true;
        } else {
            throw new UserException(MSG_EMAIL_EXISTE);
        }
    }

    private ResponseCreateUserDto buildResponse(UserDto request) {
        String operationDate = tool.getDate();
        ResponseCreateUserDto responseCreateUser = new ResponseCreateUserDto();
        responseCreateUser.setId(tool.generateUUID());
        responseCreateUser.setCreated(operationDate);
        responseCreateUser.setModified(operationDate);
        responseCreateUser.setLastLogin(operationDate);
        responseCreateUser.setToken(tool.createJWT(request.getEmail()));
        responseCreateUser.setIsActive("true");
        return responseCreateUser;

    }
}
