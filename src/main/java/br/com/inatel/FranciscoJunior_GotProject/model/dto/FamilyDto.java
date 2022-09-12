package br.com.inatel.FranciscoJunior_GotProject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object for Family Class
 * @author francisco.carvalho
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyDto {

    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private Integer deads;
}
