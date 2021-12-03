package com.ey.desafiotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCreateUserDto {

    private String id;
    private String created;
    private String modified;
    private String lastLogin;
    private String token;
    private String isActive;

}
