package uz.pdp.backend.service.collection_service;

import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionServiceImpl implements CollectionService {

    private static CollectionService collectionService;

    public static CollectionService getInstance() {
        return collectionService == null ? new CollectionServiceImpl() : collectionService;
    }

    private final FileManager<Collection> fileManager;

    public CollectionServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/collections.txt");
    }

    @Override
    public List<Collection> getUserCollections(BotUser botUser) {
        List<Collection> collections = fileManager.load(Collection.class);

        List<Collection> userCollections = new ArrayList<>();

        for (Collection collection : collections) {
            if (Objects.equals(collection.getUserName(), botUser.getUserName())) {
                userCollections.add(collection);
            }
        }

        return userCollections;
    }

    @Override
    public Collection getCollectionByName(String name) {
        List<Collection> collections = fileManager.load(Collection.class);
        for (Collection collection : collections) {
            if (Objects.equals(collection.getName(), name)) {
                return collection;
            }
        }
        return null;
    }

    @Override
    public void add(Collection collection) {
        List<Collection> collections = fileManager.load(Collection.class);
        collections.add(collection);

        fileManager.write(collections, Collection.class);
    }

    @Override
    public Collection getLastCollectionUser(BotUser myUser) {
        List<Collection> collections = fileManager.load(Collection.class);

        for (Collection collection : collections) {
            if (collection.getUserName().equals(myUser.getUserName()) && !collection.getIsFinished()) {
                return collection;
            }
        }
        return null;
    }
}
