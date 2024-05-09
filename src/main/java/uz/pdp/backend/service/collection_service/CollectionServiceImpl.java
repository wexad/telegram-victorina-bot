package uz.pdp.backend.service.collection_service;

public class CollectionServiceImpl implements CollectionService {

    private static CollectionService collectionService;

    public static CollectionService getInstance() {
        return collectionService == null ? new CollectionServiceImpl() : collectionService ;
    }

    @Override
    public void show(String str) {
        System.out.println(str);
    }
}
