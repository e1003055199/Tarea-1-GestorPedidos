/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorpedidos;
import java.util.*;
import java.io.*;
import java.sql.*;
/**
 *
 * @author ROGSTRIX
 */

// Módulo de Persistencia (SRP: Solo maneja BD)
class RepositorioPedidos {
    private Connection conexion;

    public RepositorioPedidos(Connection conexion) {
        this.conexion = conexion;
    }

    public void guardar(String cliente, double total) {
        try {
            String sql = "INSERT INTO pedidos (cliente, total) VALUES (?, ?)";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, cliente);
            pstmt.setDouble(2, total);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error BD (Guardar): " + e.getMessage());
        }
    }

    public void eliminar(int id) {
        try {
            String sql = "DELETE FROM pedidos WHERE id = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error BD (Eliminar): " + e.getMessage());
        }
    }
}

class Validador {
    public static boolean esValido(String nombre, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: nombre inválido");
            return false;
        }
        if (email == null || !email.contains("@")) {
            System.out.println("Error: email inválido");
            return false;
        }
        return true;
    }
}
class CalculadoraDescuentos {
    public double calcular(double subtotal, String tipoCliente) {
        switch (tipoCliente.toUpperCase()) {
            case "VIP": return subtotal * 0.20;
            case "FRECUENTE": return subtotal * 0.10;
            case "REGULAR": return subtotal * 0.05;
            default: return 0;
        }
    }
}

class GeneradorFacturas {
    public void generar(String cliente, List<String> productos, List<Integer> cantidades, 
                        List<Double> precios, double subtotal, double desc, double imp, double total) {
        try (FileWriter writer = new FileWriter("factura_" + cliente + ".txt")) {
            writer.write("FACTURA\nCliente: " + cliente + "\n");
            for (int i = 0; i < productos.size(); i++) {
                writer.write(productos.get(i) + " x" + cantidades.get(i) + " = $" + (precios.get(i) * cantidades.get(i)) + "\n");
            }
            writer.write("Subtotal: $" + subtotal + "\nDescuento: $" + desc + "\nImpuesto: $" + imp + "\nTOTAL: $" + total + "\n");
        } catch (IOException e) {
            System.out.println("Error Archivo: " + e.getMessage());
        }
    }
}