package uz.pdp.backend.model.base_model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Random;
import java.util.UUID;

@Data
@EqualsAndHashCode
public abstract class BaseModel {

    private Random random = new Random();

    private final Long id;

    public BaseModel() {
        this.id = random.nextLong();
    }

}
