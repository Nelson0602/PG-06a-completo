package model;

public class Node<T> {
    public T data;
    public Object weight;//este atributo sirve para guardar el peso de las aristas en grafos
    public Integer priority; //1=alta, 2=media, 3=baja
    public Node<T> next;
    public Node<T> prev;
    //Agrego un atributo para usar con las clases AdjacencyListGraph y LinkedGraph
    public Node<T> neighbor;//apuntador al nodo vecino del vertice con quien tiene una arista

    public Node(T data) {
        this.data = data;
        this.weight = data;
        this.next = this.prev = null;
        this.neighbor = null;
    }
    //Constructor sobrecargado
    public Node(){
        this.next = this.prev = null;
    }

    public Node(T data, Integer priority) {
        this.data = data;
        this.priority = priority;
        this.next = null;
    }

    public Node(T element, Object weight) {
        this.data = element;
        this.weight = weight;
        this.neighbor = null;
    }
}