package br.com.inatel.FranciscoJunior_GotProject.exception;

public class CharacterAlreadyDeadException extends RuntimeException {
    public CharacterAlreadyDeadException(String name, String family) {
        super(String.format("The character '%s' belonging to the family '%s' already died!" , name,family));
    }
}
