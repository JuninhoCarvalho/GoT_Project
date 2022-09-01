package br.com.inatel.FranciscoJunior_GotProject.configuration.security;

import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.RegistryUser;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
public class AuthenticatorTokenFilter extends OncePerRequestFilter {
    private TokenService ts;
    private UserService ur;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = fetchToken(request);
        boolean clientValid = ts.isClientValid(token);
        if (clientValid)
            authenticateUser(token, ts.getClientUserId(token));
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String token, Long id) {
        Optional<RegistryUser> userOpt = ur.find(id);
        if (userOpt.isPresent()) {
            RegistryUser user = userOpt.get();
            UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(user, token,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
    }

    private String fetchToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer "))
            return null;
        return token.substring(7);
    }

}
