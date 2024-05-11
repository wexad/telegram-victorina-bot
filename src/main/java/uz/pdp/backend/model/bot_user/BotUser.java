package uz.pdp.backend.model.bot_user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.model.base_model.BaseModel;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class BotUser extends BaseModel implements Serializable {
    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private String baseState;
    private String subState;
}
