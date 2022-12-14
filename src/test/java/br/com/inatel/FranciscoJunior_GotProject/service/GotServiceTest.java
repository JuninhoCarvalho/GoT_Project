package br.com.inatel.FranciscoJunior_GotProject.service;

import br.com.inatel.FranciscoJunior_GotProject.adapter.GotAdapter;
import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.mapper.GotMapper;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Dead;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;
import br.com.inatel.FranciscoJunior_GotProject.repository.CharacterRepository;
import br.com.inatel.FranciscoJunior_GotProject.repository.DeadRepository;
import br.com.inatel.FranciscoJunior_GotProject.repository.FamilyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for GotService
 * @author francisco.carvalho
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class GotServiceTest {

    @Mock
    private GotAdapter gotAdapter;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private DeadRepository deadRepository;
    private Character character;
    private CharacterDto characterDto;
    private Dead dead;
    private DeadDto deadDto;
    private Family family;
    private ContinentDto continentDto;
    private Page<Character> characterPage;
    private Page<Family> familyPage;
    private List<Character> characterList = new ArrayList<>();
    private List<Family> familyList = new ArrayList<>();
    private final List<Dead> deadList = new ArrayList<>();
    private List<ContinentDto> continentDtos = new ArrayList<>();
    private Set<String> familyName = new HashSet<>();
    Pageable page = PageRequest.of(0, 10);
    @InjectMocks
    private GotService gotService = new GotService();

    /**
     * Method annotated with @Before that will build each object for the following tests
     */
    @Before
    public void init() {
        character = Character.builder()
                .id(1)
                .firstName("Francisco")
                .lastName("Junior")
                .fullName("Francisco Junior")
                .title("Tester")
                .family("House Stark")
                .image("image.png")
                .imageUrl("image.com.br")
                .build();

        characterDto = CharacterDto.builder()
                .id(1)
                .firstName("Francisco")
                .lastName("Junior")
                .fullName("Francisco Junior")
                .title("Tester")
                .family("House Stark")
                .image("image.png")
                .imageUrl("image.com.br")
                .build();

        dead = Dead.builder()
                .id(1)
                .name("Francisco Junior")
                .family("House Stark")
                .continent("Westeros")
                .build();

        deadDto = DeadDto.builder()
                .id(1)
                .name("Francisco Junior")
                .family("House Stark")
                .continent("Westeros")
                .build();

        family = Family.builder()
                .id(1)
                .name("House Stark")
                .deads(0)
                .build();

        continentDto = ContinentDto.builder()
                .id(1)
                .name("Westeros")
                .build();

        characterList.add(character);
        characterPage = new PageImpl<>(characterList);
        familyList.add(family);
        familyPage = new PageImpl<>(familyList);
    }

    /**
     * Should populate de database with initials characters
     */
    @Test
    public void givenPopulateCharacterDb_shouldReturnCharacterList(){
        when(gotAdapter.listCharacters()).thenReturn(GotMapper.toCharacterDtoList(characterList));

        List<Character> characters = gotService.populateCharactersDb();

        assertEquals(character.getFirstName(), characters.get(0).getFirstName());
        assertEquals(character.getLastName(), characters.get(0).getLastName());
        assertEquals(character.getFullName(), characters.get(0).getFullName());
        assertEquals(character.getTitle(), characters.get(0).getTitle());
        assertEquals(character.getFamily(), characters.get(0).getFamily());
        assertEquals(character.getImage(), characters.get(0).getImage());
        assertEquals(character.getImageUrl(), characters.get(0).getImageUrl());
    }

    /**
     * Should return CharacterDto Page and 200 status code
     */
    @Test
    public void givenFindAllCharacters_shouldReturnCharactersDtoPage() {
        when(characterRepository.findAll(page)).thenReturn(characterPage);

        Page<CharacterDto> allCharacters = gotService.findAllCharacters(page);
        List<CharacterDto> characterDtos = allCharacters.stream().toList();

        assertEquals(1, characterDtos.size());
        assertEquals(character.getId(), characterDtos.get(0).getId());
        assertEquals(character.getFirstName(), characterDtos.get(0).getFirstName());
        assertEquals(character.getLastName(), characterDtos.get(0).getLastName());
        assertEquals(character.getFullName(), characterDtos.get(0).getFullName());
        assertEquals(character.getTitle(), characterDtos.get(0).getTitle());
        assertEquals(character.getFamily(), characterDtos.get(0).getFamily());
        assertEquals(character.getImage(), characterDtos.get(0).getImage());
        assertEquals(character.getImageUrl(), characterDtos.get(0).getImageUrl());
    }

    /**
     * Should return CharacterDto and 200 status code
     */
    @Test
    public void givenFindCharacterByName_whenFindCharacterByValidName_shouldReturnCharacterDto() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));

        CharacterDto chDto = gotService.findCharacter("Francisco Junior");

        assertEquals(character.getId(), chDto.getId());
        assertEquals(character.getFirstName(), chDto.getFirstName());
        assertEquals(character.getLastName(), chDto.getLastName());
        assertEquals(character.getFullName(), chDto.getFullName());
        assertEquals(character.getTitle(), chDto.getTitle());
        assertEquals(character.getFamily(), chDto.getFamily());
        assertEquals(character.getImage(), chDto.getImage());
        assertEquals(character.getImageUrl(), chDto.getImageUrl());
    }

    /**
     * Should throw the CharacterNotFoundException
     */
    @Test
    public void givenFindCharacterByName_whenFindCharacterByNonExistentName_shouldReturnCharacterNotFoundException() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> gotService.findCharacter("Invalid"));

        assertThat(throwable)
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("Invalid Not Found!");
    }

    /**
     * Should return new character created
     */
    @Test
    public void givenCharacterDto_whenSaveCharacterAndDataIsValid_shouldReturnCharacterDto() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(characterRepository.save(any(Character.class))).thenReturn(character);

        CharacterDto chCreated = gotService.createCharacter(characterDto);

        assertEquals(character.getId(), chCreated.getId());
        assertEquals(character.getFirstName(), chCreated.getFirstName());
        assertEquals(character.getLastName(), chCreated.getLastName());
        assertEquals(character.getFullName(), chCreated.getFullName());
        assertEquals(character.getTitle(), chCreated.getTitle());
        assertEquals(character.getFamily(), chCreated.getFamily());
        assertEquals(character.getImage(), chCreated.getImage());
        assertEquals(character.getImageUrl(), chCreated.getImageUrl());
    }

    /**
     * Should throw CharacterAlreadyExistsException
     */
    @Test
    public void givenCharacterDto_whenSaveCharacterAndCharacterAlreadyExists_shouldReturnCharacterAlreadyExistsException() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));

        Throwable throwable = catchThrowable(() -> gotService.createCharacter(characterDto));

        assertThat(throwable)
                .isInstanceOf(CharacterAlreadyExistsException.class)
                .hasMessageContaining("The character 'Francisco Junior' already exists!");
    }

    /**
     * Should throw FamilyDoesntExistException
     */
    @Test
    public void givenCharacterDto_whenSaveCharacterAndIsAInvalidFamily_shouldReturnFamilyDoesntExistException() {
        characterDto.setFamily("Invalid");
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> gotService.createCharacter(characterDto));

        assertThat(throwable)
                .isInstanceOf(FamilyDoesntExistException.class)
                .hasMessageContaining("The 'Invalid' family doesn't exist in the Game of Thrones world!");
    }

    /**
     * Should save family list in database
     */
    @Test
    public void givenInsertFamily_whenSendSetFamilyNames_shouldInsertInFamilyRepository() {
        when(familyRepository.save(any(Family.class))).thenReturn(family);
        when(familyRepository.findAll(page)).thenReturn(familyPage);
        familyName.add("House Stark");

        gotService.insertFamily(familyName);

        Page<FamilyDto> allDeadsPerFamily = gotService.findDeadsPerFamily(page);
        List<FamilyDto> familyDtos = allDeadsPerFamily.stream().toList();

        assertEquals(1, familyDtos.get(0).getId());
        assertEquals("House Stark", familyDtos.get(0).getName());
        assertEquals(0, familyDtos.get(0).getDeads());
    }

    /**
     * Should return the message with the name of the deleted character
     */
    @Test
    public void givenDeleteCharacterByName_whenDeleteExistingCharacter_shouldReturnDeletedMessage() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));

        String deleted = gotService.deleteCharacter("Francisco Junior");

        assertEquals(String.format("%s was successfully deleted!", character.getFullName()), deleted);
    }

    /**
     * Should throw CharacterNotFoundException
     */
    @Test
    public void givenDeleteCharacterByName_whenDeleteNonExistingCharacter_shouldReturnCharacterNotFoundException() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> gotService.deleteCharacter("Invalid"));

        assertThat(throwable)
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("Invalid Not Found!");
    }

    /**
     * should return empty DeadDto list
     */
    @Test
    public void givenFindAllDeads_shouldReturnDeadDtoListEmpty(){
        when(deadRepository.findAll()).thenReturn(deadList);

        List<DeadDto> deadDtos = gotService.findAllDeads();

        assertEquals(deadDtos.size(), 0);
    }

    /**
     * Should save new dead in database
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadAndDataIsValid_shouldReturnDeadDto(){
        continentDtos.add(continentDto);
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        when(gotAdapter.listContinents()).thenReturn(continentDtos);
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(deadRepository.findByNameAndFamily(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(deadRepository.save(any(Dead.class))).thenReturn(dead);

        DeadDto deadReturn = gotService.includeNewDead(deadDto);

        assertEquals(deadReturn.getId(), dead.getId());
        assertEquals(deadReturn.getName(), dead.getName());
        assertEquals(deadReturn.getFamily(), dead.getFamily());
        assertEquals(deadReturn.getContinent(), dead.getContinent());
    }

    /**
     * Should throw CharacterNotFoundException
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadAndCharacterDoesntExist_shouldReturnCharacterNotFoundException(){
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());
        deadDto.setName("Invalid");

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("%s Not Found!", deadDto.getName());
    }

    /**
     * Should throw ContinentNotFoundException
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadAndIsInvalidContinent_shouldReturnContinentNotFoundException(){
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        deadDto.setContinent("Invalid");

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(ContinentNotFoundException.class)
                .hasMessageContaining("%s is not a valid continent!", deadDto.getContinent());
    }

    /**
     * Should throw FamilyDoesntExistException
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadAndIsInvalidFamily_shouldReturnFamilyDoesntExistException(){
        continentDtos.add(continentDto);
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        when(gotAdapter.listContinents()).thenReturn(continentDtos);
        deadDto.setFamily("Invalid");

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(FamilyDoesntExistException.class)
                .hasMessageContaining("The '%s' family doesn't exist in the Game of Thrones world!",
                        deadDto.getFamily());
    }

    /**
     * Should throw CharacterAlreadyDeadException
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadAndCharacterAlreadyDead_shouldReturnCharacterAlreadyDeadException(){
        continentDtos.add(continentDto);
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        when(gotAdapter.listContinents()).thenReturn(continentDtos);
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(deadRepository.findByNameAndFamily(any(String.class), any(String.class))).thenReturn(Optional.of(dead));

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(CharacterAlreadyDeadException.class)
                .hasMessageContaining("The character '%s' belonging to the family '%s' already died!",
                        deadDto.getName(), deadDto.getFamily());
    }

    /**
     * Should throw CharacterNoBelongsToThatFamilyException
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadByCharacterInvalidFamily_shouldReturnCharacterNoBelongsToThatFamilyException(){
        continentDtos.add(continentDto);
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        when(gotAdapter.listContinents()).thenReturn(continentDtos);
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(deadRepository.findByNameAndFamily(any(String.class), any(String.class))).thenReturn(Optional.empty());

        deadDto.setFamily("Baratheon");

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(CharacterNoBelongsToThatFamilyException.class)
                .hasMessageContaining(String.format("The character '%s' no belongs to the '%s' family",
                        deadDto.getName(), deadDto.getFamily()));
    }

    /**
     * When include new dead should return list with the new dead inserted
     */
    @Test
    public void givenFindAllDeads_whenIncludeNewDeadIsValidInformations_shouldReturnDeadDtoList(){
        continentDtos.add(continentDto);
        deadList.add(dead);
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        when(gotAdapter.listContinents()).thenReturn(continentDtos);
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(deadRepository.findByNameAndFamily(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(deadRepository.save(any(Dead.class))).thenReturn(dead);
        when(deadRepository.findAll()).thenReturn(deadList);

        gotService.includeNewDead(deadDto);

        List<DeadDto> deadDtos = gotService.findAllDeads();

        assertEquals(1, deadDtos.size());
        assertEquals(dead.getId(), deadDtos.get(0).getId());
        assertEquals(dead.getName(), deadDtos.get(0).getName());
        assertEquals(dead.getFamily(), deadDtos.get(0).getFamily());
        assertEquals(dead.getContinent(), deadDtos.get(0).getContinent());
    }

    /**
     * Should return familyDto page
     */
    @Test
    public void givenFindDeadsPerFamily_shouldReturnFamilyDtoPage(){
        when(familyRepository.findAll(page)).thenReturn(familyPage);

        Page<FamilyDto> allDeadsPerFamily = gotService.findDeadsPerFamily(page);
        List<FamilyDto> familyDtos = allDeadsPerFamily.stream().toList();

        assertEquals(1, familyDtos.get(0).getId());
        assertEquals(family.getName(), familyDtos.get(0).getName());
        assertEquals(0, familyDtos.get(0).getDeads());
    }

    /**
     * When include new dead should return updated list with addition of deaths in such family
     */
    @Test
    public void givenDeadDto_whenIncludeNewDeadIsValidInformations_findDeadsPerFamilyShouldReturnListUpdated(){
        continentDtos.add(continentDto);
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        when(gotAdapter.listContinents()).thenReturn(continentDtos);
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(deadRepository.findByNameAndFamily(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(deadRepository.save(any(Dead.class))).thenReturn(dead);
        when(familyRepository.findAll(page)).thenReturn(familyPage);

        gotService.includeNewDead(deadDto);
        Page<FamilyDto> allDeadsPerFamily = gotService.findDeadsPerFamily(page);
        List<FamilyDto> familyDtos = allDeadsPerFamily.stream().toList();

        assertEquals(1, familyDtos.get(0).getId());
        assertEquals(dead.getFamily(), familyDtos.get(0).getName());
        assertEquals(1, familyDtos.get(0).getDeads());
    }
}