package ucr.lab.pg06a;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import model.graph.AdjacencyListGraph;
import model.graph.AdjacencyMatrixGraph;
import model.graph.Graph;
import model.graph.LinkedGraph;
import model.tree.*;

import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {

    private static final Color BG       = Color.web("#0d1117");
    private static final Color EDGE_CLR = Color.web("#4a5568");
    private static final Color TXT_CLR  = Color.WHITE;
    private static final double NR      = 28;

    @FXML private CheckBox graphDirCheck;
    @FXML private ComboBox<String> graphReprCombo;
    @FXML private TextArea graphEdgesArea;
    @FXML private Canvas graphCanvas;
    @FXML private Label graphStatsLabel;
    @FXML private TextArea graphInfoArea;

    private Graph<Integer> currentGraph;
    private boolean graphDirected;
    private int[] vertexValues;
    private List<int[]> currentEdges;

    @FXML private ComboBox<String> treeTypeCombo;
    @FXML private Canvas treeCanvas;
    @FXML private Label treeStatsLabel;
    @FXML private TextArea treeInfoArea;

    private BTree<Integer> currentTree;

    @FXML private Canvas btSumCanvas;
    @FXML private TextArea btSumInfoArea;

    @FXML private Canvas btNodeSumCanvas;
    @FXML private TextArea btNodeSumInfoArea;

    @FXML private ComboBox<String> tightenTypeCombo;
    @FXML private Canvas tightenCanvas;
    @FXML private TextArea tightenInfoArea;

    private BTree<Integer> tightenTree;

    @FXML private Canvas abmCanvas;
    @FXML private TextArea abmInfoArea;

    private BTree<Integer> abmTree1;
    private BTree<Integer> abmTree2;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphReprCombo.getItems().addAll("Matriz de Adyacencia", "Lista de Adyacencia", "Lista Enlazada");
        graphReprCombo.setValue("Matriz de Adyacencia");
        clearCanvas(graphCanvas);
        graphInfoArea.setText("Grafo vacío. Genere un grafo para comenzar.");

        treeTypeCombo.getItems().addAll("Árbol Binario Simple", "Árbol Búsqueda Binaria (BST)", "Árbol Auto Balanceado (AVL)");
        treeTypeCombo.setValue("Árbol Búsqueda Binaria (BST)");
        clearCanvas(treeCanvas);
        treeInfoArea.setText("Seleccione un tipo de árbol y presione 'Generar Árbol Ejemplo'.");

        clearCanvas(btSumCanvas);
        btSumInfoArea.setText("Presione 'Generar Ejemplo y Sumar'.");

        clearCanvas(btNodeSumCanvas);
        btNodeSumInfoArea.setText("Presione 'Generar Ejemplo y Calcular'.");

        tightenTypeCombo.getItems().addAll("Árbol Binario Simple", "Árbol Búsqueda Binaria (BST)", "Árbol Auto Balanceado (AVL)");
        tightenTypeCombo.setValue("Árbol Búsqueda Binaria (BST)");
        clearCanvas(tightenCanvas);
        tightenInfoArea.setText("Presione 'Generar Árbol Ejemplo'.");

        clearCanvas(abmCanvas);
        abmInfoArea.setText("Presione 'Generar Árboles ABM Ejemplo'.");

    }

    private List<int[]> parseEdges(String text) {
        List<int[]> edges = new ArrayList<>();
        if (text == null || text.isBlank()) return edges;
        for (String part : text.trim().split(",")) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] uv = part.split("-");
            int u = Integer.parseInt(uv[0].trim());
            int v = Integer.parseInt(uv[1].trim());
            edges.add(new int[]{u, v});
        }
        return edges;
    }

    @FXML void onGraphExample() {
        try {
            String repr = graphReprCombo.getValue();
            boolean directed;
            String edgesText;
            if (repr.equals("Matriz de Adyacencia")) {
                directed = false;
                edgesText = "0-1,0-2,0-3,1-4,2-3";
            } else {
                directed = true;
                edgesText = "0-1,0-2,0-3,1-4,2-3,2-4,4-0";
            }
            graphDirCheck.setSelected(directed);
            graphEdgesArea.setText(edgesText);

            List<int[]> edges = parseEdges(edgesText);
            int maxIndex = -1;
            for (int[] e : edges) maxIndex = Math.max(maxIndex, Math.max(e[0], e[1]));
            int n = Math.max(maxIndex + 1, 2);

            Graph<Integer> g;
            if (repr.equals("Lista de Adyacencia")) g = new AdjacencyListGraph<>(n, directed);
            else if (repr.equals("Lista Enlazada")) g = new LinkedGraph<>(directed);
            else g = new AdjacencyMatrixGraph<>(n, directed);

            for (int i = 0; i < n; i++) g.addVertex(i);
            for (int[] e : edges) g.addEdge(e[0], e[1]);

            currentGraph = g;
            graphDirected = directed;
            vertexValues = new int[n];
            for (int i = 0; i < n; i++) vertexValues[i] = i;
            currentEdges = new ArrayList<>(edges);

            drawGraph();
            graphInfoArea.setText(buildGraphReport());
            graphStatsLabel.setText("Representación: " + repr + "  |  Vértices: " + n + "  |  Aristas: " + currentEdges.size());
        } catch (Exception ex) {
            graphInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onGraphAddEdges() {
        try {
            if (currentGraph == null) {
                graphInfoArea.setText("Primero genere un grafo ejemplo.");
                return;
            }
            List<int[]> edges = parseEdges(graphEdgesArea.getText());
            for (int[] e : edges) {
                currentGraph.addEdge(e[0], e[1]);
                boolean exists = false;
                for (int[] ex : currentEdges) {
                    if (ex[0] == e[0] && ex[1] == e[1]) exists = true;
                }
                if (!exists) currentEdges.add(e);
            }
            drawGraph();
            graphInfoArea.setText(buildGraphReport());
        } catch (Exception ex) {
            graphInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onGraphRemoveEdges() {
        try {
            if (currentGraph == null) {
                graphInfoArea.setText("Primero genere un grafo ejemplo.");
                return;
            }
            List<int[]> edges = parseEdges(graphEdgesArea.getText());
            for (int[] e : edges) {
                currentGraph.removeEdge(e[0], e[1]);
                currentEdges.removeIf(ex -> ex[0] == e[0] && ex[1] == e[1]);
            }
            drawGraph();
            graphInfoArea.setText(buildGraphReport());
        } catch (Exception ex) {
            graphInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onGraphReset() {
        currentGraph = null;
        currentEdges = null;
        vertexValues = null;
        graphEdgesArea.clear();
        clearCanvas(graphCanvas);
        graphInfoArea.setText("Grafo vacío. Genere un grafo para comenzar.");
        graphStatsLabel.setText("Grafo vacío. Genere un grafo para comenzar.");
    }

    @FXML void onGraphDirToggle() {
        if (currentGraph == null) return;
        try {
            String repr = graphReprCombo.getValue();
            boolean directed = graphDirCheck.isSelected();
            int n = vertexValues.length;

            Graph<Integer> g;
            if (repr.equals("Lista de Adyacencia")) g = new AdjacencyListGraph<>(n, directed);
            else if (repr.equals("Lista Enlazada")) g = new LinkedGraph<>(directed);
            else g = new AdjacencyMatrixGraph<>(n, directed);

            for (int i = 0; i < n; i++) g.addVertex(i);
            for (int[] e : currentEdges) g.addEdge(e[0], e[1]);

            currentGraph = g;
            graphDirected = directed;

            drawGraph();
            graphInfoArea.setText(buildGraphReport());
            graphStatsLabel.setText("Representación: " + repr + "  |  Vértices: " + n + "  |  Aristas: " + currentEdges.size());
        } catch (Exception ex) {
            graphInfoArea.setText("Error: " + ex.getMessage());
        }
    }


    private String buildGraphReport() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(currentGraph.toString()).append("\n\n");
            sb.append("RECORRIDO CON EL ALGORITMO DFS (DEPTH FIRST SEARCH):\n");
            sb.append(currentGraph.dfs()).append("\n\n");
            sb.append("RECORRIDO CON EL ALGORITMO BFS (BREADTH FIRST SEARCH):\n");
            sb.append(currentGraph.bfs()).append("\n\n");
            sb.append("getGraphDegree(): ").append(currentGraph.getGraphDegree()).append("\n");
            sb.append("totalEdges(): ").append(currentGraph.totalEdges()).append("\n\n");
            for (int v : vertexValues) {
                sb.append("getVertexDegree(").append(v).append("): ").append(currentGraph.getVertexDegree(v));
                sb.append("   totalEdges(").append(v).append("): ").append(currentGraph.totalEdges(v));
                sb.append("   getEdges(").append(v).append("): ").append(currentGraph.getEdges(v)).append("\n");
            }
        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private void drawGraph() {
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();
        clearCanvas(graphCanvas);
        if (currentGraph == null || vertexValues == null || vertexValues.length == 0) return;
        double cx = graphCanvas.getWidth() / 2;
        double cy = graphCanvas.getHeight() / 2 - 10;
        double r = Math.min(graphCanvas.getWidth(), graphCanvas.getHeight()) / 2 - 60;
        double[][] pos = calcCirclePos(vertexValues.length, cx, cy, r);
        gc.setStroke(EDGE_CLR);
        gc.setLineWidth(2);
        for (int[] e : currentEdges) {
            int i = e[0], j = e[1];
            if (i < pos.length && j < pos.length) {
                if (graphDirected) drawArrow(gc, pos[i][0], pos[i][1], pos[j][0], pos[j][1]);
                else gc.strokeLine(pos[i][0], pos[i][1], pos[j][0], pos[j][1]);
            }
        }
        for (int i = 0; i < vertexValues.length; i++)
            drawNode(gc, pos[i][0], pos[i][1], String.valueOf(vertexValues[i]), Color.web("#1f6feb"));
    }

    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double startX = x1 + NR * Math.cos(angle);
        double startY = y1 + NR * Math.sin(angle);
        double endX = x2 - NR * Math.cos(angle);
        double endY = y2 - NR * Math.sin(angle);
        gc.strokeLine(startX, startY, endX, endY);
        double aLen = 10, aAngle = Math.PI / 8;
        double ax1 = endX - aLen * Math.cos(angle - aAngle);
        double ay1 = endY - aLen * Math.sin(angle - aAngle);
        double ax2 = endX - aLen * Math.cos(angle + aAngle);
        double ay2 = endY - aLen * Math.sin(angle + aAngle);
        gc.strokeLine(endX, endY, ax1, ay1);
        gc.strokeLine(endX, endY, ax2, ay2);
    }

    private double[][] calcCirclePos(int n, double cx, double cy, double r) {
        double[][] pos = new double[n][2];
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            pos[i][0] = cx + r * Math.cos(angle);
            pos[i][1] = cy + r * Math.sin(angle);
        }
        return pos;
    }

    @FXML void onTreeGenerate() {
        try {
            String tipo = treeTypeCombo.getValue();
            if (tipo.equals("Árbol Auto Balanceado (AVL)")) currentTree = new AVL<>();
            else if (tipo.equals("Árbol Búsqueda Binaria (BST)")) currentTree = new BST<>();
            else currentTree = new BTree<>();

            Random rand = new Random();
            boolean completo = false;
            while (!completo) {
                int value = rand.nextInt(51);
                try {
                    if (currentTree.size() < 10) {
                        if (!currentTree.contains(value)) {
                            currentTree.add(value);
                        }
                    } else {
                        completo = true;
                    }
                } catch (TreeException e) {
                    currentTree.add(value);
                }
            }

            drawTree();
            treeStatsLabel.setText("Tipo: " + tipo + "  |  size: " + safeSize() + "  |  height: " + safeHeight());
            mostrarReporte();
        } catch (Exception ex) {
            treeInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onTreeClear() {
        currentTree = null;
        clearCanvas(treeCanvas);
        treeInfoArea.setText("Árbol vacío. Genere un árbol para comenzar.");
        treeStatsLabel.setText("Árbol vacío. Genere un árbol para comenzar.");
    }

    private void mostrarReporte() {
        List<Integer> valores = inOrderList(currentTree.root);

        StringBuilder sb = new StringBuilder();
        try {
            sb.append("1.  printNodesWithChildren()\n");
            sb.append(currentTree.printNodesWithChildren()).append("\n");

            sb.append("2.  printNodes1Child()\n");
            String unoSolo = currentTree.printNodes1Child();
            sb.append(unoSolo.isEmpty() ? "(no hay nodos con un solo hijo)\n" : unoSolo).append("\n");

            sb.append("3.  printNodes2Children()\n");
            sb.append(currentTree.printNodes2Children()).append("\n");

            sb.append("4.  printLeaves()\n");
            sb.append(currentTree.printLeaves()).append("\n");

            sb.append("5.  grandFather\n");
            for (int v : valores) sb.append("grandFather(").append(v).append("): ").append(currentTree.grandFather(v)).append("\n");
            sb.append("\n");

            sb.append("6.  father\n");
            for (int v : valores) sb.append("father(").append(v).append("): ").append(currentTree.father(v)).append("\n");
            sb.append("\n");

            sb.append("7.  brother\n");
            for (int v : valores) sb.append("brother(").append(v).append("): ").append(currentTree.brother(v)).append("\n");
            sb.append("\n");

            sb.append("8.  cousins\n");
            for (int v : valores) sb.append("cousins(").append(v).append("): ").append(currentTree.cousins(v).replace("\n", " ")).append("\n");
            sb.append("\n");

            sb.append("9.  printSubtree\n");
            for (int v : valores) sb.append("printSubtree(").append(v).append("): ").append(currentTree.printSubtree(v).replace("\n", " ")).append("\n");
            sb.append("\n");

            sb.append("10. totalLeaves()\n");
            sb.append("totalLeaves(): ").append(currentTree.totalLeaves()).append("\n");

        } catch (TreeException e) {
            sb.append("Error: ").append(e.getMessage());
        }

        treeInfoArea.setText(sb.toString());
    }

    private List<Integer> inOrderList(BTreeNode<Integer> node) {
        List<Integer> lista = new ArrayList<>();
        agregarInOrder(node, lista);
        return lista;
    }

    private void agregarInOrder(BTreeNode<Integer> node, List<Integer> lista) {
        if (node == null) return;
        agregarInOrder(node.left, lista);
        lista.add(node.data);
        agregarInOrder(node.right, lista);
    }

    private String safeSize() {
        try { return String.valueOf(currentTree.size()); }
        catch (TreeException e) { return "0"; }
    }

    private String safeHeight() {
        try { return String.valueOf(currentTree.height()); }
        catch (TreeException e) { return "0"; }
    }

    private void drawTree() {
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        clearCanvas(treeCanvas);
        if (currentTree == null || currentTree.isEmpty()) return;
        double w = treeCanvas.getWidth();
        drawTreeNode(gc, currentTree.root, w / 2, 40, w / 4);
    }

    private void drawTreeNode(GraphicsContext gc, BTreeNode<Integer> node, double x, double y, double offset) {
        if (node == null) return;

        if (node.left != null) {
            gc.setStroke(EDGE_CLR);
            gc.setLineWidth(2);
            gc.strokeLine(x, y, x - offset, y + 70);
            drawTreeNode(gc, node.left, x - offset, y + 70, offset / 2);
        }
        if (node.right != null) {
            gc.setStroke(EDGE_CLR);
            gc.setLineWidth(2);
            gc.strokeLine(x, y, x + offset, y + 70);
            drawTreeNode(gc, node.right, x + offset, y + 70, offset / 2);
        }

        drawNode(gc, x, y, String.valueOf(node.data), Color.web("#238636"));
    }

    private void drawNode(GraphicsContext gc, double x, double y, String label, Color color) {
        gc.setFill(Color.color(0, 0, 0, 0.3));
        gc.fillOval(x - NR + 3, y - NR + 3, NR * 2, NR * 2);
        gc.setFill(color);
        gc.fillOval(x - NR, y - NR, NR * 2, NR * 2);
        gc.setStroke(Color.color(1, 1, 1, 0.2));
        gc.setLineWidth(1.5);
        gc.strokeOval(x - NR, y - NR, NR * 2, NR * 2);
        gc.setFill(TXT_CLR);
        gc.setFont(Font.font("System", FontWeight.BOLD, 12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(label, x, y + 4);
    }

    @FXML void onBTreesSum() {
        BTreeNode<Integer> leaf5a = new BTreeNode<>(5);
        BTreeNode<Integer> leaf7 = new BTreeNode<>(7);
        BTreeNode<Integer> nodeA = new BTreeNode<>(5);
        nodeA.left = leaf5a;
        nodeA.right = leaf7;
        BTreeNode<Integer> leaf8 = new BTreeNode<>(8);
        BTreeNode<Integer> nodeB = new BTreeNode<>(4);
        nodeB.left = leaf8;
        BTreeNode<Integer> root1 = new BTreeNode<>(2);
        root1.left = nodeA;
        root1.right = nodeB;
        BTree<Integer> tree1 = new BTree<>();
        tree1.root = root1;

        BTreeNode<Integer> leaf6 = new BTreeNode<>(6);
        BTreeNode<Integer> nodeD = new BTreeNode<>(2);
        nodeD.right = leaf6;
        BTreeNode<Integer> nodeC = new BTreeNode<>(3);
        nodeC.right = nodeD;
        BTreeNode<Integer> leaf1 = new BTreeNode<>(1);
        BTreeNode<Integer> leaf4 = new BTreeNode<>(4);
        BTreeNode<Integer> nodeE = new BTreeNode<>(15);
        nodeE.left = leaf1;
        nodeE.right = leaf4;
        BTreeNode<Integer> root2 = new BTreeNode<>(10);
        root2.left = nodeC;
        root2.right = nodeE;
        BTree<Integer> tree2 = new BTree<>();
        tree2.root = root2;

        BTree<Integer> sum = BTree.bTreesSum(tree1, tree2);

        StringBuilder sb = new StringBuilder();
        sb.append("Árbol Binario 1 (PreOrder): ").append(preOrderValues(tree1.root)).append("\n");
        sb.append("Árbol Binario 2 (PreOrder): ").append(preOrderValues(tree2.root)).append("\n");
        sb.append("Árbol Binario Sumado (PreOrder): ").append(preOrderValues(sum.root)).append("\n");

        drawTreeOn(btSumCanvas, sum.root);
        btSumInfoArea.setText(sb.toString());
    }

    @FXML void onBTreesSumClear() {
        clearCanvas(btSumCanvas);
        btSumInfoArea.setText("Presione 'Generar Ejemplo y Sumar'.");
    }

    @FXML void onBtNodeSum() {
        try {
            BTreeNode<Integer> leaf5a = new BTreeNode<>(5);
            BTreeNode<Integer> leaf7 = new BTreeNode<>(7);
            BTreeNode<Integer> nodeA = new BTreeNode<>(5);
            nodeA.left = leaf5a;
            nodeA.right = leaf7;
            BTreeNode<Integer> leaf8 = new BTreeNode<>(8);
            BTreeNode<Integer> nodeB = new BTreeNode<>(4);
            nodeB.left = leaf8;
            BTreeNode<Integer> root = new BTreeNode<>(2);
            root.left = nodeA;
            root.right = nodeB;
            BTree<Integer> tree = new BTree<>();
            tree.root = root;

            BTree<Integer> result = BTree.btNodeSum(tree);

            StringBuilder sb = new StringBuilder();
            sb.append("Árbol Binario Simple (PreOrder): ").append(preOrderValues(tree.root)).append("\n");
            sb.append("btNodeSum (PreOrder): ").append(preOrderValues(result.root)).append("\n");

            drawTreeOn(btNodeSumCanvas, result.root);
            btNodeSumInfoArea.setText(sb.toString());
        } catch (Exception ex) {
            btNodeSumInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onBtNodeSumClear() {
        clearCanvas(btNodeSumCanvas);
        btNodeSumInfoArea.setText("Presione 'Generar Ejemplo y Calcular'.");
    }

    @FXML void onTightenGenerate() {
        try {
            String tipo = tightenTypeCombo.getValue();
            int[] values = {50, 40, 70, 24, 55, 80, 10, 30, 60, 39, 57, 65};
            if (tipo.equals("Árbol Auto Balanceado (AVL)")) tightenTree = new AVL<>();
            else if (tipo.equals("Árbol Búsqueda Binaria (BST)")) tightenTree = new BST<>();
            else tightenTree = new BTree<>();
            for (int v : values) tightenTree.add(v);

            drawTreeOn(tightenCanvas, tightenTree.root);
            tightenInfoArea.setText("ANTES de tighten()\nPreOrder: " + preOrderValues(tightenTree.root)
                    + "\nsize: " + safeSize(tightenTree) + "  height: " + safeHeight(tightenTree));
        } catch (Exception ex) {
            tightenInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onTightenApply() {
        try {
            if (tightenTree == null) {
                tightenInfoArea.setText("Primero genere un árbol ejemplo.");
                return;
            }
            String before = preOrderValues(tightenTree.root);
            String beforeSize = safeSize(tightenTree);
            String beforeHeight = safeHeight(tightenTree);

            tightenTree.tighten();

            drawTreeOn(tightenCanvas, tightenTree.root);
            StringBuilder sb = new StringBuilder();
            sb.append("ANTES de tighten()\n");
            sb.append("PreOrder: ").append(before).append("\n");
            sb.append("size: ").append(beforeSize).append("  height: ").append(beforeHeight).append("\n\n");
            sb.append("DESPUÉS de tighten()\n");
            sb.append("PreOrder: ").append(preOrderValues(tightenTree.root)).append("\n");
            sb.append("size: ").append(safeSize(tightenTree)).append("  height: ").append(safeHeight(tightenTree)).append("\n");
            if (tightenTree instanceof AVL) {
                sb.append("Balanceado: ").append(((AVL<Integer>) tightenTree).isBalanced()).append("\n");
            }
            tightenInfoArea.setText(sb.toString());
        } catch (Exception ex) {
            tightenInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onTightenClear() {
        tightenTree = null;
        clearCanvas(tightenCanvas);
        tightenInfoArea.setText("Presione 'Generar Árbol Ejemplo'.");
    }

    @FXML void onAbmGenerate() {
        BTreeNode<Integer> leaf8b = new BTreeNode<>(8);
        BTreeNode<Integer> nodeLeft = new BTreeNode<>(5);
        nodeLeft.left = leaf8b;
        BTreeNode<Integer> leaf5b = new BTreeNode<>(5);
        BTreeNode<Integer> leaf7b = new BTreeNode<>(7);
        BTreeNode<Integer> nodeRight = new BTreeNode<>(4);
        nodeRight.left = leaf5b;
        nodeRight.right = leaf7b;
        BTreeNode<Integer> root1 = new BTreeNode<>(2);
        root1.left = nodeLeft;
        root1.right = nodeRight;
        abmTree1 = new BTree<>();
        abmTree1.root = root1;

        BTreeNode<Integer> leaf3 = new BTreeNode<>(3);
        BTreeNode<Integer> leaf2 = new BTreeNode<>(2);
        BTreeNode<Integer> root2 = new BTreeNode<>(1);
        root2.left = leaf3;
        root2.right = leaf2;
        abmTree2 = new BTree<>();
        abmTree2.root = root2;

        StringBuilder sb = new StringBuilder();
        sb.append("Árbol ABM 1 (PreOrder): ").append(preOrderValues(abmTree1.root)).append("\n");
        sb.append("isABM(arbol1): ").append(BTree.isABM(abmTree1)).append("\n\n");
        sb.append("Árbol ABM 2 (PreOrder): ").append(preOrderValues(abmTree2.root)).append("\n");
        sb.append("isABM(arbol2): ").append(BTree.isABM(abmTree2)).append("\n");

        drawTreeOn(abmCanvas, abmTree1.root);
        abmInfoArea.setText(sb.toString());
    }

    @FXML void onAbmJoin() {
        try {
            if (abmTree1 == null || abmTree2 == null) {
                abmInfoArea.setText("Primero genere los árboles ABM ejemplo.");
                return;
            }
            BTree<Integer> result = BTree.joinABM(abmTree1, abmTree2);
            StringBuilder sb = new StringBuilder();
            sb.append("Árbol ABM 1 (PreOrder): ").append(preOrderValues(abmTree1.root)).append("\n");
            sb.append("Árbol ABM 2 (PreOrder): ").append(preOrderValues(abmTree2.root)).append("\n\n");
            sb.append("joinABM resultado (PreOrder): ").append(preOrderValues(result.root)).append("\n");
            sb.append("isABM(resultado): ").append(BTree.isABM(result)).append("\n");

            drawTreeOn(abmCanvas, result.root);
            abmInfoArea.setText(sb.toString());
        } catch (Exception ex) {
            abmInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onAbmClear() {
        abmTree1 = null;
        abmTree2 = null;
        clearCanvas(abmCanvas);
        abmInfoArea.setText("Presione 'Generar Árboles ABM Ejemplo'.");
    }

    private String preOrderValues(BTreeNode<Integer> node) {
        StringBuilder sb = new StringBuilder();
        buildPreOrder(node, sb);
        return sb.toString().trim();
    }

    private void buildPreOrder(BTreeNode<Integer> node, StringBuilder sb) {
        if (node == null) return;
        sb.append(node.data).append(" ");
        buildPreOrder(node.left, sb);
        buildPreOrder(node.right, sb);
    }

    private void drawTreeOn(Canvas canvas, BTreeNode<Integer> root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(canvas);
        if (root == null) return;
        double w = canvas.getWidth();
        drawTreeNode(gc, root, w / 2, 40, w / 4);
    }

    private String safeSize(BTree<Integer> tree) {
        try { return String.valueOf(tree.size()); }
        catch (TreeException e) { return "0"; }
    }

    private String safeHeight(BTree<Integer> tree) {
        try { return String.valueOf(tree.height()); }
        catch (TreeException e) { return "0"; }
    }

    private void clearCanvas(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(BG);
        gc.fillRect(0, 0, c.getWidth(), c.getHeight());
    }
}



