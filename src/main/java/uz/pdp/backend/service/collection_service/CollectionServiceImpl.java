package uz.pdp.backend.service.collection_service;

import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.service.file_manager.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionServiceImpl implements CollectionService {

    private static CollectionService collectionService;

    public static CollectionService getInstance() {
        return collectionService == null ? new CollectionServiceImpl() : collectionService;
    }

    private final FileManager<Collection> fileManager;

    private final List<Collection> collections;

    public CollectionServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/collections.txt");
        this.collections = fileManager.load();
    }

    @Override
    public List<Collection> getUserCollections(BotUser botUser) {
        List<Collection> userCollections = new ArrayList<>();

        for (Collection collection : collections) {
            if (Objects.equals(collection.getUserId(), botUser.getChatId())) {
                userCollections.add(collection);
            }
        }

        return userCollections;
    }

    @Override
    public Collection getCollectionByName(String name) {
        for (Collection collection : collections) {
            if (Objects.equals(collection.getName(), name)) {
                return collection;
            }
        }
        return null;
    }

    @Override
    public void add(Collection collection) {
        collections.add(collection);
    }
}
