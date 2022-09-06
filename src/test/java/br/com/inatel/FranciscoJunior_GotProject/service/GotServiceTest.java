package br.com.inatel.FranciscoJunior_GotProject.service;

import br.com.inatel.FranciscoJunior_GotProject.adapter.GotAdapter;
import br.com.inatel.FranciscoJunior_GotProject.exception.*;
import br.com.inatel.FranciscoJunior_GotProject.mapper.GotMapper;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Client;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private Client client;
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
    Pageable page = PageRequest.of(0, 10);

    @InjectMocks
    private GotService gotService = new GotService();

    @Before
    public void init() {
        client = Client.builder()
                .id(1L)
                .name("Junior")
                .email("junior@inatel.br")
                .build();

        character = Character.builder()
                .id(1)
                .firstName("Francisco")
                .lastName("Junior")
                .fullName("Francisco Junior")
                .title("Tester")
                .family("House Stark")
                .image("image.png")
                .imageUrl("image.com.br")
                .client(client)
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

    @Test
    public void givenPopulateCharacterDb_shouldReturnCharacterList(){
        when(gotAdapter.listCharacters()).thenReturn(GotMapper.toCharacterDtoList(characterList));

        List<Character> characters = gotService.populateCharactersDb();

        assertEquals(characters.get(0).getFirstName(), character.getFirstName());
        assertEquals(characters.get(0).getLastName(), character.getLastName());
        assertEquals(characters.get(0).getFullName(), character.getFullName());
        assertEquals(characters.get(0).getTitle(), character.getTitle());
        assertEquals(characters.get(0).getFamily(), character.getFamily());
        assertEquals(characters.get(0).getImage(), character.getImage());
        assertEquals(characters.get(0).getImageUrl(), character.getImageUrl());
    }

    @Test
    public void givenFindAllCharacters_shouldReturnCharactersDtoPage() {
        when(characterRepository.findAll(page)).thenReturn(characterPage);

        Page<CharacterDto> allCharacters = gotService.findAllCharacters(page);
        List<CharacterDto> characterDtos = allCharacters.stream().toList();

        assertEquals(allCharacters.getTotalElements(), 1);
        assertEquals(characterDtos.size(), 1);
        assertEquals(characterDtos.get(0).getId(), character.getId());
        assertEquals(characterDtos.get(0).getFirstName(), character.getFirstName());
        assertEquals(characterDtos.get(0).getLastName(), character.getLastName());
        assertEquals(characterDtos.get(0).getFullName(), character.getFullName());
        assertEquals(characterDtos.get(0).getTitle(), character.getTitle());
        assertEquals(characterDtos.get(0).getFamily(), character.getFamily());
        assertEquals(characterDtos.get(0).getImage(), character.getImage());
        assertEquals(characterDtos.get(0).getImageUrl(), character.getImageUrl());
    }

    @Test
    public void givenFindCharacterByName_whenFindCharacterByValidName_shouldReturnCharacterDto() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));

        CharacterDto chDto = gotService.findCharacter("Francisco Junior");

        assertEquals(chDto.getId(), character.getId());
        assertEquals(chDto.getFirstName(), character.getFirstName());
        assertEquals(chDto.getLastName(), character.getLastName());
        assertEquals(chDto.getFullName(), character.getFullName());
        assertEquals(chDto.getTitle(), character.getTitle());
        assertEquals(chDto.getFamily(), character.getFamily());
        assertEquals(chDto.getImage(), character.getImage());
        assertEquals(chDto.getImageUrl(), character.getImageUrl());
    }

    @Test
    public void givenFindCharacterByName_whenFindCharacterByNonExistentName_shouldReturnCharacterNotFoundException() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> gotService.findCharacter("Invalid"));

        assertThat(throwable)
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("Invalid Not Found!");
    }

    @Test
    public void givenCharacterDto_whenSaveCharacterAndDataIsValid_shouldReturnCharacterDto() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.of(family));
        when(characterRepository.save(any(Character.class))).thenReturn(character);

        CharacterDto chCreated = gotService.createCharacter(characterDto);

        assertEquals(chCreated.getId(), character.getId());
        assertEquals(chCreated.getFirstName(), character.getFirstName());
        assertEquals(chCreated.getLastName(), character.getLastName());
        assertEquals(chCreated.getFullName(), character.getFullName());
        assertEquals(chCreated.getTitle(), character.getTitle());
        assertEquals(chCreated.getFamily(), character.getFamily());
        assertEquals(chCreated.getImage(), character.getImage());
        assertEquals(chCreated.getImageUrl(), character.getImageUrl());
    }

    @Test
    public void givenCharacterDto_whenSaveCharacterAndCharacterAlreadyExists_shouldReturnCharacterAlreadyExistsException() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));

        Throwable throwable = catchThrowable(() -> gotService.createCharacter(characterDto));

        assertThat(throwable)
                .isInstanceOf(CharacterAlreadyExistsException.class)
                .hasMessageContaining("The character 'Francisco Junior' already exists!");
    }

    @Test
    public void givenCharacterDto_whenSaveCharacterAndIsAInvalidFamily_shouldReturnFamilyDoesntExistException() {
        characterDto.setFamily("Invalid");
        when(familyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> gotService.createCharacter(characterDto));

        assertThat(throwable)
                .isInstanceOf(FamilyDoesntExistException.class)
                .hasMessageContaining("The 'Invalid' family doesn't exist in the Game of Thrones world!");
    }

    @Test
    public void givenDeleteCharacterByName_whenDeleteExistingCharacter_shouldReturnCharacterDto() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));

        CharacterDto chDto = gotService.deleteCharacter("Francisco Junior");

        assertEquals(chDto.getId(), character.getId());
        assertEquals(chDto.getFirstName(), character.getFirstName());
        assertEquals(chDto.getLastName(), character.getLastName());
        assertEquals(chDto.getFullName(), character.getFullName());
        assertEquals(chDto.getTitle(), character.getTitle());
        assertEquals(chDto.getFamily(), character.getFamily());
        assertEquals(chDto.getImage(), character.getImage());
        assertEquals(chDto.getImageUrl(), character.getImageUrl());
    }

    @Test
    public void givenDeleteCharacterByName_whenDeleteNonExistingCharacter_shouldReturnCharacterNotFoundException() {
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> gotService.deleteCharacter("Invalid"));

        assertThat(throwable)
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("Invalid Not Found!");
    }

    @Test
    public void givenFindAllDeads_shouldReturnDeadDtoListEmpty(){
        when(deadRepository.findAll()).thenReturn(deadList);

        List<DeadDto> deadDtos = gotService.findAllDeads();

        assertEquals(deadDtos.size(), 0);
    }

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

    @Test
    public void givenDeadDto_whenIncludeNewDeadAndCharacterDoesntExist_shouldReturnCharacterNotFoundException(){
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.empty());
        deadDto.setName("Invalid");

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(CharacterNotFoundException.class)
                .hasMessageContaining("%s Not Found!", deadDto.getName());
    }

    @Test
    public void givenDeadDto_whenIncludeNewDeadAndIsInvalidContinent_shouldReturnContinentNotFoundException(){
        when(characterRepository.findByFullName(any(String.class))).thenReturn(Optional.of(character));
        deadDto.setContinent("Invalid");

        Throwable throwable = catchThrowable(() -> gotService.includeNewDead(deadDto));

        assertThat(throwable)
                .isInstanceOf(ContinentNotFoundException.class)
                .hasMessageContaining("%s is not a valid continent!", deadDto.getContinent());
    }

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

        assertEquals(deadDtos.size(), 1);
        assertEquals(deadDtos.get(0).getId(), dead.getId());
        assertEquals(deadDtos.get(0).getName(), dead.getName());
        assertEquals(deadDtos.get(0).getFamily(), dead.getFamily());
        assertEquals(deadDtos.get(0).getContinent(), dead.getContinent());
    }

    @Test
    public void givenFindDeadsPerFamily_shouldReturnFamilyDtoPage(){
        when(familyRepository.findAll(page)).thenReturn(familyPage);

        Page<FamilyDto> allDeadsPerFamily = gotService.findDeadsPerFamily(page);
        List<FamilyDto> familyDtos = allDeadsPerFamily.stream().toList();

        assertEquals(allDeadsPerFamily.getTotalElements(), 1);
        assertEquals(familyDtos.get(0).getId(), 1);
        assertEquals(familyDtos.get(0).getName(), family.getName());
        assertEquals(familyDtos.get(0).getDeads(), 0);
    }

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

        assertEquals(allDeadsPerFamily.getTotalElements(), 1);
        assertEquals(familyDtos.get(0).getId(), 1);
        assertEquals(familyDtos.get(0).getName(), dead.getFamily());
        assertEquals(familyDtos.get(0).getDeads(), 1);
    }

}




