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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Controller class where the endpoints to manipulate the characters will be made
 * @author francisco.carvalho
 * @since 1.0
 */
@RestController
@RequestMapping("/characters")
public class CharacterController {

    GotService gotService;

    @Autowired
    public CharacterController(GotService gotService) {
        this.gotService = gotService;
    }

    /**
     * @param page
     * @return All Characters with their information
     */
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = CharacterDto.class),
            @ApiResponse(code = 400, message = "Bad Request: External Api Connection fail."),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CharacterDto> listAllCharacters(@PageableDefault(page = 0, size = 25) Pageable page){
        return gotService.findAllCharacters(page);
    }

    /**
     * @param name
     * @return Specific character, searched by full name
     */
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = CharacterDto.class),
            @ApiResponse(code = 404, message = "Not Found: Character not found in the database."),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDto listCharacter(@PathVariable @Valid String name){
        return gotService.findCharacter(name);
    }

    /**
     * Create a new Character if the information is valid and save in database
     * @param characterDto
     * @return created character
     */
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

    /**
     * @param fullName
     * @return deleted message if name is valid
     */
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
