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

    GotAdapter gotAdapter;

    CharacterRepository characterRepository;

    FamilyRepository familyRepository;

    DeadRepository deadRepository;

    @Autowired
    public GotService(GotAdapter gotAdapter, CharacterRepository characterRepository, FamilyRepository familyRepository, DeadRepository deadRepository) {
        this.gotAdapter = gotAdapter;
        this.characterRepository = characterRepository;
        this.familyRepository = familyRepository;
        this.deadRepository = deadRepository;
    }

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
        return GotMapper.toCharacterDtoPage(characterRepository.findAll(page));
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

    @CacheEvict(value = "charactersList", allEntries = true)
    public CharacterDto deleteCharacter(String fullName) {
        Optional<Character> character = characterRepository.findByFullName(fullName);

        if(character.isPresent()){
            characterRepository.deleteByFullName(fullName);
            return GotMapper.toCharacterDto(character.get());
        }

        throw new CharacterNotFoundException(fullName);
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

        if(characterRepository.findByFullName(deadDto.getName()).isEmpty()) {
            throw new CharacterNotFoundException(deadDto.getName());
        }
        else if(!isValidContinent(deadDto.getContinent())){
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
        return GotMapper.toFamilyDtoPage(familyRepository.findAll(page));
    }

    @CacheEvict(value = "deadsPerFamilyList", allEntries = true)
    public void deadPerFamilyCalculation(DeadDto dead) {
        Family family = familyRepository.findByName(dead.getFamily()).get();
        family.setDeads(family.getDeads() + 1);
        familyRepository.save(family);
    }

    private Boolean isValidContinent(String continent){
        List<ContinentDto> continentsDto = gotAdapter.listContinents();

        return continentsDto.stream().anyMatch(c -> c.getName().equals(continent));
    }

    private Boolean isValidFamily(String name){
        return familyRepository.findByName(name).isPresent();
    }

    private boolean theCharactarAlreadyDead(String name, String family) {
        return deadRepository.findByNameAndFamily(name, family).isPresent();
    }
}
