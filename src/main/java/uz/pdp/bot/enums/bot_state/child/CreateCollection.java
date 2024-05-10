package uz.pdp.bot.enums.bot_state.child;

import uz.pdp.bot.enums.bot_state.State;

import java.util.Stack;

public enum CreateCollection implements State {
    ENTER_NAME_OF_COLLECTION,
    ENTER_QUESTION,
    ENTER_ANSWER,
    CREATE_OR_ANOTHER
}
