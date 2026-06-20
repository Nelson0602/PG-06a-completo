package model.graph;

import model.LinkedList.ListException;
import model.Queue.QueueException;
import model.Stack.StackException;

public interface Graph<T> {
    public int size() throws ListException;
    public void clear();
    public boolean isEmpty();
    public boolean containsVertex(T element)throws GraphException, ListException;
    public boolean containsEdge(T a, T b)throws GraphException, ListException;
    public void addVertex(T element)throws GraphException, ListException;
    public void addEdge(T a, T b)throws GraphException, ListException;
    public void addWeight(T a, T b, T weight)throws GraphException, ListException;
    public void addEdgeAndWeight(T a, T b, T c) throws GraphException, ListException;
    public void removeVertex(T element)throws GraphException, ListException;
    public void removeEdge(T a, T b)throws GraphException, ListException;
    public String dfs()throws GraphException, StackException, ListException;
    public String bfs()throws GraphException, QueueException, ListException;
    public int getVertexDegree(T element) throws GraphException, ListException;
    public int getGraphDegree() throws GraphException, ListException;
    public int totalEdges() throws GraphException, ListException;
    public int totalEdges(T element) throws GraphException, ListException;
    public String getEdges(T element) throws GraphException, ListException;
}