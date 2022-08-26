package br.com.inatel.FranciscoJunior_GotProject.adapter;

import br.com.inatel.FranciscoJunior_GotProject.exception.ExternalApiConnectionException;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class GotAdapter {

    @Value("${GoT_URL}")
    private String URL_MANAGER;

    @Value("${X-RapidAPI-Key}")
    private String key;

    @Value("${X-RapidAPI-Host}")
    private String host;

    public List<CharacterDto> listCharacters(){

        List<CharacterDto> characterDtos = new ArrayList<>();
        try {
            Flux<CharacterDto> flux = WebClient.create(URL_MANAGER)
                    .get()
                    .uri("/Characters")
                    .header("X-RapidAPI-Key", key)
                    .header("X-RapidAPI-Host", host)
                    .retrieve()
                    .bodyToFlux(CharacterDto.class);

            flux.subscribe(f -> characterDtos.add(f));
            flux.blockLast();

            return characterDtos;
        }
        catch (WebClientException webClientException){
            throw new ExternalApiConnectionException(webClientException);
        }
    }

    public List<ContinentDto> listContinents() {

        List<ContinentDto> continentDtos = new ArrayList<>();

        try {
            Flux<ContinentDto> flux = WebClient.create(URL_MANAGER)
                    .get()
                    .uri("/Continents")
                    .header("X-RapidAPI-Key", key)
                    .header("X-RapidAPI-Host", host)
                    .retrieve()
                    .bodyToFlux(ContinentDto.class);

            flux.subscribe(f -> continentDtos.add(f));
            flux.blockLast();

            return continentDtos;
        }
        catch (WebClientException webClientException){
            throw new ExternalApiConnectionException(webClientException);
        }
    }
}
