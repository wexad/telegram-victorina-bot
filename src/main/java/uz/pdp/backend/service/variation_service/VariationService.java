package uz.pdp.backend.service.variation_service;

import uz.pdp.backend.model.variation.Variation;
import uz.pdp.backend.service.base_service.BaseService;

import java.util.List;

public interface VariationService extends BaseService<Variation> {
    List<Variation> getVariationsByQuestionId(String id);
}
