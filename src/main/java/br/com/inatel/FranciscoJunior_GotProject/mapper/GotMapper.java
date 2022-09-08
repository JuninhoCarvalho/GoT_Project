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

public class GotMapper {

    public static List<Character> toCharacterList(List<CharacterDto> charactersDto){
        return charactersDto.stream().map(GotMapper::toCharacter).collect(Collectors.toList());
    }

    public static List<CharacterDto> toCharacterDtoList(List<Character> characters){
        return characters.stream().map(GotMapper::toCharacterDto).collect(Collectors.toList());
    }

    public static List<FamilyDto> toFamilyDtoList(List<Family> families) {
        return families.stream().map(GotMapper::toFamilyDto).collect(Collectors.toList());
    }

    public static List<Continent> toContinentList(List<ContinentDto> continentsDto){
        return continentsDto.stream().map(GotMapper::toContinent).collect(Collectors.toList());
    }

    public static List<DeadDto> toDeadDtoList(List<Dead> deads){
        return deads.stream().map(GotMapper::toDeadDto).collect(Collectors.toList());
    }

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

    public static Continent toContinent(ContinentDto ct){
        return Continent.builder()
                .id(ct.getId())
                .name(ct.getName())
                .build();
    }

    public static FamilyDto toFamilyDto(Family family){
        return FamilyDto.builder()
                .id(family.getId())
                .name(family.getName())
                .deads(family.getDeads())
                .build();
    }

    public static Dead toDead(DeadDto dd){
        return Dead.builder()
                .name(dd.getName())
                .family(dd.getFamily())
                .continent(dd.getContinent())
                .build();
    }

    public static DeadDto toDeadDto(Dead dd){
        return DeadDto.builder()
                .id(dd.getId())
                .name(dd.getName())
                .family(dd.getFamily())
                .continent(dd.getContinent())
                .build();
    }


}
