#include <omp.h>
#include <stdio.h>

#define MAX 500

int main() {
  int a[MAX][MAX], b[MAX][MAX], c[MAX][MAX];
  int i, j, k;

  /**
   * Inicialización de las matrices a y b en paralelo.
   * La directiva 'omp parallel for' distribuye las iteraciones del bucle
   * entre los hilos disponibles.
   */
  #pragma omp parallel for private(i, j)
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++) {
      a[i][j] = 0;
      b[i][j] = 100 + i;
    }
    a[i][i] = 1;
  }

  /**
   * Multiplicación de las matrices en paralelo.
   * Este es el cálculo más pesado y el que más se beneficia de la paralelización.
   * Cada hilo calculará un subconjunto de las filas de la matriz resultante 'c'.
   */
  #pragma omp parallel for private(i, j, k)
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++) {
      c[i][j] = 0;
      for (k = 0; k < MAX; k++) {
        c[i][j] += a[i][k] * b[k][j];
      }
    }
  }

  /**
   * Impresión de la matriz C.
   * Esta parte se mantiene secuencial.
   */
  for (i = 0; i < MAX; i++) {
    for (j = 0; j < MAX; j++)
      printf("%5d ", c[i][j]);
    printf("\n");
  }
  return 0;
}