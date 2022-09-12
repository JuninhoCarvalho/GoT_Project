package br.com.inatel.FranciscoJunior_GotProject.mapper;


import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Dead;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class with static methods for conversions
 * @author francisco.carvalho
 * @since 1.0
 */
public class GotMapper {

    /**
     * @param charactersDto
     * @return Character list
     */
    public static List<Character> toCharacterList(List<CharacterDto> charactersDto){
        return charactersDto.stream().map(GotMapper::toCharacter).collect(Collectors.toList());
    }

    /**
     * @param characters
     * @return CharacterDto list
     */
    public static List<CharacterDto> toCharacterDtoList(List<Character> characters){
        return characters.stream().map(GotMapper::toCharacterDto).collect(Collectors.toList());
    }

    /**
     * @param characters
     * @return CharacterDto page
     */
    public static Page<CharacterDto> toCharacterDtoPage(Page<Character> characters){
        return characters.map(GotMapper::toCharacterDto);
    }

    /**
     * @param families
     * @return FamilyDto page
     */
    public static Page<FamilyDto> toFamilyDtoPage(Page<Family> families) {
        return families.map(GotMapper::toFamilyDto);
    }

    /**
     * @param deads
     * @return DeadDto list
     */
    public static List<DeadDto> toDeadDtoList(List<Dead> deads){
        return deads.stream().map(GotMapper::toDeadDto).collect(Collectors.toList());
    }


    /**
     * @param ch CharacterDto
     * @return Character
     */
    public static Character toCharacter(CharacterDto ch) {
        return Character.builder()
                .firstName(ch.getFirstName())
                .lastName(ch.getLastName())
                .fullName(ch.getFullName())
                .title(ch.getTitle())
                .family(ch.getFamily())
                .image(ch.getImage())
                .imageUrl(ch.getImageUrl())
                .build();
    }

    /**
     * @param ch Character
     * @return CharacterDto
     */
    public static CharacterDto toCharacterDto(Character ch) {
        return CharacterDto.builder()
                .id(ch.getId())
                .firstName(ch.getFirstName())
                .lastName(ch.getLastName())
                .fullName(ch.getFullName())
                .title(ch.getTitle())
                .family(ch.getFamily())
                .image(ch.getImage())
                .imageUrl(ch.getImageUrl())
                .build();
    }

    /**
     * @param family
     * @return FamilyDto
     */
    public static FamilyDto toFamilyDto(Family family){
        return FamilyDto.builder()
                .id(family.getId())
                .name(family.getName())
                .deads(family.getDeads())
                .build();
    }

    /**
     * @param dd DeadDto
     * @return Dead
     */
    public static Dead toDead(DeadDto dd){
        return Dead.builder()
                .name(dd.getName())
                .family(dd.getFamily())
                .continent(dd.getContinent())
                .build();
    }

    /**
     * @param dd Dead
     * @return DeadDto
     */
    public static DeadDto toDeadDto(Dead dd){
        return DeadDto.builder()
                .id(dd.getId())
                .name(dd.getName())
                .family(dd.getFamily())
                .continent(dd.getContinent())
                .build();
    }
}
