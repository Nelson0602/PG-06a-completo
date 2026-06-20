package model.graph;

import model.LinkedList.LinkedList;
import model.LinkedList.ListException;
import model.Node;
import model.Queue.LinkedQueue;
import model.Queue.QueueException;
import model.Stack.LinkedStack;
import model.Stack.StackException;

public class LinkedGraph<T extends Comparable<T>> extends LinkedList<T> implements Graph<T> {
    public final boolean directed;
    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;

    public LinkedGraph(boolean directed) {
        super();
        this.directed = directed;
        stack = new LinkedStack<>();
        queue = new LinkedQueue<>();
    }

    @Override
    public int size() throws ListException {
        return super.size();
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        if(isEmpty()) return false;
        return contains(element);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)) return false;
        Node<T> nodeA = getNode(a);
        if(nodeA == null) return false;
        boolean fromAtoB = getNodeNeighbor(nodeA, b) != null;
        if(!directed) {
            Node<T> nodeB = getNode(b);
            if(nodeB == null) return false;
            boolean fromBtoA = getNodeNeighbor(nodeB, a) != null;
            return fromAtoB && fromBtoA;
        }
        return fromAtoB;
    }

    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode == null || headNode.neighbor == null) return null;
        Node<T> aux = headNode.neighbor;
        while(aux != null) {
            if(aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;
        }
        return null;
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if(isEmpty() || !contains(element)) add(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            addNeighbor(nodeA, b, null);
            if(!directed) {
                Node<T> nodeB = getNode(b);
                addNeighbor(nodeB, a, null);
            }
        }
    }

    private void addNeighbor(Node<T> headNode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if(headNode.neighbor == null) {
            headNode.neighbor = node;
        } else {
            Node<T> aux = headNode.neighbor;
            while(aux.neighbor != null)
                aux = aux.neighbor;
            aux.neighbor = node;
        }
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if(containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            Node<T> neighbor = getNodeNeighbor(nodeA, b);
            if(neighbor != null) neighbor.weight = weight;
            if(!directed) {
                Node<T> nodeB = getNode(b);
                Node<T> neighborB = getNodeNeighbor(nodeB, a);
                if(neighborB != null) neighborB.weight = weight;
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            addNeighbor(nodeA, b, weight);
            if(!directed) {
                Node<T> nodeB = getNode(b);
                addNeighbor(nodeB, a, weight);
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if(!containsVertex(element))
            throw new GraphException("Linked Graph Not Contains Vertex");
        remove(element);
        if(!isEmpty()) {
            int len = size();
            for (int i = 1; i <= len; i++) {
                Node<T> node = getNodeByIndex(i);
                removeNeighborIfExists(node, element);
            }
        }
    }

    private void removeNeighborIfExists(Node<T> headNode, T element) {
        if(headNode == null || headNode.neighbor == null) return;
        if(equals(headNode.neighbor.data, element)) {
            headNode.neighbor = headNode.neighbor.neighbor;
            return;
        }
        Node<T> prev = headNode.neighbor;
        while(prev.neighbor != null) {
            if(equals(prev.neighbor.data, element)) {
                prev.neighbor = prev.neighbor.neighbor;
                return;
            }
            prev = prev.neighbor;
        }
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b))
            throw new GraphException("Linked Graph Not Contains Edge");
        Node<T> nodeA = getNode(a);
        removeNeighborIfExists(nodeA, b);
        if(!directed) {
            Node<T> nodeB = getNode(b);
            removeNeighborIfExists(nodeB, a);
        }
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        if(isEmpty()) throw new GraphException("Linked Graph is Empty");
        setVisitedAll(false);
        StringBuilder info = new StringBuilder();
        stack.clear();
        getNodeByIndex(1).visited = true;
        info.append(getNodeByIndex(1).data).append(", ");
        stack.push(1);
        while(!stack.isEmpty()) {
            int topIndex = (int) stack.top();
            int nextIndex = adjacentNotVisited(topIndex);
            if(nextIndex == -1) {
                stack.pop();
            } else {
                getNodeByIndex(nextIndex).visited = true;
                info.append(getNodeByIndex(nextIndex).data).append(", ");
                stack.push(nextIndex);
            }
        }
        return info.toString();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        if(isEmpty()) throw new GraphException("Linked Graph is Empty");
        setVisitedAll(false);
        StringBuilder info = new StringBuilder();
        queue.clear();
        getNodeByIndex(1).visited = true;
        info.append(getNodeByIndex(1).data).append(", ");
        queue.enQueue(1);
        while(!queue.isEmpty()) {
            int current = (int) queue.deQueue();
            int nextIndex;
            while((nextIndex = adjacentNotVisited(current)) != -1) {
                getNodeByIndex(nextIndex).visited = true;
                info.append(getNodeByIndex(nextIndex).data).append(", ");
                queue.enQueue(nextIndex);
            }
        }
        return info.toString();
    }

    private void setVisitedAll(boolean value) throws ListException {
        int len = size();
        for (int i = 1; i <= len; i++) {
            getNodeByIndex(i).visited = value;
        }
    }

    private int adjacentNotVisited(int index) throws ListException {
        Node<T> node = getNodeByIndex(index);
        if(node == null) return -1;
        Node<T> aux = node.neighbor;
        int len = size();
        while(aux != null) {
            for(int i = 1; i <= len; i++) {
                Node<T> candidate = getNodeByIndex(i);
                if(candidate != null && equals(candidate.data, aux.data) && !candidate.visited)
                    return i;
            }
            aux = aux.neighbor;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|——————| |Linked Graph|——————| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("※※※※※※Graph Type: ").append(graphType).append("\n");
        sb.append(super.toString());
        try {
            int len = size();
            for (int i = 1; i <= len; i++) {
                sb.append("\n( ").append(i).append(" )————Vertex [ ")
                        .append(getNodeByIndex(i).data).append(" ]");
                Node<T> aux = getNodeByIndex(i).neighbor;
                while(aux != null) {
                    sb.append("\n※※※ Edge: ").append(aux.data)
                            .append(", weight: ").append(aux.weight);
                    aux = aux.neighbor;
                }
            }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    public int compareElements(T a, T b) {
        return a.compareTo(b);
    }

    @Override
    public int getVertexDegree(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Vertex not found");
        Node<T> node = getNode(element);
        int degree = 0;
        Node<T> aux = node.neighbor;
        while (aux != null) { degree++; aux = aux.neighbor; }
        if (directed) {
            int len = size();
            for (int i = 1; i <= len; i++) {
                Node<T> n = getNodeByIndex(i);
                if (n != null && !equals(n.data, element)) {
                    Node<T> nb = n.neighbor;
                    while (nb != null) {
                        if (equals(nb.data, element)) { degree++; break; }
                        nb = nb.neighbor;
                    }
                }
            }
        }
        return degree;
    }

    @Override
    public int getGraphDegree() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Linked Graph is Empty");
        int max = 0;
        int len = size();
        for (int i = 1; i <= len; i++) {
            Node<T> n = getNodeByIndex(i);
            if (n != null) {
                int d = getVertexDegree(n.data);
                if (d > max) max = d;
            }
        }
        return max;
    }

    @Override
    public int totalEdges() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Linked Graph is Empty");
        int count = 0;
        int len = size();
        for (int i = 1; i <= len; i++) {
            Node<T> aux = getNodeByIndex(i).neighbor;
            while (aux != null) { count++; aux = aux.neighbor; }
        }
        return directed ? count : count / 2;
    }

    @Override
    public int totalEdges(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Linked Graph Not Contains Vertex");
        Node<T> node = getNode(element);
        int count = 0;
        Node<T> aux = node.neighbor;
        while (aux != null) { count++; aux = aux.neighbor; }
        return count;
    }

    @Override
    public String getEdges(T element) throws GraphException, ListException {
        if (!containsVertex(element)) throw new GraphException("Linked Graph Not Contains Vertex");
        Node<T> node = getNode(element);
        StringBuilder sb = new StringBuilder();
        Node<T> aux = node.neighbor;
        while (aux != null) {
            sb.append(aux.data).append(" ");
            aux = aux.neighbor;
        }
        if (sb.length() == 0) return "The vertex has no edges";
        return sb.toString().trim();
    }
}