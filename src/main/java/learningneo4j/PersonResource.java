package learningneo4j;

import org.neo4j.graphdb.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/persons")
public class PersonResource {

    private final GraphDatabaseService database;

    public PersonResource(@Context GraphDatabaseService database) {
        this.database = database;
    }

    @GET
    @Path("/lastnames")
    @Produces(MediaType.TEXT_PLAIN)
    public String getLastNames() {
        try(Transaction tx = database.beginTx()) {
            ResourceIterator<Node> nodes = database
                    .findNodes(Label.label("LastName"));

            List<String> lastNames = nodes
                    .stream()
                    .map(it -> (String) it.getProperty("lastName"))
                    .collect(Collectors.toList());

            tx.success();
            return lastNames.toString();
        }
    }
}
