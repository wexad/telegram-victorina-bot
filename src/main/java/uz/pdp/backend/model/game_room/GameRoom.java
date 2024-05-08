package uz.pdp.backend.model.game_room;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameRoom extends BaseModel {

    private Long groupId;

    private String collectionId;

    private int timeForQuiz;

}
