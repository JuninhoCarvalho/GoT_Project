package br.com.inatel.FranciscoJunior_GotProject.exception;

import javax.validation.constraints.NotNull;

public class CharacterNoBelongsToThatFamilyException extends RuntimeException {
    public CharacterNoBelongsToThatFamilyException(@NotNull String name, @NotNull String family) {
        super(String.format("The character '%s' no belongs to the '%s' family", name, family));
    }
}
