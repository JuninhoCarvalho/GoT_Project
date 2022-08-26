package br.com.inatel.FranciscoJunior_GotProject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String title;
    private String family;
    private String image;
    private String imageUrl;
}
