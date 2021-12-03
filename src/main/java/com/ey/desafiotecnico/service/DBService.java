package com.ey.desafiotecnico.service;

import com.ey.desafiotecnico.dto.PhoneDto;
import com.ey.desafiotecnico.dto.UserTable;
import com.ey.desafiotecnico.exception.UserException;
import com.ey.desafiotecnico.repository.HSQLDBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class DBService {

    public static final String SQL_CREATE_USER = "CREATE TABLE user (\n" +
            "    id VARCHAR (45) NOT NULL,\n" +
            "    name VARCHAR (45),\n" +
            "    email VARCHAR (45),\n" +
            "    password VARCHAR (45),\n" +
            "    created  VARCHAR (45),\n" +
            "    modified VARCHAR (45),\n" +
            "    lastLogin VARCHAR (45),\n" +
            "    token VARCHAR (200),\n" +
            "    isActive VARCHAR (5),\n" +
            "    PRIMARY KEY (ID)\n" +
            ");";
    public static final String SQL_CREATE_PHONE = "CREATE TABLE phone (\n" +
            "    id VARCHAR (45) NOT NULL,\n" +
            "    idUser VARCHAR (45) NOT NULL,\n" +
            "    number VARCHAR (10),\n" +
            "    citycode VARCHAR (2),\n" +
            "    contrycode VARCHAR (2),\n" +
            "    PRIMARY KEY (ID)\n" +
            ");";
    private final Logger LOGGER = LoggerFactory.getLogger(DBService.class);

    //@Autowired
    //private UserRepositoryInterface userRepository;

    /*

     INICIALIZAR DB

    */

    public boolean createTables() {
        LOGGER.info("creating DB Structure db");

        try {
            Connection con = HSQLDBConnection.getConnection();

            // crear tabla de usuario
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_USER);
            ps.execute();

            // crear tabla de phone
            ps = con.prepareStatement(SQL_CREATE_PHONE);
            ps.execute();

            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return true;
    }

    /*

     MANEJO DE USUARIOS

    */

    public ArrayList<UserTable> listUsers() {

        LOGGER.info("Starting looking for users in db");
        ArrayList<UserTable> users = new ArrayList<>();

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user;");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                UserTable user = new UserTable();
                user.setId(rs.getString(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setCreated(rs.getString(5));
                user.setModified(rs.getString(6));
                user.setLastLogin(rs.getString(7));
                user.setToken(rs.getString(8));
                user.setIsActive(rs.getString(9));

                users.add(user);
            }

            rs.close();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return users;
    }

    public boolean createUser(UserTable user) {

        LOGGER.info("Starting store user in db");
        Boolean result = true;

        //userRepository.save(user);

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO user" +
                    "( id, name, email, password, created, modified, lastlogin, token, isactive )" +
                    "values (" +
                    "'" + user.getId() + "'," +
                    "'" + user.getName() + "'," +
                    "'" + user.getEmail() + "'," +
                    "'" + user.getPassword() + "'," +
                    "'" + user.getCreated() + "'," +
                    "'" + user.getModified() + "'," +
                    "'" + user.getLastLogin() + "'," +
                    "'" + user.getToken() + "'," +
                    "'" + user.getIsActive() + "'" +
                    ")");

            ps.execute();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return result;
    }

    public boolean updateUser(UserTable user) {

        LOGGER.info("Starting updating user in db");
        Boolean result = true;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE user " +
                    "SET " +
                    "name='" + user.getName() + "'," +
                    "email='" + user.getEmail() + "'," +
                    "password='" + user.getPassword() + "'," +
                    "modified='" + user.getModified() + "'," +
                    "isactive='" + user.getIsActive() + "'" +
                    "WHERE id = '" + user.getId() + "';");

            ps.execute();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return result;
    }

    public UserTable getUserById(String id) {

        LOGGER.info("Starting looking for users in db");
        UserTable user=null;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE id = '" + id + "';");
            ResultSet rs = ps.executeQuery();

            if(rs.isBeforeFirst()) {

                rs.next();

                user = new UserTable();
                user.setId(rs.getString(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setCreated(rs.getString(5));
                user.setModified(rs.getString(6));
                user.setLastLogin(rs.getString(7));
                user.setToken(rs.getString(8));
                user.setIsActive(rs.getString(9));

            }

            rs.close();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return user;
    }

    public Boolean deleteUserById(String id) {

        LOGGER.info("Starting looking for users in db");
        Boolean result = true;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM user WHERE id = '" + id + "';");

            ps.execute();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return result;
    }

    public boolean existEmail(String email) {

        LOGGER.info("Starting looking for an email in db");
        boolean result = false;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE email = '" + email + "';");
            ResultSet rs = ps.executeQuery();

            result = rs.isBeforeFirst();

            rs.close();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return result;
    }

    public UserTable loadUserByEmail(String email) {

        LOGGER.info("Starting looking for an email in db");
        UserTable user=null;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE email = '" + email + "';");
            ResultSet rs = ps.executeQuery();

            if(rs.isBeforeFirst()) {

                rs.next();

                user = new UserTable();
                user.setId(rs.getString(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setCreated(rs.getString(5));
                user.setModified(rs.getString(6));
                user.setLastLogin(rs.getString(7));
                user.setToken(rs.getString(8));
                user.setIsActive(rs.getString(9));

            }

            rs.close();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return user;
    }

    /*

     MANEJO DE TELEFONOS

    */
    public boolean createPhone(PhoneDto phone) {

        LOGGER.info("Starting store phone in db");
        Boolean result = true;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO phone" +
                    "( id, iduser, number, citycode, contrycode )" +
                    "values (" +
                    "'" + phone.getId() + "'," +
                    "'" + phone.getIdUser() + "'," +
                    "'" + phone.getNumber() + "'," +
                    "'" + phone.getCitycode() + "'," +
                    "'" + phone.getContrycode() + "'" +
                    ")");

            ps.execute();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return result;
    }

    public ArrayList<PhoneDto> getPhonesByUserId(String idUser) {

        LOGGER.info("Starting looking for phones in db");
        ArrayList<PhoneDto> phones = new ArrayList<>();

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM phone WHERE iduser = '" + idUser + "';");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                PhoneDto phone = new PhoneDto();
                phone.setId(rs.getString(1));
                phone.setIdUser(rs.getString(2));
                phone.setNumber(rs.getString(3));
                phone.setCitycode(rs.getString(4));
                phone.setContrycode(rs.getString(5));

                phones.add(phone);
            }

            rs.close();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return phones;
    }

    public Boolean deletePhonesByUserId(String id) {

        LOGGER.info("Starting looking for phones in db");
        Boolean result = true;

        try {
            Connection con = HSQLDBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM phone WHERE iduser = '" + id + "';");

            ps.execute();
            con.close();
        } catch (SQLException e1) {
            LOGGER.error("Error. ", e1);
            throw new UserException("Error. " + e1.getMessage());
        }

        return result;
    }
}
