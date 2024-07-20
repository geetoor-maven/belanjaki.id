package com.belanjaki.id.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObjectDTO {
    private Integer statusCode;
    private String message;
    private Date timeStamp;
}
