package br.com.inatel.FranciscoJunior_GotProject.configuration.security;

import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.Authority;
import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.RegistryUser;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;
@Service
@Transactional
public class UserService {

    @PersistenceContext
    EntityManager em;

    public Optional<RegistryUser> findByEmail(String email) {
        try {
            return Optional.of(em.createQuery("SELECT u FROM RegistryUser u WHERE u.email = ?1", RegistryUser.class).setParameter(1, email)
                    .getSingleResult());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<RegistryUser> find(Long idUser) {
        return Optional.of(em.find(RegistryUser.class, idUser));
    }


    public Optional<RegistryUser> insert(RegistryUser user) {
        return Optional.of(em.merge(user));
    }

    public Optional<Authority> findPerfil(Long id) {
        return Optional.of(em.find(Authority.class, id));
    }
}
