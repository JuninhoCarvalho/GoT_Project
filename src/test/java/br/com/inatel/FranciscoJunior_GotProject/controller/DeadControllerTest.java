package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for DeadController
 * @author francisco.carvalho
 * @since 1.0
 */
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeadControllerTest {

    @Autowired
    private WebTestClient webTestClient;


    /**
     * Should return empty DeadDto List
     */
    @Test
    @Order(1)
    public void givenGetDeadsRequest_shouldReturnEmptyDeadDtoListAnd200Code(){
        List<DeadDto> deadDtos = webTestClient.get()
                .uri("/deads")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeadDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(0, deadDtos.size());
    }

    /**
     * @throws JSONException
     * Should return FamilyDto page
     */
    @Test
    @Order(2)
    public void givenGetDeadsPerFamily_shouldReturnFamilyDtoPageAnd200Code() throws JSONException {
        String familyDtos = webTestClient.get()
                .uri("/deads/family")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(familyDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject jsonObject1 = jsonArray.getJSONObject(1);

        assertEquals(35, jsonObject.get("totalElements"));
        assertEquals(2, jsonObject.get("totalPages"));
        assertEquals(20, jsonObject.get("size"));
        assertTrue(sortObject.get("sorted").equals(true));

        assertEquals(12, jsonObject1.get("id"));
        assertEquals("Baratheon", jsonObject1.get("name"));
        assertEquals(0, jsonObject1.get("deads"));
    }

    /**
     * @throws JSONException
     * Should return FamilyDto Page And 200 status code
     */
    @Test
    @Order(3)
    public void givenGetDeadsPerFamily_whenGetCharactersSettingPageable_shouldReturnFamilyDtoPageAnd200Code() throws JSONException {
        String familyDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deads/family")
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(familyDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

        assertEquals(35, jsonObject.get("totalElements"));
        assertEquals(4, jsonObject.get("totalPages"));
        assertEquals(10, jsonObject.get("size"));
        assertEquals(1, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(true));

        assertEquals(9, jsonObject1.get("id"));
        assertEquals("House Lanister", jsonObject1.get("name"));
        assertEquals(0, jsonObject1.get("deads"));
    }

    /**
     * @throws JSONException
     * Should return FamilyDto Page And 200 status code
     */
    @Test
    @Order(4)
    public void givenGetDeadsPerFamily_whenGetCharactersSettingJustPage_shouldReturnFamilyDtoPageAnd200Code() throws JSONException {
        String familyDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deads/family")
                        .queryParam("page", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(familyDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

        assertEquals(35, jsonObject.get("totalElements"));
        assertEquals(2, jsonObject.get("totalPages"));
        assertEquals(20, jsonObject.get("size"));
        assertEquals(1, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(true));

        assertEquals(32, jsonObject1.get("id"));
        assertEquals("Mormont", jsonObject1.get("name"));
        assertEquals(0, jsonObject1.get("deads"));
    }

    /**
     * @throws JSONException
     * Should return FamilyDto Page And 200 status code
     */
    @Test
    @Order(5)
    public void givenGetDeadsPerFamily_whenGetCharactersSettingJustSize_shouldReturnFamilyDtoPageAnd200Code() throws JSONException {
        String familyDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deads/family")
                        .queryParam("size", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(familyDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject jsonObject1 = jsonArray.getJSONObject(1);

        assertEquals(35, jsonObject.get("totalElements"));
        assertEquals(4, jsonObject.get("totalPages"));
        assertEquals(10, jsonObject.get("size"));
        assertEquals(0, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(true));

        assertEquals(12, jsonObject1.get("id"));
        assertEquals("Baratheon", jsonObject1.get("name"));
        assertEquals(0, jsonObject1.get("deads"));
    }

    /**
     * @throws JSONException
     * Should return empty FamilyDto Page And 200 status code
     */
    @Test
    @Order(6)
    public void givenGetDeadsPerFamily_whenGetCharactersSettingPageOutOfTotal_shouldReturnFamilyDtoPageAnd200Code() throws JSONException {
        String familyDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/deads/family")
                        .queryParam("page", 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(familyDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject sortObject = jsonObject.getJSONObject("sort");
        JSONObject pageableObject = jsonObject.getJSONObject("pageable");

        assertEquals(0, jsonArray.length());

        assertEquals(40, pageableObject.get("offset"));

        assertEquals(35, jsonObject.get("totalElements"));
        assertEquals(2, jsonObject.get("totalPages"));
        assertEquals(20, jsonObject.get("size"));
        assertEquals(2, jsonObject.get("number"));
        assertTrue(sortObject.get("sorted").equals(true));
    }

    /**
     * Should return new Dead And 201 status code
     */
    @Test
    @Order(7)
    public void givenACorrectPostRequest_whenCallPostMethod_shouldReturnThatNewDeadDtoAnd201Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Daenerys Targaryen")
                .family("House Targaryen")
                .continent("Westeros")
                .build();


        final DeadDto deadDto = webTestClient.post()
                .uri("/deads")
                .body(BodyInserters.fromValue(newDeadDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DeadDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(newDeadDto.getId(), deadDto.getId());
        assertEquals(newDeadDto.getName(), deadDto.getName());
        assertEquals(newDeadDto.getFamily(), deadDto.getFamily());
        assertEquals(newDeadDto.getContinent(), deadDto.getContinent());
    }

    /**
     * Should return DeadDto List And 200 status code
     */
    @Test
    @Order(8)
    public void givenGetDeadsRequest_shouldReturnDeadDtoListAnd200Code(){
        List<DeadDto> deadDtos = webTestClient.get()
                .uri("/deads")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeadDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(1, deadDtos.size());

        assertEquals("Daenerys Targaryen", deadDtos.get(0).getName());
        assertEquals("House Targaryen", deadDtos.get(0).getFamily());
        assertEquals("Westeros", deadDtos.get(0).getContinent());
    }

    /**
     * @throws JSONException
     * Should return FamilyDto page updated and 200 status code
     */
    @Test
    @Order(9)
    public void givenGetDeadsPerFamily_afterCreatedANewDead_shouldReturnFamilyDtoPageUpdatedAnd200Code() throws JSONException {
        String familyDtos = webTestClient.get()
                .uri("/deads/family")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONObject jsonObject = new JSONObject(familyDtos);
        JSONArray jsonArray = jsonObject.getJSONArray("content");
        JSONObject jsonObject1 = jsonArray.getJSONObject(14);

        assertEquals(31, jsonObject1.get("id"));
        assertEquals("House Targaryen", jsonObject1.get("name"));
        assertEquals(1, jsonObject1.get("deads"));
    }

    /**
     * Should throw CharacterNotFoundException and 404 status code
     */
    @Test
    @Order(10)
    public void givenAIncorrectPostRequest_whenCallPostMethodByInvalidCharacter_shouldReturnCharacterNotFoundExceptionAnd404Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Invalid")
                .family("House Targaryen")
                .continent("Westeros")
                .build();


        final String message = webTestClient.post()
                .uri("/deads")
                .body(BodyInserters.fromValue(newDeadDto))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("%s Not Found!", newDeadDto.getName())));
    }

    /**
     * Should throw ContinentNotFoundException and 404 status code
     */
    @Test
    @Order(11)
    public void givenAIncorrectPostRequest_whenCallPostMethodByInvalidContinent_shouldReturnContinentNotFoundExceptionAnd404Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Daenerys Targaryen")
                .family("House Targaryen")
                .continent("Invalid")
                .build();


        final String message = webTestClient.post()
                .uri("/deads")
                .body(BodyInserters.fromValue(newDeadDto))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(newDeadDto.getContinent() + " is not a valid continent!"));
    }

    /**
     * Should throw FamilyDoesntExistException and 404 status code
     */
    @Test
    @Order(12)
    public void givenAIncorrectPostRequest_whenCallPostMethodByInvalidFamily_shouldReturnFamilyDoesntExistExceptionAnd404Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Daenerys Targaryen")
                .family("Invalid")
                .continent("Westeros")
                .build();


        final String message = webTestClient.post()
                .uri("/deads")
                .body(BodyInserters.fromValue(newDeadDto))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("The '%s' family doesn't exist in the Game of Thrones world!",
                newDeadDto.getFamily())));
    }

    /**
     * Should throw CharacterAlreadyDeadException and 409 status code
     */
    @Test
    @Order(13)
    public void givenAIncorrectPostRequest_whenCallPostMethodByCharacterDead_shouldReturnCharacterAlreadyDeadExceptionAnd409Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Daenerys Targaryen")
                .family("House Targaryen")
                .continent("Ulthos")
                .build();


        final String message = webTestClient.post()
                .uri("/deads")
                .body(BodyInserters.fromValue(newDeadDto))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("The character '%s' belonging to the family '%s' already died!",
                newDeadDto.getName(), newDeadDto.getFamily())));
    }

    /**
     * Should throw CharacterNoBelongsToThatFamilyException and 400 status code
     */
    @Test
    @Order(14)
    public void givenAIncorrectPostRequest_whenCallPostMethodByCharacterInvalidFamily_shouldReturnCharacterNoBelongsToThatFamilyExceptionAnd400Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Daenerys Targaryen")
                .family("Baratheon")
                .continent("Ulthos")
                .build();


        final String message = webTestClient.post()
                .uri("/deads")
                .body(BodyInserters.fromValue(newDeadDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertTrue(message.contains(String.format("The character '%s' no belongs to the '%s' family",
                newDeadDto.getName(), newDeadDto.getFamily())));
    }
}
