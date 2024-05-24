package uz.pdp.backend.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;
import uz.pdp.bot.enums.GameStatus;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Game extends BaseModel {

    private Long groupId;

    private String collectionId;

    private Integer timeForQuiz;

    private GameStatus status;

}
