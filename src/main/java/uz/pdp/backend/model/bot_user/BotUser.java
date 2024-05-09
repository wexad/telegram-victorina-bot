package uz.pdp.backend.model.bot_user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.pdp.backend.enums.bot_state.BotState;

@Data
@AllArgsConstructor
@Builder
public class BotUser {
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private BotState botState;

}
