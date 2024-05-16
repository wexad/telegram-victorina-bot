package uz.pdp.backend.model.base_model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

@Data
@EqualsAndHashCode
public abstract class BaseModel implements Serializable {

    private final String id;

    public BaseModel() {
        this.id = UUID.randomUUID().toString();
    }
}
