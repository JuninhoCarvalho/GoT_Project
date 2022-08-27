package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Optional<Character> findByFullName(String name);

    void deleteByFullName(String fullName);
}
