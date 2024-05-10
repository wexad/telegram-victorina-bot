package uz.pdp.backend.service.variation_service;

import uz.pdp.backend.model.variation.Variation;
import uz.pdp.backend.file_manager.FileManager;

import java.util.ArrayList;
import java.util.List;

public class VariationServiceImpl implements VariationService {

    private static VariationService variationService;

    public static VariationService getInstance() {
        return variationService == null ? new VariationServiceImpl() : variationService;
    }

    private final FileManager<Variation> fileManager;

    private final List<Variation> variations;


    public VariationServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/variations.txt");
        variations = fileManager.load();
    }

    @Override
    public List<Variation> getVariationsByQuestionId(String id) {
        List<Variation> variationList = new ArrayList<>();

        for (Variation variation : variations) {
            if (variation.getQuestionId().equals(id)) {
                variationList.add(variation);
            }
        }

        return variationList;
    }
}
