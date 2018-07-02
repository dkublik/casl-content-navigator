package pl.dk.cnavigator.repo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.UUID;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.CONTENT_TYPE;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.ID;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.LINKS;
import static pl.dk.cnavigator.caslmodel.DocumentProperties.MASTER_LINK;

@Service
public class ContentRecords {


    @Value("${spring.data.mongodb.database}")
    @Getter
    private String databaseName;

    private MongoDatabase mongoDatabase;

    private String contentRecordsCollectionName = "mtvi_content_record";

    private final MongoClient mongoClient;

    ContentRecords(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @PostConstruct
    void getMongoDatabase() {
        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    @PreDestroy
    void destroy() {
        mongoClient.close();
    }

    private MongoCollection<Document> contentRecords() {
        return mongoDatabase.getCollection(contentRecordsCollectionName);
    }


    public Document findContent(UUID id) {
        return contentRecords().find(eq(ID, id))
                .first();
    }

    public FindIterable<Document> findContent(Collection<UUID> ids) {
        return contentRecords()
                .find(in(ID, ids));
    }

    public FindIterable<Document> findByLinks(UUID id) {
        return contentRecords().find(eq(LINKS, id));
    }

    public FindIterable<Document> findByLinksAndContentType(UUID id, String contentType) {
        return contentRecords().find(
                and(
                    eq(CONTENT_TYPE, contentType),
                    eq(LINKS, id)
                ));
    }

    public FindIterable<Document> findCmrs(UUID id) {
        return contentRecords().find(eq(MASTER_LINK, id));
    }


}
