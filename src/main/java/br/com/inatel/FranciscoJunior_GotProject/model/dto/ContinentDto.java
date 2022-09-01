package br.com.inatel.FranciscoJunior_GotProject.model.dto;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContinentDto {

    @NotNull
    private Integer id;
    @NotNull
    private String name;

    public ContinentDto(Continent continent) {
        this.id = continent.getId();
        this.name = continent.getName();
    }
}
