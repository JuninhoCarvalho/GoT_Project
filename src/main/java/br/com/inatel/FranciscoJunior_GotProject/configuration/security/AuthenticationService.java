package br.com.inatel.FranciscoJunior_GotProject.configuration.security;

import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.RegistryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserService us;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RegistryUser> userOpt = us.findByEmail(username);
        if(userOpt.isPresent())
            return userOpt.get();
        else
            return null;
    }
}

