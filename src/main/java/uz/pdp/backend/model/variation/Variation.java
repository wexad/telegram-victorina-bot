package uz.pdp.backend.model.variation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Variation extends BaseModel {
    private String answer;
    private Boolean isTrue;

    private Long questionId;
}
