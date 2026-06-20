package model.tree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BSTTest {

    private BST<Integer> buildTree() {
        BST<Integer> bst = new BST<>();
        int[] values = {8, 3, 10, 1, 6, 9, 14, 4, 7};
        for (int v : values) bst.add(v);
        return bst;
    }

    @Test
    void testPrintLeaves() throws TreeException {
        BST<Integer> bst = buildTree();
        String leaves = bst.printLeaves();
        assertTrue(leaves.contains("1"));
        assertTrue(leaves.contains("4"));
        assertTrue(leaves.contains("7"));
        assertTrue(leaves.contains("9"));
        assertTrue(leaves.contains("14"));
    }

    @Test
    void testTotalLeaves() throws TreeException {
        BST<Integer> bst = buildTree();
        assertEquals(5, bst.totalLeaves());
    }

    @Test
    void testPrintNodesWithChildren() throws TreeException {
        BST<Integer> bst = buildTree();
        String result = bst.printNodesWithChildren();
        assertTrue(result.contains("8"));
        assertTrue(result.contains("3"));
        assertTrue(result.contains("6"));
        assertTrue(result.contains("10"));
    }

    @Test
    void testPrintNodes2Children() throws TreeException {
        BST<Integer> bst = buildTree();
        String result = bst.printNodes2Children();
        assertTrue(result.contains("8"));
        assertTrue(result.contains("3"));
        assertTrue(result.contains("6"));
        assertTrue(result.contains("10"));
    }

    @Test
    void testGrandFather() throws TreeException {
        BST<Integer> bst = buildTree();
        assertEquals("no tiene abuelo", bst.grandFather(8));
        assertEquals("no tiene abuelo", bst.grandFather(3));
        assertEquals("no tiene abuelo", bst.grandFather(10));
        assertEquals(8, bst.grandFather(1));
        assertEquals(8, bst.grandFather(6));
        assertEquals(3, bst.grandFather(4));
        assertEquals(3, bst.grandFather(7));
        assertEquals(8, bst.grandFather(9));
        assertEquals(8, bst.grandFather(14));
    }

    @Test
    void testFather() throws TreeException {
        BST<Integer> bst = buildTree();
        assertEquals("no tiene padre", bst.father(8));
        assertEquals(8, bst.father(3));
        assertEquals(8, bst.father(10));
        assertEquals(3, bst.father(1));
        assertEquals(3, bst.father(6));
        assertEquals(6, bst.father(4));
        assertEquals(6, bst.father(7));
        assertEquals(10, bst.father(9));
        assertEquals(10, bst.father(14));
    }

    @Test
    void testBrother() throws TreeException {
        BST<Integer> bst = buildTree();
        assertEquals(10, bst.brother(3));
        assertEquals(3, bst.brother(10));
        assertEquals(6, bst.brother(1));
        assertEquals(1, bst.brother(6));
        assertEquals(7, bst.brother(4));
        assertEquals(4, bst.brother(7));
        assertEquals(14, bst.brother(9));
        assertEquals(9, bst.brother(14));
    }

    @Test
    void testCousins() throws TreeException {
        BST<Integer> bst = buildTree();
        String primosDe1 = bst.cousins(1);
        assertTrue(primosDe1.contains("9") && primosDe1.contains("14"));
        String primosDe4 = bst.cousins(4);
        assertTrue(primosDe4.contains("9") && primosDe4.contains("14"));
        assertEquals("no tiene primos", bst.cousins(8));
    }

    @Test
    void testPrintSubtree() throws TreeException {
        BST<Integer> bst = buildTree();
        String subtree = bst.printSubtree(3);
        assertTrue(subtree.contains("3"));
        assertTrue(subtree.contains("1"));
        assertTrue(subtree.contains("6"));
        assertTrue(subtree.contains("4"));
        assertTrue(subtree.contains("7"));
        assertFalse(subtree.contains("10"));
    }
}