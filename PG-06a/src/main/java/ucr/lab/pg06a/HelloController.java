package ucr.lab.pg06a;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.Node;
import model.graph.*;
import model.tree.*;

import java.net.URL;
import java.util.*;

/*
 * Controlador principal de PG-06.
 * Maneja los 4 tabs: Matriz de Adyacencia, Lista de Adyacencia,
 * Lista Enlazada y MST/Dijkstra.
 */
public class HelloController implements Initializable {

    // ── colores tema oscuro ──────────────────────────────────────────────────
    private static final Color BG       = Color.web("#0d1117");
    private static final Color NODE_CLR = Color.web("#1f6feb");
    private static final Color EDGE_CLR = Color.web("#4a5568");
    private static final Color MST_CLR  = Color.web("#00bcd4");
    private static final Color REJ_CLR  = Color.web("#ef4444");
    private static final Color ACT_CLR  = Color.web("#f59e0b");
    private static final Color SP_CLR   = Color.web("#7c3aed");
    private static final Color TXT_CLR  = Color.WHITE;
    private static final double NR      = 28; // radio nodo

    // nombres de ciudades CR para el ejemplo
    private static final String[] CR_NAMES = {"SJO","ALJ","HER","CAR","TURR","LIM","GUA","PUN","PZE","SCA"};

    // ── Tab 1: Matriz ────────────────────────────────────────────────────────
    @FXML private Spinner<Integer> matSpinner;
    @FXML private CheckBox matDirCheck;
    @FXML private TextArea matEdgesArea;
    @FXML private Canvas matCanvas;
    @FXML private Label matStatsLabel;
    @FXML private GridPane matrixGrid;
    @FXML private TextArea matInfoArea;
    @FXML private Label matVertexLegend;

    private AdjacencyMatrixGraph<Integer> matGraph;

    // ── Tab 2: Lista ─────────────────────────────────────────────────────────
    @FXML private Spinner<Integer> lstSpinner;
    @FXML private CheckBox lstDirCheck;
    @FXML private TextArea lstEdgesArea;
    @FXML private Canvas lstCanvas;
    @FXML private Label lstStatsLabel;
    @FXML private TextArea lstInfoArea;
    @FXML private Label lstVertexLegend;

    private AdjacencyListGraph<Integer> lstGraph;

    // ── Tab 3: Enlazada ──────────────────────────────────────────────────────
    @FXML private Spinner<Integer> lnkSpinner;
    @FXML private CheckBox lnkDirCheck;
    @FXML private TextArea lnkEdgesArea;
    @FXML private Canvas lnkCanvas;
    @FXML private Label lnkStatsLabel;
    @FXML private VBox lnkNodesBox;
    @FXML private TextArea lnkInfoArea;
    @FXML private Label lnkVertexLegend;

    private LinkedGraph<Integer> lnkGraph;

    // ── Tab 4: Algoritmos ────────────────────────────────────────────────────
    @FXML private ComboBox<String> algoCombo;
    @FXML private ComboBox<String> reprCombo;
    @FXML private ComboBox<String> startCombo;
    @FXML private ComboBox<String> endCombo;
    @FXML private VBox dijkstraPanel;
    @FXML private Canvas algoCanvas;
    @FXML private TextArea algoLogArea;
    @FXML private TextArea algoResultArea;
    @FXML private Label algoStepLabel;
    @FXML private Label algoStateLabel;
    @FXML private Label algoReprLabel;
    @FXML private Label algoWeightLabel;
    @FXML private Label routeLabel;
    @FXML private Label legendMstLabel;
    @FXML private Slider speedSlider;

    // grafo ponderado fijo para MST/Dijkstra (igual al de las fotos)
    private static final String[] ALGO_V = {"A","B","C","D","E","F","G"};
    private static final int[][] ALGO_E = {
            {0,1,7},{0,3,5},{1,2,8},{1,4,7},{2,4,5},{3,4,15},
            {3,5,6},{4,5,8},{4,6,9},{5,6,11}
    };

    // pasos de animación
    private List<AlgoStep> steps = new ArrayList<>();
    private int currentStep = -1;
    private Timeline autoTimeline;

    // posiciones fijas de los nodos del grafo de algoritmos
    private double[][] algoPos;

    // ── Tab 5: Árboles Binarios ──────────────────────────────────────────────
    @FXML private ComboBox<String> treeTypeCombo;
    @FXML private Canvas treeCanvas;
    @FXML private Label treeStatsLabel;
    @FXML private TextArea treeInfoArea;

    private BTree<Integer> currentTree; // puede ser BTree, BST o AVL segun el combo

    // ── Inicialización ───────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        algoCombo.getItems().addAll("Kruskal (MST)", "Prim (MST)", "Dijkstra (Shortest Path)");
        algoCombo.setValue("Kruskal (MST)");
        reprCombo.getItems().addAll("Matriz de Adyacencia", "Lista de Adyacencia");
        reprCombo.setValue("Matriz de Adyacencia");

