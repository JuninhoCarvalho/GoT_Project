package br.com.inatel.FranciscoJunior_GotProject.exception;

import org.springframework.web.reactive.function.client.WebClientException;

/**
 * Exception class in case the external api connection fails
 * @author francisco.carvalho
 * @since 1.0
 */
public class ExternalApiConnectionException extends RuntimeException {

    /**
     * @param webClientException
     */
    public ExternalApiConnectionException(WebClientException webClientException){
        super("Connection Fail! " + webClientException.getLocalizedMessage());
    }
}
