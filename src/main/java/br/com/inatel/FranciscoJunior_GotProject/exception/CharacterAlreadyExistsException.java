package br.com.inatel.FranciscoJunior_GotProject.exception;

/**
 * Exception class in case the character has already been registered
 * @author francisco.carvalho
 * @since 1.0
 */
public class CharacterAlreadyExistsException extends RuntimeException {
    /**
     * @param fullName
     */
    public CharacterAlreadyExistsException(String fullName) {
        super(String.format("The character '%s' already exists!", fullName));
    }
}
