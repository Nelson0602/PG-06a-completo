package model.Queue;

import model.Stack.ArrayStack;

import java.util.Arrays;

public class ArrayQueue <T> implements MyQueue<T> {
    private int n;
    private T[] data;
    private int front, rear; //los extremos del tda cola

    public ArrayQueue(int capacity) {
        this.n = capacity;
        data = (T[]) new Object[capacity];
        rear = n - 1; //el extremo final de la cola
        front = rear;
    }

    @Override
    public int size() {
        return rear - front;
    }

    @Override
    public void clear() {
        data = (T[]) new Object[n];
        rear = n - 1; //el extremo final de la cola
        front = rear;
    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    }
    @Override
    public int indexOf(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        HeaderLinkedQueue<T> aux = new HeaderLinkedQueue<>();
        int index = 1;
        int pos = -1;
        while (!isEmpty()) {
            if (equals(front(), element)) {
                pos = index;
            }
            aux.enQueue(deQueue());
            index ++;
        }

        while (!aux.isEmpty())
            enQueue(aux.deQueue());


        return pos;
    }
    @Override
    public void enQueue(T element) throws QueueException {
        if (size() == data.length)
            throw new QueueException("Array Queue is full");
        //la primera vez cuando la cola esta vacia, no entra al for
        for (int i = front; i <= rear; i++) {
            data[i] = data[i + 1];//remueve el elemento una posiscion a la izquierda
        }
        data[rear] = element;
        front--;
    }

    @Override
    public T deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Array Queue is empty");
        return data[++front];
    }

    @Override
    public void enQueue(T element, Integer priority) throws QueueException {

    }



    @Override
    public boolean contains(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Array Queue is empty");
        return false;
    }

    @Override
    public T peek() throws QueueException {
        if (isEmpty())
            throw new QueueException("Array Queue is empty");
        return data[front + 1];
    }

    @Override
    public T front() throws QueueException {
        if (isEmpty())
            throw new QueueException("Array Queue is empty");
        return null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Array Queue is empty";
        StringBuilder sb = new StringBuilder(" FRONT → ");
        ArrayQueue<T> auxQueue = new ArrayQueue<>(size());
        while (!isEmpty()) {
            try {
                while(!isEmpty()) {
                    sb.append("[").append(peek()).append("]");
                    auxQueue.enQueue(deQueue());
                    if(isEmpty()) sb.append(", ");
                }
                while(!auxQueue.isEmpty()) {
                    enQueue(auxQueue.deQueue());
                }
            } catch (QueueException e) {
                throw new RuntimeException(e);
            }
        }
        return sb.toString();
    }

    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }
}