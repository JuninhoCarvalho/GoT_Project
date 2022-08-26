package br.com.inatel.FranciscoJunior_GotProject.mapper;


import br.com.inatel.FranciscoJunior_GotProject.model.dto.CharacterDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.ContinentDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.DeadDto;
import br.com.inatel.FranciscoJunior_GotProject.model.dto.FamilyDto;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Character;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Dead;
import br.com.inatel.FranciscoJunior_GotProject.model.entity.Family;

import java.util.List;
import java.util.stream.Collectors;

public class GotMapper {

    public static List<Character> toCharacterList(List<CharacterDto> charactersDto){
        return charactersDto.stream().map(characterDto -> toCharacter(characterDto)).collect(Collectors.toList());
    }

    public static List<CharacterDto> toCharacterDtoList(List<Character> characters){
        return characters.stream().map(character -> toCharacterDto(character)).collect(Collectors.toList());
    }

    public static List<Continent> toContinentList(List<ContinentDto> continentsDto){
        return continentsDto.stream().map(continents -> toContinent(continents)).collect(Collectors.toList());
    }

    public static List<ContinentDto> toContinentDtoList(List<Continent> continents){
        return continents.stream().map(cts -> toContinentDto(cts)).collect(Collectors.toList());
    }

    public static List<Family> toFamilyList(List<FamilyDto> familyDtos){
        return familyDtos.stream().map(familyDto -> toFamily(familyDto)).collect(Collectors.toList());
    }

    public static List<FamilyDto> toFamilyDtoList(List<Family> families){
        return families.stream().map(family -> toFamilyDto(family)).collect(Collectors.toList());
    }

    public static List<Dead> toDeadList(List<DeadDto> deadsDto){
        return deadsDto.stream().map(dDto -> toDead(dDto)).collect(Collectors.toList());
    }

    public static List<DeadDto> toDeadDtoList(List<Dead> deads){
        return deads.stream().map(dd -> toDeadDto(dd)).collect(Collectors.toList());
    }

    public static Character toCharacter(CharacterDto ch) {
        Character character = Character.builder()
                .firstName(ch.getFirstName())
                .lastName(ch.getLastName())
                .fullName(ch.getFullName())
                .title(ch.getTitle())
                .family(ch.getFamily())
                .image(ch.getImage())
                .imageUrl(ch.getImageUrl())
                .build();

        return character;
    }

    public static CharacterDto toCharacterDto(Character ch) {
        CharacterDto characterDto = CharacterDto.builder()
                .firstName(ch.getFirstName())
                .lastName(ch.getLastName())
                .fullName(ch.getFullName())
                .title(ch.getTitle())
                .family(ch.getFamily())
                .image(ch.getImage())
                .imageUrl(ch.getImageUrl())
                .build();

        return characterDto;
    }

    public static Continent toContinent(ContinentDto ct){
        Continent continent = Continent.builder()
                .id(ct.getId())
                .name(ct.getName())
                .build();

        return continent;
    }

    public static ContinentDto toContinentDto(Continent ct){
        ContinentDto continentDto = ContinentDto.builder()
                .id(ct.getId())
                .name(ct.getName())
                .build();

        return continentDto;
    }

    public static Family toFamily(FamilyDto familyDto){
        Family family = Family.builder()
                .id(familyDto.getId())
                .name(familyDto.getName())
                .deads(familyDto.getDeads())
                .build();

        return family;
    }

    public static FamilyDto toFamilyDto(Family family){
        FamilyDto familyDto = FamilyDto.builder()
                .id(family.getId())
                .name(family.getName())
                .deads(family.getDeads())
                .build();

        return familyDto;
    }

    public static Dead toDead(DeadDto dd){
        Dead dead = Dead.builder()
                .name(dd.getName())
                .family(dd.getFamily())
                .build();

        return dead;
    }

    public static DeadDto toDeadDto(Dead dd){
        DeadDto deadDto = DeadDto.builder()
                .name(dd.getName())
                .family(dd.getFamily())
                .build();

        return deadDto;
    }
}
