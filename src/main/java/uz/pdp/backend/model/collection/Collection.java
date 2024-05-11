package uz.pdp.backend.model.collection;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Collection extends BaseModel {
    private String name;
    private String userName;
    private Boolean isFinished;
}
