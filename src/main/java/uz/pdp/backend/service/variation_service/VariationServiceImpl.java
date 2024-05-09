package uz.pdp.backend.service.variation_service;

public class VariationServiceImpl implements VariationService {

    private static VariationService variationService;

    public static VariationService getInstance() {
        return variationService == null ? new VariationServiceImpl() : variationService;
    }
}
