package br.com.inatel.FranciscoJunior_GotProject.exception;

public class FamilyDoesntExistException extends RuntimeException {
    public FamilyDoesntExistException(String family) {
        super(String.format("The '%s' family doesn't exist in the Game of Thrones world!", family));
    }
}
