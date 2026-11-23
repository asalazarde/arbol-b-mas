# Árbol B+

Segunda práctica del curso Estructuras de Datos, Universidad CENFOTEC.

**Estudiante:** Aylin Salazar Delgado  
**Curso:** Estructuras de Datos

## Sobre el proyecto

El programa implementa un árbol B+ en Java y permite manejar registros desde un menú de consola. Cada registro tiene una clave entera y un dato de texto. El árbol del menú es de orden 4.

Las opciones disponibles son:

- Agregar un registro.
- Buscar una clave.
- Eliminar un registro.
- Mostrar registros a partir de una clave, indicando cuántos se quieren ver.
- Ver la estructura del árbol.

## Organización

- `Main.java`: contiene el menú y la lectura de los datos ingresados.
- `ArbolBMas.java`: contiene las operaciones del árbol, las divisiones y el rebalanceo.
- `NodoArbolBMas.java`: representa los nodos y guarda las claves, los hijos, los datos y el enlace a la siguiente hoja.

Los datos se guardan en las hojas. Los nodos internos usan las claves para dirigir la búsqueda. Al eliminar, se revisa la ocupación del nodo y se toma una entrada de un hermano o se fusionan los nodos cuando hace falta. El recorrido por rango aprovecha los enlaces entre hojas para seguir mostrando los registros en orden.

## Cómo ejecutarlo

Se necesita un JDK de Java 8 o superior. No utiliza librerías externas.

En IntelliJ IDEA, abrir la carpeta del proyecto, seleccionar el JDK y ejecutar `Main.java` desde `src`.

También se puede ejecutar desde una terminal, ubicada en la carpeta del proyecto:

```sh
javac -encoding UTF-8 -d out src/Main.java src/ArbolBMas.java src/NodoArbolBMas.java
java -cp out Main
```

## Ejemplo de uso

Agregar los registros `10 -> Ana`, `20 -> Luis` y `30 -> Sofía`. Luego elegir la opción 4, empezar desde la clave `15` y pedir `2` registros. Se mostrarán:

```text
20 -> Luis
30 -> Sofía
```

El programa permite claves repetidas; cada eliminación quita una sola coincidencia. Los registros se mantienen en memoria durante la ejecución y se pierden al cerrar el programa.
