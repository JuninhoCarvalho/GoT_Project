package br.com.inatel.FranciscoJunior_GotProject.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContinentNotFoundException extends RuntimeException {

    public ContinentNotFoundException(String continent) {
        super(continent + " is not a valid continent!");
    }
}