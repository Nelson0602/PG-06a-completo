package model.Queue;

import model.Node;

public class LinkedQueue<T> implements MyQueue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    public LinkedQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public LinkedQueue(int capacity) {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = rear = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int indexOf(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        Node<T> aux = front;
        int index = 1;

        while (aux != null) {
            if (equals(aux.data, element)) {
                return index;
            }
            index++;
            aux = aux.next;
        }

        return -1;
    }

    @Override
    public void enQueue(T element) throws QueueException {
        Node<T> node = new Node<>(element);

        if (isEmpty()) {
            front = rear = node;
        } else {
            rear.next = node;
            rear = node;
        }

        size++;
    }

    @Override
    public T deQueue() throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        T element = front.data;

        // Caso 1. Cuando solo hay un elemento
        if (front == rear) {
            clear();
        }
        // Caso 2. Cuando hay más de un elemento
        else {
            front = front.next;
            size--;
        }

        return element;
    }

    @Override
    public void enQueue(T element, Integer priority) throws QueueException {
        enQueue(element);
    }

    @Override
    public boolean contains(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        Node<T> aux = front;

        while (aux != null) {
            if (equals(aux.data, element)) {
                return true;
            }
            aux = aux.next;
        }

        return false;
    }

    @Override
    public T peek() throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        return front.data;
    }

    @Override
    public T front() throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        return front.data;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Linked Queue is empty";

        StringBuilder sb = new StringBuilder(" FRONT → ");

        try {
            LinkedQueue<T> auxQueue = new LinkedQueue<>(size());

            while (!isEmpty()) {
                sb.append("[").append(peek()).append("]");

                auxQueue.enQueue(deQueue());

                if (!isEmpty()) {
                    sb.append(" → ");
                }
            }

            while (!auxQueue.isEmpty()) {
                enQueue(auxQueue.deQueue());
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }

        sb.append(" ← REAR");
        return sb.toString();
    }

    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }
}