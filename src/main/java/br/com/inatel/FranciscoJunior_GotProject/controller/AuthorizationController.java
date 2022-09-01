package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.configuration.security.TokenService;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.UserService;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.Login;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.RegistryUser;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.Token;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.UserClient;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Client;
import br.com.inatel.FranciscoJunior_GotProject.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/client")
    public ResponseEntity<?> postAuthClient(@RequestBody @Valid Login login) {
        try {
            Authentication a = authenticationManager.authenticate(login.converter());
            String token = tokenService.generateToken(a);
            Token tokenDTO = new Token(token, "Bearer");
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Login failed.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Null value detected, probably at: "
                    + e.getStackTrace()[0].toString() + ". Contact the System Administrator.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(e.getLocalizedMessage());
        }
    }

    @PostMapping("/client/registry")
    public ResponseEntity<?> createClient(@RequestBody @Valid UserClient userClient) {
        try {
            if(userService.findByEmail(userClient.getEmail()).isPresent())
                throw new EntityExistsException("E-mail already registered");
            if(userClient.getPassword().length()<6)
                throw new BadCredentialsException("Password must have at least 6 characters");
            Optional<Client> clientOpt = Optional.of(clientRepository.save(userClient.getClientInfo()));
            if (clientOpt.isPresent()) {
                Optional<RegistryUser> userOpt = userService.insert(userClient.getUserInfo(clientOpt.get().getId()));
                if (userOpt.isPresent())
                    return ResponseEntity.ok("Client registered");
            }
            return ResponseEntity.ok("Client not registered");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Null value detected, probably at: "
                    + e.getStackTrace()[0].toString() + ". Contact the System Administrator.");
        } catch (EntityExistsException e) {
            return ResponseEntity.ok(e.getLocalizedMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(e.getLocalizedMessage());
        }
    }
}
