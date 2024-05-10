package uz.pdp.bot.enums.bot_state.base;

import lombok.Getter;
import uz.pdp.bot.enums.bot_state.State;
@Getter
public enum BaseState implements State {
    MAIN_STATE,
    MY_COLLECTIONS,
    CREATE_COLLECTION
}
