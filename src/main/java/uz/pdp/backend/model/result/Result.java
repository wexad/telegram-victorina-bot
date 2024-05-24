package uz.pdp.backend.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class Result extends BaseModel {
    private String username;
    private Long groupId;
    private Integer count;

}
