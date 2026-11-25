package pe.edu.upeu.entregable1.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import pe.edu.upeu.entregable1.modelo.Cita;
import pe.edu.upeu.entregable1.modelo.Cliente;
import pe.edu.upeu.entregable1.modelo.Consulta;
import pe.edu.upeu.entregable1.modelo.Mascota;
import pe.edu.upeu.entregable1.modelo.Tratamiento;
import pe.edu.upeu.entregable1.modelo.Veterinario;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PdfExporter {

    private static final Color AZUL = new Color(20, 91, 112);
    private static final Color TURQUESA = new Color(46, 157, 164);
    private static final Color FONDO = new Color(243, 248, 249);
    private static final Color BORDE = new Color(218, 229, 231);
    private static final Color TEXTO = new Color(43, 57, 62);
    private static final Color SECUNDARIO = new Color(104, 122, 127);

    private PdfExporter() {
    }

    public static void exportaTratamiento(Tratamiento tratamiento, File archivo) throws Exception {
        if (tratamiento == null || archivo == null) {
            throw new IllegalArgumentException("Seleccione un tratamiento y una ubicación para guardar la ficha.");
        }

        Document documento = new Document(PageSize.A4, 42, 42, 36, 48);
        try (FileOutputStream salida = new FileOutputStream(archivo)) {
            try {
                PdfWriter.getInstance(documento, salida);
                documento.addTitle("Ficha individual de tratamiento");
                documento.addSubject("Ficha veterinaria del tratamiento seleccionado");
                documento.addCreator("Sistema de Gestión Veterinaria");
                documento.open();

                Consulta consulta = tratamiento.getConsulta();
                Cita cita = consulta == null ? null : consulta.getCita();
                Mascota mascota = cita == null ? null : cita.getMascota();
                Cliente cliente = mascota == null ? null : mascota.getCliente();
                Veterinario veterinario = cita == null ? null : cita.getVeterinario();

                agregarEncabezado(documento);
                agregarTitulo(documento);
                agregarDatosPaciente(documento, mascota, cliente, cita, veterinario);
                agregarBloque(documento, "Diagnóstico", consulta == null ? null : consulta.getDiagnostico());
                agregarBloque(documento, "Observaciones de la consulta", consulta == null ? null : consulta.getObservaciones());
                agregarBloque(documento, "Tratamiento indicado", tratamiento.getDescripcion());
                agregarBloque(documento, "Indicaciones", tratamiento.getIndicaciones());
                agregarDuracion(documento, tratamiento.getDuracionDias());
                agregarEspacioSeguimiento(documento);
                agregarPie(documento);
            } finally {
                if (documento.isOpen()) {
                    documento.close();
                }
            }
        }
    }

    private static void agregarEncabezado(Document documento) throws Exception {
        PdfPTable encabezado = new PdfPTable(new float[]{1.1f, 5.2f, 2.3f});
        encabezado.setWidthPercentage(100);
        encabezado.setSpacingAfter(12);

        PdfPCell celdaLogo = new PdfPCell();
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        URL logoUrl = PdfExporter.class.getResource("/pe/edu/upeu/entregable1/img/logo veterinaria.png");
        if (logoUrl != null) {
            Image logo = Image.getInstance(logoUrl);
            logo.scaleToFit(58, 58);
            celdaLogo.addElement(logo);
        }

        Font nombreClinica = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, AZUL);
        Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 9, SECUNDARIO);
        PdfPCell celdaTitulo = new PdfPCell();
        celdaTitulo.setBorder(Rectangle.NO_BORDER);
        celdaTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaTitulo.addElement(new Paragraph("CLÍNICA VETERINARIA", nombreClinica));
        celdaTitulo.addElement(new Paragraph("Cuidado y bienestar animal", subtitulo));

        PdfPCell celdaFecha = new PdfPCell();
        celdaFecha.setBorder(Rectangle.NO_BORDER);
        celdaFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph etiqueta = new Paragraph("FECHA DE EMISIÓN", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, TURQUESA));
        etiqueta.setAlignment(Element.ALIGN_RIGHT);
        celdaFecha.addElement(etiqueta);
        Paragraph fecha = new Paragraph(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es", "PE"))),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL));
        fecha.setAlignment(Element.ALIGN_RIGHT);
        celdaFecha.addElement(fecha);

        encabezado.addCell(celdaLogo);
        encabezado.addCell(celdaTitulo);
        encabezado.addCell(celdaFecha);
        documento.add(encabezado);
    }

    private static void agregarTitulo(Document documento) throws Exception {
        PdfPTable banda = new PdfPTable(1);
        banda.setWidthPercentage(100);
        PdfPCell celda = new PdfPCell(new Phrase("FICHA INDIVIDUAL DE TRATAMIENTO",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.WHITE)));
        celda.setBackgroundColor(AZUL);
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setPadding(11);
        banda.addCell(celda);
        documento.add(banda);

        Paragraph bajada = new Paragraph("Indicaciones registradas para el paciente y la consulta seleccionados.",
                FontFactory.getFont(FontFactory.HELVETICA, 9, SECUNDARIO));
        bajada.setSpacingBefore(8);
        bajada.setSpacingAfter(11);
        documento.add(bajada);
    }

    private static void agregarDatosPaciente(Document documento, Mascota mascota, Cliente cliente,
                                             Cita cita, Veterinario veterinario) throws Exception {
        Paragraph titulo = new Paragraph("Datos de atención", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL));
        titulo.setSpacingAfter(5);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(new float[]{1.1f, 2.2f, 1.1f, 2.2f});
        tabla.setWidthPercentage(100);
        tabla.setSpacingAfter(10);

        agregarCampo(tabla, "Mascota", mascota == null ? null : mascota.getNombre(),
                "Especie / raza", mascota == null ? null : unir(mascota.getEspecie(), mascota.getRaza()));
        agregarCampo(tabla, "Propietario", cliente == null ? null : unir(cliente.getNombres(), cliente.getApellidos()),
                "DNI", cliente == null ? null : cliente.getDni());
        agregarCampo(tabla, "Veterinario", veterinario == null ? null : unir(veterinario.getNombres(), veterinario.getApellidos()),
                "Especialidad", veterinario == null ? null : veterinario.getEspecialidad());
        agregarCampo(tabla, "Fecha de cita", cita == null || cita.getFecha() == null ? null : cita.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                "Hora", cita == null ? null : cita.getHora());
        agregarCampoCompleto(tabla, "Motivo de cita", cita == null ? null : cita.getMotivo());
        documento.add(tabla);
    }

    private static void agregarCampo(PdfPTable tabla, String etiqueta1, String valor1, String etiqueta2, String valor2) {
        tabla.addCell(crearCelda(etiqueta1, true));
        tabla.addCell(crearCelda(valor1, false));
        tabla.addCell(crearCelda(etiqueta2, true));
        tabla.addCell(crearCelda(valor2, false));
    }

    private static void agregarCampoCompleto(PdfPTable tabla, String etiqueta, String texto) {
        tabla.addCell(crearCelda(etiqueta, true));
        PdfPCell celdaValor = crearCelda(texto, false);
        celdaValor.setColspan(3);
        tabla.addCell(celdaValor);
    }

    private static PdfPCell crearCelda(String texto, boolean esEtiqueta) {
        Font fuente = esEtiqueta
                ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, AZUL)
                : FontFactory.getFont(FontFactory.HELVETICA, 8.5f, TEXTO);
        PdfPCell celda = new PdfPCell(new Phrase(valor(texto), fuente));
        celda.setBackgroundColor(esEtiqueta ? FONDO : Color.WHITE);
        celda.setBorderColor(BORDE);
        celda.setPadding(7);
        return celda;
    }

    private static void agregarBloque(Document documento, String titulo, String contenido) throws Exception {
        Paragraph encabezado = new Paragraph(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL));
        encabezado.setSpacingBefore(5);
        encabezado.setSpacingAfter(4);
        documento.add(encabezado);

        PdfPTable bloque = new PdfPTable(1);
        bloque.setWidthPercentage(100);
        PdfPCell celda = new PdfPCell(new Phrase(valor(contenido), FontFactory.getFont(FontFactory.HELVETICA, 9, TEXTO)));
        celda.setBackgroundColor(FONDO);
        celda.setBorderColor(BORDE);
        celda.setPadding(9);
        bloque.addCell(celda);
        documento.add(bloque);
    }

    private static void agregarDuracion(Document documento, int dias) throws Exception {
        PdfPTable duracion = new PdfPTable(1);
        duracion.setWidthPercentage(100);
        duracion.setSpacingBefore(8);
        PdfPCell celda = new PdfPCell(new Phrase("DURACIÓN INDICADA: " + dias + (dias == 1 ? " día" : " días"),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL)));
        celda.setBackgroundColor(new Color(226, 243, 242));
        celda.setBorderColor(new Color(190, 224, 222));
        celda.setPadding(10);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        duracion.addCell(celda);
        documento.add(duracion);
    }

    private static void agregarEspacioSeguimiento(Document documento) throws Exception {
        Paragraph titulo = new Paragraph("Seguimiento profesional", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL));
        titulo.setSpacingBefore(12);
        titulo.setSpacingAfter(4);
        documento.add(titulo);

        PdfPTable espacio = new PdfPTable(1);
        espacio.setWidthPercentage(100);
        PdfPCell celda = new PdfPCell(new Phrase(" "));
        celda.setBorderColor(BORDE);
        celda.setFixedHeight(48);
        espacio.addCell(celda);
        documento.add(espacio);

        PdfPTable firma = new PdfPTable(new float[]{1f, 1f});
        firma.setWidthPercentage(100);
        firma.setSpacingBefore(10);
        PdfPCell profesional = new PdfPCell(new Phrase("Firma y sello del profesional", FontFactory.getFont(FontFactory.HELVETICA, 8, SECUNDARIO)));
        profesional.setBorder(Rectangle.TOP);
        profesional.setBorderColor(BORDE);
        profesional.setPaddingTop(5);
        PdfPCell fecha = new PdfPCell(new Phrase("Fecha de seguimiento: __________________", FontFactory.getFont(FontFactory.HELVETICA, 8, SECUNDARIO)));
        fecha.setBorder(Rectangle.TOP);
        fecha.setBorderColor(BORDE);
        fecha.setPaddingTop(5);
        firma.addCell(profesional);
        firma.addCell(fecha);
        documento.add(firma);
    }

    private static void agregarPie(Document documento) throws Exception {
        Paragraph pie = new Paragraph("Ficha generada desde el Sistema de Gestión Veterinaria.",
                FontFactory.getFont(FontFactory.HELVETICA, 8, SECUNDARIO));
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(12);
        documento.add(pie);
    }

    private static String unir(String primero, String segundo) {
        String izquierda = primero == null ? "" : primero.trim();
        String derecha = segundo == null ? "" : segundo.trim();
        return (izquierda + " " + derecha).trim();
    }

    private static String valor(String texto) {
        return texto == null || texto.isBlank() ? "-" : texto.trim();
    }
}
