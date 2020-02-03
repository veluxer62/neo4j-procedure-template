package learningneo4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringHackingFunctionalTest {
    private static final Config driverConfig = Config.build().withoutEncryption().toConfig();
    private ServerControls embeddedDatabaseServer;

    @BeforeAll
    void initializeNeo4j() {
        this.embeddedDatabaseServer = TestServerBuilders
                .newInProcessBuilder()
                .withFunction(StringHacking.class)
                .newServer();
    }

    @Test
    public void regroup_by_last_name() {
        try (Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);
             Session session = driver.session()) {
            session.run("MATCH (p:Person) DETACH DELETE (p)");
            session.run("CREATE (p:Person {name: \"John Smith\"})");
            session.run("CREATE (p:Person {name: \"Juan Carlos\"})");
            session.run("CREATE (p:Person {name: \"Mike\"})");

            session.run("CREATE CONSTRAINT ON (ln:LastName) ASSERT ln.lastName IS UNIQUE");
            session.run("MATCH (ln:LastName) DETACH DELETE (ln)");

            session.run(
                    "MATCH (p:Person) " +
                            "WITH learningneo4j.getLastWord(p.name) as lw, p " +
                            "MERGE (ln:LastName {lastName: lw}) " +
                            "WITH ln,p,lw " +
                            "CREATE (p)-[:IS_NAMED]->(ln)"
            );

            List<String> result = session.run("MATCH (p:Person)-[IS_NAMED]->(ln:LastName) RETURN ln.lastName")
                    .list()
                    .stream()
                    .map(it -> it.values().get(0).asString())
                    .collect(Collectors.toList());

            assertThat(result).isNotEmpty();
            assertThat(result).contains("Smith");
            assertThat(result).contains("Carlos");
            assertThat(result).contains("Mike");
        }
    }
}
