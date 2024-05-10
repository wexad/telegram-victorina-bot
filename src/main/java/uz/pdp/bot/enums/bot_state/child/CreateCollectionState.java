package uz.pdp.bot.enums.bot_state.child;

import lombok.Getter;
import uz.pdp.bot.enums.bot_state.State;

@Getter
public enum CreateCollectionState implements State {
    ENTER_NAME_OF_COLLECTION(null),
    ENTER_QUESTION(ENTER_NAME_OF_COLLECTION),
    ENTER_ANSWER(ENTER_QUESTION),
    CREATE_OR_ANOTHER(ENTER_ANSWER);

    public CreateCollectionState pervState;

    CreateCollectionState(CreateCollectionState pervState) {
        this.pervState = pervState;
    }
}
