package model.tree;

import java.util.ArrayList;
import java.util.List;
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

        Object leftResult = grandFather(node.left, element, node, parent);
        if (!leftResult.equals("no tiene abuelo")) return leftResult;

        return grandFather(node.right, element, node, parent);
    }

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

        Object leftResult = father(node.left, element, node);
        if (!leftResult.equals("no tiene padre")) return leftResult;

        return father(node.right, element, node);
    }

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

        Object leftResult = brother(node.left, element, node);
        if (!leftResult.equals("no tiene hermano")) return leftResult;

        return brother(node.right, element, node);
    }

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

    public int totalLeaves() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return totalLeaves(root);
    }

    private int totalLeaves(BTreeNode<T> node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return totalLeaves(node.left) + totalLeaves(node.right);
    }

    public void tighten() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        root = tighten(root);
    }

    private BTreeNode<T> tighten(BTreeNode<T> node) {
        if (node == null) return null;
        node.left = tighten(node.left);
        node.right = tighten(node.right);
        if (node.left != null && node.right == null) return node.left;
        if (node.right != null && node.left == null) return node.right;
        return node;
    }

    public static BTree<Integer> bTreesSum(BTree<Integer> btree1, BTree<Integer> btree2) {
        BTree<Integer> result = new BTree<>();
        BTreeNode<Integer> root1 = btree1 == null ? null : btree1.root;
        BTreeNode<Integer> root2 = btree2 == null ? null : btree2.root;
        result.root = bTreesSumNodes(root1, root2);
        return result;
    }

    private static BTreeNode<Integer> bTreesSumNodes(BTreeNode<Integer> n1, BTreeNode<Integer> n2) {
        if (n1 == null && n2 == null) return null;
        int v1 = n1 == null ? 0 : n1.data;
        int v2 = n2 == null ? 0 : n2.data;
        BTreeNode<Integer> node = new BTreeNode<>(v1 + v2);
        node.left = bTreesSumNodes(n1 == null ? null : n1.left, n2 == null ? null : n2.left);
        node.right = bTreesSumNodes(n1 == null ? null : n1.right, n2 == null ? null : n2.right);
        return node;
    }

    public static BTree<Integer> btNodeSum(BTree<Integer> btree) throws TreeException {
        if (btree == null || btree.isEmpty()) throw new TreeException("Binary Tree is empty");
        BTree<Integer> result = new BTree<>();
        result.root = btNodeSumNodes(btree.root);
        return result;
    }

    private static BTreeNode<Integer> btNodeSumNodes(BTreeNode<Integer> node) {
        if (node == null) return null;
        BTreeNode<Integer> left = btNodeSumNodes(node.left);
        BTreeNode<Integer> right = btNodeSumNodes(node.right);
        int leftSum = left == null ? 0 : left.data;
        int rightSum = right == null ? 0 : right.data;
        BTreeNode<Integer> newNode = new BTreeNode<>(node.data + leftSum + rightSum);
        newNode.left = left;
        newNode.right = right;
        return newNode;
    }

    public static boolean isABM(BTree<Integer> btree) {
        if (btree == null || btree.root == null) return true;
        return isABMNodes(btree.root);
    }

    private static boolean isABMNodes(BTreeNode<Integer> node) {
        if (node == null) return true;
        if (node.left != null && node.data > node.left.data) return false;
        if (node.right != null && node.data > node.right.data) return false;
        return isABMNodes(node.left) && isABMNodes(node.right);
    }

    public static BTree<Integer> joinABM(BTree<Integer> a, BTree<Integer> b) throws TreeException {
        if (!isABM(a) || !isABM(b))
            throw new TreeException("Uno de los arboles no es ABM");
        List<Integer> values = new ArrayList<>();
        collectPreOrder(a == null ? null : a.root, values);
        collectPreOrder(b == null ? null : b.root, values);
        Integer[] heap = values.toArray(new Integer[0]);
        buildMinHeap(heap);
        BTree<Integer> result = new BTree<>();
        result.root = buildFromHeap(heap, 0);
        return result;
    }

    private static void collectPreOrder(BTreeNode<Integer> node, List<Integer> list) {
        if (node == null) return;
        list.add(node.data);
        collectPreOrder(node.left, list);
        collectPreOrder(node.right, list);
    }

    private static void buildMinHeap(Integer[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
    }

    private static void heapify(Integer[] arr, int n, int i) {
        int smallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && arr[left] < arr[smallest]) smallest = left;
        if (right < n && arr[right] < arr[smallest]) smallest = right;
        if (smallest != i) {
            Integer tmp = arr[i]; arr[i] = arr[smallest]; arr[smallest] = tmp;
            heapify(arr, n, smallest);
        }
    }

    private static BTreeNode<Integer> buildFromHeap(Integer[] arr, int i) {
        if (i >= arr.length) return null;
        BTreeNode<Integer> node = new BTreeNode<>(arr[i]);
        node.left = buildFromHeap(arr, 2 * i + 1);
        node.right = buildFromHeap(arr, 2 * i + 2);
        return node;
    }
}