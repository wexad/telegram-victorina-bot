package uz.pdp.backend.model.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Question extends BaseModel {

    private String collectionId;

    private String text;
}
