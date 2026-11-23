import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Uso el mismo orden que venía en el ejemplo del profe.
        ArbolBMas arbol = new ArbolBMas(4);
        try (Scanner entrada = new Scanner(System.in)) {
            menu(arbol, entrada);
        } catch (NoSuchElementException finDeEntrada) {
            System.out.println("\n¡Hasta luego!");
        }
    }

    private static void menu(ArbolBMas arbol, Scanner entrada) {
        int opcion;
        do {
            System.out.println("\nÁrbol B+");
            System.out.println("1. Agregar un registro");
            System.out.println("2. Buscar una clave");
            System.out.println("3. Eliminar un registro");
            System.out.println("4. Ver registros desde una clave");
            System.out.println("5. Ver el árbol");
            System.out.println("0. Salir");

            opcion = leerEntero(entrada, "Elegí una opción: ");
            switch (opcion) {
                case 1:
                    insertarRegistro(arbol, entrada);
                    break;
                case 2:
                    int claveBuscada = leerEntero(entrada, "¿Qué clave querés buscar? ");
                    System.out.println(arbol.buscar(claveBuscada)
                            ? "Encontré esa clave." : "No encontré esa clave.");
                    break;
                case 3:
                    int claveEliminada = leerEntero(entrada, "¿Qué clave querés eliminar? ");
                    System.out.println(arbol.eliminar(claveEliminada)
                            ? "Listo, se eliminó el registro." : "No encontré esa clave.");
                    break;
                case 4:
                    int inicio = leerEntero(entrada, "¿Desde qué clave querés empezar? ");
                    int cantidad;
                    do {
                        cantidad = leerEntero(entrada, "¿Cuántos registros querés ver? ");
                        if (cantidad <= 0) {
                            System.out.println("Escribí una cantidad mayor que cero.");
                        }
                    } while (cantidad <= 0);
                    arbol.recorrerRango(inicio, cantidad);
                    break;
                case 5:
                    arbol.imprimirArbol();
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Esa opción no está en el menú. Probá con una de la lista.");
            }
        } while (opcion != 0);
    }

    private static void insertarRegistro(ArbolBMas arbol, Scanner entrada) {
        int clave = leerEntero(entrada, "Clave (número entero): ");
        String dato;
        do {
            System.out.print("¿Qué dato querés guardar con esa clave? ");
            dato = entrada.nextLine().trim();
            if (dato.isEmpty()) {
                System.out.println("Escribí algo para poder guardar el registro.");
            }
        } while (dato.isEmpty());
        arbol.insertar(clave, dato);
        System.out.println("Listo, se guardó el registro.");
    }

    private static int leerEntero(Scanner entrada, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException error) {
                System.out.println("Escribí un número entero, por ejemplo 10.");
            }
        }
    }
}
