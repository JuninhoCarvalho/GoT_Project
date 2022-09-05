package br.com.inatel.FranciscoJunior_GotProject.configuration.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public @Getter class Token {
    private String token;
    private String type;

    @Override
    public String toString() {
        return type + " " + token;
    }
}
