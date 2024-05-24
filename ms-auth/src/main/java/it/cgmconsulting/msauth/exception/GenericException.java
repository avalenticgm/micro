package it.cgmconsulting.msauth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException{

    private String msg;
    private HttpStatus status;

    public GenericException(String msg, HttpStatus status) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }
}
