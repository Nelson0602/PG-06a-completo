package model;

public class Node<T> {
    public T data;
    public Object weight;
    public Integer priority;
    public Node<T> next;
    public Node<T> prev;
    public Node<T> neighbor;
    public boolean visited;

    public Node(T data) {
        this.data = data;
        this.weight = data;
        this.next = this.prev = null;
        this.neighbor = null;
        this.visited = false;
    }

    public Node(){
        this.next = this.prev = null;
        this.visited = false;
    }

    public Node(T data, Integer priority) {
        this.data = data;
        this.priority = priority;
        this.next = null;
        this.visited = false;
    }

    public Node(T element, Object weight) {
        this.data = element;
        this.weight = weight;
        this.neighbor = null;
        this.visited = false;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}