package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Family Repository
 * @author francisco.carvalho
 * @since 1.0
 */
public interface FamilyRepository extends JpaRepository<Family, Integer> {
    Optional<Family> findByName(String family);
}
