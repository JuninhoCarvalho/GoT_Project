package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    GotService gotService;

    @Autowired
    public CharacterController(GotService gotService) {
        this.gotService = gotService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CharacterDto> listAllCharacters(){
        return gotService.findAllCharacters();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDto listCharacter(@PathVariable @Valid String name){
        return gotService.findCharacter(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterDto createACharacter(@RequestBody @Valid CharacterDto characterDto){
        return gotService.createCharacter(characterDto);
    }

    @DeleteMapping("/delete/{fullName}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCharacter(@PathVariable String fullName){
        gotService.deleteCharacter(fullName);
        return String.format("%s was successfully deleted!", fullName);
    }
}
