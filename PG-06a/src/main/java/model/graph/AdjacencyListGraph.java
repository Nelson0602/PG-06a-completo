package model.graph;

import model.LinkedList.ListException;
import model.Node;
import model.Queue.QueueException;
import model.Stack.StackException;

public class AdjacencyListGraph<T extends Comparable<T>> extends AdjacencyMatrixGraph<T> {

    public AdjacencyListGraph(int n, boolean directed) {
        super(n, directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency List Graph is Empty");
        Vertex<T> vertexA = getVertex(a);
        if(vertexA == null) return false;
        boolean getVertexA = getNodeNeighbor(vertexA.headNode, b) != null;
        boolean getVertexB = false;
        if(!directed) {
            Vertex<T> vertexB = getVertex(b);
            if(vertexB != null)
                getVertexB = getNodeNeighbor(vertexB.headNode, a) != null;
        }
        return !directed ? getVertexA && getVertexB : getVertexA;
    }

    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode == null) return null;
        Node<T> aux = headNode;
        while(aux != null){
            if(aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;
        }
        return null;
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, null);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode = addNeighbor(vertexB.headNode, a, null);
            }
            adjacencyMatrix[indexOf(a)][indexOf(b)] = (T) Integer.valueOf(1);
            if(!directed) adjacencyMatrix[indexOf(b)][indexOf(a)] = (T) Integer.valueOf(1);
        }
    }

    private Node<T> addNeighbor(Node<T> headNode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if(headNode == null)
            headNode = node;
        else{
            Node<T> aux = headNode;
            while(aux.neighbor != null)
                aux = aux.neighbor;
            aux.neighbor = node;
        }
        return headNode;
    }

    private Vertex<T> getVertex(T element) {
        for (int i = 0; i < counter; i++)
            if(equals(vertexList[i].data, element)) return vertexList[i];
        return null;
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            Node<T> nodeNeighbor = getNodeNeighbor(vertexA.headNode, b);
            if(nodeNeighbor != null) nodeNeighbor.weight = weight;
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                Node<T> nodeNeighborB = getNodeNeighbor(vertexB.headNode, a);
                if(nodeNeighborB != null) nodeNeighborB.weight = weight;
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, weight);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode = addNeighbor(vertexB.headNode, a, weight);
            }
            adjacencyMatrix[indexOf(a)][indexOf(b)] = weight;
            if(!directed) adjacencyMatrix[indexOf(b)][indexOf(a)] = weight;
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if(!containsVertex(element))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        int index = indexOf(element);
        if(index != -1) {
            for (int i = index; i < counter - 1; i++) {
                vertexList[i] = vertexList[i + 1];
                for (int j = 0; j < counter; j++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
                }
            }
            for (int i = 0; i < counter; i++) {
                for (int j = index; j < counter - 1; j++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i][j + 1];
                }
            }
            counter--;
            vertexList[counter] = null;
            for (int i = 0; i <= counter; i++) {
                adjacencyMatrix[i][counter] = (T) Integer.valueOf(0);
                adjacencyMatrix[counter][i] = (T) Integer.valueOf(0);
            }

            for (int i = 0; i < counter; i++) {
                Vertex<T> vertex = vertexList[i];
                vertex.headNode = removeNeighborIfExists(vertex.headNode, element);
            }
        }
    }

    private Node<T> removeNeighborIfExists(Node<T> headNode, T element) {
        if(headNode == null) return null;
        if(equals(headNode.data, element))
            return headNode.neighbor;
        Node<T> prev = headNode;
        while(prev.neighbor != null){
            if(equals(prev.neighbor.data, element)){
                prev.neighbor = prev.neighbor.neighbor;
                break;
            }
            prev = prev.neighbor;
        }
        return headNode;
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b))
            throw new GraphException("Adjacency List Graph Not Contains Edge");
        Vertex<T> vertexA = getVertex(a);
        vertexA.headNode = removeNeighborIfExists(vertexA.headNode, b);
        if(!directed) {
            Vertex<T> vertexB = getVertex(b);
            vertexB.headNode = removeNeighborIfExists(vertexB.headNode, a);
        }
        int i = indexOf(a);
        int j = indexOf(b);
        if(i != -1 && j != -1) {
            adjacencyMatrix[i][j] = (T) Integer.valueOf(0);
            if(!directed) adjacencyMatrix[j][i] = (T) Integer.valueOf(0);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|——————| |Adjacency List Graph|——————| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("※※※※※※Graph Type: ").append(graphType).append("\n");
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is: ")
                    .append(vertexList[i].data);
        }
        for (int i = 0; i < counter; i++) {
            sb.append("\n( ").append(i).append(" )----Vertex [ ").append(getVertexByIndex(i).data).append(" ]");
            Node<T> aux = getVertexByIndex(i).headNode;
            while(aux != null){
                sb.append("\n※※※ Edge: ").append(aux.data).append(", weight: ").append(aux.weight);
                aux = aux.neighbor;
            }
        }
        return sb.toString();
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency List Graph is Empty");
        setVisitedAll(false);
        StringBuilder info = new StringBuilder();
        stack.clear();
        vertexList[0].setVisited(true);
        info.append(vertexList[0].data).append(", ");
        stack.push(0);
        while(!stack.isEmpty()) {
            int topIndex = (int) stack.top();
            int nextIndex = adjacentNotVisitedByList(topIndex);
            if(nextIndex == -1) {
                stack.pop();
            } else {
                vertexList[nextIndex].setVisited(true);
                info.append(vertexList[nextIndex].data).append(", ");
                stack.push(nextIndex);
            }
        }
        return info.toString();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency List Graph is Empty");
        setVisitedAll(false);
        StringBuilder info = new StringBuilder();
        queue.clear();
        vertexList[0].setVisited(true);
        info.append(vertexList[0].data).append(", ");
        queue.enQueue(0);
        while(!queue.isEmpty()) {
            int current = (int) queue.deQueue();
            int nextIndex;
            while((nextIndex = adjacentNotVisitedByList(current)) != -1) {
                vertexList[nextIndex].setVisited(true);
                info.append(vertexList[nextIndex].data).append(", ");
                queue.enQueue(nextIndex);
            }
        }
        return info.toString();
    }

    private int adjacentNotVisitedByList(int index) {
        Node<T> aux = vertexList[index].headNode;
        while(aux != null) {
            int neighborIndex = indexOf(aux.data);
            if(neighborIndex != -1 && !vertexList[neighborIndex].isVisited())
                return neighborIndex;
            aux = aux.neighbor;
        }
        return -1;
    }

    private void setVisitedAll(boolean value) {
        for(int i = 0; i < counter; i++)
            vertexList[i].setVisited(value);
    }

    @Override
    public int getVertexDegree(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Vertex not found");
        Vertex<T> v = getVertex(element);
        int degree = 0;
        Node<T> aux = v.headNode;
        while (aux != null) { degree++; aux = aux.neighbor; }
        if (directed) {
            for (int i = 0; i < counter; i++) {
                if (!equals(vertexList[i].data, element)) {
                    Node<T> n = getNodeNeighbor(vertexList[i].headNode, element);
                    if (n != null) degree++;
                }
            }
        }
        return degree;
    }

    @Override
    public int getGraphDegree() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");
        int max = 0;
        for (int i = 0; i < counter; i++) {
            int d = getVertexDegree(vertexList[i].data);
            if (d > max) max = d;
        }
        return max;
    }

    @Override
    public int totalEdges() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");
        int count = 0;
        for (int i = 0; i < counter; i++) {
            Node<T> aux = vertexList[i].headNode;
            while (aux != null) { count++; aux = aux.neighbor; }
        }
        return directed ? count : count / 2;
    }
}