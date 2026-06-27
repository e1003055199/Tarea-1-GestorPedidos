/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestorpedidos;
import java.util.*; 
import java.io.*; 
import java.sql.*;
/**
 *
 * @author ROGSTRIX
 */
public class GestorPedidos {
    
    private Connection conexionBD;
    //solid inversion de dependencias: inyeccion de dependencias
    // Constructor con credenciales hardcodeadas
    /*public GestorPedidos() { 
        try { 
            this.conexionBD = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "admin123"); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
    }*/
    
    // Constructor que recibe la conexión (inyección de dependencias)
    public GestorPedidos(Connection conexionBD) {
        this.conexionBD = conexionBD;
    }

   
    
    // Nuevo método privado que centraliza las validaciones
    //Eliminar duplicacion de codigo (DRY)
    private boolean validarCliente(String nombreCliente, String emailCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            System.out.println("Error: nombre de cliente invalido");
            return false;
        }
        if (emailCliente == null || !emailCliente.contains("@")) {
            System.out.println("Error: email invalido");
            return false;
        }
        return true;
    }
    
    // Nuevo método privado que calcula el descuento
    //Reemplaza el segmento de codigo Abierto/Cerrado - Extraer calculo de descuentos
    private double calcularDescuento(double subtotal, String tipoCliente) {
        switch (tipoCliente.toUpperCase()) {
            case "VIP":
                return subtotal * 0.20;
            case "FRECUENTE":
                return subtotal * 0.10;
            case "REGULAR":
                return subtotal * 0.05;
            case "NUEVO":
            default:
                return 0;
        }
    }
    
    // Nuevo método privado dedicado a generar la factura
    //solid: responsabilidad unica - separar generacion de factura
    private void generarFactura(String nombreCliente, List<String> nombresProductos,
            List<Integer> cantidades, List<Double> preciosProductos,
            double subtotal, double descuento, double impuesto, double total) {
        try {
            FileWriter writer = new FileWriter("factura_" + nombreCliente + ".txt");
            writer.write("FACTURA\n");
            writer.write("Cliente: " + nombreCliente + "\n");
            for (int i = 0; i < nombresProductos.size(); i++) {
                writer.write(nombresProductos.get(i) + " x" + cantidades.get(i)
                + " = $" + (preciosProductos.get(i) * cantidades.get(i)) + "\n");
            }
            writer.write("Subtotal: $" + subtotal + "\n");
            writer.write("Descuento: $" + descuento + "\n");
            writer.write("Impuesto: $" + impuesto + "\n");
            writer.write("TOTAL: $" + total + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error al generar la factura: " + e.getMessage());
        }
    }
    
    // Nuevo método privado para notificación
    //solid: responsabilidad unica - separar notificacion por correo
    private void enviarNotificacion(String email, String asunto, String cuerpo) {
        System.out.println("Enviando correo a " + email + "...");
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo: " + cuerpo);
    }
    
    // Nuevo método privado para guardar en BD (con PreparedStatement para seguridad)
    /*private void guardarPedidoEnBD(String nombreCliente, double total) {
        try {
            String sql = "INSERT INTO pedidos (cliente, total) VALUES (?, ?)";
            PreparedStatement pstmt = conexionBD.prepareStatement(sql);
            pstmt.setString(1, nombreCliente);
            pstmt.setDouble(2, total);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido: " + e.getMessage());
        }
    }*/
    
    public void procesarPedido(String nombreCliente, String emailCliente, 
                                  List<String> nombresProductos, 
                                  List<Double> preciosProductos, 
                                  List<Integer> cantidades, 
                                  String tipoCliente) { 
        
        // Las validaciones duplicadas en procesarPedido() y cancelarPedido()
        //Eliminar duplicacion de codigo (DRY)
        /*if (nombreCliente == null || nombreCliente.trim().isEmpty()) { 
            System.out.println("Error: nombre de cliente invalido"); 
            return; 
        } 
        if (emailCliente == null || !emailCliente.contains("@")) { 
            System.out.println("Error: email invalido"); 
            return; 
        }*/
        //Se reemplaza el ódigo anterior por esta llamda
        if (!validarCliente(nombreCliente, emailCliente)) {
            return;
        }
  
        double subtotal = 0; 
        for (int i = 0; i < nombresProductos.size(); i++) { 
            subtotal += preciosProductos.get(i) * cantidades.get(i); 
        } 
        
        //Abierto/Cerrado - Extraer calculo de descuentos
        /*double descuento = 0; 
        if (tipoCliente.equals("VIP")) { 
            descuento = subtotal * 0.20; 
        } else if (tipoCliente.equals("FRECUENTE")) { 
            descuento = subtotal * 0.10; 
        } else if (tipoCliente.equals("REGULAR")) { 
            descuento = subtotal * 0.05; 
        } else if (tipoCliente.equals("NUEVO")) { 
            descuento = 0; 
        }*/ 
        //Reemplazo de codigo anterior
        double descuento = calcularDescuento(subtotal, tipoCliente);
        
        double impuesto = (subtotal - descuento) * 0.12; 
        double total = subtotal - descuento + impuesto;
        
        //solid: responsabilidad unica - separar persistencia en bdd
        // El bloque try-catch con Statement en procesarPedido()
        /*try { 
            Statement stmt = conexionBD.createStatement(); 
            String sql = "INSERT INTO pedidos (cliente, total) VALUES ('" 
                         + nombreCliente + "', " + total + ")"; 
            stmt.executeUpdate(sql); 
        } catch (SQLException e) { 
            System.out.println("Error al guardar el pedido: " + e.getMessage()); 
        }*/
        
        //Se reemplaza el ódigo anterior por esta llamda
        //guardarPedidoEnBD(nombreCliente, total);
        
        //solid: responsabilidad unica - separar generacion de factura
        /*try { 
            FileWriter writer = new FileWriter("factura_" + nombreCliente + ".txt"); 
            writer.write("FACTURA\n"); 
            writer.write("Cliente: " + nombreCliente + "\n"); 
            for (int i = 0; i < nombresProductos.size(); i++) { 
                writer.write(nombresProductos.get(i) + " x" + cantidades.get(i) 
                             + " = $" + (preciosProductos.get(i) * cantidades.get(i)) + "\n"); 
            } 
            writer.write("Subtotal: $" + subtotal + "\n"); 
            writer.write("Descuento: $" + descuento + "\n"); 
            writer.write("Impuesto: $" + impuesto + "\n"); 
            writer.write("TOTAL: $" + total + "\n"); 
            writer.close(); 
        } catch (IOException e) { 
            System.out.println("Error al generar la factura: " + e.getMessage()); 
        } */
        //Se reemplaza el ódigo anterior por esta llamda
        generarFactura(nombreCliente, nombresProductos, cantidades, preciosProductos, 
               subtotal, descuento, impuesto, total);
        
        //solid: responsabilidad unica - separar notificacion por correo
        /*System.out.println("Enviando correo a " + emailCliente + "..."); 
        System.out.println("Asunto: Confirmacion de pedido"); 
        System.out.println("Cuerpo: Estimado " + nombreCliente + ", su pedido por $" 
                            + total + " ha sido procesado.");*/
        
        //Se reemplaza el ódigo anterior por esta llamda
        enviarNotificacion(emailCliente, "Confirmacion de pedido",
                "Estimado " + nombreCliente + ", su pedido por $" + total + " ha sido procesado.");
  
        System.out.println("[LOG] Pedido procesado para " + nombreCliente 
                            + " - Total: " + total); 
    }
    /*
    // Nuevo método privado para eliminar en BD (con PreparedStatement para seguridad)
    private void eliminarPedidoDeBD(int idPedido) {
        try {
            String sql = "DELETE FROM pedidos WHERE id = ?";
            PreparedStatement pstmt = conexionBD.prepareStatement(sql);
            pstmt.setInt(1, idPedido);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al cancelar el pedido: " + e.getMessage());
        }
    }*/
    
    public void cancelarPedido(String nombreCliente, String emailCliente, int idPedido) { 
        // Las validaciones duplicadas en procesarPedido() y cancelarPedido()
        /*if (nombreCliente == null || nombreCliente.trim().isEmpty()) { 
            System.out.println("Error: nombre de cliente invalido"); 
            return; 
        } 
        if (emailCliente == null || !emailCliente.contains("@")) { 
            System.out.println("Error: email invalido"); 
            return; 
        }*/
        //Se reemplaza el ódigo anterior por esta llamda
        if (!validarCliente(nombreCliente, emailCliente)) {
            return;
        }
        
        //solid: responsabilidad unica - separar persistencia en bdd
        /*try { 
            Statement stmt = conexionBD.createStatement(); 
            String sql = "DELETE FROM pedidos WHERE id = " + idPedido; 
            stmt.executeUpdate(sql); 
        } catch (SQLException e) { 
            System.out.println("Error al cancelar el pedido: " + e.getMessage()); 
        }*/
        
        // El bloque antiguo por:
        //eliminarPedidoDeBD(idPedido);

        //solid: responsabilidad unica - separar notificacion por correo
        /*
        System.out.println("Enviando correo a " + emailCliente + "..."); 
        System.out.println("Asunto: Cancelacion de pedido"); 
        System.out.println("Cuerpo: Estimado " + nombreCliente + ", su pedido #" 
                            + idPedido + " ha sido cancelado.");*/
        //Se reemplaza el ódigo anterior por esta llamda
        enviarNotificacion(emailCliente, "Cancelacion de pedido",
                "Estimado " + nombreCliente + ", su pedido #" + idPedido + " ha sido cancelado.");
    } 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
