package model.LinkedList;
import model.Node;

/**
 * Lista enlazada circular y doble
 * */
public class CircularDoublyLinkedList<T> implements List<T>, Cloneable {

    private Node<T> head;
    private Node<T> tail;

    public CircularDoublyLinkedList() {
        this.head = this.tail = null;
    }

    public Node<T> getHead() { return head; }
    public Node<Integer> getHead2() { return (Node<Integer>) head; }
    public Node<T> getTail() { return tail; }

    @Override
    public int size() throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
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
    public boolean contains(T element) throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        Node<T> aux = head;
        do {
            if (equals(aux.data, element)) return true;
            aux = aux.next;
        } while (aux != head);
        return false;
    }

    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
            tail = newNode;
        }
    }

    @Override
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            newNode.next = head;
            newNode.prev = tail;
            head.prev = newNode;
            tail.next = newNode;
            head = newNode;
        }
    }

    @Override
    public void addLast(T element) {
        add(element);
    }

    @Override
    public void addInSortedList(T element) { }

    @Override
    public void remove(T element) throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");

        // Solo un nodo
        if (head == tail) {
            if (equals(head.data, element)) { clear(); return; }
            else throw new ListException("Element not found");
        }

        // Buscar el nodo a eliminar
        Node<T> aux = head;
        do {
            if (equals(aux.data, element)) {
                // Desenlazar
                aux.prev.next = aux.next;
                aux.next.prev = aux.prev;
                if (aux == head) head = aux.next;
                if (aux == tail) tail = aux.prev;
                return;
            }
            aux = aux.next;
        } while (aux != head);

        throw new ListException("Element not found: " + element);
    }

    @Override
    public T removeFirst() throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        T first = head.data;
        if (head == tail) { clear(); return first; }
        head = head.next;
        head.prev = tail;
        tail.next = head;
        return first;
    }

    @Override
    public T removeLast() throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        T last = tail.data;
        if (head == tail) { clear(); return last; }
        tail = tail.prev;
        tail.next = head;
        head.prev = tail;
        return last;
    }

    @Override
    public void sort() throws ListException { }

    @Override
    public int indexOf(T element) throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        Node<T> aux = head;
        int index = 1;
        do {
            if (equals(aux.data, element)) return index;
            index++;
            aux = aux.next;
        } while (aux != head);
        return -1;
    }

    @Override
    public T getFirst() throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        return head.data;
    }

    @Override
    public T getLast() throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        return tail.data;
    }

    @Override
    public T getPrev(T element) throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        Node<T> aux = head;
        do {
            if (equals(aux.data, element)) return aux.prev.data;
            aux = aux.next;
        } while (aux != head);
        return null;
    }

    @Override
    public T getNext(T element) throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        Node<T> aux = head;
        do {
            if (equals(aux.data, element)) return aux.next.data;
            aux = aux.next;
        } while (aux != head);
        return null;
    }

    @Override
    public T get(int index) throws ListException {
        return getNodeByIndex(index).data;
    }

    public Node<T> getNodeByIndex(int index) throws ListException {
        if (isEmpty()) throw new ListException("Circular Doubly Linked List is empty");
        Node<T> aux = head;
        int i = 1;
        do {
            if (index == i) return aux;
            i++;
            aux = aux.next;
        } while (aux != head);
        return null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "HEAD ←→ HEAD";
        StringBuilder sb = new StringBuilder("HEAD ←→ ");
        Node<T> cur = head;
        do {
            sb.append("[").append(cur.data).append("]");
            cur = cur.next;
            if (cur != head) sb.append(" ←→ ");
        } while (cur != head);
        sb.append(" ←→ HEAD");
        return sb.toString();
    }

    private boolean equals(T a, T b) {
        if (a == null) return b == null;
        return a.equals(b);
    }
}
