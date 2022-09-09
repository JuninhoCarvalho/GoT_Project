package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public class DeadControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void t1_givenGetDeadsRequest_shouldReturnDeadDtoListAnd200Code(){
        List<DeadDto> deadDtos = webTestClient.get()
                .uri("/deads")
                .exchange()
                .expectBodyList(DeadDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(0, deadDtos.size());
    }

    @Test
    public void t2_givenGetDeadsPerFamily_shouldReturnFamilyDtoListAnd200Code(){
        List<FamilyDto> familyDtos = webTestClient.get()
                .uri("/deads/family")
                .exchange()
                .expectBodyList(FamilyDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(35, familyDtos.size());
        familyDtos.forEach(f -> assertTrue(f.getDeads().equals(0)));
        assertEquals("Baratheon", familyDtos.get(1).getName());
        assertEquals("Worm", familyDtos.get(34).getName());
    }

    @Test
    public void t3_givenACorrectPostRequest_whenCallPostMethod_shouldReturnThatNewDeadDtoAnd201Code(){
        final DeadDto newDeadDto = DeadDto.builder()
                .id(1)
                .name("Daenerys Targaryen")
                .family("House Targaryen")
                .continent("Ulthos")
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

    @Test
    public void t4_givenGetDeadsRequest_shouldReturnDeadDtoListAnd200Code(){
        List<DeadDto> deadDtos = webTestClient.get()
                .uri("/deads")
                .exchange()
                .expectBodyList(DeadDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(1, deadDtos.size());
    }

    @Test
    public void t5_givenAIncorrectPostRequest_whenCallPostMethodByInvalidCharacter_shouldReturnCharacterNotFoundExceptionAnd404Code(){
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

    @Test
    public void t6_givenAIncorrectPostRequest_whenCallPostMethodByInvalidContinent_shouldReturnContinentNotFoundExceptionAnd404Code(){
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

    @Test
    public void t7_givenAIncorrectPostRequest_whenCallPostMethodByInvalidFamily_shouldReturnFamilyDoesntExistExceptionAnd404Code(){
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

    @Test
    public void t8_givenAIncorrectPostRequest_whenCallPostMethodByCharacterDead_shouldReturnCharacterAlreadyDeadExceptionAnd404Code(){
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
}
