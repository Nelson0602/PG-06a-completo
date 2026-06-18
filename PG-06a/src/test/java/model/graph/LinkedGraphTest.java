package model.graph;

import model.LinkedList.ListException;
import model.Queue.QueueException;
import model.Stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LinkedGraphTest {

    @Test
    void testLinkedGraphTest() {
        LinkedGraph<Integer> graph = new LinkedGraph<>(true);
        try {
            for (int i = 1; i <= 10; i++) {
                graph.addVertex(i);
            }
            graph.addEdgeAndWeight(1, 2, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(1, 3, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(2, 3, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(2, 5, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(3, 4, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(4, 5, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(6, 7, new Random().nextInt(5, 30));
            System.out.println(graph);
            System.out.println("DFS: " + graph.dfs());
            System.out.println("BFS: " + graph.bfs());
            System.out.println("Remove Vertex: 1");
            graph.removeVertex(1);
            System.out.println("Remove Vertex: 2");
            graph.removeVertex(2);
            System.out.println("Remove Vertex: 3");
            graph.removeVertex(3);
            System.out.println(graph);
        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }
}