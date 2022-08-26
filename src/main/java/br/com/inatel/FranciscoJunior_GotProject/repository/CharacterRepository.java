package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Character findByFullName(String name);
}
