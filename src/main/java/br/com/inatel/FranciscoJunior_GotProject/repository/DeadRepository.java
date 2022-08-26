package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Dead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadRepository extends JpaRepository<Dead, Integer> {
    Dead findByNameAndFamily(String name, String family);
}
