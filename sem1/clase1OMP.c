#include <omp.h>
#include <stdio.h>

#define MAX 500

int main() {
  int a[MAX][MAX], b[MAX][MAX], c[MAX][MAX];
  int i, j, k, tid, nthreads;

#pragma omp parallel shared(a, b, c, nethreads) private(tid, i, j, k)
  {
    tid = omp_get_thread_num();

#pragma omp for

    for (i = 0; i < MAX; i++) {
      for (j = 0; j < MAX; j++) {
        a[i][j] = 0;
      }
    }
  }
  return 0;
}
