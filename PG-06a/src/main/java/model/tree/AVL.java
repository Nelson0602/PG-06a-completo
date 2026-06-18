package model.tree;

public class AVL<T extends Comparable<T>> extends BST<T> {

    public AVL() {
        super();
    }

    @Override
    public void add(T element) {
        this.root = add(root, element);
    }

    private BTreeNode<T> add(BTreeNode<T> node, T element) {
        if (node == null) {
            return new BTreeNode<>(element);
        }

        if (compareElements(element, node.data) < 0) {
            node.left = add(node.left, element);
        } else if (compareElements(element, node.data) > 0) {
            node.right = add(node.right, element);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalanceFactor(node);

        if (balance > 1 && compareElements(element, node.left.data) < 0) {
            return rightRotate(node);
        }

        if (balance < -1 && compareElements(element, node.right.data) > 0) {
            return leftRotate(node);
        }

        if (balance > 1 && compareElements(element, node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && compareElements(element, node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public void remove(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("AVL Tree is empty");
        this.root = remove(root, element);
    }

    private BTreeNode<T> remove(BTreeNode<T> node, T element) {
        if (node == null) {
            return node;
        }

        if (compareElements(element, node.data) < 0) {
            node.left = remove(node.left, element);
        } else if (compareElements(element, node.data) > 0) {
            node.right = remove(node.right, element);
        } else {
            if (node.left == null || node.right == null) {
                BTreeNode<T> temp = null;
                if (temp == node.left) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }

                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                BTreeNode<T> temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = remove(node.right, temp.data);
            }
        }

        if (node == null) {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalanceFactor(node);

        if (balance > 1 && getBalanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }

        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }

        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private int height(BTreeNode<T> node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalanceFactor(BTreeNode<T> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private BTreeNode<T> rightRotate(BTreeNode<T> y) {
        BTreeNode<T> x = y.left;
        BTreeNode<T> T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    private BTreeNode<T> leftRotate(BTreeNode<T> x) {
        BTreeNode<T> y = x.right;
        BTreeNode<T> T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }

    private BTreeNode<T> minValueNode(BTreeNode<T> node) {
        BTreeNode<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    @Override
    public int height() throws TreeException {
        if (isEmpty()) throw new TreeException("AVL Tree is empty");
        return height(root);
    }

    @Override
    public int height(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("AVL Tree is empty");
        BTreeNode<T> node = findNode(root, element);
        if (node == null) throw new TreeException("Element not found in AVL Tree");
        return height(node);
    }

    public BTreeNode<T> findNode(BTreeNode<T> node, T element) {
        if (node == null) return null;
        if (compareElements(element, node.data) == 0) return node;
        if (compareElements(element, node.data) < 0) return findNode(node.left, element);
        return findNode(node.right, element);
    }

    public boolean isBalanced() throws TreeException {
        if (isEmpty()) return true;
        return isBalanced(root);
    }

    private boolean isBalanced(BTreeNode<T> node) {
        if (node == null) {
            return true;
        }
        int balanceFactor = getBalanceFactor(node);
        if (Math.abs(balanceFactor) > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }
}