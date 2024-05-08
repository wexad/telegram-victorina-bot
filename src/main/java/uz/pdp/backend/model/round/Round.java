package uz.pdp.backend.model.round;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;


@EqualsAndHashCode(callSuper = true)
@Data
public class Round extends BaseModel {

    private String gameRoomId;

    private String questionId;

    private String winnerId;
}
