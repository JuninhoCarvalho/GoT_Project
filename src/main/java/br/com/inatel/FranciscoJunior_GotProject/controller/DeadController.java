package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Deads")
public class DeadController {

    @Autowired
    GotService gotService;

    @GetMapping
    public ResponseEntity<List<DeadDto>> listAllDeads(){
        return ResponseEntity.ok(gotService.findAllDeads());
    }

    @GetMapping("/Family")
    public ResponseEntity<Page<FamilyDto>> listDeadsPerFamily(@RequestParam int page, @RequestParam int qtd, @RequestParam String sort){
        return ResponseEntity.ok(gotService.findDeadsPerFamily(page, qtd, sort));
    }

    @PostMapping
    public ResponseEntity<DeadDto> includeNewDead(@RequestBody DeadDto deadDto) {
        return ResponseEntity.created(null).body(gotService.includeNewDead(deadDto));
    }
}
