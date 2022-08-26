package br.com.inatel.FranciscoJunior_GotProject.repository;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Integer> {

    Family findByName(String family);
}