        for (String v : ALGO_V) { startCombo.getItems().add(v); endCombo.getItems().add(v); }
        startCombo.setValue("A");
        endCombo.setValue("G");

        // mostrar/ocultar panel de Dijkstra según el algoritmo seleccionado
        algoCombo.setOnAction(e -> {
            boolean isDijk = algoCombo.getValue().contains("Dijkstra");
            dijkstraPanel.setVisible(isDijk);
            dijkstraPanel.setManaged(isDijk);
            legendMstLabel.setText(isDijk ? "Shortest Path" : "MST");
        });
        dijkstraPanel.setVisible(false);
        dijkstraPanel.setManaged(false);

        algoPos = calcCirclePos(7, 370, 290, 200);
        drawAlgoGraph(null, null, null);
        clearCanvas(matCanvas);
        clearCanvas(lstCanvas);
        clearCanvas(lnkCanvas);

        treeTypeCombo.getItems().addAll("Árbol Binario Simple", "Árbol Búsqueda Binaria (BST)", "Árbol Auto Balanceado (AVL)");
        treeTypeCombo.setValue("Árbol Búsqueda Binaria (BST)");
        clearCanvas(treeCanvas);
        treeInfoArea.setText("Seleccione un tipo de árbol y presione 'Generar Árbol Aleatorio'.");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TAB 1 – MATRIZ DE ADYACENCIA
    // ════════════════════════════════════════════════════════════════════════

