package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/Characters")
public class CharacterController {

    @Autowired
    GotService gotService;

    @GetMapping
    public ResponseEntity<List<Character>> listAllCharacters(){
        return ResponseEntity.ok(gotService.findAllCharacters());
    }

    @GetMapping
    @RequestMapping("/{name}")
    public ResponseEntity<Character> listCharacter(@PathVariable @Valid String name){
        return ResponseEntity.ok(gotService.findCharacter(name));
    }

    @PostMapping
    public ResponseEntity<Character> createACharacter(@RequestBody CharacterDto characterDto){
        return ResponseEntity.created(null).body(gotService.createCharacter(characterDto));
    }
}
