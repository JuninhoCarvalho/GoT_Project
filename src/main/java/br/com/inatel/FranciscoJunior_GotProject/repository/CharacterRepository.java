package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Character Repository
 * @author francisco.carvalho
 * @since 1.0
 */
public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Optional<Character> findByFullName(String name);

    void deleteByFullName(String fullName);
}