    @FXML void onMatGenerate() {
        try {
            Integer nVal = matSpinner.getValue();
            int n = (nVal != null) ? nVal : 6;
            boolean dir = matDirCheck.isSelected();
            matGraph = new AdjacencyMatrixGraph<>(n, dir);
            for (int i = 0; i < n; i++) matGraph.addVertex(i);
            parseAndAddEdges(matEdgesArea.getText(), matGraph, false);
            drawGraph(matCanvas, matGraph, n, false);
            updateMatrixGrid(matGraph, n);
            updateMatInfo(matGraph, n, dir);
        } catch (Exception ex) {
            matInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onMatExample() {
        matSpinner.getValueFactory().setValue(6);
        matDirCheck.setSelected(true);
        matEdgesArea.setText("0-1,0-2,1-2,1-3,2-4,3-4,3-5,4-5");
        onMatGenerate();
    }

    @FXML void onMatClear() {
        matGraph = null;
        clearCanvas(matCanvas);
        matrixGrid.getChildren().clear();
        matInfoArea.clear();
    }

    private void updateMatrixGrid(AdjacencyMatrixGraph<Integer> g, int n) {
        matrixGrid.getChildren().clear();
        // encabezados
        for (int j = 0; j < n; j++) {
            Label lbl = styledLabel(nameOf(j), "#8b949e", true);
            matrixGrid.add(lbl, j + 1, 0);
        }
        for (int i = 0; i < n; i++) {
            matrixGrid.add(styledLabel(nameOf(i), "#8b949e", true), 0, i + 1);
            for (int j = 0; j < n; j++) {
                Object val = getCell(g, i, j);
                boolean edge = !val.equals(0);
                Label cell = styledLabel(val.toString(), edge ? "white" : "#4a5568", false);
                cell.setStyle(cell.getStyle() + (edge
                        ? "-fx-background-color:#1f6feb; -fx-padding:3 6;"
                        : "-fx-background-color:#21262d; -fx-padding:3 6;"));
                matrixGrid.add(cell, j + 1, i + 1);
            }
        }
    }

    private void updateMatInfo(AdjacencyMatrixGraph<Integer> g, int n, boolean dir) {
        StringBuilder sb = new StringBuilder();
        sb.append("==== MATRIZ DE ADYACENCIA ====\n\n");
        // encabezado
        sb.append("     ");
        for (int j = 0; j < n; j++) sb.append(String.format("%-4s", nameOf(j)));
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-5s", nameOf(i)));
            for (int j = 0; j < n; j++) sb.append(String.format("%-4s", getCell(g, i, j)));
            sb.append("\n");
        }
        try {
            sb.append("\n---- Estadísticas ----\n");
            sb.append("Vértices (V): ").append(g.size()).append("\n");
            sb.append("Aristas  (E): ").append(g.totalEdges()).append("\n");
            double dens = n > 1 ? (g.totalEdges() * 2.0 / (n * (n - 1))) * 100 : 0;
            sb.append(String.format("Densidad: %.1f%%\n", dens));
            sb.append("¿Grafo denso? ").append(dens > 50 ? "Sí" : "No").append("\n");
            sb.append("\n---- Vecinos ----\n");
            for (int i = 0; i < n; i++) {
                sb.append(nameOf(i)).append(": ");
                boolean any = false;
                for (int j = 0; j < n; j++) {
                    if (!getCell(g, i, j).equals(0)) { sb.append(nameOf(j)).append(" "); any = true; }
                }
                if (!any) sb.append("(sin vecinos)");
                sb.append("\n");
            }
        } catch (Exception ex) { sb.append("Error: ").append(ex.getMessage()); }
        matInfoArea.setText(sb.toString());
        matStatsLabel.setText("Espacio: O(V²) = " + n + "²=" + (n * n) + " celdas  |  existeArista: O(1)  |  vecinos: O(V)  |  insertar vértice: O(V²)");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TAB 2 – LISTA DE ADYACENCIA
    // ════════════════════════════════════════════════════════════════════════

    @FXML void onLstGenerate() {
        try {
            Integer nVal = lstSpinner.getValue();
            int n = (nVal != null) ? nVal : 6;
            boolean dir = lstDirCheck.isSelected();
            lstGraph = new AdjacencyListGraph<>(n, dir);
            for (int i = 0; i < n; i++) lstGraph.addVertex(i);
            parseAndAddEdges(lstEdgesArea.getText(), lstGraph, false);
            drawGraph(lstCanvas, lstGraph, n, false);
            updateLstInfo(lstGraph, n, dir);
        } catch (Exception ex) {
            lstInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onLstExample() {
        lstSpinner.getValueFactory().setValue(6);
        lstDirCheck.setSelected(true);
        lstEdgesArea.setText("0-1,0-2,1-2,1-3,2-4,3-4,3-5,4-5");
        onLstGenerate();
    }

    @FXML void onLstClear() {
        lstGraph = null;
        clearCanvas(lstCanvas);
        lstInfoArea.clear();
    }

    private void updateLstInfo(AdjacencyListGraph<Integer> g, int n, boolean dir) {
        StringBuilder sb = new StringBuilder();
        sb.append("==== LISTA DE ADYACENCIA ====\n\n");
        sb.append("Tipo de grafo: ").append(dir ? "Dirigido" : "No Dirigido").append("\n");
        for (int i = 0; i < n; i++) {
            sb.append(nameOf(i)).append(" → Vecinos: ");
            Node<Integer> aux = g.vertexList[i].headNode;
            if (aux == null) sb.append("→ null");
            while (aux != null) {
                sb.append(nameOf(aux.data)).append(" → ");
                aux = aux.neighbor;
            }
            sb.append("null\n");
        }
        try {
            sb.append("\n---- Estadísticas ----\n");
            sb.append("Vértices (V): ").append(g.size()).append("\n");
            sb.append("Aristas  (E): ").append(g.totalEdges()).append("\n");
            sb.append("\n---- Grados ----\n");
            for (int i = 0; i < n; i++)
                sb.append("grado(").append(nameOf(i)).append(") = ").append(g.getVertexDegree(i)).append("\n");
        } catch (Exception ex) { sb.append("Error: ").append(ex.getMessage()); }
        lstInfoArea.setText(sb.toString());
        try {
            lstStatsLabel.setText("Espacio: O(V+E) = " + n + "+" + g.totalEdges() + " entradas  |  existeArista: O(grado)  |  vecinos: O(1)  |  insertar: O(1)");
        } catch (Exception ignored) {}
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TAB 3 – LISTA ENLAZADA
    // ════════════════════════════════════════════════════════════════════════

    @FXML void onLnkGenerate() {
        try {
            Integer nVal = lnkSpinner.getValue();
            int n = (nVal != null) ? nVal : 6;
            boolean dir = lnkDirCheck.isSelected();
            lnkGraph = new LinkedGraph<>(dir);
            for (int i = 0; i < n; i++) lnkGraph.addVertex(i);
            parseAndAddEdges(lnkEdgesArea.getText(), lnkGraph, false);
            drawGraph(lnkCanvas, lnkGraph, n, true);
            updateLnkInfo(lnkGraph, n, dir);
        } catch (Exception ex) {
            lnkInfoArea.setText("Error: " + ex.getMessage());
        }
    }

    @FXML void onLnkExample() {
        lnkSpinner.getValueFactory().setValue(6);
        lnkDirCheck.setSelected(true);
        lnkEdgesArea.setText("0-1,0-2,1-2,1-3,2-4,3-4,3-5,4-5");
        onLnkGenerate();
    }

    @FXML void onLnkClear() {
        lnkGraph = null;
        clearCanvas(lnkCanvas);
        lnkNodesBox.getChildren().clear();
        lnkInfoArea.clear();
    }

    private void updateLnkInfo(LinkedGraph<Integer> g, int n, boolean dir) {
        lnkNodesBox.getChildren().clear();
        try {
            int len = g.size();
            for (int i = 1; i <= len; i++) {
                Node<Integer> node = g.getNodeByIndex(i);
                HBox row = new HBox(4);
                row.setStyle("-fx-alignment:CENTER_LEFT;");
                // nodo principal (vértice)
                row.getChildren().add(chipLabel(nameOf(node.data), "#1f6feb", "white"));
                row.getChildren().add(arrowLabel("→"));
                // vecinos encadenados
                Node<Integer> aux = node.neighbor;
                if (aux == null) {
                    row.getChildren().add(nullLabel());
                } else {
                    while (aux != null) {
                        row.getChildren().add(chipLabel(nameOf(aux.data), "#7c3aed", "white"));
                        row.getChildren().add(arrowLabel("→"));
                        aux = aux.neighbor;
                    }
                    row.getChildren().add(nullLabel());
                }
                lnkNodesBox.getChildren().add(row);
            }
        } catch (Exception ex) { lnkInfoArea.setText("Error: " + ex.getMessage()); return; }

        StringBuilder sb = new StringBuilder();
        sb.append("==== LISTA ENLAZADA ====\n\n");
        sb.append("HEAD → ");
        try {
            int len = g.size();
            for (int i = 1; i <= len; i++) {
                sb.append("[").append(nameOf(g.getNodeByIndex(i).data)).append("]");
                if (i < len) sb.append(" → ");
            }
            sb.append(" → NULL\n");
            sb.append("---- Estadísticas ----\n");
            sb.append("Vértices (V): ").append(g.size()).append("\n");
            sb.append("Aristas  (E): ").append(g.totalEdges()).append("\n");
            int nodos = 0;
            for (int i = 1; i <= g.size(); i++) {
                Node<Integer> aux = g.getNodeByIndex(i).neighbor;
                while (aux != null) { nodos++; aux = aux.neighbor; }
            }
            sb.append("Nodos Enlazados: ").append(nodos).append(" objetos Nodo\n\n");
            sb.append("---- Grados ----\n");
            for (int i = 1; i <= g.size(); i++) {
                Node<Integer> node = g.getNodeByIndex(i);
                sb.append("grado(").append(nameOf(node.data)).append(") = ").append(g.getVertexDegree(node.data)).append("\n");
            }
        } catch (Exception ex) { sb.append("Error: ").append(ex.getMessage()); }
        lnkInfoArea.setText(sb.toString());
        try {
            lnkStatsLabel.setText("Espacio: O(V+E)  |  insertar arista: O(V+E)  |  existeArista: O(grado)  |  Control total de memoria");
        } catch (Exception ignored) {}
    }

    // ════════════════════════════════════════════════════════════════════════
    //  DIBUJO DE GRAFOS (Tabs 1, 2, 3)
    // ════════════════════════════════════════════════════════════════════════

    private void drawGraph(Canvas canvas, AdjacencyMatrixGraph<Integer> g, int n, boolean circular) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(canvas);
        if (g == null || g.isEmpty()) return;

        double w = canvas.getWidth(), h = canvas.getHeight();
        double[][] pos = circular
                ? calcCirclePos(n, w / 2, h / 2 - 20, Math.min(w, h) / 2 - 60)
                : calcCirclePos(n, w / 2, h / 2 - 20, Math.min(w, h) / 2 - 60);

        // aristas
        gc.setStroke(EDGE_CLR);
        gc.setLineWidth(1.5);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!getCell(g, i, j).equals(0)) {
                    drawEdge(gc, pos[i][0], pos[i][1], pos[j][0], pos[j][1], g.directed, NR);
                    // peso si no es 0 ni 1
                    Object w2 = getCell(g, i, j);
                    if (!w2.equals(0) && !w2.equals(1)) {
                        double mx = (pos[i][0] + pos[j][0]) / 2;
                        double my = (pos[i][1] + pos[j][1]) / 2;
                        gc.setFill(Color.web("#f59e0b"));
                        gc.setFont(Font.font("System", FontWeight.BOLD, 11));
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.fillText(w2.toString(), mx, my);
                    }
                }
            }
        }
        // nodos
        for (int i = 0; i < n; i++) drawNode(gc, pos[i][0], pos[i][1], nameOf(i), NODE_CLR);
    }

    // sobrecarga para LinkedGraph
    private void drawGraph(Canvas canvas, LinkedGraph<Integer> g, int n, boolean circular) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(canvas);
        if (g == null || g.isEmpty()) return;

        double w = canvas.getWidth(), h = canvas.getHeight();
        double[][] pos = calcCirclePos(n, w / 2, h / 2 - 20, Math.min(w, h) / 2 - 60);

        try {
            int len = g.size();
            // aristas
            gc.setStroke(EDGE_CLR);
            gc.setLineWidth(1.5);
            for (int i = 1; i <= len; i++) {
                Node<Integer> node = g.getNodeByIndex(i);
                Node<Integer> aux = node.neighbor;
                while (aux != null) {
                    int j = aux.data; // índice del vecino (0-based)
                    if (j < n) drawEdge(gc, pos[i-1][0], pos[i-1][1], pos[j][0], pos[j][1], g.directed, NR);
                    aux = aux.neighbor;
                }
            }
            // nodos
            for (int i = 1; i <= len; i++) {
                Node<Integer> node = g.getNodeByIndex(i);
                drawNode(gc, pos[i-1][0], pos[i-1][1], nameOf(node.data), NODE_CLR);
            }
        } catch (Exception ex) { gc.setFill(Color.RED); gc.fillText("Error: " + ex.getMessage(), 20, 20); }
    }

