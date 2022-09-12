package br.com.inatel.FranciscoJunior_GotProject.listener;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.service.GotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Listener class, used to execute commands once the application is ready
 * @author francisco.carvalho
 * @since 1.0
 */
@Component
public class ExecuteInstructionsListener implements ApplicationListener<ApplicationReadyEvent> {

    GotService gotService;

    @Autowired
    public ExecuteInstructionsListener(GotService gotService) {
        this.gotService = gotService;
    }

    /**
     * The method will populate de database with chacters provided by the external api and create the initial families
     * @param event the event to respond to when the application is ready
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Character> characters = gotService.populateCharactersDb();

        Set<String> familyNames = new HashSet<>();
        characters.forEach(c -> familyNames.add(c.getFamily()));

        gotService.insertFamily(familyNames);
    }
}
