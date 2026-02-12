package app;
/**
Main launcher aplicacion reservas aulas.
* Inicializa Menu TUI con AWSClient local y ejecuta bucle interactivo.
* Manejo exceptions silencioso para UX consola robusta.
* @author Mario Garcia
* @author Mateo Ayarra
* @author Samuel Cobreros
* @author Zacaria Daghri
* @version 1.0
* @since 1.0
 */

public class Launch {
    
    /**
    Punto entrada principal.
    * Crea Menu() con DynamoDB Local y lanza start() interactivo.
    * @param args argumentos linea comandos (ignorados)
    */
    public static void main(String[] args) {
        try {
            Menu menu = new Menu();
            menu.start();
        } catch (Exception e) {
            // Exceptions silenciosas (UX consola): FileNotFound/IOException AWS
            System.err.println("Error inicializacion: " + e.getMessage());
        }
    }
}
