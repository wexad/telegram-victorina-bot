package uz.pdp.backend.model.group;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class Group extends BaseModel {

    private Long chatId;

    private String title;

    private String userName;
}
