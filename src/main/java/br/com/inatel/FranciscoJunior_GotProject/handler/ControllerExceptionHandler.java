package br.com.inatel.FranciscoJunior_GotProject.handler;

import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.model.rest.Error;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Objects;

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

    @ExceptionHandler(FamilyDoesntExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Error familyDoesntExistException(FamilyDoesntExistException familyDoesnExistException){
        return Error.builder()
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .message(familyDoesnExistException.getMessage())
                .build();
    }

    @ExceptionHandler(CharacterAlreadyDeadException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error characterAlreadyDeadException(CharacterAlreadyDeadException characterAlreadyDeadException){
        return Error.builder()
                .httpStatusCode(HttpStatus.CONFLICT)
                .message(characterAlreadyDeadException.getMessage())
                .build();
    }

    @ExceptionHandler(CharacterAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error characterAlreadyExistsException(CharacterAlreadyExistsException characterAlreadyExistsException){
        return Error.builder()
                .httpStatusCode(HttpStatus.CONFLICT)
                .message(characterAlreadyExistsException.getMessage())
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
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public Error externalApiConnectionException(ExternalApiConnectionException externalApiConnectionException){
        return Error.builder()
                .httpStatusCode(HttpStatus.SERVICE_UNAVAILABLE)
                .message(externalApiConnectionException.getMessage())
                .build();
    }

    @ExceptionHandler(JDBCConnectionException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public Error connectionJDBCFailedException(JDBCConnectionException connectionJDBCFailedException){
        return Error.builder()
                .httpStatusCode(HttpStatus.SERVICE_UNAVAILABLE)
                .message(connectionJDBCFailedException.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error fieldNullException(MethodArgumentNotValidException fieldNullException){
        return Error.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(Objects.requireNonNull(fieldNullException.getFieldError()).getField() +  " field is missing!")
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error jsonErrorException(HttpMessageNotReadableException jsonError){
        return Error.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(jsonError.getMessage())
                .build();
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error pageableFieldSortException(PropertyReferenceException property){
        return Error.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(property.getMessage())
                .build();
    }

    @ExceptionHandler(WebClientException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error externalApiConnectionException(WebClientException web){
        return Error.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(web.getMessage())
                .build();
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Error nonUniqueResultException(IncorrectResultSizeDataAccessException web){
        return Error.builder()
                .httpStatusCode(HttpStatus.CONFLICT)
                .message(web.getMessage())
                .build();
    }
}
