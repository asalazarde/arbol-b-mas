import java.util.ArrayList;
import java.util.List;

public class ArbolBMas {

    private final int orden;
    private NodoArbolBMas raiz;

    public ArbolBMas(int orden) {
        if (orden < 3) {
            throw new IllegalArgumentException("El orden debe ser de al menos 3.");
        }
        this.orden = orden;
        raiz = new NodoArbolBMas(true);
    }

    public void insertar(int clave) {
        insertar(clave, String.valueOf(clave));
    }

    public void insertar(int clave, String dato) {
        insertarRecursivo(raiz, clave, dato);

        if (raiz.getClaves().size() == orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(raiz);
            dividirHijo(nuevaRaiz, 0);
            raiz = nuevaRaiz;
        }
    }

    private void insertarRecursivo(NodoArbolBMas nodo, int clave, String dato) {
        int indice = posicionDespuesDe(nodo.getClaves(), clave);

        if (nodo.esHoja()) {
            nodo.getClaves().add(indice, clave);
            nodo.getDatos().add(indice, dato);
            return;
        }

        NodoArbolBMas hijo = nodo.getHijos().get(indice);
        insertarRecursivo(hijo, clave, dato);
        if (hijo.getClaves().size() == orden) {
            dividirHijo(nodo, indice);
        }
    }

    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        List<Integer> claves = nodoLleno.getClaves();
        int mitad = orden / 2;
        int separador = claves.get(mitad);

        if (nodoLleno.esHoja()) {
            nuevoNodo.setClaves(new ArrayList<>(claves.subList(mitad, claves.size())));
            List<String> datos = nodoLleno.getDatos().subList(mitad, claves.size());
            nuevoNodo.getDatos().addAll(datos);
            datos.clear();

            // Enlazo la hoja nueva con la que seguía antes de cambiar el enlace viejo.
            nuevoNodo.setSiguiente(nodoLleno.getSiguiente());
            nodoLleno.setSiguiente(nuevoNodo);
        } else {
            nuevoNodo.setClaves(new ArrayList<>(claves.subList(mitad + 1, claves.size())));
            List<NodoArbolBMas> hijos = nodoLleno.getHijos();
            nuevoNodo.setHijos(new ArrayList<>(hijos.subList(mitad + 1, hijos.size())));
            hijos.subList(mitad + 1, hijos.size()).clear();
        }