    private void drawEdge(GraphicsContext gc, double x1, double y1, double x2, double y2, boolean directed, double nr) {
        // offset para que la flecha no quede dentro del nodo
        double dx = x2 - x1, dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;
        double ux = dx / len, uy = dy / len;
        double sx = x1 + ux * nr, sy = y1 + uy * nr;
        double ex = x2 - ux * nr, ey = y2 - uy * nr;
        gc.strokeLine(sx, sy, ex, ey);
        if (directed) drawArrow(gc, sx, sy, ex, ey);
    }

    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double aLen = 10, aAngle = 0.4;
        double ax1 = x2 - aLen * Math.cos(angle - aAngle);
        double ay1 = y2 - aLen * Math.sin(angle - aAngle);
        double ax2 = x2 - aLen * Math.cos(angle + aAngle);
        double ay2 = y2 - aLen * Math.sin(angle + aAngle);
        gc.strokeLine(x2, y2, ax1, ay1);
        gc.strokeLine(x2, y2, ax2, ay2);
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

    // ════════════════════════════════════════════════════════════════════════
    //  TAB 4 – ALGORITMOS MST Y DIJKSTRA
    // ════════════════════════════════════════════════════════════════════════

    @FXML void onAlgoRun() {
        if (autoTimeline != null) autoTimeline.stop();
        steps.clear();
        currentStep = -1;
        algoLogArea.clear();
        algoResultArea.clear();
        algoWeightLabel.setText("Peso acumulado: 0");

        String algo = algoCombo.getValue();
        int start = startCombo.getSelectionModel().getSelectedIndex();

        if (algo.contains("Kruskal")) buildKruskalSteps();
        else if (algo.contains("Prim")) buildPrimSteps(start);
        else buildDijkstraSteps(start);

        algoStepLabel.setText("Paso 0 / " + steps.size());
        algoStateLabel.setText(algo);
        algoReprLabel.setText(reprCombo.getValue());
        drawAlgoGraph(null, null, null);
    }

