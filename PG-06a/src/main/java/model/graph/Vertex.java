package model.graph;

import model.Node;

public class Vertex<T> {
    public T data;
    public Node<T> headNode; //sirve para crear la lista enlazada de aristas y pesos
    private boolean visited;//sirve para los recorridos de DFS Y DBFS


    public Vertex(T data) {
        this.data = data;
        this.headNode = null;
    }
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
