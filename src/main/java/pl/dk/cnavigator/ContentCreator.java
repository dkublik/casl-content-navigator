package pl.dk.cnavigator;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.springframework.stereotype.Service;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.*;
import pl.dk.cnavigator.model.Content;
import pl.dk.cnavigator.model.ContentObject;
import pl.dk.cnavigator.repo.ContentRecords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ContentCreator {

    private final ContentRecords contentRecords;

    ContentCreator(ContentRecords contentRecords) {
        this.contentRecords = contentRecords;
    }

    Content from(Map<String, Object> document) {
        Content content = new Content();
        moveTo(document, content.getIdentity(), ID);
        moveTo(document, content.getIdentity(), CONTENT_TYPE);
        moveTo(document, content.getIdentity(), SHORT_ID);
        moveTo(document, content.getIdentity(), URL_KEY);

        moveTo(document, content.getTitles(), TITLE);
        moveTo(document, content.getTitles(), TITLE_CI);
        moveTo(document, content.getTitles(), TITLE_CS);
        moveTo(document, content.getTitles(), TITLE_MTVI);
        moveTo(document, content.getTitles(), TITLE_CI_MTVI);

        moveParams(document, content.getParams());

        moveObjects(document, content.getObjects());

        content.getOthers().putAll(document);
        return content;
    }

    private void moveTo(Map<String, Object> source, Map<String, Object> target, String property) {
        if (source.containsKey(property)) {
            target.put(property, source.remove(property));
        }
    }

    private void moveParams(Map<String, Object> source, Map<String, Object> target) {
        if (source.containsKey(PARAMS)) {
            List<Map<String, Object>> sourceParams = (List<Map<String, Object>>) source.remove(PARAMS);
            for (Map<String, Object> paramDoc: sourceParams) {
                target.put((String) paramDoc.get(PARAMS_NAME), paramDoc.get(PARAMS_VALUE));
            }
        }
    }

    private void moveObjects(Map<String, Object> source, Map<String, Object> target) {
        if (source.containsKey(OBJECTS)) {
            List<Map<String, Object>> sourceObjects = (List<Map<String, Object>>) source.remove(OBJECTS);

            Map<UUID, Document> cached = fetchObjects(sourceObjects);

            for (Map<String, Object> objectDoc: sourceObjects) {
                String objectTitle = (String) cached.get(objectDoc.get(OBJECT)).get(TITLE);
                String typeTitle = (String) cached.get(objectDoc.get(TYPE)).get(TITLE);
                ContentObject contentObject = new ContentObject();
                contentObject.setItemId((String) objectDoc.get(ITEM_ID));
                contentObject.setContentType((String) objectDoc.get(CONTENT_TYPE));
                contentObject.setId((UUID) objectDoc.get(OBJECT));
                contentObject.setTitle(objectTitle);
                contentObject.setTypeId((UUID) objectDoc.get(OBJECT));
                contentObject.setTypeTitle(typeTitle);
                target.put((String) objectDoc.get(ITEM_ID), contentObject);
            }
        }
    }

    private Map<UUID, Document> fetchObjects(List<Map<String, Object>> sourceObjects) {
        List<UUID> idsToFetch = new ArrayList<>();
        for (Map<String, Object> objectDoc: sourceObjects) {
            idsToFetch.add((UUID) objectDoc.get(OBJECT));
            idsToFetch.add((UUID) objectDoc.get(TYPE));
        }

        List<Document> fetched = contentRecords.findContent(idsToFetch).into(new ArrayList<>());
        return fetched.stream()
                .collect(Collectors.toMap(d -> (UUID) d.get(ID), Function.identity()));
    }

}
