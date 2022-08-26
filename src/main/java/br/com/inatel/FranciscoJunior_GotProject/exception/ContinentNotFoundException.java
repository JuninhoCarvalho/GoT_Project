package br.com.inatel.FranciscoJunior_GotProject.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContinentNotFoundException extends RuntimeException {

    public ContinentNotFoundException(String deadDto) {
        super(deadDto + " not valid as a continent");
    }
}
