package br.com.inatel.FranciscoJunior_GotProject.model.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class Error {

    private HttpStatus httpStatusCode;
    private String message;
}
