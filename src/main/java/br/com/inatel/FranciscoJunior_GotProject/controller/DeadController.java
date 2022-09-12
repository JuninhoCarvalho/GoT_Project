package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class where the endpoints to manipulate the deads will be made
 * @author francisco.carvalho
 * @since 1.0
 */
@RestController
@RequestMapping("/deads")
public class DeadController {

    GotService gotService;

    @Autowired
    public DeadController(GotService gotService) {
        this.gotService = gotService;
    }

    /**
     * @return All deads inserted into the database
     */
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = DeadDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DeadDto> listAllDeads(){
        return gotService.findAllDeads();
    }

    /**
     * @param page
     * @return List of families and their numbers of dead
     */
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = FamilyDto.class),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @GetMapping("/family")
    @ResponseStatus(HttpStatus.OK)
    public Page<FamilyDto> listDeadsPerFamily(@PageableDefault(sort = "name", direction = Sort.Direction.ASC,
            page = 0, size = 20) Pageable page){
        return gotService.findDeadsPerFamily(page);
    }

    /**
     * @param deadDto
     * @return The dead character if the information is valid
     */
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = DeadDto.class),
            @ApiResponse(code = 400, message = "Bad Request: Character No Belongs to that family."),
            @ApiResponse(code = 404, message = "Not Found: Character, continent or family Not Found."),
            @ApiResponse(code = 409, message = "Conflict: Character Already inserted in database."),
            @ApiResponse(code = 500, message = "Internal Server Error: Exception Specialized Message."),
            @ApiResponse(code = 503, message = "Service Unavailable: JDBC Connection fail.")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeadDto includeNewDead(@RequestBody @Valid DeadDto deadDto) {
        return gotService.includeNewDead(deadDto);
    }
}
