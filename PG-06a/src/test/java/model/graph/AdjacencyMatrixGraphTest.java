package model.graph;

import model.LinkedList.ListException;
import model.Queue.QueueException;
import model.Stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    @Test
    void testAdjacencyMatrixGraph() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10, false);
        try {
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

            System.out.println("Remove vertex: 1");
            graph.removeVertex(1);
            System.out.println("Remove vertex: 2");
            graph.removeVertex(2);
            System.out.println("Remove vertex: 3");
            graph.removeVertex(3);
            System.out.println(graph);

            graph.addVertex(6);
            graph.addVertex(7);
            graph.addEdgeAndWeight(4, 7, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(5, 6, new Random().nextInt(5, 30));
            System.out.println(graph.printMatrix());
            System.out.println();
            System.out.println(graph);

            System.out.println("\nRemove edge: 4--5");
            graph.removeEdge(4, 5);
            System.out.println("Remove edge: 5--6");
            graph.removeEdge(5, 6);

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
        System.out.println(graph);
    }
}
