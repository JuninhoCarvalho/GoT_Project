package br.com.inatel.FranciscoJunior_GotProject.service;

import br.com.inatel.FranciscoJunior_GotProject.adapter.GotAdapter;
import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.mapper.GotMapper;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;
import br.com.inatel.FranciscoJunior_GotProject.repository.CharacterRepository;
import br.com.inatel.FranciscoJunior_GotProject.repository.DeadRepository;
import br.com.inatel.FranciscoJunior_GotProject.repository.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public List<CharacterDto> findAllCharacters(){
        return GotMapper.toCharacterDtoList(characterRepository.findAll());
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
            throw new FamilyDoesntExistException(characterDto.getFamily());
        }

        return GotMapper.toCharacterDto(characterRepository.save(GotMapper.toCharacter(characterDto)));
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

    public void insertFamily(List<String> familyNames) {
        familyNames.forEach(f -> familyRepository.save(new Family(f,0)));
    }

    @Cacheable(value = "deadsList")
    public List<DeadDto> findAllDeads() {
        return GotMapper.toDeadDtoList(deadRepository.findAll());
    }

    @CacheEvict(value = "deadsList", allEntries = true)
    public DeadDto includeNewDead(DeadDto deadDto) {

        if(characterRepository.findByFullName(deadDto.getName()).isEmpty()) {
            throw new CharacterNotFoundException(deadDto.getName());
        }
        else if(!isValidContinent(deadDto.getContinent())){
            throw new ContinentNotFoundException(deadDto.getContinent());
        }
        else if(!isValidFamily(deadDto.getFamily())) {
            throw new FamilyDoesntExistException(deadDto.getFamily());
        }
        else if(theCharacterAlreadyDead(deadDto.getName(), deadDto.getFamily())){
            throw new CharacterAlreadyDeadException(deadDto.getName(), deadDto.getFamily());
        }

        deadPerFamilyCalculation(deadDto);
        return GotMapper.toDeadDto(deadRepository.save(GotMapper.toDead(deadDto)));
    }

    @Cacheable(value = "deadsPerFamilyList")
    public List<FamilyDto> findDeadsPerFamily() {
        return GotMapper.toFamilyDtoList(familyRepository.findAll());
    }

    @CacheEvict(value = "deadsPerFamilyList", allEntries = true)
    public void deadPerFamilyCalculation(DeadDto dead) {
        Family family = familyRepository.findByName(dead.getFamily()).get();
        family.setDeads(family.getDeads() + 1);
        familyRepository.save(family);
    }

    @Cacheable(value = "continentsApi")
    public Boolean isValidContinent(String continent){
        List<ContinentDto> continentsDto = gotAdapter.listContinents();

        return continentsDto.stream().anyMatch(c -> c.getName().equals(continent));
    }

    private Boolean isValidFamily(String name){
        return familyRepository.findByName(name).isPresent();
    }

    private boolean theCharacterAlreadyDead(String name, String family) {
        return deadRepository.findByNameAndFamily(name, family).isPresent();
    }
}
