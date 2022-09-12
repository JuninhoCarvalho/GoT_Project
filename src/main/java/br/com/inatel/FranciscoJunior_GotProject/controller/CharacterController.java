package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = CharacterDto.class),
            @ApiResponse(code = 400, message = "Bad Request: External Api Connection fail."),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CharacterDto> listAllCharacters(@PageableDefault(page = 0, size = 25) Pageable page){
        return gotService.findAllCharacters(page);
    }

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = CharacterDto.class),
            @ApiResponse(code = 404, message = "Not Found: Character not found in the database."),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDto listCharacter(@PathVariable @Valid String name){
        return gotService.findCharacter(name);
    }

    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = CharacterDto.class),
        @ApiResponse(code = 404, message = "Not Found: Family not found in the database."),
        @ApiResponse(code = 409, message = "Conflict: Character already exist in the database."),
        @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
        @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterDto createACharacter(@RequestBody @Valid CharacterDto characterDto){
        return gotService.createCharacter(characterDto);
    }

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = String.class),
            @ApiResponse(code = 404, message = "Not Found: Character not found in the database."),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @DeleteMapping("/delete/{fullName}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCharacter(@PathVariable String fullName){
        return gotService.deleteCharacter(fullName);
    }
}
