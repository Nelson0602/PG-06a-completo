package model.LinkedList;

import model.Node;

public class CircularLinkedList<T> implements List<T> {

    private Node<T> head;
    private Node<T> tail;

    public CircularLinkedList() {
        head = tail = null;
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    @Override
    public int size() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Node<T> aux = head;
        int count = 0;
        do {
            count++;
            aux = aux.next;
        } while (aux != head);
        return count;
    }

    @Override
    public void clear() {
        head = tail = null;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void add(T element) {
        addLast(element);
    }

    @Override
    public void addFirst(T element) {
        Node<T> node = new Node<>(element);
        if (isEmpty()) {
            head = tail = node;
            node.next = head;
        } else {
            node.next = head;
            head = node;
            tail.next = head;
        }
    }

    @Override
    public void addLast(T element) {
        Node<T> node = new Node<>(element);
        if (isEmpty()) {
            head = tail = node;
            node.next = head;
        } else {
            tail.next = node;
            tail = node;
            tail.next = head;
        }
    }

    @Override
    public void addInSortedList(T element) {
        if (isEmpty()) {
            addFirst(element);
            return;
        }

        if (compare(element, head.data) <= 0) {
            addFirst(element);
            return;
        }

        if (compare(element, tail.data) >= 0) {
            addLast(element);
            return;
        }

        Node<T> aux = head;
        while (aux.next != head && compare(aux.next.data, element) < 0) {
            aux = aux.next;
        }

        Node<T> node = new Node<>(element);
        node.next = aux.next;
        aux.next = node;
    }

    @Override
    public void remove(T element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }

        if (head == tail && equals(head.data, element)) {
            clear();
            return;
        }

        if (equals(head.data, element)) {
            head = head.next;
            tail.next = head;
            return;
        }

        Node<T> prev = head;
        Node<T> aux = head.next;
        do {
            if (equals(aux.data, element)) {
                prev.next = aux.next;
                if (aux == tail) {
                    tail = prev;
                }
                return;
            }
            prev = aux;
            aux = aux.next;
        } while (aux != head);
    }

    @Override
    public T removeFirst() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        T first = head.data;
        if (head == tail) {
            clear();
        } else {
            head = head.next;
            tail.next = head;
        }
        return first;
    }

    @Override
    public T removeLast() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        T last = tail.data;
        if (head == tail) {
            clear();
            return last;
        }
        Node<T> aux = head;
        while (aux.next != tail) {
            aux = aux.next;
        }
        tail = aux;
        tail.next = head;
        return last;
    }

    @Override
    public boolean contains(T element) throws ListException {
        if (isEmpty()) {
            return false;
        }
        Node<T> aux = head;
        do {
            if (equals(aux.data, element)) {
                return true;
            }
            aux = aux.next;
        } while (aux != head);
        return false;
    }

    @Override
    public void sort() throws ListException {
        if (isEmpty()) {
            return;
        }

        boolean swapped;
        do {
            swapped = false;
            Node<T> aux = head;
            do {
                Node<T> next = aux.next;
                if (next != head && compare(aux.data, next.data) > 0) {
                    T tmp = aux.data;
                    aux.data = next.data;
                    next.data = tmp;
                    swapped = true;
                }
                aux = aux.next;
            } while (aux.next != head);
        } while (swapped);
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (a instanceof Comparable) {
            return ((Comparable<T>) a).compareTo(b);
        }
        return 0;
    }

    @Override
    public int indexOf(T element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Node<T> aux = head;
        int index = 1;
        do {
            if (equals(aux.data, element)) {
                return index;
            }
            index++;
            aux = aux.next;
        } while (aux != head);
        return -1;
    }

    @Override
    public T getFirst() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        return head.data;
    }

    @Override
    public T getLast() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        return tail.data;
    }

    @Override
    public T getPrev(T element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Node<T> aux = head;
        do {
            if (equals(aux.next.data, element)) {
                return aux.data;
            }
            aux = aux.next;
        } while (aux != head);
        return null;
    }

    @Override
    public T getNext(T element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Node<T> aux = head;
        do {
            if (equals(aux.data, element)) {
                return aux.next.data;
            }
            aux = aux.next;
        } while (aux != head);
        return null;
    }

    @Override
    public T get(int index) throws ListException {
        return getNodeByIndex(index).data;
    }

    @Override
    public Node<T> getNodeByIndex(int index) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Node<T> aux = head;
        int pos = 1;
        do {
            if (pos == index) {
                return aux;
            }
            pos++;
            aux = aux.next;
        } while (aux != head);
        return null;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "HEAD → HEAD";
        }
        StringBuilder sb = new StringBuilder("HEAD → ");
        Node<T> aux = head;
        do {
            sb.append("[").append(aux.data).append("]");
            if (aux.next != head) {
                sb.append(" → ");
            }
            aux = aux.next;
        } while (aux != head);
        sb.append(" → HEAD");
        return sb.toString();
    }

    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }
}