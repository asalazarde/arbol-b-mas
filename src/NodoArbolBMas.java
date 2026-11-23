import java.util.ArrayList;
import java.util.List;

public class NodoArbolBMas {

    private final boolean esHoja;
    private List<Integer> claves;
    private List<NodoArbolBMas> hijos;
    private final List<String> datos;
    private NodoArbolBMas siguiente;

    public NodoArbolBMas(boolean esHoja) {
        this.esHoja = esHoja;
        claves = new ArrayList<>();
        hijos = new ArrayList<>();

        // Guardo cada dato en el mismo índice que su clave para poder moverlos juntos.
        datos = esHoja ? new ArrayList<>() : null;
    }

    public boolean esHoja() {
        return esHoja;
    }

    public List<Integer> getClaves() {
        return claves;
    }

    public List<NodoArbolBMas> getHijos() {
        return hijos;
    }

    public List<String> getDatos() {
        return datos;
    }

    public NodoArbolBMas getSiguiente() {
        return siguiente;
    }

    public void setClaves(List<Integer> claves) {
        this.claves = claves;
    }

    public void setHijos(List<NodoArbolBMas> hijos) {
        this.hijos = hijos;
    }

    void setSiguiente(NodoArbolBMas siguiente) {
        this.siguiente = siguiente;
    }
}
