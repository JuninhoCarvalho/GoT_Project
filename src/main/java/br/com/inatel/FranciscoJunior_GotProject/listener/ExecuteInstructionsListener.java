package br.com.inatel.FranciscoJunior_GotProject.listener;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExecuteInstructionsListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    GotService gotService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Character> characters = gotService.populateCharactersDb();

        Set<String> familyNames = new HashSet<>();
        characters.forEach(c -> familyNames.add(c.getFamily()));

        gotService.insertFamilys(familyNames);
    }
}
