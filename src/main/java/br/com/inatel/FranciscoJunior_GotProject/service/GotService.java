package br.com.inatel.FranciscoJunior_GotProject.service;

import br.com.inatel.FranciscoJunior_GotProject.adapter.GotAdapter;
import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.mapper.GotMapper;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Dead;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;
import br.com.inatel.FranciscoJunior_GotProject.repository.CharacterRepository;
import br.com.inatel.FranciscoJunior_GotProject.repository.DeadRepository;
import br.com.inatel.FranciscoJunior_GotProject.repository.FamilyRepository;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GotService {

    @Autowired
    GotAdapter gotAdapter;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    DeadRepository deadRepository;

    public List<Character> populateCharactersDb(){
        try {
            List<Character> characters = GotMapper.toCharacterList(gotAdapter.listCharacters());
            characters.forEach(c -> characterRepository.save(c));

            return characters;
        }catch(WebClientException webClientException){
            throw new ExternalApiConnectionException(webClientException);
        }
    }

    public List<Character> findAllCharacters(){
        try {
            List<Character> characters = characterRepository.findAll();

            return characters;
        }catch(JDBCConnectionException jdbcConnectionException){
            throw new ConnectionJDBCFailedException(jdbcConnectionException);
        }
    }

    public Character findCharacter(String name) {
        return characterRepository.findByFullName(name);
    }
    public List<Continent> findAllContinents(){
        return GotMapper.toContinentList(gotAdapter.listContinents());
    }

    public Character createCharacter(CharacterDto characterDto) {
        return characterRepository.save(GotMapper.toCharacter(characterDto));
    }

    public List<Dead> findAllDeads() {
        return deadRepository.findAll();
    }

    public Dead includeNewDead(DeadDto deadDto) {

        if(!isValidFamily(deadDto.getFamily())) {
            throw new FamilyDoesnExistException(deadDto.getFamily());
        }
        else if(theCharactarAlreadyDead(deadDto.getName(), deadDto.getFamily())){
            throw new CharacterAlreadyDeadException(deadDto.getName(), deadDto.getFamily());
        }

        deadPerFamilyCalculation(deadDto);
        return deadRepository.save(GotMapper.toDead(deadDto));
    }



    private void deadPerFamilyCalculation(DeadDto dead) {
        Family family = familyRepository.findByName(dead.getFamily());
        family.setDeads(family.getDeads() + 1);
        familyRepository.save(family);
    }

    public void insertFamilys(Set<String> familyNames) {
        familyNames.forEach(f -> familyRepository.save(new Family(f,0)));
    }

    public List<Family> findDeadsPerFamily() {
        return familyRepository.findAll();
    }

    private Boolean isValidContinent(String continent){
        List<Continent> continents = GotMapper.toContinentList(gotAdapter.listContinents());

        return continents.stream().anyMatch(c -> c.getName().equals(continent));
    }

    private Boolean isValidFamily(String name){
        return familyRepository.findByName(name) != null;
    }

    private boolean theCharactarAlreadyDead(String name, String family) {
        return deadRepository.findByNameAndFamily(name, family) != null;
    }
}
