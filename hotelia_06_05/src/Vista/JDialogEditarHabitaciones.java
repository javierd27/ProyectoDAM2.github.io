/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;

import Controlador.HabitacionDAO;
import Modelo.Habitacion;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Diálogo para editar habitaciones existentes.
 * Valida que no se pueda poner "Libre" si tiene reservas activas.
 * 
 * @author DAM2Alu16
 */
public class JDialogEditarHabitaciones extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = 
        java.util.logging.Logger.getLogger(JDialogEditarHabitaciones.class.getName());

    private static final double MARGEN = 0.30;
    private static final double IVA = 0.21;

    private HabitacionDAO habitacionDAO;
    private int idHabitacion;
    private Habitacion habitacionActual;
    private boolean tieneReservasActivas = false;

    public JDialogEditarHabitaciones(java.awt.Frame parent, boolean modal, int idHabitacion) {
        super(parent, modal);
        this.idHabitacion = idHabitacion;
        this.habitacionDAO = new HabitacionDAO();
        initComponents();
        cargarDatos();
    }

    public void MostrarEditable(int idHabitacion) {
        this.idHabitacion = idHabitacion;
        cargarDatos();
    }

    private void cargarDatos() {
        habitacionActual = habitacionDAO.buscarHabitacionPorId(idHabitacion);
        if (habitacionActual == null) {
            JOptionPane.showMessageDialog(this,
                "Habitacion no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        jLabelNombre.setText("Habitacion " + habitacionActual.getNumero());
        jetNumHab.setText(String.valueOf(habitacionActual.getNumero()));
        jcbTipo.setSelectedItem(habitacionActual.getTipo());
        jsCapacidad.setValue(habitacionActual.getCapacidad());
        jsPrecioB.setValue(habitacionActual.getPrecio_base());
        actualizarPrecioPublico();

        List<Object[]> reservas = habitacionDAO.buscarFechayEstadoPorHabitacion(idHabitacion);
        tieneReservasActivas = false;

        for (Object[] r : reservas) {
            String estado = (String) r[2];
            LocalDate fin = (LocalDate) r[1];
            if (estado != null && !estado.equalsIgnoreCase("Cancelado")
                && !fin.isBefore(LocalDate.now())) {
                tieneReservasActivas = true;
                break;
            }
        }

        String estadoActual = habitacionActual.getEstado();
        jcbEstado.removeAllItems();

        if (tieneReservasActivas) {
            jcbEstado.addItem("Ocupado");
            jcbEstado.addItem("Reparacion");
            if (!"Reparacion".equalsIgnoreCase(estadoActual)) {
                jcbEstado.setSelectedItem("Ocupado");
            } else {
                jcbEstado.setSelectedItem("Reparacion");
            }
        } else {
            jcbEstado.addItem("Libre");
            jcbEstado.addItem("Reparacion");
            jcbEstado.setSelectedItem(estadoActual);
        }
    }

    private void actualizarPrecioPublico() {
        double base = ((Number) jsPrecioB.getValue()).doubleValue();
        double publico = jcbFormulas.isSelected()
            ? base * (1 + MARGEN) * (1 + IVA)
            : base;
    }

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {                                               
        List<String> errores = new ArrayList<>();

        String numStr = jetNumHab.getText().trim();
        if (numStr.isEmpty()) {
            errores.add("El numero de la habitacion no puede ser nulo");
        } else {
            try {
                int num = Integer.parseInt(numStr);
                if (num <= 0) errores.add("El numero debe ser mayor que 0");
            } catch (NumberFormatException e) {
                errores.add("El numero debe ser un valor numerico");
            }
        }

        double precioBase = ((Number) jsPrecioB.getValue()).doubleValue();
        if (precioBase <= 0) errores.add("El precio base debe ser mayor que 0");

        String nuevoEstado = (String) jcbEstado.getSelectedItem();
        if ("Libre".equals(nuevoEstado) && tieneReservasActivas) {
            errores.add("No se puede poner 'Libre': tiene reservas activas");
        }

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, 
                "Corrija los errores:\n\n• " + String.join("\n• ", errores),
                "Errores",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numero = Integer.parseInt(numStr);
            String tipo = (String) jcbTipo.getSelectedItem();
            int capacidad = (Integer) jsCapacidad.getValue();
            String estado = nuevoEstado;
            double precioPublico = jcbFormulas.isSelected()
                ? precioBase * (1 + MARGEN) * (1 + IVA)
                : precioBase;

            // ===== USAR HABITACIONDAO (DTO) con precio_publico =====
            habitacionDAO.actualizarHabitacionPorId(idHabitacion, numero, tipo, 
                capacidad, precioBase, precioPublico, estado);

            JOptionPane.showMessageDialog(rootPane, "Habitacion actualizada correctamente");
            dispose();

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error actualizando", e);
            JOptionPane.showMessageDialog(rootPane, 
                "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                              

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {                                                
        dispose();
    }                                               

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabelNombre = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jetNumHab = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jcbTipo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jsCapacidad = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jsPrecioB = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jcbFormulas = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jcbEstado = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jButtonCancelar = new javax.swing.JButton();
        jButtonAceptar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setLayout(new java.awt.GridLayout(7, 2, 0, 20));

        jLabelNombre.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelNombre.setText("Habitacion id");
        jPanel2.add(jLabelNombre);
        jPanel2.add(jLabel6);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Numero de habitacion");
        jPanel2.add(jLabel1);
        jPanel2.add(jetNumHab);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tipo de habitacion");
        jPanel2.add(jLabel2);

        jcbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simple", "Duplex", "Junior", "Presidencial" }));
        jPanel2.add(jcbTipo);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Capacidad de la habitacion");
        jPanel2.add(jLabel3);

        jsCapacidad.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jPanel2.add(jsCapacidad);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Precio base");
        jPanel2.add(jLabel4);

        jsPrecioB.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 10.0d));
        jPanel2.add(jsPrecioB);
        jPanel2.add(jLabel7);

        jcbFormulas.setSelected(true);
        jcbFormulas.setText("¿Usar formulas de precio?");
        jPanel2.add(jcbFormulas);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Estado de la habitacion");
        jPanel2.add(jLabel5);

        jcbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Libre", "Reparacion", "Ocupado" }));
        jcbEstado.setToolTipText("");
        jPanel2.add(jcbEstado);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCancelar);

        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonAceptar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    FlatCobalt2IJTheme.setup();

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
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> jcbEstado;
    private javax.swing.JCheckBox jcbFormulas;
    private javax.swing.JComboBox<String> jcbTipo;
    private javax.swing.JTextField jetNumHab;
    private javax.swing.JSpinner jsCapacidad;
    private javax.swing.JSpinner jsPrecioB;
    // End of variables declaration//GEN-END:variables
}