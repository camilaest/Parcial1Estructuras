package co.edu.uniquindio;

public interface Collection<T> {
    public void agregar(T t);

    public void eliminar(T t);

    public T obtener(int posicion);

    public int cantidadElementos();

    public boolean estaVacia();
}
