/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
/**
 *
 * @author DAM2Alu4
 */
import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {
    // Genera una contraseña haseada para la base de datos
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    // comprobar la contraseña haseada de la base de datos
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
     public static void hashPrueba(String password) {
        String c= BCrypt.hashpw(password, BCrypt.gensalt());
         System.out.println(c);
    }
}