    @FXML void onAlgoNext() {
        if (currentStep < steps.size() - 1) {
            currentStep++;
            applyStep(currentStep);
        }
    }

    @FXML void onAlgoPrev() {
        if (currentStep > 0) {
            currentStep--;
            // redibujar desde 0 hasta currentStep
            algoLogArea.clear();
            drawAlgoGraph(null, null, null);
            for (int i = 0; i <= currentStep; i++) applyStep(i);
        } else if (currentStep == 0) {
            currentStep = -1;
            algoLogArea.clear();
            algoResultArea.clear();
            algoWeightLabel.setText("Peso acumulado: 0");
            drawAlgoGraph(null, null, null);
            algoStepLabel.setText("Paso 0 / " + steps.size());
        }
    }

    @FXML void onAlgoAuto() {
        if (autoTimeline != null) autoTimeline.stop();
        if (steps.isEmpty()) onAlgoRun();
        autoTimeline = new Timeline(new KeyFrame(
                Duration.millis(speedSlider.getValue()), e -> { if (currentStep < steps.size() - 1) onAlgoNext(); else autoTimeline.stop(); }
        ));
        autoTimeline.setCycleCount(steps.size());
        autoTimeline.play();
    }

    @FXML void onAlgoReset() {
        if (autoTimeline != null) autoTimeline.stop();
        steps.clear();
        currentStep = -1;
        algoLogArea.clear();
        algoResultArea.clear();
        algoWeightLabel.setText("Peso acumulado: 0");
        algoStepLabel.setText("Paso 0 / 0");
        drawAlgoGraph(null, null, null);
        routeLabel.setText("Ruta: —");
    }

    @FXML void onFindRoute() {
        int s = startCombo.getSelectionModel().getSelectedIndex();
        int t = endCombo.getSelectionModel().getSelectedIndex();
        if (steps.isEmpty()) buildDijkstraSteps(s);
        // buscar el step final y extraer la ruta
        if (!steps.isEmpty()) {
            AlgoStep last = steps.get(steps.size() - 1);
            if (last.route != null && last.route.containsKey(t)) {
                routeLabel.setText("Ruta: " + last.route.get(t) + " (costo: " + last.dist[t] + ")");
            }
        }
    }

