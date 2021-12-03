package com.ey.desafiotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {

    private String id;
    private String idUser;
    private String number;
    private String citycode;
    private String contrycode;

}
