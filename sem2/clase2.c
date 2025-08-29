#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

int sum;                   /* los hilos comparten esta variable */
void *runner(void *param); /* los hilos llaman a esta funcion */

int main(int argc, char *argv[]) {
  pthread_t tid;       /* el identificador del hilo */
  pthread_attr_t attr; /* conjunto de atributos del hilo */

  /* configura los atributos del hilo por defecto */
  pthread_attr_init(&attr);

  /* crea el hilo */
  pthread_create(&tid, &attr, runner, argv[1]);

  /* esperar que el hilo termine */
  pthread_join(tid, NULL);

  printf("sum = %d", sum);
}

/* El hilo inicia su ejecución en esta funcion */
void *runner(void *param) {
  int i, upper = atoi(param);
  sum = 0;
  for (i = 1; i <= upper; i++)
    sum += i;
  pthread_exit(0);
}