    private void applyStep(int idx) {
        AlgoStep s = steps.get(idx);
        algoStepLabel.setText("Paso " + (idx + 1) + " / " + steps.size());
        algoLogArea.appendText("[" + (idx + 1) + "] " + s.log + "\n");
        algoWeightLabel.setText("Peso acumulado: " + s.totalWeight);
        drawAlgoGraph(s.mstEdges, s.rejEdges, s.currentEdge);
        if (idx == steps.size() - 1 && s.result != null)
            algoResultArea.setText(s.result);
    }

    // ── Kruskal ──────────────────────────────────────────────────────────────
    private void buildKruskalSteps() {
        // ordenar aristas por peso
        List<int[]> edges = new ArrayList<>();
        for (int[] e : ALGO_E) edges.add(e.clone());
        edges.sort(Comparator.comparingInt(e -> e[2]));

        int[] parent = new int[ALGO_V.length];
        for (int i = 0; i < parent.length; i++) parent[i] = i;

        List<int[]> mst = new ArrayList<>();
        List<int[]> rejected = new ArrayList<>();
        int totalW = 0;
        int stepNum = 1;

        steps.add(new AlgoStep("[" + stepNum++ + "] Inicio: ordenar aristas por peso.", new ArrayList<>(), new ArrayList<>(), null, totalW, null, null, null));

        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            int[] current = {u, v, w};
            int pu = find(parent, u), pv = find(parent, v);
            if (pu == pv) {
                rejected.add(current);
                steps.add(new AlgoStep("[" + stepNum++ + "] ✗ Rechazar " + ALGO_V[u] + "-" + ALGO_V[v] + " (W=" + w + ") → forma ciclo.",
                        new ArrayList<>(mst), new ArrayList<>(rejected), current, totalW, null, null, null));
            } else {
                mst.add(current);
                totalW += w;
                union(parent, pu, pv);
                steps.add(new AlgoStep("[" + stepNum++ + "] ✓ Agregar " + ALGO_V[u] + "-" + ALGO_V[v] + " (W=" + w + ") → no forma ciclo.",
                        new ArrayList<>(mst), new ArrayList<>(rejected), current, totalW, null, null, null));
            }
        }
        // resultado final
        StringBuilder res = new StringBuilder("MST final:\n");
        for (int[] e : mst) res.append("  ").append(ALGO_V[e[0]]).append("-").append(ALGO_V[e[1]]).append("  W=").append(e[2]).append("\n");
        res.append("\nPeso total: ").append(totalW);
        steps.get(steps.size() - 1).log += "\n[" + stepNum + "] ✓ MST completo. Peso total = " + totalW + ".";
        steps.get(steps.size() - 1).result = res.toString();
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private void union(int[] parent, int a, int b) { parent[a] = b; }

