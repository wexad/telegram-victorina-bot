package uz.pdp.backend.model.group;

import lombok.Data;
import uz.pdp.backend.model.base_model.BaseModel;

@Data
public class Group extends BaseModel {

    private Long id;

    private String title;

    private String userName;
}
