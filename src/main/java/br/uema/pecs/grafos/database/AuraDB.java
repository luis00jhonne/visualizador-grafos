package br.uema.pecs.grafos.database;

import br.uema.pecs.grafos.MatrizAdjacencia;
import org.neo4j.driver.*;

import java.util.Map;

public class AuraDB {

    private Driver driver;

    public AuraDB() {
        final String dbUri = "neo4j+s://820ac1b3.databases.neo4j.io";
        final String dbUser = "neo4j";
        final String dbPassword = "uYjPaIeFU8aqg_K1-BTqtlEFY366hjFyHD9Ta-uoB2E";
        connect(dbUri, dbUser, dbPassword);
    }

    public void connect(String dbUri, String dbUser, String dbPassword) {
        // URI examples: "neo4j://localhost", "neo4j+s://xxx.databases.neo4j.io"
        driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword));
    }

    public void criarAuraDBNodes(MatrizAdjacencia matriz){
        for (int x = 0; x < matriz.getTamanho(); x++) {
            for (int y = x; y < matriz.getTamanho(); y++) {
                if (matriz.verificarAdjacencia(x, y)) {
                    addNode(x, y);
                }
            }
        }
    }

    public void addNode(int node1, int node2){
        try (var session = driver.session(SessionConfig.builder().withDatabase("neo4j").build())) {
            try {
                session.executeWrite(tx -> createNodesAndNeighboor(tx, node1, node2));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }



    static String createNodesAndNeighboor(TransactionContext tx, int node1, int node2) {
        createNode(tx, node1);
        createNode(tx, node2);
        addNodeNeighboor(tx, node1, node2);
        return "OK";
    }

    static void createNode(TransactionContext tx, int node1) {
        tx.run("MERGE (n1:Node {id: $node1})", Map.of("node1", node1));
    }

    static void addNodeNeighboor(TransactionContext tx, int node1, int node2) {
        System.out.printf("Criando vizinhos para %d e %d%n", node1, node2);
        tx.run("""
            MATCH (o:Node {id: $node1})
            MATCH (p:Node {id: $node2})
            MERGE (o)-[:EDGES]->(p)
            """, Map.of("node1", node1, "node2", node2)
        );
    }
}
