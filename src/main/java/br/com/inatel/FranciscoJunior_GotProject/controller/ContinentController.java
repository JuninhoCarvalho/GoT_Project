package br.com.inatel.FranciscoJunior_GotProject.controller;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Continents")
public class ContinentController {

    @Autowired
    GotService gotService;

    @GetMapping
    public ResponseEntity<List<Continent>> listContinents(){
        return ResponseEntity.ok(gotService.findAllContinents());
    }



}
