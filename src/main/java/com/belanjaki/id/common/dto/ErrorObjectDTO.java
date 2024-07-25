package com.belanjaki.id.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ErrorObjectDTO {
    private Integer statusCode;
    private String message;
    private Date timeStamp;
}
