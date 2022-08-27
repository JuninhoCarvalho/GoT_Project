package br.com.inatel.FranciscoJunior_GotProject.exception;

public class CharacterAlreadyExistsException extends RuntimeException {
    public CharacterAlreadyExistsException(String fullName) {
        super(String.format("The character '%s' already exists!", fullName));
    }
}
