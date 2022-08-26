package br.com.inatel.FranciscoJunior_GotProject.service;

import br.com.inatel.FranciscoJunior_GotProject.adapter.GotAdapter;
import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.mapper.GotMapper;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
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

    public List<CharacterDto> findAllCharacters(){
        try {
            List<Character> characters = characterRepository.findAll();

            return GotMapper.toCharacterDtoList(characters);
        }catch(JDBCConnectionException jdbcConnectionException){
            throw new ConnectionJDBCFailedException(jdbcConnectionException);
        }
    }

    public CharacterDto findCharacter(String name) {
        return GotMapper.toCharacterDto(characterRepository.findByFullName(name));
    }
    public List<Continent> findAllContinents(){
        return GotMapper.toContinentList(gotAdapter.listContinents());
    }

    public CharacterDto createCharacter(CharacterDto characterDto) {
        return GotMapper.toCharacterDto(characterRepository.save(GotMapper.toCharacter(characterDto)));
    }

    public List<DeadDto> findAllDeads() {
        return GotMapper.toDeadDtoList(deadRepository.findAll());
    }

    public DeadDto includeNewDead(DeadDto deadDto) {

        if(!isValidContinent(deadDto.getContinent())){
            throw new ContinentNotFoundException(deadDto.getContinent());
        }
        else if(!isValidFamily(deadDto.getFamily())) {
            throw new FamilyDoesnExistException(deadDto.getFamily());
        }
        else if(theCharactarAlreadyDead(deadDto.getName(), deadDto.getFamily())){
            throw new CharacterAlreadyDeadException(deadDto.getName(), deadDto.getFamily());
        }

        deadPerFamilyCalculation(deadDto);
        return GotMapper.toDeadDto(deadRepository.save(GotMapper.toDead(deadDto)));
    }

    private void deadPerFamilyCalculation(DeadDto dead) {
        Family family = familyRepository.findByName(dead.getFamily());
        family.setDeads(family.getDeads() + 1);
        familyRepository.save(family);
    }

    public void insertFamilys(List<String> familyNames) {
        familyNames.forEach(f -> familyRepository.save(new Family(f,0)));
    }

    public List<FamilyDto> findDeadsPerFamily() {
        return GotMapper.toFamilyDtoList(familyRepository.findAll());
    }

    public CharacterDto deleteCharacter(String fullName) {
        Character character = characterRepository.findByFullName(fullName);
        if(character != null){
            characterRepository.deleteByFullName(fullName);
            return GotMapper.toCharacterDto(character);
        }

        throw new CharacterNotFoundException(fullName);
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
