package model.tree;

import java.util.Random;

public class BTree<T extends Comparable<T>> implements Tree<T> {
    public BTreeNode<T> root;

    public BTree() {
        this.root = null;
    }

    @Override
    public int size() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return size(root);
    }

    private int size(BTreeNode<T> node) {
        if (node == null) return 0;
        return size(node.left) + size(node.right) + 1;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public boolean contains(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return binarySearch(this.root, element);
    }

    private boolean binarySearch(BTreeNode<T> node, T element) {
        if (node == null) return false;
        if (equals(node.data, element)) return true;
        return binarySearch(node.left, element) || binarySearch(node.right, element);
    }

    @Override
    public void add(T element) {
        this.root = add(root, element, "root");
    }

    private BTreeNode<T> add(BTreeNode<T> node, T element, String path) {
        if (node == null) {
            node = new BTreeNode<>(element, path);
        } else {
            int value = new Random().nextInt(10);
            if (value % 2 == 0) {
                node.left = add(node.left, element, path + "/left");
            } else {
                node.right = add(node.right, element, path + "/right");
            }
        }
        return node;
    }

    @Override
    public void remove(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        boolean[] removed = new boolean[1];
        root = remove(root, element, removed);
        if (!removed[0]) throw new TreeException("Element not found in Binary Tree");
    }

    private BTreeNode<T> remove(BTreeNode<T> node, T element, boolean[] removed) {
        if (node == null) return null;

        if (equals(node.data, element)) {
            removed[0] = true;
            if (node.left == null && node.right == null) {
                return null;
            }
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            T successor = min(node.right);
            node.data = successor;
            boolean[] dummy = new boolean[1];
            node.right = remove(node.right, successor, dummy);
            return node;
        } else {
            node.left = remove(node.left, element, removed);
            if (removed[0]) return node;
            node.right = remove(node.right, element, removed);
            return node;
        }
    }

    @Override
    public int height(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        BTreeNode<T> target = findNode(root, element);
        if (target == null) throw new TreeException("Element not found in Binary Tree");
        return height(target);
    }

    @Override
    public int height() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return height(root);
    }

    private int height(BTreeNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public BTreeNode<T> findNode(BTreeNode<T> node, T element) {
        if (node == null) return null;
        if (equals(node.data, element)) return node;
        BTreeNode<T> foundLeft = findNode(node.left, element);
        if (foundLeft != null) return foundLeft;
        return findNode(node.right, element);
    }

    @Override
    public T min() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return min(root);
    }

    T min(BTreeNode<T> node) {
        if (node == null) return null;
        if (node.left != null && node.right != null) {
            return minElement(node.data, minElement(min(node.left), min(node.right)));
        } else if (node.left != null) {
            return minElement(node.data, min(node.left));
        } else if (node.right != null) {
            return minElement(node.data, min(node.right));
        } else {
            return node.data;
        }
    }

    private T minElement(T a, T b) {
        if (a == null) return b;
        if (b == null) return a;
        return compareElements(a, b) <= 0 ? a : b;
    }

    @Override
    public T max() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return max(root);
    }

    private T max(BTreeNode<T> node) {
        if (node == null) return null;
        if (node.left != null && node.right != null) {
            return maxElement(node.data, maxElement(max(node.left), max(node.right)));
        } else if (node.left != null) {
            return maxElement(node.data, max(node.left));
        } else if (node.right != null) {
            return maxElement(node.data, max(node.right));
        } else {
            return node.data;
        }
    }

    private T maxElement(T a, T b) {
        if (a == null) return b;
        if (b == null) return a;
        return compareElements(a, b) >= 0 ? a : b;
    }

    @Override
    public String preOrder() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return preOrder(root);
    }

    private String preOrder(BTreeNode<T> node) {
        String result = "";
        if (node != null) {
            result = node.data + "(" + node.path + ") ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return result;
    }

    @Override
    public String inOrder() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return inOrder(root);
    }

    private String inOrder(BTreeNode<T> node) {
        String result = "";
        if (node != null) {
            result += inOrder(node.left);
            result += node.data + ", ";
            result += inOrder(node.right);
        }
        return result;
    }

    @Override
    public String postOrder() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return postOrder(root);
    }

    private String postOrder(BTreeNode<T> node) {
        String result = "";
        if (node != null) {
            result += postOrder(node.left);
            result += postOrder(node.right);
            result += node.data + ", ";
        }
        return result;
    }

    @Override
    public String nodeHeight() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        StringBuilder sb = new StringBuilder();
        buildNodeHeight(root, sb);
        return sb.toString();
    }

    private void buildNodeHeight(BTreeNode<T> node, StringBuilder sb) {
        if (node == null) return;
        sb.append(node.data).append(": ").append(height(node)).append("\n");
        buildNodeHeight(node.left, sb);
        buildNodeHeight(node.right, sb);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Binary Tree is empty";
        String result = "Binary Tree Tour\n";
        result += "PreOrder (N-L-R): " + preOrder(root) + "\n";
        result += "InOrder (L-N-R): " + inOrder(root) + "\n";
        result += "PostOrder (L-R-N): " + postOrder(root) + "\n";
        return result;
    }

    public boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    public int compareElements(T a, T b) {
        return a.compareTo(b);
    }

    /*
     *Practica examen
     *
     */

    //punto 1: nodos que tienen al menos un hijo
    public String printNodesWithChildren() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return printNodesWithChildren(root);
    }

    private String printNodesWithChildren(BTreeNode<T> node) {
        if (node == null) return "";

        String result = "";
        if (node.left != null || node.right != null) {
            result += "Nodo: " + node.data + " -> ";
            if (node.left != null) result += "izq: " + node.left.data + " ";
            if (node.right != null) result += "der: " + node.right.data;
            result += "\n";
        }

        result += printNodesWithChildren(node.left);
        result += printNodesWithChildren(node.right);
        return result;
    }

    //punto 2: nodos que tienen exactamente un hijo
    public String printNodes1Child() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return printNodes1Child(root);
    }

    private String printNodes1Child(BTreeNode<T> node) {
        if (node == null) return "";

        String result = "";
        boolean tieneUnSoloHijo = (node.left != null && node.right == null) || (node.left == null && node.right != null);

        if (tieneUnSoloHijo) {
            result += "Nodo: " + node.data + " -> ";
            if (node.left != null) result += "izq: " + node.left.data;
            else result += "der: " + node.right.data;
            result += "\n";
        }

        result += printNodes1Child(node.left);
        result += printNodes1Child(node.right);
        return result;
    }

    //punto 3: nodos que tienen los dos hijos
    public String printNodes2Children() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return printNodes2Children(root);
    }

    private String printNodes2Children(BTreeNode<T> node) {
        if (node == null) return "";

        String result = "";
        if (node.left != null && node.right != null) {
            result += "Nodo: " + node.data + " -> izq: " + node.left.data + " der: " + node.right.data + "\n";
        }

        result += printNodes2Children(node.left);
        result += printNodes2Children(node.right);
        return result;
    }

    //punto 4: hojas del arbol
    public String printLeaves() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return printLeaves(root);
    }

    private String printLeaves(BTreeNode<T> node) {
        if (node == null) return "";

        String result = "";
        if (node.left == null && node.right == null) {
            result += "Hoja: " + node.data + "\n";
        }

        result += printLeaves(node.left);
        result += printLeaves(node.right);
        return result;
    }

    //punto 5: abuelo de un elemento
    public Object grandFather(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return grandFather(root, element, null, null);
    }

    private Object grandFather(BTreeNode<T> node, T element, BTreeNode<T> parent, BTreeNode<T> grandParent) {
        if (node == null) return "no tiene abuelo";

        if (equals(node.data, element)) {
            if (grandParent == null) return "no tiene abuelo";
            return grandParent.data;
        }

        if (compareElements(element, node.data) < 0)
            return grandFather(node.left, element, node, parent);
        else
            return grandFather(node.right, element, node, parent);
    }

    //punto 6: padre de un elemento
    public Object father(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return father(root, element, null);
    }

    private Object father(BTreeNode<T> node, T element, BTreeNode<T> parent) {
        if (node == null) return "no tiene padre";

        if (equals(node.data, element)) {
            if (parent == null) return "no tiene padre";
            return parent.data;
        }

        if (compareElements(element, node.data) < 0)
            return father(node.left, element, node);
        else
            return father(node.right, element, node);
    }

    //punto 7: hermano de un elemento
    public Object brother(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return brother(root, element, null);
    }

    private Object brother(BTreeNode<T> node, T element, BTreeNode<T> parent) {
        if (node == null) return "no tiene hermano";

        if (equals(node.data, element)) {
            if (parent == null) return "no tiene hermano";

            if (parent.left != null && !equals(parent.left.data, element))
                return parent.left.data;
            if (parent.right != null && !equals(parent.right.data, element))
                return parent.right.data;

            return "no tiene hermano";
        }

        if (compareElements(element, node.data) < 0)
            return brother(node.left, element, node);
        else
            return brother(node.right, element, node);
    }

    //punto 8: primos de un elemento
    public String cousins(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");

        Object abuelo = grandFather(element);
        if (abuelo.equals("no tiene abuelo")) return "no tiene primos";

        BTreeNode<T> nodoAbuelo = findNode(root, (T) abuelo);
        Object papa = father(element);

        String result = "";

        if (nodoAbuelo.left != null && !equals(nodoAbuelo.left.data, (T) papa)) {
            if (nodoAbuelo.left.left != null) result += nodoAbuelo.left.left.data + "\n";
            if (nodoAbuelo.left.right != null) result += nodoAbuelo.left.right.data + "\n";
        }

        if (nodoAbuelo.right != null && !equals(nodoAbuelo.right.data, (T) papa)) {
            if (nodoAbuelo.right.left != null) result += nodoAbuelo.right.left.data + "\n";
            if (nodoAbuelo.right.right != null) result += nodoAbuelo.right.right.data + "\n";
        }

        if (result.isEmpty()) return "no tiene primos";
        return result;
    }

    //punto 9: subarbol a partir de un elemento
    public String printSubtree(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");

        BTreeNode<T> node = findNode(root, element);
        if (node == null) return "elemento no encontrado";

        return printSubtree(node);
    }

    private String printSubtree(BTreeNode<T> node) {
        if (node == null) return "";

        String result = node.data + "\n";
        result += printSubtree(node.left);
        result += printSubtree(node.right);
        return result;
    }

    //punto 10: cantidad total de hojas
    public int totalLeaves() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return totalLeaves(root);
    }

    private int totalLeaves(BTreeNode<T> node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return totalLeaves(node.left) + totalLeaves(node.right);
    }
}
