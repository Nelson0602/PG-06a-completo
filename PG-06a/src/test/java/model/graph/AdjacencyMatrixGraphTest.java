package model.graph;

import model.LinkedList.ListException;
import model.Queue.QueueException;
import model.Stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    @Test
    void testAdjacencyMatrixGraph() throws GraphException, ListException, StackException, QueueException {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10, false);
        for (int i = 1; i <= 5; i++) {
            graph.addVertex(i);
        }
        graph.addEdgeAndWeight(1, 2, new Random().nextInt(5, 30));
        graph.addEdgeAndWeight(1, 3, new Random().nextInt(5, 30));
        graph.addEdgeAndWeight(2, 3, new Random().nextInt(5, 30));
        graph.addEdgeAndWeight(2, 5, new Random().nextInt(5, 30));
        graph.addEdgeAndWeight(3, 4, new Random().nextInt(5, 30));
        graph.addEdgeAndWeight(4, 5, new Random().nextInt(5, 30));
        System.out.println(graph);
        System.out.println("DFS Transversal Tour: " + graph.dfs());
        System.out.println("BFS Transversal Tour: " + graph.bfs());

        graph.removeVertex(1);
        graph.removeVertex(2);
        graph.removeVertex(3);

        graph.addVertex(6);
        graph.addVertex(7);
        graph.addEdgeAndWeight(4, 7, new Random().nextInt(5, 30));
        graph.addEdgeAndWeight(5, 6, new Random().nextInt(5, 30));

        graph.removeEdge(4, 5);
        graph.removeEdge(5, 6);
    }

    @Test
    void testDfsBfsUndirected() throws Exception {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(5, false);
        for (int i = 0; i <= 4; i++) graph.addVertex(i);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);

        assertEquals("0, 1, 4, 2, 3, ", graph.dfs());
        assertEquals("0, 1, 2, 3, 4, ", graph.bfs());
    }

    @Test
    void testDegreesAndEdgesUndirected() throws Exception {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(5, false);
        for (int i = 0; i <= 4; i++) graph.addVertex(i);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);

        assertEquals(3, graph.getGraphDegree());
        assertEquals(5, graph.totalEdges());

        assertEquals(3, graph.getVertexDegree(0));
        assertEquals(3, graph.totalEdges(0));
        assertEquals("1 2 3", graph.getEdges(0));

        assertEquals(2, graph.getVertexDegree(1));
        assertEquals(2, graph.totalEdges(1));
        assertEquals("0 4", graph.getEdges(1));

        assertEquals(2, graph.getVertexDegree(2));
        assertEquals(2, graph.totalEdges(2));
        assertEquals("0 3", graph.getEdges(2));

        assertEquals(2, graph.getVertexDegree(3));
        assertEquals(2, graph.totalEdges(3));
        assertEquals("0 2", graph.getEdges(3));

        assertEquals(1, graph.getVertexDegree(4));
        assertEquals(1, graph.totalEdges(4));
        assertEquals("1", graph.getEdges(4));
    }

    @Test
    void testDegreesAndEdgesDirected() throws Exception {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(5, true);
        for (int i = 0; i <= 4; i++) graph.addVertex(i);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);

        assertEquals(3, graph.getGraphDegree());
        assertEquals(5, graph.totalEdges());

        assertEquals(3, graph.getVertexDegree(0));
        assertEquals(3, graph.totalEdges(0));
        assertEquals("1 2 3", graph.getEdges(0));

        assertEquals(2, graph.getVertexDegree(1));
        assertEquals(1, graph.totalEdges(1));
        assertEquals("4", graph.getEdges(1));

        assertEquals(2, graph.getVertexDegree(2));
        assertEquals(1, graph.totalEdges(2));
        assertEquals("3", graph.getEdges(2));

        assertEquals(2, graph.getVertexDegree(3));
        assertEquals(0, graph.totalEdges(3));
        assertEquals("The vertex has no edges", graph.getEdges(3));

        assertEquals(1, graph.getVertexDegree(4));
        assertEquals(0, graph.totalEdges(4));
        assertEquals("The vertex has no edges", graph.getEdges(4));
    }
}