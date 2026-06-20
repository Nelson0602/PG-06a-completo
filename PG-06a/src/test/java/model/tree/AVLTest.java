package model.tree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AVLTest {

    private AVL<Integer> buildTree() {
        AVL<Integer> avl = new AVL<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) avl.add(v);
        return avl;
    }

    @Test
    void testIsBalanced() throws TreeException {
        AVL<Integer> avl = buildTree();
        assertTrue(avl.isBalanced());
    }

    @Test
    void testPrintLeaves() throws TreeException {
        AVL<Integer> avl = buildTree();
        String leaves = avl.printLeaves();
        assertTrue(leaves.contains("20"));
        assertTrue(leaves.contains("40"));
        assertTrue(leaves.contains("60"));
        assertTrue(leaves.contains("80"));
    }

    @Test
    void testTotalLeaves() throws TreeException {
        AVL<Integer> avl = buildTree();
        assertEquals(4, avl.totalLeaves());
    }

    @Test
    void testGrandFather() throws TreeException {
        AVL<Integer> avl = buildTree();
        assertEquals("no tiene abuelo", avl.grandFather(50));
        assertEquals("no tiene abuelo", avl.grandFather(30));
        assertEquals("no tiene abuelo", avl.grandFather(70));
        assertEquals(50, avl.grandFather(20));
        assertEquals(50, avl.grandFather(40));
        assertEquals(50, avl.grandFather(60));
        assertEquals(50, avl.grandFather(80));
    }

    @Test
    void testFather() throws TreeException {
        AVL<Integer> avl = buildTree();
        assertEquals("no tiene padre", avl.father(50));
        assertEquals(50, avl.father(30));
        assertEquals(50, avl.father(70));
        assertEquals(30, avl.father(20));
        assertEquals(30, avl.father(40));
        assertEquals(70, avl.father(60));
        assertEquals(70, avl.father(80));
    }

    @Test
    void testBrother() throws TreeException {
        AVL<Integer> avl = buildTree();
        assertEquals(70, avl.brother(30));
        assertEquals(30, avl.brother(70));
        assertEquals(40, avl.brother(20));
        assertEquals(20, avl.brother(40));
        assertEquals(80, avl.brother(60));
        assertEquals(60, avl.brother(80));
    }

    @Test
    void testCousins() throws TreeException {
        AVL<Integer> avl = buildTree();
        String primosDe20 = avl.cousins(20);
        assertTrue(primosDe20.contains("60") && primosDe20.contains("80"));
        assertEquals("no tiene primos", avl.cousins(50));
    }

    @Test
    void testRemainsBalancedAfterInsertions() throws TreeException {
        AVL<Integer> avl = new AVL<>();
        for (int i = 1; i <= 20; i++) avl.add(i);
        assertTrue(avl.isBalanced());
    }
}