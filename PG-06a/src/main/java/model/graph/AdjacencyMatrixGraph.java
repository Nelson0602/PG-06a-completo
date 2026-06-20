package model.graph;

import model.LinkedList.ListException;
import model.Queue.LinkedQueue;
import model.Queue.QueueException;
import model.Stack.LinkedStack;
import model.Stack.StackException;

public class AdjacencyMatrixGraph<T extends Comparable<T>> implements Graph<T> {
    public int n;
    public Vertex<T>[] vertexList;
    public T[][] adjacencyMatrix;
    public int counter;
    public final boolean directed;

    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;

    public AdjacencyMatrixGraph(int n, boolean directed) {
        if (n <= 0) System.exit(1);
        this.n = n;
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = (T[][]) new Comparable[n][n];
        this.stack = new LinkedStack<>();
        this.queue = new LinkedQueue<>();
        this.counter = 0;
        this.directed = directed;
        initMatrix();
    }

    private void initMatrix() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = (T) Integer.valueOf(0);
            }
        }
    }

    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = (T[][]) new Comparable[n][n];
        this.counter = 0;
        initMatrix();
    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data)) return true;
        }
        return false;
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");
        return !equals(adjacencyMatrix[indexOf(a)][indexOf(b)], (T)Integer.valueOf(0));
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if(counter >=vertexList.length)
            throw new GraphException("Adjacency Matrix Graph is Full");
        vertexList[counter++] = new Vertex<>(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            adjacencyMatrix[indexOf(a)][indexOf(b)] = (T) Integer.valueOf(1);
            if(!directed) adjacencyMatrix[indexOf(b)][indexOf(a)] = (T) Integer.valueOf(1);
        }
    }

    public int indexOf(T element) {
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data)) return i;
        }
        return -1;
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency Matrix Graph Not Contains Vertex");
        if(containsEdge(a, b)) {
            adjacencyMatrix[indexOf(a)][indexOf(b)] = weight;
            if(!directed) adjacencyMatrix[indexOf(b)][indexOf(a)] = weight;
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            adjacencyMatrix[indexOf(a)][indexOf(b)] = weight;
            if(!directed) adjacencyMatrix[indexOf(b)][indexOf(a)] = weight;
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
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
        }
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a, b))
            throw new GraphException("Adjacency Matrix Graph Not Contains Edge");
        int i = indexOf(a);
        int j = indexOf(b);
        if(i!=-1 && j!=-1){
            adjacencyMatrix[i][j] = (T)Integer.valueOf(0);
            if(!directed) adjacencyMatrix[j][i] = (T)Integer.valueOf(0);
        }
    }

    public String printMatrix() {
        String result = "";
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                result+= adjacencyMatrix[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|——————| |Adjacency Matrix Graph|——————| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("※※※※※※Graph Type: ").append(graphType).append("\n");
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is: ")
                    .append(vertexList[i].data);
        }
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if(!adjacencyMatrix[i][j].equals(0)){
                    sb.append("\nThere is edge between the vertexes: ")
                            .append(vertexList[i].data)
                            .append("..........").append(vertexList[j].data);
                    if(!adjacencyMatrix[j][i].equals(1))
                        sb.append(". Weight: ").append(adjacencyMatrix[j][i]);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);
        String info =vertexList[0].data+", ";
        vertexList[0].setVisited(true);
        stack.clear();
        stack.push(0);
        while( !stack.isEmpty() ){
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1)
                stack.pop();
            else{
                vertexList[index].setVisited(true);
                info+=vertexList[index].data+", ";
                stack.push(index);
            }
        }
        return info;
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);
        String info =vertexList[0].data+", ";
        vertexList[0].setVisited(true);
        queue.clear();
        queue.enQueue(0);
        int v2;
        while(!queue.isEmpty()){
            int v1 = (int) queue.deQueue();
            while((v2=adjacentVertexNotVisited(v1)) != -1 ){
                vertexList[v2].setVisited(true);
                info+=vertexList[v2].data+", ";
                queue.enQueue(v2);
            }
        }
        return info;
    }

    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value);
        }
    }

    private int adjacentVertexNotVisited(int index) {
        for (int i = 0; i < counter; i++) {
            if(!adjacencyMatrix[index][i].equals(0)
                    && !vertexList[i].isVisited())
                return i;
        }
        return -1;
    }

    public boolean equals(T a, T b)  {
        return a==null ? b==null : a.equals(b);
    }

    public int compareElements(T a, T b) {
        return a.compareTo(b);
    }

    public Vertex<T> getVertexByIndex(int index) {
        for (int i = 0; i < counter; i++)
            if(i==index) return this.vertexList[i];
        return null;
    }

    @Override
    public int getVertexDegree(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Vertex not found");
        int index = indexOf(element);
        int degree = 0;
        for (int j = 0; j < counter; j++) {
            if (!adjacencyMatrix[index][j].equals(0)) degree++;
        }
        if (!directed) return degree;
        for (int i = 0; i < counter; i++) {
            if (i != index && !adjacencyMatrix[i][index].equals(0)) degree++;
        }
        return degree;
    }

    @Override
    public int getGraphDegree() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");
        int max = 0;
        for (int i = 0; i < counter; i++) {
            int d = getVertexDegree(vertexList[i].data);
            if (d > max) max = d;
        }
        return max;
    }

    @Override
    public int totalEdges() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");
        int count = 0;
        for (int i = 0; i < counter; i++)
            for (int j = 0; j < counter; j++)
                if (!adjacencyMatrix[i][j].equals(0)) count++;
        return directed ? count : count / 2;
    }

    @Override
    public int totalEdges(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Adjacency Matrix Graph Not Contains Vertex");
        int index = indexOf(element);
        int count = 0;
        for (int j = 0; j < counter; j++) {
            if (!adjacencyMatrix[index][j].equals(0)) count++;
        }
        return count;
    }

    @Override
    public String getEdges(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Adjacency Matrix Graph Not Contains Vertex");
        int index = indexOf(element);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < counter; j++) {
            if (!adjacencyMatrix[index][j].equals(0))
                sb.append(vertexList[j].data).append(" ");
        }
        if (sb.length() == 0) return "The vertex has no edges";
        return sb.toString().trim();
    }
}