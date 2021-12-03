package com.ey.desafiotecnico.controller;

import com.ey.desafiotecnico.dto.MessageResponseDto;
import com.ey.desafiotecnico.dto.ResponseCreateUserDto;
import com.ey.desafiotecnico.dto.UserDto;
import com.ey.desafiotecnico.dto.UserTable;
import com.ey.desafiotecnico.service.UserServiceFacade;
import com.ey.desafiotecnico.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1")
@Validated()
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceFacade userServiceFacade;

    @Autowired
    private Tool tool;

    @PostMapping(value = {"/createTables"},
            produces = { "application/json" })
    ResponseEntity<?> createTables(){

        LOGGER.info("Starting creating createTables");

        return new ResponseEntity<>(
                MessageResponseDto
                        .builder()
                        .mensaje("Created: " + this.userServiceFacade.createTables())
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping(value = {"/createUser"},
            consumes = { "application/json" },
            produces = { "application/json" })
    ResponseEntity<?> createUser(@RequestBody UserDto request){

        LOGGER.info("Starting createUser");

        return new ResponseEntity<>(
                this.userServiceFacade.createUser(request),
                HttpStatus.OK);
    }

    @GetMapping(produces = { "application/json" })
    ResponseEntity<?> listUsers(){

        LOGGER.info("Starting list Users");

        return new ResponseEntity<>(
                this.userServiceFacade.listUsers(),
                HttpStatus.OK);
    }

    @GetMapping(value = {"/getUserById/{id}"},
            produces = { "application/json" })
    ResponseEntity<?> getUserById(@PathVariable(required = true, name = "id") String id ){

        LOGGER.info("Starting get User By Id");

        return new ResponseEntity<>(
                this.userServiceFacade.getUserById(id),
                HttpStatus.OK);
    }

    @PutMapping(value = {"/updateUser"},
            consumes = { "application/json" },
            produces = { "application/json" })
    ResponseEntity<?> updateUser(@RequestBody UserTable request){

        LOGGER.info("Starting update User");

        return new ResponseEntity<>(
                MessageResponseDto
                        .builder()
                        .mensaje("Updated: " + this.userServiceFacade.updateUser(request))
                        .build(),
                HttpStatus.OK);
    }


    @DeleteMapping(value = {"/deleteUserById/{id}"},
            produces = { "application/json" })
    ResponseEntity<?> deleteUserById(@PathVariable(required = true, name = "id") String id ){

        LOGGER.info("Starting delete User By Id");

        return new ResponseEntity<>(
                MessageResponseDto
                        .builder()
                        .mensaje("Deleted: " + this.userServiceFacade.deleteUserById(id))
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping(value = {"/decodeJWT"},
            consumes = { "application/json" },
            produces = { "application/json" })
    ResponseEntity<?> decodeJWT(@RequestBody ResponseCreateUserDto request){

        LOGGER.info("Starting decodeJWT");

        return new ResponseEntity<>(
                this.tool.decodeJWT(request.getToken()),
                HttpStatus.OK);
    }


}
