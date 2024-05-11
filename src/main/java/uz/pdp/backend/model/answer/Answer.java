package uz.pdp.backend.model.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Answer extends BaseModel {
    private String text;
    private Boolean isTrue;

    private String questionId;
}
