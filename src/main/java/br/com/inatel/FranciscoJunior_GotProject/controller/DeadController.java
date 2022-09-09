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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/deads")
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

    @GetMapping("/family")
    @ResponseStatus(HttpStatus.OK)
    public Page<FamilyDto> listDeadsPerFamily(@PageableDefault(sort = "name", direction = Sort.Direction.ASC,
            page = 0, size = 20) Pageable page){
        return gotService.findDeadsPerFamily(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeadDto includeNewDead(@RequestBody @Valid DeadDto deadDto) {
        return gotService.includeNewDead(deadDto);
    }
}
