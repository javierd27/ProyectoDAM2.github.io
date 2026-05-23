/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;

import Controlador.HabitacionDAO;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Calendario de habitaciones con panel visual tipo timeline.
 *
 * @author DAM2Alu16
 */
public class JDialogCalendarioHab extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = 
        java.util.logging.Logger.getLogger(JDialogCalendarioHab.class.getName());

    // Configuracion visual
    private static final int FILA_ALTURA = 40;
    private static final int DIA_ANCHO = 35;
    private static final int CABECERA_ALTURA = 50;
    private static final int HABITACION_ANCHO = 120;

    // Colores
    private static final Color COLOR_LIBRE = new Color(200, 230, 200);
    private static final Color COLOR_OCUPADO = new Color(255, 150, 150);
    private static final Color COLOR_REPARACION = new Color(100, 150, 200);
    private static final Color COLOR_HOY = new Color(255, 235, 100);
    private static final Color COLOR_FONDO = Color.WHITE;
    private static final Color COLOR_LINEA = new Color(220, 220, 220);

    // Datos
    private LocalDate fechaInicio;
    private int diasVisibles = 30;
    private HabitacionDAO habitacionDAO;
    private List<Object[]> datosHabitaciones;
    private List<Object[]> datosFiltrados;
    private TimelinePanel timeline;

    public JDialogCalendarioHab(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.fechaInicio = LocalDate.now();
        this.habitacionDAO = new HabitacionDAO();
        this.datosHabitaciones = new ArrayList<>();
        this.datosFiltrados = new ArrayList<>();
        initComponents();
        initCalendario();
    }

    private void initCalendario() {
        // Crear timeline
        timeline = new TimelinePanel();
        
        JScrollPane scrollTimeline = new JScrollPane(timeline);
        scrollTimeline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollTimeline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Limpiar panelCalendario y añadir timeline
        panelCalendario.removeAll();
        panelCalendario.setLayout(new BorderLayout());
        panelCalendario.add(scrollTimeline, BorderLayout.CENTER);
        panelCalendario.revalidate();
        panelCalendario.repaint();
        
        // ===== INICIALIZAR FILTROS =====
        cmbFiltroTipo.removeAllItems();
        cmbFiltroTipo.addItem("Todos");
        cmbFiltroTipo.addItem("Simple");
        cmbFiltroTipo.addItem("Duplex");
        cmbFiltroTipo.addItem("Junior");
        cmbFiltroTipo.addItem("Presidencial");
        
        cmbFiltroEstado.removeAllItems();
        cmbFiltroEstado.addItem("Todos");
        cmbFiltroEstado.addItem("Libre");
        cmbFiltroEstado.addItem("Ocupado");
        cmbFiltroEstado.addItem("Reparacion");
        
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnLimpiarFiltro.addActionListener(e -> limpiarFiltros());
        
        // Listeners botones navegacion
        btnAnterior.addActionListener(e -> moverFecha(-diasVisibles));
        btnHoy.addActionListener(e -> { fechaInicio = LocalDate.now(); cargarDatos(); });
        btnSiguiente.addActionListener(e -> moverFecha(diasVisibles));
        btnNueva.addActionListener(e -> abrirNueva());
        btnEditar.addActionListener(e -> abrirEditar());
        btnVolver.addActionListener(e -> dispose());
        
        cargarDatos();
    }

    private void moverFecha(int dias) {
        fechaInicio = fechaInicio.plusDays(dias);
        cargarDatos();
    }

    private void cargarDatos() {
        datosHabitaciones.clear();
        
        try {
            List<String> ids = habitacionDAO.buscarIdHabitaciones();

            for (String idStr : ids) {
                int idHab = Integer.parseInt(idStr);
                var hab = habitacionDAO.buscarHabitacionPorId(idHab);
                if (hab == null) continue;

                List<Object[]> reservas = habitacionDAO.buscarFechayEstadoPorHabitacion(idHab);
                List<Object[]> reservasFiltradas = new ArrayList<>();
                for (Object[] r : reservas) {
                    String estado = (String) r[2];
                    if (estado == null || estado.equalsIgnoreCase("Cancelado")) continue;
                    reservasFiltradas.add(r);
                }

                datosHabitaciones.add(new Object[]{
                    idHab, hab.getNumero(), hab.getTipo(),
                    hab.getCapacidad(), hab.getEstado(), reservasFiltradas
                });
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error cargando datos", e);
            JOptionPane.showMessageDialog(this,
                "Error al cargar habitaciones: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        aplicarFiltros();
        actualizarLabelFechas();
    }

    private void actualizarLabelFechas() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fin = fechaInicio.plusDays(diasVisibles - 1);
        lblFechas.setText(fechaInicio.format(fmt) + "  -  " + fin.format(fmt));
    }

    private void aplicarFiltros() {
        String tipoFiltro = (String) cmbFiltroTipo.getSelectedItem();
        String estadoFiltro = (String) cmbFiltroEstado.getSelectedItem();
        
        datosFiltrados.clear();
        
        for (Object[] hab : datosHabitaciones) {
            String tipo = (String) hab[2];
            String estado = (String) hab[4];
            
            if (!"Todos".equals(tipoFiltro) && !tipoFiltro.equalsIgnoreCase(tipo)) {
                continue;
            }
            if (!"Todos".equals(estadoFiltro)) {
                String est = estado != null ? estado.toLowerCase() : "";
                if (!est.contains(estadoFiltro.toLowerCase())) {
                    continue;
                }
            }
            datosFiltrados.add(hab);
        }
        
        timeline.repaint();
    }

    private void limpiarFiltros() {
        cmbFiltroTipo.setSelectedIndex(0);
        cmbFiltroEstado.setSelectedIndex(0);
        aplicarFiltros();
    }

    private void abrirNueva() {
        JDialogAnadirHabitaciones dialog = new JDialogAnadirHabitaciones(
            (JFrame) this.getParent(), true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        cargarDatos();
    }

    private void abrirEditar() {
        String input = JOptionPane.showInputDialog(this, "Numero de habitacion a editar:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int numHab = Integer.parseInt(input.trim());
            int idEncontrado = -1;
            for (Object[] h : datosHabitaciones) {
                if ((Integer) h[1] == numHab) {
                    idEncontrado = (Integer) h[0];
                    break;
                }
            }
            if (idEncontrado == -1) {
                JOptionPane.showMessageDialog(this, "Habitacion no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JDialogEditarHabitaciones dialog = new JDialogEditarHabitaciones(
                (JFrame) this.getParent(), true, idEncontrado);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            cargarDatos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Numero no valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== PANEL VISUAL ====================

    private class TimelinePanel extends JPanel {
        private int filaHover = -1;
        private int diaHover = -1;

        public TimelinePanel() {
            setBackground(COLOR_FONDO);
            setPreferredSize(new Dimension(800, 400));
            
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    calcularHover(e.getPoint());
                    repaint();
                }
            });
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (filaHover >= 0 && filaHover < datosFiltrados.size()) {
                        Object[] hab = datosFiltrados.get(filaHover);
                        int idHab = (Integer) hab[0];
                        int numero = (Integer) hab[1];
                        String tipo = (String) hab[2];
                        int capacidad = (Integer) hab[3];
                        String estado = (String) hab[4];

                        lblInfoHabitacion.setText("Seleccionada: Hab. " + numero + " | " + tipo + " | " + capacidad + " pers. | Estado: " + estado);

                        if (e.getClickCount() == 2) {
                            // ✅ FIX: No usar getParent() en cadena
                            Window ventana = SwingUtilities.getWindowAncestor(TimelinePanel.this);
                            JFrame framePadre = (ventana instanceof JFrame) ? (JFrame) ventana : null;
                            JDialogEditarHabitaciones dialog = new JDialogEditarHabitaciones(
                                framePadre, true, idHab);
                            dialog.setLocationRelativeTo(JDialogCalendarioHab.this);
                            dialog.setVisible(true);
                            cargarDatos();
                        }
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    filaHover = -1;
                    diaHover = -1;
                    lblInfoHabitacion.setText("Haz doble clic en una habitacion para editar");
                    repaint();
                }
            });
        }

        private void calcularHover(Point p) {
            filaHover = -1;
            diaHover = -1;
            
            int yInicio = CABECERA_ALTURA + 5;
            if (p.y >= yInicio) {
                filaHover = (p.y - yInicio) / FILA_ALTURA;
            }
            if (p.x >= HABITACION_ANCHO) {
                diaHover = (p.x - HABITACION_ANCHO) / DIA_ANCHO;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int anchoTotal = HABITACION_ANCHO + (diasVisibles * DIA_ANCHO) + 20;
            int altoTotal = CABECERA_ALTURA + (datosFiltrados.size() * FILA_ALTURA) + 30;
            setPreferredSize(new Dimension(anchoTotal, Math.max(altoTotal, 300)));
            revalidate();

            // Fondo
            g2d.setColor(COLOR_FONDO);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Cabecera
            g2d.setColor(new Color(60, 60, 60));
            g2d.fillRect(0, 0, getWidth(), CABECERA_ALTURA);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2d.drawString("HABITACION", 30, 30);

            // Dias
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd");
            String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};
            for (int i = 0; i < diasVisibles; i++) {
                LocalDate fecha = fechaInicio.plusDays(i);
                int x = HABITACION_ANCHO + (i * DIA_ANCHO);
                boolean esHoy = fecha.equals(LocalDate.now());
                boolean esFinde = fecha.getDayOfWeek().getValue() >= 6;

                if (esHoy) {
                    g2d.setColor(COLOR_HOY);
                    g2d.fillRect(x, 0, DIA_ANCHO, CABECERA_ALTURA);
                    g2d.setColor(Color.BLACK);
                } else if (esFinde) {
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.fillRect(x, 0, DIA_ANCHO, CABECERA_ALTURA);
                    g2d.setColor(Color.GRAY);
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String diaStr = fecha.format(fmt);
                int w = g2d.getFontMetrics().stringWidth(diaStr);
                g2d.drawString(diaStr, x + (DIA_ANCHO - w) / 2, 25);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                g2d.drawString(diasSemana[fecha.getDayOfWeek().getValue() - 1], x + 12, 42);
            }

            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine(0, CABECERA_ALTURA, getWidth(), CABECERA_ALTURA);
            g2d.drawLine(HABITACION_ANCHO, 0, HABITACION_ANCHO, getHeight());

            // Filas
            int yInicio = CABECERA_ALTURA + 5;
            for (int i = 0; i < datosFiltrados.size(); i++) {
                Object[] hab = datosFiltrados.get(i);
                int numero = (Integer) hab[1];
                String tipo = (String) hab[2];
                int capacidad = (Integer) hab[3];
                String estadoGlobal = (String) hab[4];
                @SuppressWarnings("unchecked")
                List<Object[]> reservas = (List<Object[]>) hab[5];

                int y = yInicio + (i * FILA_ALTURA);
                
                // Hover
                if (i == filaHover) {
                    g2d.setColor(new Color(200, 220, 255));
                } else {
                    g2d.setColor(i % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                g2d.fillRect(0, y, getWidth(), FILA_ALTURA);
                g2d.setColor(COLOR_LINEA);
                g2d.drawLine(0, y + FILA_ALTURA, getWidth(), y + FILA_ALTURA);

                // Info habitacion - NUMERO GRANDE + DETALLES
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2d.drawString(String.valueOf(numero), 10, y + 20);
                
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawString(tipo + " | " + capacidad + " pers.", 10, y + 34);

                // Indicador estado
                Color colorEstado = COLOR_LIBRE;
                if (estadoGlobal != null) {
                    String e = estadoGlobal.toLowerCase();
                    if (e.contains("ocup")) colorEstado = COLOR_OCUPADO;
                    else if (e.contains("repar")) colorEstado = COLOR_REPARACION;
                }
                g2d.setColor(colorEstado);
                g2d.fillRect(0, y + 2, 4, FILA_ALTURA - 4);

                // Reservas
                for (Object[] r : reservas) {
                    LocalDate ini = (LocalDate) r[0];
                    LocalDate fin = (LocalDate) r[1];
                    String estadoRes = (String) r[2];

                    long offsetIni = ChronoUnit.DAYS.between(fechaInicio, ini);
                    long offsetFin = ChronoUnit.DAYS.between(fechaInicio, fin);
                    int colIni = (int) Math.max(0, offsetIni);
                    int colFin = (int) Math.min(diasVisibles - 1, offsetFin);
                    if (colFin < 0 || colIni >= diasVisibles) continue;

                    int xBarra = HABITACION_ANCHO + (colIni * DIA_ANCHO) + 1;
                    int anchoBarra = ((colFin - colIni + 1) * DIA_ANCHO) - 2;

                    Color colorBarra = COLOR_OCUPADO;
                    if (estadoRes != null) {
                        String er = estadoRes.toLowerCase();
                        if (er.contains("repar")) colorBarra = COLOR_REPARACION;
                    }

                    g2d.setColor(colorBarra);
                    g2d.fillRoundRect(xBarra, y + 8, anchoBarra, FILA_ALTURA - 16, 4, 4);
                    g2d.setColor(colorBarra.darker());
                    g2d.drawRoundRect(xBarra, y + 8, anchoBarra, FILA_ALTURA - 16, 4, 4);

                    if (anchoBarra > 40) {
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 9));
                        String txt = estadoRes.length() > 8 ? estadoRes.substring(0, 8) : estadoRes;
                        g2d.drawString(txt, xBarra + 4, y + 24);
                    }
                }

                // Dia actual
                long diasHastaHoy = ChronoUnit.DAYS.between(fechaInicio, LocalDate.now());
                if (diasHastaHoy >= 0 && diasHastaHoy < diasVisibles) {
                    int xHoy = HABITACION_ANCHO + ((int) diasHastaHoy * DIA_ANCHO);
                    g2d.setColor(new Color(255, 235, 100, 60));
                    g2d.fillRect(xHoy, y, DIA_ANCHO, FILA_ALTURA);
                }
            }

            // Lineas verticales
            g2d.setColor(COLOR_LINEA);
            for (int i = 0; i <= diasVisibles; i++) {
                int x = HABITACION_ANCHO + (i * DIA_ANCHO);
                g2d.drawLine(x, CABECERA_ALTURA, x, yInicio + (datosFiltrados.size() * FILA_ALTURA));
            }
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            int fila = calcularFila(event.getY());
            if (fila >= 0 && fila < datosFiltrados.size() && diaHover >= 0) {
                Object[] hab = datosFiltrados.get(fila);
                int numero = (Integer) hab[1];
                LocalDate fecha = fechaInicio.plusDays(diaHover);
                @SuppressWarnings("unchecked")
                List<Object[]> reservas = (List<Object[]>) hab[5];
                
                String estado = "Libre";
                for (Object[] r : reservas) {
                    LocalDate ini = (LocalDate) r[0];
                    LocalDate fin = (LocalDate) r[1];
                    if (!fecha.isBefore(ini) && !fecha.isAfter(fin)) {
                        estado = (String) r[2];
                        break;
                    }
                }
                return "Hab. " + numero + " | " + fecha + " | " + estado;
            }
            return null;
        }
        
        private int calcularFila(int y) {
            int yInicio = CABECERA_ALTURA + 5;
            if (y < yInicio) return -1;
            return (y - yInicio) / FILA_ALTURA;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMain = new javax.swing.JPanel();
        jPanelFiltros = new javax.swing.JPanel();
        lblFiltroTipo = new javax.swing.JLabel();
        cmbFiltroTipo = new javax.swing.JComboBox<>();
        lblFiltroEstado = new javax.swing.JLabel();
        cmbFiltroEstado = new javax.swing.JComboBox<>();
        btnFiltrar = new javax.swing.JButton();
        btnLimpiarFiltro = new javax.swing.JButton();
        panelCalendario = new javax.swing.JPanel();
        lblFechas = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblInfoHabitacion = new javax.swing.JLabel();
        btnAnterior = new javax.swing.JButton();
        btnHoy = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnNueva = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelFiltros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblFiltroTipo.setText("Tipo:");
        jPanelFiltros.add(lblFiltroTipo);

        cmbFiltroTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Simple", "Duplex", "Junior", "Presidencial" }));
        jPanelFiltros.add(cmbFiltroTipo);

        lblFiltroEstado.setText("Estado:");
        jPanelFiltros.add(lblFiltroEstado);

        cmbFiltroEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Libre", "Ocupado", "Reparacion" }));
        jPanelFiltros.add(cmbFiltroEstado);

        btnFiltrar.setText("Filtrar");
        jPanelFiltros.add(btnFiltrar);

        btnLimpiarFiltro.setText("Limpiar");
        jPanelFiltros.add(btnLimpiarFiltro);

        panelCalendario.setLayout(new java.awt.BorderLayout());

        lblFechas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFechas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechas.setText("01/01/2024  -  30/01/2024");
        panelCalendario.add(lblFechas, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblInfoHabitacion.setText("Haz doble clic en una habitacion para editar");
        jPanel3.add(lblInfoHabitacion);

        btnAnterior.setText("<");
        jPanel3.add(btnAnterior);

        btnHoy.setText("Hoy");
        jPanel3.add(btnHoy);

        btnSiguiente.setText(">");
        jPanel3.add(btnSiguiente);

        btnNueva.setText("Nueva");
        jPanel3.add(btnNueva);

        btnEditar.setText("Editar");
        jPanel3.add(btnEditar);

        btnVolver.setText("Volver");
        jPanel3.add(btnVolver);

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelCalendario, javax.swing.GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addComponent(jPanelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCalendario, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
    // Nimbus fallback primero
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }
    
    // FlatLaf DESPUÉS (sobrescribe Nimbus)
    com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme.setup();

    // Conectar BD
    try {
        new Controlador.ConexionBBDD();
    } catch (Exception e) {
        System.out.println("Error conectando: " + e.getMessage());
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            JDialogCalendarioHab dialog = new JDialogCalendarioHab(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnHoy;
    private javax.swing.JButton btnLimpiarFiltro;
    private javax.swing.JButton btnNueva;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cmbFiltroEstado;
    private javax.swing.JComboBox<String> cmbFiltroTipo;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelFiltros;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JLabel lblFechas;
    private javax.swing.JLabel lblFiltroEstado;
    private javax.swing.JLabel lblFiltroTipo;
    private javax.swing.JLabel lblInfoHabitacion;
    private javax.swing.JPanel panelCalendario;
    // End of variables declaration//GEN-END:variables
}