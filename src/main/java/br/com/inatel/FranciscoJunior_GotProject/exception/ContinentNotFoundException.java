package br.com.inatel.FranciscoJunior_GotProject.exception;

/**
 * Exception class in case the continent is not found
 * @author francisco.carvalho
 * @since 1.0
 */
public class ContinentNotFoundException extends RuntimeException {

    /**
     * @param continent
     */
    public ContinentNotFoundException(String continent) {
        super(continent + " is not a valid continent!");
    }
}