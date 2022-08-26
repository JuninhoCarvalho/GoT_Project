package br.com.inatel.FranciscoJunior_GotProject.exception;

public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException(String fullName) {
        super(fullName + " Not Found!");
    }
}
