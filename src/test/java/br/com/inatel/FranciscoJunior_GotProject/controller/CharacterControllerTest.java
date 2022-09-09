package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class CharacterControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void givenGetCharactersRequest_shouldReturnCharacterDtoListAnd200Code(){
        List<CharacterDto> characterDtos = webTestClient.get()
                .uri("/characters")
                .exchange()
                .expectBodyList(CharacterDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(52, characterDtos.size());
        assertEquals(Optional.of(1).get(), characterDtos.get(0).getId());
        assertEquals("Daenerys", characterDtos.get(0).getFirstName());
        assertEquals("Targaryen", characterDtos.get(0).getLastName());
        assertEquals("Daenerys Targaryen", characterDtos.get(0).getFullName());
        assertEquals("Mother of Dragons", characterDtos.get(0).getTitle());
        assertEquals("House Targaryen", characterDtos.get(0).getFamily());
        assertEquals("daenerys.jpg", characterDtos.get(0).getImage());
        assertEquals("https://thronesapi.com/assets/images/daenerys.jpg", characterDtos.get(0).getImageUrl());
    }

    @Test
    public void givenGetCharacterRequest_whenCallGetMethodByValidName_shouldReturnCharacterDtoAnd200Code(){
       CharacterDto characterDto = webTestClient.get()
                .uri("/characters/Jon Snow")
                .exchange()
                .expectBody(CharacterDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(Optional.of(3).get(), characterDto.getId());
        assertEquals("Jon", characterDto.getFirstName());
        assertEquals("Snow", characterDto.getLastName());
        assertEquals("Jon Snow", characterDto.getFullName());
        assertEquals("King of the North", characterDto.getTitle());
        assertEquals("House Stark", characterDto.getFamily());
        assertEquals("jon-snow.jpg", characterDto.getImage());
        assertEquals("https://thronesapi.com/assets/images/jon-snow.jpg", characterDto.getImageUrl());
    }

    @Test
    public void givenGetCharacterRequest_whenCallGetMethodByInvalidName_shouldReturnCharacterNotFoundExceptionAnd404Code(){
        String message = webTestClient.get()
                .uri("/characters/Invalid")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains("Invalid Not Found!"));
    }

    @Test
    public void givenACorrectPostRequest_whenCallPostMethod_shouldReturnNewCharacterDtoAnd201Code(){
        CharacterDto newCharacterDto = CharacterDto.builder()
                .id(54)
                .firstName("Juninho")
                .lastName("Carvalho")
                .fullName("Juninho Carvalho")
                .title("Tester")
                .family("House Targaryen")
                .image("image.jpg")
                .imageUrl("img.com.br/juninho-carvalho")
                .build();

        CharacterDto characterDto = webTestClient.post()
                .uri("/characters")
                .body(BodyInserters.fromValue(newCharacterDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CharacterDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(newCharacterDto.getId(), characterDto.getId());
        assertEquals(newCharacterDto.getFirstName(), characterDto.getFirstName());
        assertEquals(newCharacterDto.getLastName(), characterDto.getLastName());
        assertEquals(newCharacterDto.getFullName(), characterDto.getFullName());
        assertEquals(newCharacterDto.getTitle(), characterDto.getTitle());
        assertEquals(newCharacterDto.getFamily(), characterDto.getFamily());
        assertEquals(newCharacterDto.getImage(), characterDto.getImage());
        assertEquals(newCharacterDto.getImageUrl(), characterDto.getImageUrl());
    }

    @Test
    public void givenAIncorrectPostRequest_whenCallPostMethodByCharacterAlreadyCreated_shouldReturnCharacterAlreadyExistsExceptionAnd409Code(){
        CharacterDto newCharacterDto = CharacterDto.builder()
                .id(1)
                .firstName("Daenerys")
                .lastName("Targaryen")
                .fullName("Daenerys Targaryen")
                .title("Mother of Dragons")
                .family("House Targaryen")
                .image("daenerys.jpg")
                .imageUrl("https://thronesapi.com/assets/images/daenerys.jpg")
                .build();

        String message = webTestClient.post()
                .uri("/characters")
                .body(BodyInserters.fromValue(newCharacterDto))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("The character '%s' already exists!", newCharacterDto.getFullName())));
    }

    @Test
    public void givenAIncorrectPostRequest_whenCallPostMethodByInvalidFamily_shouldReturnFamilyDoesntExistExceptionAnd404Code(){
        CharacterDto newCharacterDto = CharacterDto.builder()
                .id(55)
                .firstName("Maria")
                .lastName("Targaryen")
                .fullName("Maria Targaryen")
                .title("Tester")
                .family("House Invalid")
                .image("image.jpg")
                .imageUrl("img.com.br/Maria-Targaryen")
                .build();

        String message = webTestClient.post()
                .uri("/characters")
                .body(BodyInserters.fromValue(newCharacterDto))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("The '%s' family doesn't exist in the Game of Thrones world!",
                newCharacterDto.getFamily())));
    }

    @Test
    public void givenDeleteRequest_whenCallDeleteMethodByValidName_shouldReturnDeletedMessageAnd200Code(){
        String message = webTestClient.delete()
                .uri("/characters/delete/Sansa Stark")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertEquals(message, String.format("Sansa Stark was successfully deleted!"));
    }

    @Test
    public void givenDeleteRequest_whenCallDeleteMethodByInvalidName_shouldReturnCharacterNotFoundExceptionAnd404Code(){
        String message = webTestClient.delete()
                .uri("/characters/delete/Invalid")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("Invalid Not Found!")));
    }

}