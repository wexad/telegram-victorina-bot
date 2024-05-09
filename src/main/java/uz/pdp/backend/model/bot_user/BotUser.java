package uz.pdp.backend.model.bot_user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.backend.enums.bot_state.BotState;
import uz.pdp.backend.model.base_model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class BotUser extends BaseModel {
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private BotState botState;

}
