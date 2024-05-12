package uz.pdp.backend.service.collection_service;

import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.service.base_service.BaseService;

import java.util.List;

public interface CollectionService extends BaseService<Collection> {

    List<Collection> getUserCollections(BotUser botUser);

    Collection getCollectionByName(String name);

    void add(Collection collection);

    Collection getLastCollectionUser(BotUser myUser);

    Collection getCollectionById(String collectionId);
}
