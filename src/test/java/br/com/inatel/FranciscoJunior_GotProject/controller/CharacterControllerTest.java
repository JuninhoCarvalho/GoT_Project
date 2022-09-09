package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class CharacterControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void givenGetCharactersRequest_whenGetCharactersSettingWithDefaultPageable_shouldReturnCharacterDtoPageAnd200Code() throws JSONException {
        String characterDtos = webTestClient.get()
                .uri("/characters")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(characterDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

        assertEquals(53, jsonObject.get("totalElements"));
        assertEquals(3, jsonObject.get("totalPages"));
        assertEquals(25, jsonObject.get("size"));
        assertEquals(0, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(false));

        assertEquals(Optional.of(1).get(), jsonObject1.get("id"));
        assertEquals("Daenerys", jsonObject1.get("firstName"));
        assertEquals("Targaryen", jsonObject1.get("lastName"));
        assertEquals("Daenerys Targaryen", jsonObject1.get("fullName"));
        assertEquals("Mother of Dragons", jsonObject1.get("title"));
        assertEquals("House Targaryen", jsonObject1.get("family"));
        assertEquals("daenerys.jpg", jsonObject1.get("image"));
        assertEquals("https://thronesapi.com/assets/images/daenerys.jpg", jsonObject1.get("imageUrl"));
    }

    @Test
    @Order(2)
    public void givenGetCharactersRequest_whenGetCharactersSettingPageable_shouldReturnCharacterDtoPageAnd200Code() throws JSONException {
        String characterDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/characters")
                        .queryParam("page", 1)
                        .queryParam("size", 15)
                        .queryParam("sort", "firstName,asc")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(characterDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

        assertEquals(53, jsonObject.get("totalElements"));
        assertEquals(4, jsonObject.get("totalPages"));
        assertEquals(15, jsonObject.get("size"));
        assertEquals(1, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(true));

        assertEquals(Optional.of(42).get(), jsonObject1.get("id"));
        assertEquals("Jaqen", jsonObject1.get("firstName"));
        assertEquals("H'ghar", jsonObject1.get("lastName"));
        assertEquals("Jaqen H'ghar", jsonObject1.get("fullName"));
        assertEquals("Faceless Men of Braavos", jsonObject1.get("title"));
        assertEquals("Lorath", jsonObject1.get("family"));
        assertEquals("jaqen-hghar.jpg", jsonObject1.get("image"));
        assertEquals("https://thronesapi.com/assets/images/jaqen-hghar.jpg", jsonObject1.get("imageUrl"));
    }

    @Test
    @Order(3)
    public void givenGetCharactersRequest_whenGetCharactersSettingJustPage_shouldReturnCharacterDtoPageAnd200Code() throws JSONException {
        String characterDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/characters")
                        .queryParam("page", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(characterDtos);
        JSONObject sortObject = jsonObject.getJSONObject("sort");

        assertEquals(53, jsonObject.get("totalElements"));
        assertEquals(3, jsonObject.get("totalPages"));
        assertEquals(25, jsonObject.get("size"));
        assertEquals(1, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(false));
    }

    @Test
    @Order(4)
    public void givenGetCharactersRequest_whenGetCharactersSettingJustPageableSize_shouldReturnCharacterDtoPageAnd200Code() throws JSONException {
        String characterDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/characters")
                        .queryParam("size", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(characterDtos);
        JSONObject sortObject = jsonObject.getJSONObject("sort");

        assertEquals(53, jsonObject.get("totalElements"));
        assertEquals(6, jsonObject.get("totalPages"));
        assertEquals(10, jsonObject.get("size"));
        assertEquals(0, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(false));
    }

    @Test
    @Order(5)
    public void givenGetCharactersRequest_whenGetCharactersSettingJustPageableSort_shouldReturnCharacterDtoPageAnd200Code() throws JSONException {
        String characterDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/characters")
                        .queryParam("sort", "firstName")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(characterDtos);
        JSONObject sortObject = jsonObject.getJSONObject("sort");

        assertEquals(53, jsonObject.get("totalElements"));
        assertEquals(3, jsonObject.get("totalPages"));
        assertEquals(25, jsonObject.get("size"));
        assertEquals(0, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(true));
    }

    @Test
    @Order(6)
    public void givenGetCharactersRequest_whenGetCharactersSettingInvalidPageableSortReference_shouldReturnCharacterDtoPageAnd404Code() {
        String message = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/characters")
                        .queryParam("sort", "invalid,asc")
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains("No property 'invalid' found for type 'Character'"));
    }

    @Test
    @Order(7)
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
    @Order(8)
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
    @Order(9)
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
    @Order(10)
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
    @Order(11)
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
    @Order(12)
    public void givenAIncorrectPostRequest_whenCallPostMethodByANullField_shouldReturnMethodArgumentNotValidExceptionAnd400Code(){
        CharacterDto newCharacterDto = CharacterDto.builder()
                .id(55)
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
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains("firstName field is missing!"));
    }

    @Test
    @Order(13)
    public void givenDeleteRequest_whenCallDeleteMethodByValidName_shouldReturnDeletedMessageAnd200Code(){
        String message = webTestClient.delete()
                .uri("/characters/delete/Sansa Stark")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Sansa Stark was successfully deleted!", message);
    }

    @Test
    @Order(14)
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