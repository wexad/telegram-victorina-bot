package uz.pdp.backend.model.poll_back;

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
public class PollBack extends BaseModel implements Serializable {
    private String pollId;
    private Long chatId;
    private Integer trueIndex;
}
