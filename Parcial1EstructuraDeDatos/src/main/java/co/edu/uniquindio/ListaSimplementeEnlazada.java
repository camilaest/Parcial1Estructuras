package co.edu.uniquindio;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaSimplementeEnlazada<T> implements Collection<T>, Iterable<T> {
    private Nodo inicio;
    private int tam;

    public ListaSimplementeEnlazada() {
        this.inicio = null;
        this.tam = 0;
    }

    /**
     * Para cumplir con la condición de que no se acceda directamente a los nodos se crea la clase privada Nodo dentro de la lista que lo recorre.
     * ?????????????
     */
    private class Nodo{
        private T dato;
        private Nodo siguiente;

        public Nodo(T dato) {
            this.dato = dato;
        }
    }

    /**
     * Método para agregar un elemento a la lista, crea un nuevo nodo con el dato ingresado.
     * Si la lista está vacía, asigna el nodo cómo el inicio, sino, lo agrega al final de la lista.
     * @param dato genérico
     */
    @Override
    public void agregar(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (estaVacia()) {
            inicio = nuevo;
        }
        else {
            Nodo aux = inicio;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
        tam++;
    }

    /**
     * Método que me permite eliminar un elemento de la lista sin importar su posición.
     * Si el elemento no exite dentro de la lista, lanza excepción
     * @param datoBuscar
     */
    @Override
    public void eliminar(T datoBuscar) {
        if (!buscar(datoBuscar)) {
           throw new NoSuchElementException("Elemento no existe dentro de la lista");
        }
        if(inicio.dato.equals(datoBuscar)) {
            inicio = inicio.siguiente;
            tam--;
            return;
        }
        Nodo aux = inicio;
        while (aux.siguiente != null && !aux.siguiente.dato.equals(datoBuscar)) {
            aux = aux.siguiente;
        }
        aux.siguiente = aux.siguiente.siguiente;
        tam--;
    }

    /**
     * Método que me devuelve el elemento según la posición ingresada
     * Verifica primero que la lista no esté vacía, si no lo está va iterando hasta encontrar el elemento
     * @param posicion
     * @return Elemento que está en la posición
     */
    @Override
    public T obtener(int posicion) {
        if (estaVacia()) {
            throw new NoSuchElementException("No hay elementos en la lista");
        }
        Nodo aux = inicio;
        int contador = 0;
        while (aux != null && contador <= posicion) {
            if(contador == posicion){
                return aux.dato;
            }
            contador++;
            aux = aux.siguiente;
        }
        return null;
    }

    /**
     * Método que devuelve la cantidad de elementos de la lista
     * @return tamaño
     */
    @Override
    public int cantidadElementos() {
        return tam;
    }

    /**
     * Método resultado de la implementación de Collection, que me permite determinar si la lista se encuentra vacía
     * @return boolean está vacía o no
     */
    @Override
    public boolean estaVacia() {
        if(inicio==null && tam==0){
            return true;
        }
        return false;
    }

    /**
     * Método con el cuál se determina si un elemento existe dentro de una lista.
     * Primero verifica que la lista no esté vacía, luego la recorre hasta finalizar o hasta encontrar el elemento
     * @param datoBuscar genérico
     * @return boolean el elemento se encuentra en la lista o no
     */
    public boolean buscar(T datoBuscar){
        if (estaVacia() ) {
            return false;
        }
        Nodo aux = inicio;
        do{
            if (aux.dato.equals(datoBuscar)){
                return true;
            }
            aux = aux.siguiente;
        } while (aux != inicio);

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListaSimplementeEnlazadaIterator();
    }

    /**
     * Clase privada que únicamente se implementa en la ListaSimplementeEnlazada
     */
    private class ListaSimplementeEnlazadaIterator implements Iterator<T> {
        private Nodo actual = inicio;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            T valor = actual.dato;
            actual = actual.siguiente;
            return valor;
        }
    }

    public Nodo getInicio() {
        return inicio;
    }

    public void setInicio(Nodo inicio) {
        this.inicio = inicio;
    }

    public int getTam() {
        return tam;
    }

    public void setTam(int tam) {
        this.tam = tam;
    }
}