    // ── Prim ─────────────────────────────────────────────────────────────────
    private void buildPrimSteps(int start) {
        int n = ALGO_V.length;
        int[] key = new int[n], parent = new int[n];
        boolean[] inMST = new boolean[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[start] = 0;

        List<int[]> mst = new ArrayList<>();
        int totalW = 0, stepNum = 1;
        steps.add(new AlgoStep("[1] Inicio: key[" + ALGO_V[start] + "]=0, todos los demás = ∞.", new ArrayList<>(), new ArrayList<>(), null, 0, null, null, null));

        for (int iter = 0; iter < n; iter++) {
            // extraer mínimo no en MST
            int u = -1;
            for (int i = 0; i < n; i++)
                if (!inMST[i] && (u == -1 || key[i] < key[u])) u = i;
            inMST[u] = true;
            if (parent[u] != -1) {
                totalW += key[u];
                int[] edge = {parent[u], u, key[u]};
                mst.add(edge);
                steps.add(new AlgoStep("[" + (++stepNum) + "] Extraer min: " + ALGO_V[u] + " (key=" + key[u] + "). Agregar al MST.",
                        new ArrayList<>(mst), new ArrayList<>(), edge, totalW, null, null, null));
            } else {
                steps.add(new AlgoStep("[" + (++stepNum) + "] Extraer min: " + ALGO_V[u] + " (vértice inicial).",
                        new ArrayList<>(mst), new ArrayList<>(), null, totalW, null, null, null));
            }
            // relajar vecinos
            for (int[] e : ALGO_E) {
                int a = e[0], b = e[1], w = e[2];
                int neighbor = -1;
                if (a == u && !inMST[b]) neighbor = b;
                else if (b == u && !inMST[a]) neighbor = a;
                if (neighbor != -1 && w < key[neighbor]) {
                    key[neighbor] = w;
                    parent[neighbor] = u;
                    steps.add(new AlgoStep("[" + (++stepNum) + "] Relajar: key[" + ALGO_V[neighbor] + "] = " + w + " (vía " + ALGO_V[u] + ").",
                            new ArrayList<>(mst), new ArrayList<>(), null, totalW, null, null, null));
                }
            }
        }
        StringBuilder res = new StringBuilder("MST (Prim):\n");
        for (int[] e : mst) res.append("  ").append(ALGO_V[e[0]]).append("-").append(ALGO_V[e[1]]).append("  W=").append(e[2]).append("\n");
        res.append("\nTotal: ").append(totalW);
        steps.get(steps.size() - 1).result = res.toString();
        steps.get(steps.size() - 1).log += "\n[" + (++stepNum) + "] ✓ MST completo. Peso total = " + totalW + ".";
    }

    // ── Dijkstra ─────────────────────────────────────────────────────────────
    private void buildDijkstraSteps(int src) {
        int n = ALGO_V.length;
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        List<int[]> spEdges = new ArrayList<>();
        int stepNum = 1;
        steps.add(new AlgoStep("[1] Inicio: dist[" + ALGO_V[src] + "]=0, todos los demás = ∞.\n► Ejecutando Dijkstra (Shortest Path) (" + reprCombo.getValue() + ")",
                new ArrayList<>(), new ArrayList<>(), null, 0, dist.clone(), buildRoute(prev, src, n), null));

        for (int iter = 0; iter < n; iter++) {
            int u = -1;
            for (int i = 0; i < n; i++)
                if (!visited[i] && (u == -1 || dist[i] < dist[u])) u = i;
            if (dist[u] == Integer.MAX_VALUE) break;
            visited[u] = true;
            steps.add(new AlgoStep("[" + (++stepNum) + "] Extraer min: " + ALGO_V[u] + " (dist=" + dist[u] + ").",
                    new ArrayList<>(spEdges), new ArrayList<>(), null, 0, dist.clone(), buildRoute(prev, src, n), null));
            // relajar
            for (int[] e : ALGO_E) {
                int a = e[0], b = e[1], w = e[2];
                int neighbor = -1;
                if (a == u) neighbor = b;
                else if (b == u) neighbor = a; // grafo no dirigido
                if (neighbor != -1 && !visited[neighbor] && dist[u] + w < dist[neighbor]) {
                    dist[neighbor] = dist[u] + w;
                    prev[neighbor] = u;
                    steps.add(new AlgoStep("[" + (++stepNum) + "] Relajar: dist[" + ALGO_V[neighbor] + "] = " + dist[neighbor] + " (vía " + ALGO_V[u] + ").",
                            new ArrayList<>(spEdges), new ArrayList<>(), null, 0, dist.clone(), buildRoute(prev, src, n), null));
                }
            }
            // agregar arista al SP tree si viene de algún lado
            if (prev[u] != -1) {
                int finalU = u;
                spEdges.removeIf(e -> e[1] == finalU);
                spEdges.add(new int[]{prev[u], u, dist[u]});
            }
        }
        // resultado
        StringBuilder res = new StringBuilder("Distancias mínimas desde " + ALGO_V[src] + ":\n\n");
        Map<Integer,String> routes = buildRoute(prev, src, n);
        for (int i = 0; i < n; i++) {
            res.append("  ").append(ALGO_V[src]).append(" → ").append(ALGO_V[i]).append(" = ")
                    .append(dist[i] == Integer.MAX_VALUE ? "∞" : dist[i])
                    .append("  [").append(routes.getOrDefault(i, "—")).append("]\n");
        }
        steps.get(steps.size() - 1).mstEdges = new ArrayList<>(spEdges);
        steps.get(steps.size() - 1).result = res.toString();
        steps.get(steps.size() - 1).dist = dist;
        steps.get(steps.size() - 1).route = routes;
        steps.get(steps.size() - 1).log += "\n[" + (++stepNum) + "] ✓ Dijkstra completo desde " + ALGO_V[src] + ".";
    }

    private Map<Integer,String> buildRoute(int[] prev, int src, int n) {
        Map<Integer,String> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            StringBuilder path = new StringBuilder(ALGO_V[i]);
            int cur = i;
            while (prev[cur] != -1) { cur = prev[cur]; path.insert(0, ALGO_V[cur] + "→"); }
            map.put(i, path.toString());
        }
        return map;
    }

