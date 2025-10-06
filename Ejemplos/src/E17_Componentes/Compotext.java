/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E17_Componentes;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.JTextField;

/**
 *
 * @author DAM2Alu3
 */
public class Compotext extends JTextField implements Serializable{
    private Color colorFondo;
    private int numCaracteres;
    private Color colorDefecto;
    
    private void compruebaLongitud(){
        if(getText().length() >= numCaracteres)
            setBackground(colorFondo);
        else
            setBackground(colorDefecto);
    }

    public Compotext() {
        super();
        colorDefecto = getBackground();
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                compruebaLongitud();
            }
        });
        
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
    }

    public int getNumCaracteres() {
        return numCaracteres;
    }

    public void setNumCaracteres(int numCaracteres) {
        this.numCaracteres = numCaracteres;
    }

    public Color getColorDefecto() {
        return colorDefecto;
    }

    public void setColorDefecto(Color colorDefecto) {
        this.colorDefecto = colorDefecto;
    }
    
    
}
