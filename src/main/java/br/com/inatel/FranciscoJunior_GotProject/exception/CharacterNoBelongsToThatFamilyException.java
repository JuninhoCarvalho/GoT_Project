package br.com.inatel.FranciscoJunior_GotProject.exception;

import javax.validation.constraints.NotNull;

/**
 * Exception class in case the character no belongs to the family inserted
 * @author francisco.carvalho
 * @since 1.0
 */
public class CharacterNoBelongsToThatFamilyException extends RuntimeException {
    /**
     * @param name
     * @param family
     */
    public CharacterNoBelongsToThatFamilyException(@NotNull String name, @NotNull String family) {
        super(String.format("The character '%s' no belongs to the '%s' family", name, family));
    }
}
