package br.com.inatel.FranciscoJunior_GotProject.configuration;


import br.com.inatel.FranciscoJunior_GotProject.configuration.security.AuthenticationService;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.AuthenticatorTokenFilter;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.TokenService;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

    @Autowired
    private AuthenticationService authServ;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuild = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuild.userDetailsService(authServ);
        AuthenticationManager authManager = authBuild.build();

        return http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/").permitAll().antMatchers(HttpMethod.POST, "/auth/client", "/auth/client/registry")
                .permitAll()//
                .anyRequest().authenticated()
                .and().authenticationManager(authManager)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(
                        new AuthenticatorTokenFilter(tokenService, userService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public HttpFirewall allowUrlDoubleSlash() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2", "/h2/", "/h2/*", "/swagger-ui/", "/swagger-ui/*", "/webjars/**",
                "/v2/**", "/swagger-resources/**");
    }

}

