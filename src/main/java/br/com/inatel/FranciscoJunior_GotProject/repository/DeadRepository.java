package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Dead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Dead Repository
 * @author francisco.carvalho
 * @since 1.0
 */
public interface DeadRepository extends JpaRepository<Dead, Integer> {
    Optional<Dead> findByNameAndFamily(String name, String family);
}
