package pl.dk.cnavigator;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.dk.cnavigator.model.Content;
import pl.dk.cnavigator.model.ContentObject;
import pl.dk.cnavigator.repo.ContentRecords;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static pl.dk.cnavigator.caslmodel.DocumentProperties.CONTENT_TYPE;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.ITEM_ID;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.OBJECT;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.TYPE;

@Controller
class ContentController {

    private final ContentRecords contentRecords;
    private final ContentCreator contentCreator;

    ContentController(ContentRecords contentRecords, ContentCreator contentCreator) {
        this.contentRecords = contentRecords;
        this.contentCreator = contentCreator;
    }

    // http://localhost:8080/content/26ca2de6-3a9d-45dc-9785-e4063f99d9fb
    @GetMapping("/content/{id}")
    public String content(@PathVariable(name = "id") UUID id, Model model) {
        Document document = contentRecords.findContent(id);
        Content content = contentCreator.from(document);
        String contentHtml = "<div>";
        contentHtml += "<h2>identity</h2><div class = 'paramsC'>" + createUl(content.getIdentity()) + "</div>";
        contentHtml += "<h2>titles</h2><div class = 'paramsC'>" + createUl(content.getTitles()) + "</div>";
        contentHtml += "<h2>params</h2><div class = 'paramsC'>" + createUl(content.getParams()) + "</div>";
        contentHtml += "<h2>objects</h2><div class = 'paramsC'>" + createUl(content.getObjects()) + "</div>";
        contentHtml += "<h2>others</h2><div class = 'paramsC'>" + createUl(content.getOthers()) + "</div>";
        contentHtml += "<h2>edition data</h2><div class = 'paramsC'>" + createUl(content.getEditionData()) + "</div>";
        contentHtml += "<h2>publish data</h2><div class = 'paramsC'>" + createUl(content.getPublishData()) + "</div>";
        contentHtml += "<h2>cmrs</h2><div class = 'paramsC'>" + createUl(content.getCmrs()) + "</div>";
        contentHtml += "<h2>links</h2><div class = 'paramsC' id = 'linksC'>" + createUl(content.getLinks()) + "</div>";
        contentHtml += "</div>";
        model.addAttribute("contentHtml", contentHtml);
        return "content";
    }

    private String createUl(Set<UUID> elements) {
        String contentHtml = "<ul class = 'tree'>";
        int i = 0;
        for (UUID element: elements) {
            contentHtml += createLi("["+ i + "]", element);
            i++;
        }
        contentHtml += "</ul>";
        return contentHtml;
    }

    private String createUl(Map<String, Object> document) {
        String contentHtml = "<ul class = 'tree'>";
        for (String key: document.keySet()) {
            contentHtml += createLi(key, document.get(key));
        }
        contentHtml += "</ul>";
        return contentHtml;
    }

    private String createLi(String key, Object value) {
        if (value instanceof UUID) {
            return  "<li>" + nullableKey(key) + "<a href='/content/" + value + "'>" + value + "</a></li>";
        } else if (value instanceof Map) {
            return  "<li>" + nullableKey(key) + createUl((Map<String, Object>) value);
        } else if (value instanceof List) {
            String contentHtml = "<li>" + nullableKey(key) + "<ul class = 'tree'>";
            List list = (List) value;
            for (int i = 0; i< list.size(); i++) {
                contentHtml += createLi("[" + i + "]", list.get(i));
            }
            contentHtml += "</li></ul>";
            return contentHtml;
        } else if (value instanceof String && isUUID((String) value)) {
            return  "<li>" + nullableKey(key) + "<a href='/content/" + value + "'>" + value + "</a></li>";
        } else if (value instanceof ContentObject) {
            String contentHtml = "<li>[" + key + "]<ul class = 'tree'>";
            ContentObject contentObject = (ContentObject) value;
            contentHtml += "<li>" + nullableKey(ITEM_ID) + contentObject.getItemId() + "</li>";
            contentHtml += "<li>" + nullableKey(CONTENT_TYPE) + contentObject.getContentType() + "</li>";
            contentHtml += "<li>" + nullableKey(OBJECT) + "<a href='/content/" + contentObject.getId() + "' title = '" + contentObject.getId() + "'>"
                    + contentObject.getTitle() + "</a></li>";
            contentHtml += "<li>" + nullableKey(TYPE) + "<a href='/content/" + contentObject.getTypeId() + "' title = '" + contentObject.getTypeId() + "'>"
                    + contentObject.getTypeTitle() + "</a></li>";
            contentHtml += "</ul></li>";
            return contentHtml;
        }

        return  "<li>" +  nullableKey(key) + value + "</li>";
    }

    private boolean isUUID(String maybeUUID) {
        try {
            UUID.fromString(maybeUUID);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String nullableKey(String key) {
        return key == null ? "" : "<span class = 'bold'>" + key + " </span>";
    }

}
