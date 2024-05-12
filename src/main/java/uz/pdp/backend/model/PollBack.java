package uz.pdp.backend.model;

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
    private Long polId;
    private Long chatId;
    private String username;
    private Integer trueIndex;
}