    // ── Dibujo del grafo de algoritmos ───────────────────────────────────────
    private void drawAlgoGraph(List<int[]> mstEdges, List<int[]> rejEdges, int[] currentEdge) {
        GraphicsContext gc = algoCanvas.getGraphicsContext2D();
        clearCanvas(algoCanvas);
        double[][] pos = algoPos;

        // todas las aristas en gris primero
        gc.setLineWidth(2);
        for (int[] e : ALGO_E) {
            gc.setStroke(EDGE_CLR);
            gc.strokeLine(pos[e[0]][0], pos[e[0]][1], pos[e[1]][0], pos[e[1]][1]);
            // peso
            double mx = (pos[e[0]][0] + pos[e[1]][0]) / 2;
            double my = (pos[e[0]][1] + pos[e[1]][1]) / 2;
            gc.setFill(Color.web("#c9d1d9"));
            gc.setFont(Font.font("System", FontWeight.BOLD, 12));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(String.valueOf(e[2]), mx, my - 4);
        }
        // rechazadas
        if (rejEdges != null) {
            gc.setStroke(REJ_CLR);
            gc.setLineWidth(2.5);
            for (int[] e : rejEdges)
                gc.strokeLine(pos[e[0]][0], pos[e[0]][1], pos[e[1]][0], pos[e[1]][1]);
        }
        // MST / SP edges
        if (mstEdges != null) {
            gc.setStroke(MST_CLR);
            gc.setLineWidth(3);
            for (int[] e : mstEdges)
                gc.strokeLine(pos[e[0]][0], pos[e[0]][1], pos[e[1]][0], pos[e[1]][1]);
        }
        // arista actual
        if (currentEdge != null) {
            gc.setStroke(ACT_CLR);
            gc.setLineWidth(4);
            gc.strokeLine(pos[currentEdge[0]][0], pos[currentEdge[0]][1], pos[currentEdge[1]][0], pos[currentEdge[1]][1]);
        }
        // nodos
        for (int i = 0; i < ALGO_V.length; i++)
            drawNode(gc, pos[i][0], pos[i][1], ALGO_V[i], Color.web("#1abc9c"));
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TAB 5 – ÁRBOLES BINARIOS (BST, AVL, BINARY TREE)
    // ════════════════════════════════════════════════════════════════════════

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

    // recorre el arbol y arma el reporte con los 10 puntos de la practica
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

    // recorre el arbol en inorder y devuelve los valores en una lista
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

    // ════════════════════════════════════════════════════════════════════════
    //  UTILIDADES
    // ════════════════════════════════════════════════════════════════════════

    private Object getCell(AdjacencyMatrixGraph<Integer> g, int i, int j) {
        Object[][] raw = (Object[][]) g.adjacencyMatrix;
        return raw[i][j];
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

    private void parseAndAddEdges(String text, AdjacencyMatrixGraph<Integer> g, boolean withWeight) throws Exception {
        if (text == null || text.isBlank()) return;
        for (String part : text.trim().split(",")) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] uv = part.split("-");
            int u = Integer.parseInt(uv[0].trim());
            int v = Integer.parseInt(uv[1].trim());
            if (withWeight && uv.length > 2) g.addEdgeAndWeight(u, v, Integer.parseInt(uv[2].trim()));
            else g.addEdge(u, v);
        }
    }

    // Nuevo método sobrecargado (para la lista enlazada)
    private void parseAndAddEdges(String text, LinkedGraph<Integer> g, boolean withWeight) throws Exception {
        if (text == null || text.isBlank()) return;
        for (String part : text.trim().split(",")) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] uv = part.split("-");
            int u = Integer.parseInt(uv[0].trim());
            int v = Integer.parseInt(uv[1].trim());
            if (withWeight && uv.length > 2) g.addEdgeAndWeight(u, v, Integer.parseInt(uv[2].trim()));
            else g.addEdge(u, v);
        }
    }

    private String nameOf(int idx) {
        if (idx < CR_NAMES.length) return CR_NAMES[idx];
        return String.valueOf(idx);
    }

    private void clearCanvas(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(BG);
        gc.fillRect(0, 0, c.getWidth(), c.getHeight());
    }

    private Label styledLabel(String text, String color, boolean bold) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + color + ";" + (bold ? "-fx-font-weight:bold;" : "") + "-fx-font-size:11;");
        return l;
    }

    private Label chipLabel(String text, String bg, String fg) {
        Label l = new Label(text);
        l.setStyle("-fx-background-color:" + bg + "; -fx-text-fill:" + fg + "; -fx-padding:2 8; -fx-font-weight:bold; -fx-font-size:11;");
        return l;
    }

    private Label arrowLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:#58a6ff; -fx-font-weight:bold;");
        return l;
    }

    private Label nullLabel() {
        Label l = new Label("null");
        l.setStyle("-fx-background-color:#b91c1c; -fx-text-fill:white; -fx-padding:2 6; -fx-font-size:11;");
        return l;
    }

    // ── Clase interna para los pasos de animación ────────────────────────────
    private static class AlgoStep {
        String log;
        List<int[]> mstEdges;
        List<int[]> rejEdges;
        int[] currentEdge;
        int totalWeight;
        int[] dist;
        Map<Integer,String> route;
        String result;

        AlgoStep(String log, List<int[]> mst, List<int[]> rej, int[] cur, int tw,
                 int[] dist, Map<Integer,String> route, String result) {
            this.log = log; this.mstEdges = mst; this.rejEdges = rej;
            this.currentEdge = cur; this.totalWeight = tw;
            this.dist = dist; this.route = route; this.result = result;
        }
    }
}