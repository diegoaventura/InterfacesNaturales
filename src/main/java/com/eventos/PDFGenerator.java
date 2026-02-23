package com.eventos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generador de informe PDF con gráficas de barras y tablas.
 * Autor: Diego
 * Usa iText 7 para crear el PDF con estadísticas de los eventos capturados.
 */
public class PDFGenerator {

    public static void generar(String rutaSalida, int raton, int teclado,
                                int scroll, int gestos) throws Exception {

        com.itextpdf.kernel.pdf.PdfWriter writer =
                new com.itextpdf.kernel.pdf.PdfWriter(rutaSalida);
        com.itextpdf.kernel.pdf.PdfDocument pdf =
                new com.itextpdf.kernel.pdf.PdfDocument(writer);
        com.itextpdf.layout.Document document =
                new com.itextpdf.layout.Document(pdf, com.itextpdf.kernel.geom.PageSize.A4);
        document.setMargins(40, 50, 40, 50);

        // ===== COLORES =====
        com.itextpdf.kernel.colors.Color cPrimario  = rgb(44,  62,  80);
        com.itextpdf.kernel.colors.Color cAzul      = rgb(52,  152, 219);
        com.itextpdf.kernel.colors.Color cVerde      = rgb(39,  174, 96);
        com.itextpdf.kernel.colors.Color cNaranja    = rgb(230, 126, 34);
        com.itextpdf.kernel.colors.Color cCian       = rgb(26,  188, 156);
        com.itextpdf.kernel.colors.Color cRojo       = rgb(231, 76,  60);
        com.itextpdf.kernel.colors.Color cGrisClaro  = rgb(240, 244, 248);
        com.itextpdf.kernel.colors.Color cGrisMedio  = rgb(189, 195, 199);
        com.itextpdf.kernel.colors.Color cBlanco     = com.itextpdf.kernel.colors.ColorConstants.WHITE;

        // ===== FUENTES =====
        com.itextpdf.kernel.font.PdfFont fBold = com.itextpdf.kernel.font.PdfFontFactory.createFont(
                com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
        com.itextpdf.kernel.font.PdfFont fNormal = com.itextpdf.kernel.font.PdfFontFactory.createFont(
                com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
        com.itextpdf.kernel.font.PdfFont fItalic = com.itextpdf.kernel.font.PdfFontFactory.createFont(
                com.itextpdf.io.font.constants.StandardFonts.HELVETICA_OBLIQUE);

        int total = raton + teclado + scroll + gestos;

        // ============================================================
        // CABECERA
        // ============================================================
        com.itextpdf.layout.element.Table header = tabla(new float[]{1});
        header.setWidth(pct(100)).setBackgroundColor(cPrimario).setMarginBottom(20);
        header.addCell(new com.itextpdf.layout.element.Cell()
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                .setPadding(25)
                .add(parrafo("Informe de Interfaces Naturales - Diego", fBold, 22, cBlanco))
                .add(parrafo("Estadisticas de eventos capturados durante la sesion", fNormal, 11, cGrisMedio))
                .add(parrafo("Generado: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), fItalic, 10, cGrisMedio)));
        document.add(header);

        // ============================================================
        // TARJETAS DE ESTADÍSTICAS
        // ============================================================
        document.add(parrafo("Resumen de eventos capturados", fBold, 14, cPrimario).setMarginBottom(8));

        com.itextpdf.kernel.colors.Color[] tarjetaColores = {cAzul, cVerde, cNaranja, cCian, cRojo};
        String[]  tarjetaNombres = {"Raton", "Teclado", "Scroll/Drag", "Gestos", "TOTAL"};
        int[]     tarjetaValores = {raton, teclado, scroll, gestos, total};

        com.itextpdf.layout.element.Table cards = tabla(new float[]{1, 1, 1, 1, 1});
        cards.setWidth(pct(100)).setMarginBottom(20);
        for (int i = 0; i < 5; i++) {
            cards.addCell(new com.itextpdf.layout.element.Cell()
                    .setBackgroundColor(tarjetaColores[i])
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                    .setPadding(12)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .add(parrafo(String.valueOf(tarjetaValores[i]), fBold, 24, cBlanco)
                            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER))
                    .add(parrafo(tarjetaNombres[i], fNormal, 9, rgb(220, 230, 240))
                            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)));
        }
        document.add(cards);

        // ============================================================
        // GRÁFICA DE BARRAS
        // ============================================================
        document.add(parrafo("Distribucion de eventos por tipo", fBold, 14, cPrimario).setMarginBottom(8));

        com.itextpdf.kernel.colors.Color[] barColores = {cAzul, cVerde, cNaranja, cCian};
        String[]  barNombres  = {"Raton", "Teclado", "Scroll/Drag", "Gestos"};
        int[]     barValores  = {raton, teclado, scroll, gestos};
        int       maxVal      = 1;
        for (int v : barValores) if (v > maxVal) maxVal = v;

        com.itextpdf.layout.element.Table graficaWrapper = tabla(new float[]{1});
        graficaWrapper.setWidth(pct(100)).setMarginBottom(20);
        com.itextpdf.layout.element.Cell graficaCell = new com.itextpdf.layout.element.Cell()
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(cGrisMedio, 1))
                .setBackgroundColor(cGrisClaro).setPadding(15);

        com.itextpdf.layout.element.Table grafica = tabla(new float[]{1, 1, 1, 1});
        grafica.setWidth(pct(100));

        for (int i = 0; i < 4; i++) {
            int barH = Math.max(5, (int)(120.0 * barValores[i] / maxVal));
            String pctStr = total > 0
                    ? String.format("%.0f%%", (double) barValores[i] / total * 100) : "0%";

            com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell()
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
                    .setPadding(5)
                    .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.BOTTOM);

            // Valor
            c.add(parrafo(String.valueOf(barValores[i]), fBold, 13, barColores[i])
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            // Barra
            com.itextpdf.layout.element.Table barra = tabla(new float[]{1});
            barra.setWidth(pct(60)).setHeight(barH).setBackgroundColor(barColores[i])
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            barra.addCell(new com.itextpdf.layout.element.Cell()
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER).setHeight(barH));
            c.add(barra);
            // Porcentaje
            c.add(parrafo(pctStr, fItalic, 9, cGrisMedio)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            // Nombre
            c.add(parrafo(barNombres[i], fBold, 10, cPrimario)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            grafica.addCell(c);
        }
        graficaCell.add(grafica);
        graficaWrapper.addCell(graficaCell);
        document.add(graficaWrapper);

        // ============================================================
        // TABLA DETALLADA DE EVENTOS
        // ============================================================
        document.add(parrafo("Tipos de eventos implementados", fBold, 14, cPrimario).setMarginBottom(8));

        com.itextpdf.layout.element.Table tablaEvt =
                tabla(new float[]{1.8f, 2f, 2.5f, 3.5f});
        tablaEvt.setWidth(pct(100)).setMarginBottom(15);

        // Cabecera
        for (String cab : new String[]{"Tipo de Evento", "Clase JavaFX", "Metodo Listener", "Descripcion"}) {
            tablaEvt.addHeaderCell(new com.itextpdf.layout.element.Cell()
                    .setBackgroundColor(cPrimario)
                    .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER).setPadding(8)
                    .add(parrafo(cab, fBold, 9, cBlanco)));
        }

        // Filas: {tipo, clase, metodo, descripcion}
        String[][] filas = {
            {"Mouse clicked",     "MouseEvent",   "setOnMouseClicked",     "Clic simple o multiple en el nodo"},
            {"Mouse pressed",     "MouseEvent",   "setOnMousePressed",     "Boton del raton presionado"},
            {"Mouse released",    "MouseEvent",   "setOnMouseReleased",    "Boton del raton soltado"},
            {"Mouse entered",     "MouseEvent",   "setOnMouseEntered",     "Cursor entra en el area del nodo"},
            {"Mouse exited",      "MouseEvent",   "setOnMouseExited",      "Cursor sale del area del nodo"},
            {"Mouse moved",       "MouseEvent",   "setOnMouseMoved",       "Cursor se mueve sobre el nodo"},
            {"Mouse dragged",     "MouseEvent",   "setOnMouseDragged",     "Cursor se mueve con boton pulsado"},
            {"Scroll rueda",      "ScrollEvent",  "setOnScroll",           "Desplazamiento con rueda o touchpad"},
            {"Key pressed",       "KeyEvent",     "setOnKeyPressed",       "Tecla presionada"},
            {"Key typed",         "KeyEvent",     "setOnKeyTyped",         "Caracter imprimible escrito"},
            {"Key released",      "KeyEvent",     "setOnKeyReleased",      "Tecla soltada"},
            {"Drag detected",     "DragEvent",    "setOnDragDetected",     "Inicio del arrastre"},
            {"Drag over",         "DragEvent",    "setOnDragOver",         "Objeto pasando sobre el destino"},
            {"Drag dropped",      "DragEvent",    "setOnDragDropped",      "Objeto soltado en el destino"},
            {"Drag exited",       "DragEvent",    "setOnDragExited",       "Objeto sale del area del destino"},
            {"Drag done",         "DragEvent",    "setOnDragDone",         "Arrastre completado en el origen"},
            {"Touch pressed",     "TouchEvent",   "setOnTouchPressed",     "Toque inicial en pantalla tactil"},
            {"Touch moved",       "TouchEvent",   "setOnTouchMoved",       "Dedo moviendose en pantalla tactil"},
            {"Touch released",    "TouchEvent",   "setOnTouchReleased",    "Dedo levantado de pantalla tactil"},
            {"Touch stationary",  "TouchEvent",   "setOnTouchStationary",  "Dedo quieto en pantalla tactil"},
            {"Swipe",             "SwipeEvent",   "setOnSwipeLeft/Right",  "Gesto de deslizamiento"},
            {"Zoom",              "ZoomEvent",    "setOnZoom",             "Gesto pellizco para zoom"},
            {"Rotate",            "RotateEvent",  "setOnRotate",           "Gesto de rotacion con dos dedos"},
            {"Scroll tactil",     "ScrollEvent",  "setOnScroll",           "Desplazamiento tactil / touchpad"},
        };

        boolean alt = false;
        for (String[] fila : filas) {
            com.itextpdf.kernel.colors.Color bg = alt ? rgb(235, 245, 251)
                    : com.itextpdf.kernel.colors.ColorConstants.WHITE;
            for (int col = 0; col < 4; col++) {
                com.itextpdf.kernel.font.PdfFont f = col == 0 ? fBold : (col == 1 ? fItalic : fNormal);
                tablaEvt.addCell(new com.itextpdf.layout.element.Cell()
                        .setBackgroundColor(bg)
                        .setBorder(new com.itextpdf.layout.borders.SolidBorder(cGrisMedio, 0.3f))
                        .setPadding(5)
                        .add(parrafo(fila[col], f, 8, cPrimario)));
            }
            alt = !alt;
        }
        document.add(tablaEvt);

        // ============================================================
        // PIE DE PÁGINA
        // ============================================================
        document.add(parrafo(
                "Informe generado por Diego  |  Interfaces Naturales JavaFX  |  " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                fItalic, 8, cGrisMedio)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setMarginTop(10));

        document.close();
    }

    // ===== MÉTODOS AUXILIARES =====

    private static com.itextpdf.kernel.colors.DeviceRgb rgb(int r, int g, int b) {
        return new com.itextpdf.kernel.colors.DeviceRgb(r, g, b);
    }

    private static com.itextpdf.layout.properties.UnitValue pct(float v) {
        return com.itextpdf.layout.properties.UnitValue.createPercentValue(v);
    }

    private static com.itextpdf.layout.element.Table tabla(float[] cols) {
        return new com.itextpdf.layout.element.Table(
                com.itextpdf.layout.properties.UnitValue.createPercentArray(cols));
    }

    private static com.itextpdf.layout.element.Paragraph parrafo(
            String texto,
            com.itextpdf.kernel.font.PdfFont font,
            float size,
            com.itextpdf.kernel.colors.Color color) {
        return new com.itextpdf.layout.element.Paragraph(texto)
                .setFont(font).setFontSize(size).setFontColor(color).setMargin(2);
    }
}
