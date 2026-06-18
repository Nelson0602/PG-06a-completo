package model.tree;


public class BST<T extends Comparable<T>> extends BTree<T>{


    @Override
    public void remove(T element) throws TreeException {

        if (isEmpty()) throw new TreeException("Tree is empty");
        root= remove(root,element);

    }

    private BTreeNode<T> remove(BTreeNode<T> node, T element) {
        if (node!=null){

            if (compareElements(element,node.data)<0)
                node.left=remove(node.left,element);
            else if (compareElements(element,node.data)>0)
                node.right=remove(node.right,element);
            else if (equals(element,node.data)){ //ya lo encontro
                //caso 1: el nodo a suprimir no tiene hijo, es una hoja
                if (node.left==null && node.right == null) return null;
                    //caso 2: el nodo a suprimir solo tiene un hijo
                    //              en este caso, el nodo es reemplazado por su hijo
                else if (node.right==null && node.left != null) return node.left; // Corrected
                else if (node.left == null && node.right != null) return node.right; // Corrected
                    //caso 3 el nodo a suprimir tiene 2 hijos
                else{
                    //se obtiene el elemento menor del subarbol der
                    //se reemplaza la data del nodo con valor
                    //luego se suprime el valor min del sub arbol der
                    T minValue = min(node.right);
                    node.data=minValue;
                    node.right=remove(node.right,minValue);
                }
            }
        }
        return node;
    }

    @Override
    public boolean contains(T element) throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return binarySearch(this.root,element);
    }
    private boolean binarySearch(BTreeNode<T> node, T element) {
        if (node == null) return false;
        if(equals(node.data,element)) return true;
        else if(compareElements(element,node.data) < 0)
            return binarySearch(node.left,element);
        else return binarySearch(node.right,element);
    }

    @Override
    public void add(T element) {
        this.root = add(root,element);

    }//a
    private BTreeNode<T> add(BTreeNode<T> node, T element){
        if(node == null){
            node = new BTreeNode<>(element);

        }else if(compareElements(element,node.data) < 0)
            node.left = add(node.left,element);
        else if(compareElements(element,node.data) > 0)
            node.right = add(node.right,element);

        return node;
    }



    @Override
    public T min() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return min(root);
    }

    public T min(BTreeNode<T> node){
        if (node == null) return null;
        if (node.left == null) return node.data;
        return min(node.left);
    }

    @Override
    public T max() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return max(root);
    }

    private T max(BTreeNode<T> node){
        if(node.right!=null) return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return preOrder(root);
    }

    // Recorrido: N-L-R
    private String preOrder(BTreeNode<T> node) {
        String result = "";
        if (node != null) {
            result = node.data + " ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return result;
    }
    @Override
    public String toString() {
        if (isEmpty()) return "Binary Tree is empty";
        String result = "Binary Tree Tour\n";
        try{
            result += "PreOrder (N-L-R): " + preOrder() + "\n";
            result += "InOrder (L-N-R): " + inOrder() + "\n";
            result += "PostOrder (L-R-N): " + postOrder() + "\n";
        }catch (TreeException e)

        {
            new RuntimeException("Error: " + e.getMessage());
        }
        return result;
    }

    /*
    *Practica examen
    *
    */


    //punto 1: nodos que tienen al menos un hijo
    public String printNodesWithChildren() throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");

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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");

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
        if (isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return totalLeaves(root);
    }

    private int totalLeaves(BTreeNode<T> node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return totalLeaves(node.left) + totalLeaves(node.right);
    }

}
