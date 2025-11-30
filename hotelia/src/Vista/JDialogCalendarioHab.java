/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;

import Controlador.ConexionBBDD;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DAM2Alu16
 * 
 * En este JDialog, se crea un JTable, el cual te muestra las habitaciones que tienes a la izquierda y a la derecha la fecha,
 * marcando cuando esta disponible, ocupada o en reparacion
 */

import java.time.temporal.ChronoUnit;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;

//Esta es una clase para que la tabla pueda tener colores, el verde, el rojo y el azul
class EstadoCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // columna 0 es "Habitaciones" -> fondo normal
        if (column == 0) {
            c.setBackground(Color.WHITE);
            return c;
        }

        String s = value == null ? "" : value.toString();

        // normaliza a minúsculas para comparar
        String low = s.toLowerCase();

        if (low.contains("ocup")) { // "Ocupado"
            c.setBackground(new Color(0xFFCCCC)); // rojo claro
        } else if (low.contains("repar")) { // "Reparación"
            c.setBackground(new Color(0x0B3D91)); // azul oscuro (puedes ajustar)
            c.setForeground(Color.WHITE);
        } else if (s.isEmpty()) {
            c.setBackground(new Color(0xCCFFCC)); // verde claro para libre
            c.setForeground(Color.BLACK);
            setText(""); // opcional, muestra vacío
        } else {
            // si hay otros estados (ej: "Reservado", "Check-in") ajusta aquí
            c.setBackground(Color.LIGHT_GRAY);
            c.setForeground(Color.BLACK);
        }

        if (isSelected) {
            // respeta selección (opcional)
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        }

        return c;
    }
}

public class JDialogCalendarioHab extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JDialogCalendarioHab.class.getName());

    
    ConexionBBDD database = new ConexionBBDD();
    Connection conexion;
    DefaultTableModel dtm;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate dateActual;
    
    public void cargarCalendario() {
    // modelo sin filas prefijadas
    dtm = new DefaultTableModel();
    jTableCalenHab.setModel(dtm);

    // columna de habitaciones
    dtm.addColumn("Habitaciones");

    dateActual = LocalDate.now();

    // añade columnas de fechas (ej. 730 días = 2 años)
    for (int i = 0; i < 730; i++) {
        LocalDate date = dateActual.plusDays(i);
        dtm.addColumn(date.format(dtf));
    }

    // aplica renderer (pinta las celdas según su texto)
    jTableCalenHab.setDefaultRenderer(Object.class, new EstadoCellRenderer());
    jTableCalenHab.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

    // recoge lista de habitaciones
    List<String> habitaciones = database.buscarIdHabitaciones();

    for (int rowIndex = 0; rowIndex < habitaciones.size(); rowIndex++) {
        String idHabStr = habitaciones.get(rowIndex);
        int idHab = Integer.parseInt(idHabStr);

        // crea fila con el número correcto de columnas
        Object[] fila = new Object[dtm.getColumnCount()];
        fila[0] = idHabStr; // primera columna: id habitacion (o número)
        dtm.addRow(fila);

        // usa tu método que devuelve todas las reservas (inicio, fin, estado)
        List<Object[]> reservas = database.buscarFechayEstadoPorHabitacion(idHab);

        for (Object[] reg : reservas) {
            // reg[0] = inicio (LocalDate), reg[1] = fin (LocalDate), reg[2] = estado (String)
            LocalDate inicio = (LocalDate) reg[0];
            LocalDate fin = (LocalDate) reg[1];
            String estado = (String) reg[2];

            // ignora reservas canceladas (ajusta según cómo guardes el estado)
            if (estado != null && estado.equalsIgnoreCase("Cancelado")) {
                continue;
            }

            // calcula indices relativos desde dateActual
            long startOffset = ChronoUnit.DAYS.between(dateActual, inicio);
            long endOffset = ChronoUnit.DAYS.between(dateActual, fin);

            // si la reserva está fuera del rango [0,729] la ignoramos o recortamos
            int startCol = (int) Math.max(0, startOffset) + 1; // +1 porque col 0 = Habitaciones
            int endCol = (int) Math.min(729, endOffset) + 1;

            // si fin < dateActual o inicio > último día => nada que pintar
            if (endCol < 1 || startCol > 730) continue;

            // establece el texto (puedes mapear estados a etiquetas: Ocupado, Reparacion...)
            String etiqueta = "Ocupado";
            if (estado.equalsIgnoreCase("Reparacion") || estado.equalsIgnoreCase("En reparacion")) {
                etiqueta = "Reparacion";
            }

            for (int col = startCol; col <= endCol; col++) {
                dtm.setValueAt(etiqueta, rowIndex, col);
            }
        }

        // además, si la habitación actualmente está en reparación globalmente (consulta tabla habitacion)
        String estadoHab = database.buscarEstadoHabitacion(idHab);
        if (estadoHab != null && estadoHab.toLowerCase().contains("repar")) {
            // marca toda la fila (o un rango) como reparación; aquí marco todas las fechas visibles:
            for (int col = 1; col < dtm.getColumnCount(); col++) {
                dtm.setValueAt("Reparacion", rowIndex, col);
            }
        }
    }


            //da error las fechas(fecha_inicio), las devuelve vacias, mirarlo
            
            //queda actualizar el estado de la habitacion si coincide fecha de reserva con la fecha de la tabla
            
            //comprobar que la reserva no esta cancelada al inicio
            
            //editar el campo seleccionado u otra forma, pero poder editar, añadir y borrar, 
            //para eso se tendra que actualizar la pantalla (poner este metodo en los otros)
            //con jdialogs
            
            //mirar si estan en reparacion, si lo estan marcarlo 
            
        
   
        
//        database.getConnection();
//        PreparedStatement ps;
//        try {
//            ps = conexion.prepareStatement("select idHabitacion from habitacion");
//            database.selectSQL(ps, dtm);
//            
//        } catch (SQLException ex) {
//            System.getLogger(JDialogCalendarioHab.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//        }
        
        
    }
    
    /**
     * Creates new form JDialogCalendarioHab
     */
    public JDialogCalendarioHab(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        cargarCalendario();
        
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCalenHab = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButtonBuscar = new javax.swing.JButton();
        jButtonEditar = new javax.swing.JButton();
        jButtonVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableCalenHab.setBorder(new javax.swing.border.MatteBorder(null));
        jTableCalenHab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableCalenHab.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTableCalenHab);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonBuscar.setText("Buscar");
        jPanel3.add(jButtonBuscar);

        jButtonEditar.setText("Editar");
        jPanel3.add(jButtonEditar);

        jButtonVolver.setText("Volver");
        jPanel3.add(jButtonVolver);

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the dialog */
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
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonEditar;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCalenHab;
    // End of variables declaration//GEN-END:variables
}
