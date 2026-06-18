package model.tree;
/**
 *
 * @author Profesor Lic. Gilberth Chaves A.
 */
public interface Tree<T> {

    //devuelve el número de elementos en el árbol
    public int size() throws TreeException;

    //remueve todos los elementos del árbol
    public void clear();

    //true si el árbol está vacío
    public boolean isEmpty();

    //true si el elemento existe en el árbol
    public boolean contains(T element) throws TreeException;

    //inserta un elemento en el árbol
    public void add (T element);

    //suprime un elemento del árbol
    public void remove(T element) throws TreeException;

    //devuelve la altura de un nodo (el número de ancestros)
    public int height(T element) throws TreeException;

    //devuelve la altura del árbol
    public int height() throws TreeException;

    //devuelve el valor mínimo contenido en el árbol
    public T min() throws TreeException;

    //devuelve el valor máximo contenido en el árbol
    public T max() throws TreeException;

    //Pre Order Transversal Tour: N-L-R
    public String preOrder() throws TreeException;

    //In Order Transversal Tour: L-N-R
    public String inOrder() throws TreeException;

    //Post Order Transversal Tour: L-R-N
    public String postOrder() throws TreeException;

    //muestra por consola al altura de cada elemento del arbol
    public String nodeHeight() throws TreeException;

}
