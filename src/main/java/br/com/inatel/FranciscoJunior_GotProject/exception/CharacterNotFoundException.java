package br.com.inatel.FranciscoJunior_GotProject.exception;

/**
 * Exception class in case the character is not found
 * @author francisco.carvalho
 * @since 1.0
 */
public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException(String fullName) {
        super(fullName + " Not Found!");
    }
}
