package com.redhat.postureKeeper.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.redhat.postureKeeper.sarif.Run;

@ApplicationScoped
public class AnalysisService {

        private static final String DATABASE_NAME = "sarif";
        @Inject MongoClient mongo;
   
        public String add(String workspace, String sarif, String repoUrl, String commit_sha ){
            final Document document = new Document(); 
            document.append("sarif", BasicDBObject.parse(sarif)).
            append("repo", repoUrl).
            append("commit_sha",commit_sha);
            return getCollection(workspace).insertOne(document).getInsertedId().asObjectId().getValue().toString();
        }
        
        public String appendRuns(String workspace, String id, List<Run> runs) {
            final Bson query = Filters.eq("_id", new ObjectId(id));
            final Bson updates = Updates.pushEach("sarif.runs", runs);
            final Document document = getCollection(workspace).findOneAndUpdate(query, updates);
            if(document == null) {
                return null;
            }
            return document.getObjectId("_id").toString();
        }

        public String find( String workspace, String id ) {
            final ObjectId objectId = new ObjectId(id);
            final BasicDBObject query = new BasicDBObject("_id", objectId);
            final Document document = getCollection(workspace).find(query).first();
            if ( document == null ){
                return null;
            }
            Document sarifDoc = (Document)document.get("sarif");
            return sarifDoc.toJson();
        }

        public String findId( String workspace, String repo, String commit ) {
            final BasicDBObject query = new BasicDBObject();
            query.append("repo", repo);
            query.append("commit_sha", commit);
            final Document document = getCollection(workspace).find(query).first();
            if(document == null ){
                return null;
            }
            return document.getObjectId("_id").toString();
        }

        private MongoCollection<Document> getCollection(String workspace){
            return mongo.getDatabase(DATABASE_NAME).getCollection(workspace);
        }

}
