package br.com.inatel.FranciscoJunior_GotProject.exception;

/**
 * Exception class in case the family is not found
 * @author francisco.carvalho
 * @since 1.0
 */
public class FamilyDoesntExistException extends RuntimeException {
    /**
     * @param family
     */
    public FamilyDoesntExistException(String family) {
        super(String.format("The '%s' family doesn't exist in the Game of Thrones world!", family));
    }
}
