package br.com.inatel.FranciscoJunior_GotProject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyDto {

    private Integer id;
    private String name;
    private Integer deads;
}
