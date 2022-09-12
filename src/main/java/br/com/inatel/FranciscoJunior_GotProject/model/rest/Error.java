package br.com.inatel.FranciscoJunior_GotProject.model.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Class with attributes that will appear when an exception is thrown
 * @author francisco.carvalho
 * @since 1.0
 */
@Data
@AllArgsConstructor
@Builder
public class Error {

    private HttpStatus httpStatusCode;
    private String message;
}
