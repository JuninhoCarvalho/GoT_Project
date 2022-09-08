package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Deads")
public class DeadController {

    GotService gotService;

    @Autowired
    public DeadController(GotService gotService) {
        this.gotService = gotService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DeadDto> listAllDeads(){
        return gotService.findAllDeads();
    }

    @GetMapping("/Family")
    @ResponseStatus(HttpStatus.OK)
    public List<FamilyDto> listDeadsPerFamily(){
        return gotService.findDeadsPerFamily();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeadDto includeNewDead(@RequestBody DeadDto deadDto) {
        return gotService.includeNewDead(deadDto);
    }
}
