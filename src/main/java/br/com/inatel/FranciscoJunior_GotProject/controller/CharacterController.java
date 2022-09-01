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

@RestController
@RequestMapping("/Characters")
public class CharacterController {

    @Autowired
    GotService gotService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CharacterDto> listAllCharacters(@PageableDefault(page = 0, size = 25) Pageable page){
        return gotService.findAllCharacters(page);
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

    @DeleteMapping("/Delete/{fullName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CharacterDto> deleteCharacter(@PathVariable String fullName){
        return ResponseEntity.ok(gotService.deleteCharacter(fullName));
    }
}
