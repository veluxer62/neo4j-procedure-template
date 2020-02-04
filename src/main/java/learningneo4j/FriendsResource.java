package learningneo4j;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

@Path("/friends")
public class FriendsResource {
    private GraphDatabaseService graphDb;
    private final ObjectMapper objectMapper;

    private static final RelationshipType FRIEND_OF = RelationshipType.withName("FRIEND_OF");
    private static final Label PERSON = Label.label("Person");

    public FriendsResource(@Context GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
        this.objectMapper = new ObjectMapper();
    }

    @GET
    @Path("/{personName")
    public Response findFriends(@PathParam("personName") final String personName) {
        StreamingOutput stream = outputStream -> {
            JsonGenerator streamingOutput = objectMapper.getJsonFactory().createJsonGenerator(outputStream, JsonEncoding.UTF8);
            streamingOutput.writeStartObject();
            streamingOutput.writeFieldName("friends");
            streamingOutput.writeStartArray();

            try (Transaction tx = graphDb.beginTx();
                 ResourceIterator<Node> persons = graphDb.findNodes(PERSON, "name", personName)) {
                while (persons.hasNext()) {
                    Node person = persons.next();
                    for (Relationship relationshipTo : person.getRelationships(FRIEND_OF, Direction.OUTGOING)) {
                        Node friend = relationshipTo.getEndNode();
                        streamingOutput.writeString(friend.getProperty("name").toString());
                    }
                    for (Relationship relationshipFrom : person.getRelationships(FRIEND_OF, Direction.INCOMING)) {
                        Node friendComing = relationshipFrom.getStartNode();
                        streamingOutput.writeString(friendComing.getProperty("name").toString());
                    }
                }
                tx.success();
            }
            streamingOutput.writeEndArray();
            streamingOutput.writeEndObject();
            streamingOutput.flush();
            streamingOutput.close();
        };
        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
    }
}
