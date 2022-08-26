package br.com.inatel.FranciscoJunior_GotProject.model.dto;

import br.com.inatel.FranciscoJunior_GotProject.model.entity.Continent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContinentDto {

    private Integer id;
    private String name;

    public ContinentDto(Continent continent) {
        this.id = continent.getId();
        this.name = continent.getName();
    }
}
