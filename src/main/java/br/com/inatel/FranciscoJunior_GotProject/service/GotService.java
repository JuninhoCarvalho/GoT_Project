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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Service class where the methods to be called in the Controllers class are located
 * @author francisco.carvalho
 * @since 1.0
 */
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

    /**
     * Method called by the listener to populate the initial characters
     * @return The initials characters provided by the external api
     */
    public List<Character> populateCharactersDb(){
        List<Character> characters = GotMapper.toCharacterList(gotAdapter.listCharacters());
        characterRepository.saveAll(characters);

        return characters;
    }

    /**
     * @param page
     * @return The characters contained in the database
     */
    @Cacheable(value = "charactersList")
    public Page<CharacterDto> findAllCharacters(Pageable page){
        return GotMapper.toCharacterDtoPage(characterRepository.findAll(page));
    }

    /**
     * @param name
     * @return The specific character searched by the name
     */
    public CharacterDto findCharacter(String name) {
        Optional<Character> character = characterRepository.findByFullName(name);

        if(character.isPresent()){
            return GotMapper.toCharacterDto(character.get());
        }

        throw new CharacterNotFoundException(name);
    }

    /**
     * @param characterDto
     * @return The new Character created
     */
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

    /**
     * @param fullName
     * @return Message if the character has been deleted
     */
    @CacheEvict(value = "charactersList", allEntries = true)
    public String deleteCharacter(String fullName) {
        Optional<Character> character = characterRepository.findByFullName(fullName);

        if(character.isPresent()){
            characterRepository.deleteByFullName(fullName);
            return String.format("%s was successfully deleted!", fullName);
        }

        throw new CharacterNotFoundException(fullName);
    }

    /**
     * @param familyNames
     * Method called by the listener to populate the families
     */
    public void insertFamily(Set<String> familyNames) {
        familyNames.forEach(f -> familyRepository.save(new Family(f,0)));
    }

    /**
     * @return All deads contained in the database
     */
    @Cacheable(value = "deadsList")
    public List<DeadDto> findAllDeads() {
        return GotMapper.toDeadDtoList(deadRepository.findAll());
    }

    /**
     * @param deadDto
     * @return The new dead included in the database if the information is valid
     */
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
        else if(!characterBelongsToThatFamily(deadDto.getName(), deadDto.getFamily())){
            throw new CharacterNoBelongsToThatFamilyException(deadDto.getName(), deadDto.getFamily());
        }

        deadPerFamilyCalculation(deadDto);
        return GotMapper.toDeadDto(deadRepository.save(GotMapper.toDead(deadDto)));
    }

    /**
     * @param page
     * @return Page with the families and their deads quantity
     */
    public Page<FamilyDto> findDeadsPerFamily(Pageable page) {
        return GotMapper.toFamilyDtoPage(familyRepository.findAll(page));
    }

    /**
     * @param dead
     * If a new death is registered, this method adds to the family's death count
     */
    public void deadPerFamilyCalculation(DeadDto dead) {
        Family family = familyRepository.findByName(dead.getFamily()).get();
        family.setDeads(family.getDeads() + 1);
        familyRepository.save(family);
    }

    /**
     * @param continent
     * @return The answer for the continent to be valid or not
     */
    public Boolean isValidContinent(String continent){
        List<String> cNames = new ArrayList<>();

        loadContinents().forEach(c -> cNames.add(c.getName()));

        return cNames.stream().anyMatch(c -> c.equals(continent));
    }

    /**
     * @return Continents provided by the external api
     */
    @Cacheable(value = "continentsApi")
    public List<ContinentDto> loadContinents(){
        return gotAdapter.listContinents();
    }

    /**
     * @param name
     * @return The answer for the family to be valid or not
     */
    private Boolean isValidFamily(String name){
        return familyRepository.findByName(name).isPresent();
    }

    /**
     * @param name
     * @param family
     * @return The answer for the character already registered as dead or not
     */
    private boolean theCharacterAlreadyDead(String name, String family) {
        return deadRepository.findByNameAndFamily(name, family).isPresent();
    }

    /**
     * @param name
     * @param family
     * @return The answer if the character is not from that family
     */
    private boolean characterBelongsToThatFamily(String name, String family) {
        Optional<Character> character = characterRepository.findByFullName(name);

        return character.get().getFamily().equals(family);
    }
}
