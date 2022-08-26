package br.com.inatel.FranciscoJunior_GotProject.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class ExternalApiConnectionException extends RuntimeException {

    public ExternalApiConnectionException(WebClientException webClientException){
        super("Connection Fail! " + webClientException.getLocalizedMessage());
    }
}
