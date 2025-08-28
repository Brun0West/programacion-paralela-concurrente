# Guía de Laboratorio N° 01

| | |
|---|---|
| **Escuela**: Ingeniería de Software | **Asignatura**: PROGRAMACIÓN CONCURRENTE Y PARALELA |
| **Semestre Académico**: 2025-II | |

# Procesamiento secuencial y paralelo de la multiplicación de matrices

## Tabla de contenido

- Definición de la multiplicación de matrices
- Versión secuencial en Python
- Versión secuencial en C
- Versión paralela de la multiplicación de matrices

## Definición de la multiplicación de matrices

Sean *A* y *B* dos matrices de dimensiones *m×m*. La multiplicación de estas dos matrices da como resultado la matriz *C* de dimensiones *m×m* y donde:

\[ c[i,j] = \sum_{k=1}^{m} a[i,k] \cdot b[k,j], \quad \forall i,j \in [1\ldots m]. \]

En otras palabras, la posición *c[i,j]* es el resultado de multiplicar la fila *i* de *A* por la columna *j* de *B*.

Gráficamente, la multiplicación de matrices se puede representar como:

![Multiplicación de matrices](./media/image1.png)

Las siguientes implementaciones en Python y C se realizaron en un entorno Linux.

## Versión en Python

A continuación, se muestra la implementación de la multiplicación de matrices en Python. El código consta de las siguientes partes:

- **Creación de variables**: Se definen *MAX*, *a*, *b* y *c*. Estas tres últimas son matrices de dimensión *MAX×MAX*.
- **Inicialización de matrices A y B**
- **Cálculo del producto de A x B**
- **Impresión de la matriz C = A x B**

```bash
$ nano multimat.py
```

```python
import numpy as np

## Creación de variables
MAX = 500
a = np.arange(MAX * MAX).reshape(MAX, MAX)
b = np.arange(MAX * MAX).reshape(MAX, MAX)
c = np.arange(MAX * MAX).reshape(MAX, MAX)

## Inicialización de las matrices. Observe que A será la matriz identidad
for i in range(0, MAX):
    for j in range(0, MAX):
        a[i][j] = 0
        b[i][j] = 100 + i
    a[i][i] = 1

## Multiplicación efectivamente de las matrices
for i in range(0, MAX):
    for j in range(0, MAX):
        c[i][j] = 0
        for k in range(0, MAX):
            c[i][j] = c[i][j] + a[i][k] * b[k][j]

## Impresión de la matriz (C) resultado de multiplicar A x B
for i in range(0, MAX):
    for j in range(0, MAX):
        print(f"{c[i][j]}  ", end="")
    print()
```

Ejecutamos el código midiendo su tiempo de ejecución:

```bash
$ time python3 multimat.py
```

```
...
599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599  599

real    1m56.766s
user    1m56.445s
sys     0m0.135s
```

## Versión en C

A continuación, se muestra el código en C de la multiplicación de matrices. El código tiene una estructura similar al de Python (definición de variables, inicialización, multiplicación y visualización), pero con la sintaxis del lenguaje C.

```bash
$ nano multimat.c
```

```c
#include <stdio.h>
#define MAX 500

int main() {
  /** Definición de las matrices: a, b, y c. */
  int a[MAX][MAX], b[MAX][MAX], c[MAX][MAX];
  int i, j, k;

  /** Inicialización de las matrices a y b. a será la matriz identidad. */
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++) {
      a[i][j] = 0;
      b[i][j] = 100 + i;
    }
    a[i][i] = 1;
  }

  /** Multiplicación de las matrices */
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++) {
      c[i][j] = 0;
      for (k = 0; k < MAX; k++) {
        c[i][j] += a[i][k] * b[k][j];
      }
    }
  }

  /** Impresión de la matriz C */
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++)
      printf("%5d ", c[i][j]);
    printf("\n");
  }

  return 0;
}
```

Se compila y ejecuta, midiendo su tiempo de ejecución:

```bash
$ gcc multimat.c -o multimat && time ./multimat
```

```
...
599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599 ...

real    0m0.490s
user    0m0.413s
sys     0m0.012s
```

### Pregunta de reflexión: ¿Por qué cree que C es más rápido que Python?

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

### Ejercicio: Implemente el mismo programa en Java y obtenga su tiempo de ejecución. Reflexione acerca de los resultados.

___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

## Versión paralela de la multiplicación de matrices

Ahora implementaremos la multiplicación con programación paralela. Presentaremos una versión en C y ustedes investigarán una versión paralela en Python.

### Versión paralela en C

Se utilizará la API OpenMP, que será desarrollada en el curso. A continuación, se muestra el código con cambios mínimos:

```bash
$ nano multimat_parallel.c
```

```c
#include <stdio.h>
#include <omp.h>
#define MAX 500

int main() {
  /** Definición de las matrices: a, b, y c. */
  int a[MAX][MAX], b[MAX][MAX], c[MAX][MAX];
  int i, j, k, tid, nthreads;

  /** Inicialización de las matrices a y b. a será la matriz identidad. */
  #pragma omp parallel shared(a, b, c, nthreads) private(tid, i, j, k)
  {
    tid = omp_get_thread_num();
    #pragma omp for
    for (i = 0; i < MAX; i++) {
      for (j = 0; j < MAX; j++) {
        a[i][j] = 0;
        b[i][j] = 100 + i;
      }
      a[i][i] = 1;
    }

    /** Multiplicación de las matrices */
    #pragma omp for
    for (i = 0; i < MAX; i++) {
      for (j = 0; j < MAX; j++) {
        c[i][j] = 0;
        for (k = 0; k < MAX; k++) {
          c[i][j] += a[i][k] * b[k][j];
        }
      }
    }
  }

  /** Impresión de la matriz C */
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++)
      printf("%5d ", c[i][j]);
    printf("\n");
  }

  return 0;
}
```

Se compila y ejecuta, midiendo su tiempo de ejecución:

```bash
$ gcc -fopenmp multimat_parallel.c -o multimat_parallel && time ./multimat_parallel
```

```
...
599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599   599

real    0m0.275s
user    0m0.629s
sys     0m0.014s
```

## Preguntas a desarrollar:

1. **¿Cuánto es la mejora del código paralelo versus el código secuencial o serial en el Lenguaje C?**

__________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

2. **¿Qué partes aún siguen siendo secuenciales en ambos códigos del Lenguaje C?**

__________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

3. **En Linux, mediante el programa htop, compruebe el uso de todos los núcleos del procesador (Adjunte imagen)**

4. **Investigue cómo se implementaría el código Python de forma paralela.**

5. **También implemente el programa paralelo en Lenguaje C y Python en un entorno Windows.**