        claves.subList(mitad, claves.size()).clear();
        padre.getClaves().add(indice, separador);
        padre.getHijos().add(indice + 1, nuevoNodo);
    }

    public boolean buscar(int clave) {
        NodoArbolBMas hoja = buscarHoja(clave, false);
        return hoja.getClaves().contains(clave);
    }

    public boolean eliminar(int clave) {
        boolean eliminado = eliminarRecursivo(raiz, clave);
        if (!raiz.esHoja() && raiz.getHijos().size() == 1) {
            raiz = raiz.getHijos().get(0);
        }
        return eliminado;
    }

    private boolean eliminarRecursivo(NodoArbolBMas nodo, int clave) {
        int indice = posicionDespuesDe(nodo.getClaves(), clave);

        if (nodo.esHoja()) {
            int posicion = indice - 1;
            if (posicion < 0 || nodo.getClaves().get(posicion) != clave) {
                return false;
            }
            nodo.getClaves().remove(posicion);
            nodo.getDatos().remove(posicion);
            return true;
        }

        NodoArbolBMas hijo = nodo.getHijos().get(indice);
        if (!eliminarRecursivo(hijo, clave)) {
            return false;
        }

        if (ocupacion(hijo) < minimo(hijo)) {
            rebalancearHijo(nodo, indice);
        }
        actualizarSeparadores(nodo);
        return true;
    }

    private void rebalancearHijo(NodoArbolBMas padre, int indice) {
        List<NodoArbolBMas> hijos = padre.getHijos();
        NodoArbolBMas hijo = hijos.get(indice);
        NodoArbolBMas izquierdo = indice > 0 ? hijos.get(indice - 1) : null;
        NodoArbolBMas derecho = indice + 1 < hijos.size() ? hijos.get(indice + 1) : null;

        // Junto dos nodos solo cuando ninguno de los hermanos puede prestar una entrada.
        if (izquierdo != null && ocupacion(izquierdo) > minimo(izquierdo)) {
            moverEntrada(izquierdo, hijo, ocupacion(izquierdo) - 1, 0);
        } else if (derecho != null && ocupacion(derecho) > minimo(derecho)) {
            moverEntrada(derecho, hijo, 0, ocupacion(hijo));
        } else if (izquierdo != null) {
            fusionar(izquierdo, hijo);
            hijos.remove(indice);
        } else {
            fusionar(hijo, derecho);
            hijos.remove(indice + 1);
        }
    }

    private void moverEntrada(NodoArbolBMas origen, NodoArbolBMas destino,
                              int desde, int hasta) {
        if (origen.esHoja()) {
            destino.getClaves().add(hasta, origen.getClaves().remove(desde));
            destino.getDatos().add(hasta, origen.getDatos().remove(desde));
        } else {
            destino.getHijos().add(hasta, origen.getHijos().remove(desde));
            actualizarSeparadores(origen);
            actualizarSeparadores(destino);
        }
    }

    private void fusionar(NodoArbolBMas izquierdo, NodoArbolBMas derecho) {
        if (izquierdo.esHoja()) {
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());
        } else {
            izquierdo.getHijos().addAll(derecho.getHijos());
            actualizarSeparadores(izquierdo);
        }
    }

    private int minimo(NodoArbolBMas nodo) {
        if (nodo.esHoja()) {
            return orden / 2;
        }
        return orden - orden / 2;
    }

    private int ocupacion(NodoArbolBMas nodo) {
        return nodo.esHoja() ? nodo.getClaves().size() : nodo.getHijos().size();
    }

    private void actualizarSeparadores(NodoArbolBMas nodo) {
        nodo.getClaves().clear();

        // Al borrar un dato puede cambiar la primera clave de un hijo. De ahí saco el separador nuevo.
        for (int i = 1; i < nodo.getHijos().size(); i++) {
            NodoArbolBMas hijo = nodo.getHijos().get(i);
            while (!hijo.esHoja()) {
                hijo = hijo.getHijos().get(0);
            }
            nodo.getClaves().add(hijo.getClaves().get(0));
        }
    }

    public void recorrerRango(int claveInicial, int cantidad) {
        if (cantidad <= 0) {
            System.out.println("Escribí una cantidad mayor que cero.");
            return;
        }

        NodoArbolBMas hoja = buscarHoja(claveInicial, true);
        int mostrados = 0;
        while (hoja != null && mostrados < cantidad) {
            for (int i = 0; i < hoja.getClaves().size() && mostrados < cantidad; i++) {
                int clave = hoja.getClaves().get(i);
                if (clave >= claveInicial) {
                    System.out.println(clave + " -> " + hoja.getDatos().get(i));
                    mostrados++;
                }
            }
            hoja = hoja.getSiguiente();
        }

        if (mostrados == 0) {
            System.out.println("No hay registros desde esa clave.");
        }
    }

    private NodoArbolBMas buscarHoja(int clave, boolean desdePrimeraCoincidencia) {
        NodoArbolBMas nodo = raiz;
        while (!nodo.esHoja()) {
            List<Integer> claves = nodo.getClaves();
            int indice = 0;

            // Si la clave coincide con el separador, en el rango reviso primero por la izquierda por si hay repetidas.
            while (indice < claves.size() && (claves.get(indice) < clave
                    || (!desdePrimeraCoincidencia && claves.get(indice) == clave))) {
                indice++;
            }
            nodo = nodo.getHijos().get(indice);
        }
        return nodo;
    }

    private int posicionDespuesDe(List<Integer> claves, int clave) {
        int indice = claves.size();
        while (indice > 0 && clave < claves.get(indice - 1)) {
            indice--;
        }
        return indice;
    }

    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        String tipo = nodo.esHoja() ? "Hoja" : "Interno";
        System.out.println(indentacion + "+- " + tipo + " > " + nodo.getClaves());
        String sangriaHijos = indentacion + (esUltimo ? "   " : "|  ");
        for (int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), sangriaHijos, i == nodo.getHijos().size() - 1);
        }
    }
}
