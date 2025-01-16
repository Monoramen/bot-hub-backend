package com.monora.personalbothub.bot_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

}
