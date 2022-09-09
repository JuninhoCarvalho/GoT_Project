package br.com.inatel.FranciscoJunior_GotProject.adapter;

import br.com.inatel.FranciscoJunior_GotProject.exception.ExternalApiConnectionException;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GotAdapter {

    @Value("${GoT_URL}")
    private String URL_MANAGER;

    @Value("${X-RapidAPI-Key}")
    private String key;

    @Value("${X-RapidAPI-Host}")
    private String host;

    public List<CharacterDto> listCharacters(){

        List<CharacterDto> characterDtos;
        try {
            Mono<List<CharacterDto>> listMono = WebClient.create(URL_MANAGER)
                    .get()
                    .uri("/Characters")
                    .header("X-RapidAPI-Key", key)
                    .header("X-RapidAPI-Host", host)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CharacterDto>>() {});

            characterDtos = listMono.block();

            return characterDtos.stream().collect(Collectors.toList());
        }
        catch (WebClientException webClientException){
            throw new ExternalApiConnectionException(webClientException);
        }
    }

    public List<ContinentDto> listContinents() {

        List<ContinentDto> continentDtos;

        try {
            Mono<List<ContinentDto>> listMono = WebClient.create(URL_MANAGER)
                    .get()
                    .uri("/Continents")
                    .header("X-RapidAPI-Key", key)
                    .header("X-RapidAPI-Host", host)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ContinentDto>>() {});

            continentDtos = listMono.block();

            return continentDtos.stream().collect(Collectors.toList());
        }
        catch (WebClientException webClientException){
            throw new ExternalApiConnectionException(webClientException);
        }
    }
}
