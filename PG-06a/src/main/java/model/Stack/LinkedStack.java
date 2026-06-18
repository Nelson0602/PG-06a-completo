package model.Stack;

import model.Node;

public class LinkedStack <T> implements MyStack<T> {

    private Node<T> top; //es un apuntador
    private int size; //elementos apilados

    public LinkedStack() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return top == null; //size == 0;
    }

    @Override
    public T peek() throws StackException {
        if (isEmpty()) throw new StackException("Linked Stack is empty");
        return top.data;
    }

    @Override
    public T top() throws StackException {
        if (isEmpty()) throw new StackException("Linked Stack is empty");
        return top.data;
    }

    @Override
    public void push(T element) throws StackException {
        Node<T> node = new Node<>(element);
        if (isEmpty()) top = node;
        else {
            node.next = top;
            top = node; //para que tope quede en el nodo con el util apilado
        }
        size++; //contador de elementos apilados
    }

    @Override
    public T pop() throws StackException {
        if (isEmpty()) throw new StackException("Linked Stack is empty");
        T data = top.data;//elemento del tope de la pila
        top = top.next;
        size--; //al elminiar un elemento, decrementamos el contador
        return data;//la data del nodo eliminado
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Linked Stack is Empty";
        StringBuilder sb = new StringBuilder(" → ");
        try {
            LinkedStack<T> auxStack = new LinkedStack<>();
            while (!isEmpty()) {
                sb.append("[").append(peek()).append("] ");
                auxStack.push(pop());
                if (isEmpty()) sb.append(", ");
            }
            //dejamos la pila original
            while (!auxStack.isEmpty())
                push(auxStack.pop());

        } catch (StackException e) {
            System.out.println(e.getMessage());
        }
        sb.append(" → BOTTOM");
        return sb.toString();


    }
}