package model.tree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BTreeTest {

    private BTree<Integer> buildFixedTree() {
        BTreeNode<Integer> n20 = new BTreeNode<>(20);
        BTreeNode<Integer> n12 = new BTreeNode<>(12);
        BTreeNode<Integer> n15 = new BTreeNode<>(15);
        n15.left = n12;
        n15.right = n20;
        BTreeNode<Integer> n3 = new BTreeNode<>(3);
        BTreeNode<Integer> n5 = new BTreeNode<>(5);
        n5.left = n3;
        n5.right = n15;
        BTreeNode<Integer> n7 = new BTreeNode<>(7);
        BTreeNode<Integer> n10 = new BTreeNode<>(10);
        n10.left = n5;
        n10.right = n7;
        BTree<Integer> tree = new BTree<>();
        tree.root = n10;
        return tree;
    }

    @Test
    void testPrintLeaves() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        String leaves = tree.printLeaves();
        assertTrue(leaves.contains("3"));
        assertTrue(leaves.contains("12"));
        assertTrue(leaves.contains("20"));
        assertTrue(leaves.contains("7"));
    }

    @Test
    void testTotalLeaves() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        assertEquals(4, tree.totalLeaves());
    }

    @Test
    void testPrintNodesWithChildren() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        String result = tree.printNodesWithChildren();
        assertTrue(result.contains("10"));
        assertTrue(result.contains("5"));
        assertTrue(result.contains("15"));
    }

    @Test
    void testPrintNodes1Child() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        String result = tree.printNodes1Child();
        assertTrue(result.contains("10"));
    }

    @Test
    void testPrintNodes2Children() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        String result = tree.printNodes2Children();
        assertTrue(result.contains("5"));
        assertTrue(result.contains("15"));
        assertFalse(result.contains("Nodo: 10"));
    }

    @Test
    void testGrandFather() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        assertEquals("no tiene abuelo", tree.grandFather(10));
        assertEquals("no tiene abuelo", tree.grandFather(5));
        assertEquals("no tiene abuelo", tree.grandFather(7));
        assertEquals(10, tree.grandFather(3));
        assertEquals(10, tree.grandFather(15));
        assertEquals(5, tree.grandFather(12));
        assertEquals(5, tree.grandFather(20));
    }

    @Test
    void testFather() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        assertEquals("no tiene padre", tree.father(10));
        assertEquals(10, tree.father(5));
        assertEquals(10, tree.father(7));
        assertEquals(5, tree.father(3));
        assertEquals(5, tree.father(15));
        assertEquals(15, tree.father(12));
        assertEquals(15, tree.father(20));
    }

    @Test
    void testBrother() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        assertEquals("no tiene hermano", tree.brother(10));
        assertEquals(7, tree.brother(5));
        assertEquals(5, tree.brother(7));
        assertEquals(15, tree.brother(3));
        assertEquals(3, tree.brother(15));
        assertEquals(20, tree.brother(12));
        assertEquals(12, tree.brother(20));
    }

    @Test
    void testCousins() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        assertEquals("no tiene primos", tree.cousins(10));
        assertEquals("no tiene primos", tree.cousins(3));
        assertEquals("no tiene primos", tree.cousins(15));
        String primosDe12 = tree.cousins(12);
        assertEquals("no tiene primos", primosDe12);
    }

    @Test
    void testPrintSubtree() throws TreeException {
        BTree<Integer> tree = buildFixedTree();
        String subtree = tree.printSubtree(5);
        assertTrue(subtree.contains("5"));
        assertTrue(subtree.contains("3"));
        assertTrue(subtree.contains("15"));
        assertTrue(subtree.contains("12"));
        assertTrue(subtree.contains("20"));
        assertFalse(subtree.contains("7"));
    }
}