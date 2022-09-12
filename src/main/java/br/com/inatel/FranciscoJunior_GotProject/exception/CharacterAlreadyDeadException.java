package br.com.inatel.FranciscoJunior_GotProject.exception;

/**
 * Exception class in case the dead character has already been registered
 * @author francisco.carvalho
 * @since 1.0
 */
public class CharacterAlreadyDeadException extends RuntimeException {
    /**
     * @param name
     * @param family
     */
    public CharacterAlreadyDeadException(String name, String family) {
        super(String.format("The character '%s' belonging to the family '%s' already died!" , name,family));
    }
}
