package br.com.inatel.FranciscoJunior_GotProject.exception;

public class FamilyDoesnExistException extends RuntimeException {
    public FamilyDoesnExistException(String family) {
        super(String.format("The '%s' family doesn't exist in the Game of Thrones world!", family));
    }
}
