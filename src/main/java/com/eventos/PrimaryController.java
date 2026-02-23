package com.eventos;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controlador principal - Interfaces Naturales
 * Autor: Diego
 *
 * Opciones de implementación usadas:
 * - Opción 1: Métodos @FXML vinculados desde el .fxml (limpiarLog*, simular*, generarPDF)
 * - Opción 2: Listeners programáticos en initialize() (ratón, teclado, scroll, drag)
 * - Opción 3: Métodos con nombre del evento en la clase (onTouchPressed, onTouchMoved, etc.)
 */
public class PrimaryController implements Initializable {

    // ===== CONTADORES =====
    private int contRaton = 0, contTeclado = 0, contScroll = 0, contGestos = 0;
    private int contadorClics = 0;
    private int totalEventos = 0;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    // ===== TAB RATÓN =====
    @FXML private Pane zonaRaton;
    @FXML private Label labelZonaRaton;
    @FXML private TextArea logRaton;
    @FXML private Button btnHover, btnDraggable, btnContador, btnScrollDemo;

    // ===== TAB TECLADO =====
    @FXML private TextField campoTeclado;
    @FXML private Label lblUltimaTecla, lblTipoTecla, lblCodigoTecla;
    @FXML private TextArea logTeclado;

    // ===== TAB SCROLL/DRAG =====
    @FXML private Pane zonaScroll, zonaOrigen, zonaDestino;
    @FXML private Rectangle rectScroll;
    @FXML private Label lblScrollInfo;
    @FXML private TextArea logScrollDrag;

    // ===== TAB GESTOS =====
    @FXML private Pane zonaGestos;
    @FXML private Rectangle rectGesto;
    @FXML private Label lblGestoInfo;
    @FXML private TextArea logGestos;

    // ===== TAB PDF =====
    @FXML private Label statRaton, statTeclado, statScroll, statGestos;
    @FXML private Label lblEstadoPDF;
    @FXML private Button btnGenerarPDF;

    // ===== BARRA DE ESTADO =====
    @FXML private Label lblStatus, lblContadorTotal;

