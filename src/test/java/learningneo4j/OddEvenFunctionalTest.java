package learningneo4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OddEvenFunctionalTest {

    private static final Config driverConfig = Config.build().withoutEncryption().toConfig();
    private ServerControls embeddedDatabaseServer;

    @BeforeAll
    void initializeNeo4j() {

        this.embeddedDatabaseServer = TestServerBuilders
                .newInProcessBuilder()
                .withFunction(OddEven.class)
                .newServer();
    }

    @Test
    public void isOdd_should_return_odd_value_node() {
        try(Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);
            Session session = driver.session())
        {
            session.run( "CREATE (p:Test {val: 7})" );
            session.run( "CREATE (p:Test {val: 15})" );
            session.run( "CREATE (p:Test {val: 16})" );


            List<Record> list = session
                    .run("MATCH (t:Test) WHERE learningneo4j.isOdd(t.val) RETURN t")
                    .list();

            assertThat(list.size()).isEqualTo(2);

            session.run("MATCH (t:Test) delete t");
        }
    }

    @Test
    public void isEven_should_return_odd_value_node() {
        try(Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);
            Session session = driver.session())
        {
            session.run( "CREATE (p:Test {val: 7})" );
            session.run( "CREATE (p:Test {val: 15})" );
            session.run( "CREATE (p:Test {val: 16})" );


            List<Record> list = session
                    .run("MATCH (t:Test) WHERE learningneo4j.isEven(t.val) RETURN t")
                    .list();

            assertThat(list.size()).isEqualTo(1);

            session.run("MATCH (t:Test) delete t");
        }
    }

}