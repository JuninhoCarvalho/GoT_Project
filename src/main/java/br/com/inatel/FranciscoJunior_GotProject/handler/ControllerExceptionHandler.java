package br.com.inatel.FranciscoJunior_GotProject.handler;

import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.model.rest.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(ContinentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Error continentNotFoundException(ContinentNotFoundException continentNotFoundException){
        return Error.builder()
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .message(continentNotFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(FamilyDoesnExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Error continentNotFoundException(FamilyDoesnExistException familyDoesnExistException){
        return Error.builder()
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .message(familyDoesnExistException.getMessage())
                .build();
    }

    @ExceptionHandler(CharacterAlreadyDeadException.class)
    @ResponseStatus(value = HttpStatus.FOUND)
    public Error characterAlreadyDeadException(CharacterAlreadyDeadException characterAlreadyDeadException){
        return Error.builder()
                .httpStatusCode(HttpStatus.FOUND)
                .message(characterAlreadyDeadException.getMessage())
                .build();
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Error characterNotFoundException(CharacterNotFoundException characterNotFoundException){
        return Error.builder()
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .message(characterNotFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(ExternalApiConnectionException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Error externalApiConnectionException(ExternalApiConnectionException externalApiConnectionException){
        return Error.builder()
                .httpStatusCode(HttpStatus.FORBIDDEN)
                .message(externalApiConnectionException.getMessage())
                .build();
    }

    @ExceptionHandler(ConnectionJDBCFailedException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public Error connectionJDBCFailedException(ConnectionJDBCFailedException connectionJDBCFailedException){
        return Error.builder()
                .httpStatusCode(HttpStatus.SERVICE_UNAVAILABLE)
                .message(connectionJDBCFailedException.getMessage())
                .build();
    }
}
