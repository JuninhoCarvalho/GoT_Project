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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
            characterRepository.saveAll(characters);

            return characters;
        }catch(WebClientException webClientException){
            throw new ExternalApiConnectionException(webClientException);
        }
    }
    @Cacheable(value = "charactersList")
    public Page<CharacterDto> findAllCharacters(Pageable page){
        try {
            Page<Character> characters = characterRepository.findAll(page);

            return GotMapper.toCharacterDtoPage(characters);
        }catch(JDBCConnectionException jdbcConnectionException) {
            throw new ConnectionJDBCFailedException(jdbcConnectionException);
        }
    }

    public CharacterDto findCharacter(String name) {
        Optional<Character> character = characterRepository.findByFullName(name);

        if(character.isPresent()){
            return GotMapper.toCharacterDto(character.get());
        }

        throw new CharacterNotFoundException(name);

    }

    @CacheEvict(value = "charactersList", allEntries = true)
    public CharacterDto createCharacter(CharacterDto characterDto) {
        Optional<Character> character = characterRepository.findByFullName(characterDto.getFullName());

        if(character.isPresent()){
            throw new CharacterAlreadyExistsException(characterDto.getFullName());
        }
        else if(!isValidFamily(characterDto.getFamily())){
            throw new FamilyDoesnExistException(characterDto.getFamily());
        }

        return GotMapper.toCharacterDto(characterRepository.save(GotMapper.toCharacter(characterDto)));
    }

    public void insertFamilys(Set<String> familyNames) {
        familyNames.forEach(f -> familyRepository.save(new Family(f,0)));
    }

    @Cacheable(value = "deadsList")
    public List<DeadDto> findAllDeads() {
        return GotMapper.toDeadDtoList(deadRepository.findAll());
    }

    @CacheEvict(value = "deadsList", allEntries = true)
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

    @Cacheable(value = "deadsPerFamilyList")
    public Page<FamilyDto> findDeadsPerFamily(Pageable page) {
        Page<Family> families = familyRepository.findAll(page);

        return GotMapper.toFamilyDtoPage(families);
    }

    @CacheEvict(value = "deadsPerFamilyList", allEntries = true)
    public void deadPerFamilyCalculation(DeadDto dead) {
        Family family = familyRepository.findByName(dead.getFamily()).get();
        family.setDeads(family.getDeads() + 1);
        familyRepository.save(family);
    }

    @CacheEvict(value = "charactersList", allEntries = true)
    public CharacterDto deleteCharacter(String fullName) {
        Optional<Character> character = characterRepository.findByFullName(fullName);
        if(character.isPresent()){
            characterRepository.deleteByFullName(fullName);
            return GotMapper.toCharacterDto(character.get());
        }

        throw new CharacterNotFoundException(fullName);
    }

    private Boolean isValidContinent(String continent){
        List<Continent> continents = GotMapper.toContinentList(gotAdapter.listContinents());

        return continents.stream().anyMatch(c -> c.getName().equals(continent));
    }

    private Boolean isValidFamily(String name){
        return familyRepository.findByName(name).isPresent();
    }

    private boolean theCharactarAlreadyDead(String name, String family) {
        return deadRepository.findByNameAndFamily(name, family).isPresent();
    }
}
