package br.com.inatel.FranciscoJunior_GotProject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object for Character Class
 * @author francisco.carvalho
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterDto {

    private Integer id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String fullName;
    @NotNull
    private String title;
    @NotNull
    private String family;
    @NotNull
    private String image;
    @NotNull
    private String imageUrl;
}