    // =========================================================
    // INICIALIZACIÓN
    // =========================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarEventosRaton();
        configurarEventosTeclado();
        configurarEventosScrollDrag();
        configurarEventosGestos();
    }

    // =========================================================
    // TAB 1: EVENTOS DE RATÓN (MouseEvent)
    // Opción 2: Listeners programáticos
    // =========================================================
    private void configurarEventosRaton() {

        // MouseEvent: setOnMouseMoved - cursor se mueve sobre el nodo
        zonaRaton.setOnMouseMoved(event -> {
            logearRaton("MOVED    → X:" + (int) event.getX() + " Y:" + (int) event.getY());
            labelZonaRaton.setLayoutX(event.getX() - 50);
            labelZonaRaton.setLayoutY(event.getY() - 10);
            labelZonaRaton.setText(String.format("X:%.0f Y:%.0f", event.getX(), event.getY()));
            registrarEvento("Ratón movido");
        });

        // MouseEvent: setOnMouseClicked - clic en el nodo
        zonaRaton.setOnMouseClicked(event -> {
            logearRaton("CLICKED  → Botón:" + event.getButton() + " Clics:" + event.getClickCount());
            registrarEvento("Clic en zona ratón");
        });

        // MouseEvent: setOnMousePressed - botón del ratón presionado
        zonaRaton.setOnMousePressed(event -> {
            logearRaton("PRESSED  → X:" + (int) event.getX() + " Y:" + (int) event.getY());
            zonaRaton.setStyle("-fx-background-color:linear-gradient(to bottom right,#D6EAF8,#AED6F1);" +
                    "-fx-background-radius:8;-fx-border-color:#3498DB;-fx-border-radius:8;-fx-border-width:2;");
            registrarEvento("Ratón presionado");
        });

        // MouseEvent: setOnMouseReleased - botón del ratón soltado
        zonaRaton.setOnMouseReleased(event -> {
            logearRaton("RELEASED → X:" + (int) event.getX() + " Y:" + (int) event.getY());
            zonaRaton.setStyle("-fx-background-color:linear-gradient(to bottom right,#EBF5FB,#D6EAF8);" +
                    "-fx-background-radius:8;-fx-border-color:#AED6F1;-fx-border-radius:8;-fx-border-width:2;-fx-cursor:crosshair;");
            registrarEvento("Ratón soltado");
        });

        // MouseEvent: setOnMouseDragged - arrastrar mientras se mantiene pulsado
        zonaRaton.setOnMouseDragged(event -> {
            logearRaton("DRAGGED  → X:" + (int) event.getX() + " Y:" + (int) event.getY());
            registrarEvento("Ratón arrastrado en zona");
        });

        // MouseEvent: setOnMouseEntered - cursor entra en el área del nodo
        btnHover.setOnMouseEntered(event -> {
            btnHover.setText("¡Estoy aquí!");
            btnHover.setStyle("-fx-background-color:#2980B9;-fx-text-fill:white;-fx-font-size:13px;" +
                    "-fx-padding:8 16;-fx-background-radius:6;-fx-cursor:hand;");
            logearRaton("ENTERED  → Cursor entró en el botón Hover");
            registrarEvento("Mouse entered");
        });

        // MouseEvent: setOnMouseExited - cursor sale del área del nodo
        btnHover.setOnMouseExited(event -> {
            btnHover.setText("Hover sobre mí");
            btnHover.setStyle("-fx-background-color:#3498DB;-fx-text-fill:white;-fx-font-size:13px;" +
                    "-fx-padding:8 16;-fx-background-radius:6;-fx-cursor:hand;");
            logearRaton("EXITED   → Cursor salió del botón Hover");
            registrarEvento("Mouse exited");
        });

        // MouseEvent: setOnMouseDragged en botón arrastrable
        btnDraggable.setOnMouseDragged(event -> {
            btnDraggable.setLayoutX(event.getSceneX() - btnDraggable.getWidth() / 2);
            btnDraggable.setLayoutY(event.getSceneY() - btnDraggable.getHeight() / 2 - 250);
            logearRaton("DRAG BTN → SceneX:" + (int) event.getSceneX() + " SceneY:" + (int) event.getSceneY());
            registrarEvento("Botón arrastrado");
        });

        // MouseEvent: setOnMouseClicked - contador de clics
        btnContador.setOnMouseClicked(event -> {
            contadorClics++;
            btnContador.setText("Clic (" + contadorClics + ")");
            logearRaton("CLICK    → Contador clics: " + contadorClics + " | Botón: " + event.getButton());
            registrarEvento("Clic contador");
        });

        // ScrollEvent en botón
        btnScrollDemo.setOnScroll(event -> {
            logearRaton("SCROLL   → deltaY:" + String.format("%.1f", event.getDeltaY()));
            registrarEvento("Scroll en botón");
        });
    }

    private void logearRaton(String msg) {
        contRaton++;
        actualizarStats();
        appendLog(logRaton, msg);
    }

    // =========================================================
    // TAB 2: EVENTOS DE TECLADO (KeyEvent)
    // Opción 2: Listeners programáticos
    // =========================================================
    private void configurarEventosTeclado() {

        // KeyEvent: setOnKeyPressed - tecla presionada
        campoTeclado.setOnKeyPressed(event -> {
            lblUltimaTecla.setText(event.getText().isEmpty() ? event.getCode().toString() : event.getText());
            lblTipoTecla.setText("KEY_PRESSED");
            lblCodigoTecla.setText(event.getCode().toString());
            String mod = "";
            if (event.isControlDown()) mod += "[Ctrl] ";
            if (event.isShiftDown())   mod += "[Shift] ";
            if (event.isAltDown())     mod += "[Alt] ";
            logearTeclado("PRESSED  → Tecla:" + event.getCode() + " Texto:'" + event.getText() + "' " + mod);
            registrarEvento("Tecla presionada: " + event.getCode());
        });

        // KeyEvent: setOnKeyTyped - carácter escrito (solo caracteres imprimibles)
        campoTeclado.setOnKeyTyped(event -> {
            lblTipoTecla.setText("KEY_TYPED");
            logearTeclado("TYPED    → Carácter:'" + event.getCharacter() + "'" +
                    " (Unicode: " + (int) event.getCharacter().charAt(0) + ")");
            registrarEvento("Carácter escrito");
        });

        // KeyEvent: setOnKeyReleased - tecla soltada
        campoTeclado.setOnKeyReleased(event -> {
            lblTipoTecla.setText("KEY_RELEASED");
            logearTeclado("RELEASED → Tecla:" + event.getCode() + " soltada");
            registrarEvento("Tecla soltada: " + event.getCode());
        });
    }

    private void logearTeclado(String msg) {
        contTeclado++;
        actualizarStats();
        appendLog(logTeclado, msg);
    }

    // =========================================================
    // TAB 3: SCROLL Y DRAG (ScrollEvent / DragEvent)
    // Opción 2: Listeners programáticos
    // =========================================================
    private void configurarEventosScrollDrag() {

        // ScrollEvent: setOnScroll - desplazamiento con rueda/touchpad
        zonaScroll.setOnScroll(event -> {
            double dx = event.getDeltaX();
            double dy = event.getDeltaY();
            rectScroll.setX(Math.max(0, Math.min(220, rectScroll.getX() + dx)));
            rectScroll.setY(Math.max(0, Math.min(150, rectScroll.getY() - dy)));
            lblScrollInfo.setText(String.format("deltaX:%.1f  deltaY:%.1f", dx, dy));
            logearScrollDrag("SCROLL       → deltaX:" + String.format("%.1f", dx) + " deltaY:" + String.format("%.1f", dy));
            registrarEvento("Scroll");
        });

        // DragEvent: setOnDragDetected - inicio del arrastre
        zonaOrigen.setOnDragDetected(event -> {
            Dragboard db = zonaOrigen.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString("Contenido arrastrado - Diego");
            db.setContent(content);
            logearScrollDrag("DRAG_DETECTED → Arrastre iniciado desde zona origen");
            registrarEvento("Drag detected");
            event.consume();
        });

        // DragEvent: setOnDragOver - arrastre pasando por encima del destino
        zonaDestino.setOnDragOver(event -> {
            if (event.getGestureSource() != zonaDestino && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
                zonaDestino.setStyle("-fx-background-color:#ABEBC6;-fx-background-radius:8;" +
                        "-fx-border-color:#27AE60;-fx-border-radius:8;-fx-border-width:3;");
                logearScrollDrag("DRAG_OVER     → Objeto sobre zona destino");
            }
            event.consume();
        });

        // DragEvent: setOnDragDropped - objeto soltado en el destino
        zonaDestino.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                logearScrollDrag("DRAG_DROPPED  → ¡Soltado! Contenido: '" + db.getString() + "'");
                registrarEvento("Drag dropped");
                event.setDropCompleted(true);
            }
            event.consume();
        });

        // DragEvent: setOnDragExited - objeto sale del área del destino
        zonaDestino.setOnDragExited(event -> {
            zonaDestino.setStyle("-fx-background-color:#D5F5E3;-fx-background-radius:8;" +
                    "-fx-border-color:#27AE60;-fx-border-radius:8;-fx-border-width:2;");
            logearScrollDrag("DRAG_EXITED   → Objeto salió de zona destino");
            event.consume();
        });

        // DragEvent: setOnDragDone - arrastre completado
        zonaOrigen.setOnDragDone(event -> {
            logearScrollDrag("DRAG_DONE     → Modo transferencia: " + event.getTransferMode());
            event.consume();
        });
    }

    private void logearScrollDrag(String msg) {
        contScroll++;
        actualizarStats();
        appendLog(logScrollDrag, msg);
    }

    // =========================================================
    // TAB 4: GESTOS TÁCTILES
    // Opción 3: Métodos con nombre del evento dentro de la clase
    // =========================================================
    private void configurarEventosGestos() {

        // TouchEvent: setOnTouchPressed
        zonaGestos.setOnTouchPressed(this::onTouchPressed);

        // TouchEvent: setOnTouchMoved
        zonaGestos.setOnTouchMoved(this::onTouchMoved);

        // TouchEvent: setOnTouchReleased
        zonaGestos.setOnTouchReleased(this::onTouchReleased);

        // TouchEvent: setOnTouchStationary
        zonaGestos.setOnTouchStationary(event -> {
            logearGestos("TOUCH_STAT    → Punto estacionario id:" + event.getTouchPoint().getId());
            registrarEvento("Touch stationary");
            event.consume();
        });

        // SwipeEvent: setOnSwipeLeft
        zonaGestos.setOnSwipeLeft(event -> {
            logearGestos("SWIPE_LEFT    → Gesto swipe izquierda detectado");
            registrarEvento("Swipe left");
            event.consume();
        });

        // SwipeEvent: setOnSwipeRight
        zonaGestos.setOnSwipeRight(event -> {
            logearGestos("SWIPE_RIGHT   → Gesto swipe derecha detectado");
            registrarEvento("Swipe right");
            event.consume();
        });

        // ZoomEvent: setOnZoom
        zonaGestos.setOnZoom(event -> {
            double factor = event.getZoomFactor();
            rectGesto.setScaleX(rectGesto.getScaleX() * factor);
            rectGesto.setScaleY(rectGesto.getScaleY() * factor);
            logearGestos("ZOOM          → factor=" + String.format("%.3f", factor));
            registrarEvento("Zoom");
            event.consume();
        });

        // RotateEvent: setOnRotate
        zonaGestos.setOnRotate(event -> {
            rectGesto.setRotate(rectGesto.getRotate() + event.getAngle());
            logearGestos("ROTATE        → ángulo=" + String.format("%.1f", event.getAngle()) + "°");
            registrarEvento("Rotate");
            event.consume();
        });

        // ScrollEvent táctil: setOnScroll
        zonaGestos.setOnScroll(event -> {
            rectGesto.setX(Math.max(0, Math.min(220, rectGesto.getX() + event.getDeltaX())));
            rectGesto.setY(Math.max(0, Math.min(170, rectGesto.getY() - event.getDeltaY())));
            logearGestos("SCROLL_TOUCH  → deltaX:" + String.format("%.1f", event.getDeltaX())
                    + " deltaY:" + String.format("%.1f", event.getDeltaY()));
            registrarEvento("Scroll táctil");
        });

        // MouseDragged como simulación táctil
        zonaGestos.setOnMouseDragged(event -> {
            rectGesto.setX(Math.max(0, Math.min(220, event.getX() - rectGesto.getWidth() / 2)));
            rectGesto.setY(Math.max(0, Math.min(170, event.getY() - rectGesto.getHeight() / 2)));
            lblGestoInfo.setText(String.format("X:%.0f Y:%.0f", event.getX(), event.getY()));
        });
    }

    // Opción 3: métodos con el nombre del evento
    @FXML
    private void onTouchPressed(TouchEvent event) {
        logearGestos("TOUCH_PRESS   → Toque en: " + event.getTouchPoint().getX()
                + "," + event.getTouchPoint().getY());
        registrarEvento("Touch pressed");
        event.consume();
    }

    @FXML
    private void onTouchMoved(TouchEvent event) {
        logearGestos("TOUCH_MOVED   → Movimiento en: " + event.getTouchPoint().getX()
                + "," + event.getTouchPoint().getY());
        registrarEvento("Touch moved");
        event.consume();
    }

    @FXML
    private void onTouchReleased(TouchEvent event) {
        logearGestos("TOUCH_REL     → Toque liberado");
        registrarEvento("Touch released");
        event.consume();
    }

    // Botones de simulación de gestos (Opción 1: @FXML desde .fxml)
    @FXML private void simularSwipeIzq() {
        logearGestos("SWIPE_LEFT    → [SIMULADO] Swipe izquierda");
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), rectGesto);
        tt.setByX(-30); tt.setAutoReverse(true); tt.setCycleCount(2); tt.play();
    }

    @FXML private void simularSwipeDer() {
        logearGestos("SWIPE_RIGHT   → [SIMULADO] Swipe derecha");
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), rectGesto);
        tt.setByX(30); tt.setAutoReverse(true); tt.setCycleCount(2); tt.play();
    }

    @FXML private void simularZoomIn() {
        double factor = 1.2;
        rectGesto.setScaleX(rectGesto.getScaleX() * factor);
        rectGesto.setScaleY(rectGesto.getScaleY() * factor);
        logearGestos("ZOOM_IN       → [SIMULADO] ScaleX:" + String.format("%.2f", rectGesto.getScaleX()));
    }

    @FXML private void simularZoomOut() {
        double factor = 0.85;
        rectGesto.setScaleX(rectGesto.getScaleX() * factor);
        rectGesto.setScaleY(rectGesto.getScaleY() * factor);
        logearGestos("ZOOM_OUT      → [SIMULADO] ScaleX:" + String.format("%.2f", rectGesto.getScaleX()));
    }

    @FXML private void simularRotarPos() {
        rectGesto.setRotate(rectGesto.getRotate() + 15);
        logearGestos("ROTATE+       → [SIMULADO] Ángulo:" + String.format("%.0f", rectGesto.getRotate()) + "°");
    }

    @FXML private void simularRotarNeg() {
        rectGesto.setRotate(rectGesto.getRotate() - 15);
        logearGestos("ROTATE-       → [SIMULADO] Ángulo:" + String.format("%.0f", rectGesto.getRotate()) + "°");
    }

    @FXML private void resetGesto() {
        rectGesto.setScaleX(1); rectGesto.setScaleY(1);
        rectGesto.setRotate(0); rectGesto.setX(110); rectGesto.setY(85);
        rectGesto.setTranslateX(0); rectGesto.setTranslateY(0);
        logearGestos("RESET         → Transformaciones restablecidas");
    }

    private void logearGestos(String msg) {
        contGestos++;
        actualizarStats();
        appendLog(logGestos, msg);
    }

    // =========================================================
    // TAB 5: GENERACIÓN DE PDF (Opción 1: @FXML desde .fxml)
    // =========================================================
    @FXML
    private void generarPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar informe PDF - Diego");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("informe_eventos_Diego.pdf");

        java.io.File file = fileChooser.showSaveDialog(btnGenerarPDF.getScene().getWindow());
        if (file != null) {
            lblEstadoPDF.setText("⏳ Generando PDF...");
            String filePath = file.getAbsolutePath();
            new Thread(() -> {
                try {
                    PDFGenerator.generar(filePath, contRaton, contTeclado, contScroll, contGestos);
                    Platform.runLater(() -> {
                        lblEstadoPDF.setText("✅ PDF generado correctamente en:\n" + filePath);
                        registrarEvento("PDF generado");
                    });
                } catch (Exception e) {
                    Platform.runLater(() ->
                            lblEstadoPDF.setText("❌ Error al generar PDF: " + e.getMessage()));
                }
            }).start();
        }
    }

    // =========================================================
    // MÉTODOS @FXML para limpiar logs (Opción 1)
    // =========================================================
    @FXML private void limpiarLogRaton()      { logRaton.clear(); }
    @FXML private void limpiarLogTeclado()    { logTeclado.clear(); }
    @FXML private void limpiarLogScrollDrag() { logScrollDrag.clear(); }
    @FXML private void limpiarLogGestos()     { logGestos.clear(); }

    // =========================================================
    // UTILIDADES
    // =========================================================
    private void appendLog(TextArea log, String msg) {
        String line = "[" + LocalTime.now().format(fmt) + "] " + msg + "\n";
        Platform.runLater(() -> {
            log.appendText(line);
            log.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void registrarEvento(String desc) {
        totalEventos++;
        Platform.runLater(() -> {
            lblStatus.setText("Último evento: " + desc);
            lblContadorTotal.setText("Total eventos: " + totalEventos);
        });
    }

    private void actualizarStats() {
        Platform.runLater(() -> {
            statRaton.setText(String.valueOf(contRaton));
            statTeclado.setText(String.valueOf(contTeclado));
            statScroll.setText(String.valueOf(contScroll));
            statGestos.setText(String.valueOf(contGestos));
        });
    }
}
