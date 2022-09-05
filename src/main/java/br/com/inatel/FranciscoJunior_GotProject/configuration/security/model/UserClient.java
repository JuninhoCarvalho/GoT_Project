package br.com.inatel.FranciscoJunior_GotProject.configuration.security.model;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Builder

public @Getter class UserClient {
    private String name;
    private String email;
    private String password;

    public Client getClientInfo() {
        return Client.builder().name(name).email(email).build();
    }

    public RegistryUser getUserInfo() {
        return RegistryUser.builder().username(genUsernameInitials(name)).password(new BCryptPasswordEncoder().encode(password))
                .email(email).build();
    }

    public RegistryUser getUserInfo(Long id) {
        return RegistryUser.builder().id(id).username(genUsernameInitials(name))
                .password(new BCryptPasswordEncoder().encode(password)).email(email).build();
    }

    private String genUsernameInitials(String name) {
        StringBuilder username = new StringBuilder();
        String[] nameSplit = name.toLowerCase().split(" ");
        username.append(nameSplit[0]);
        Arrays.stream(nameSplit).skip(1l).forEach(ns -> username.append(ns.substring(0, 1)));
        return username.toString();
    }


    private String genUsernameTruncate(String name) {
        return name.toLowerCase().replace(" ", "").substring(0, 20);
    }
}
