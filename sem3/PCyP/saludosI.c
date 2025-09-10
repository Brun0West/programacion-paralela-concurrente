#include <unistd.h>
#include <limits.h>
#include <stdio.h>
#include <mpi.h>

int main(int argc, char **argv) {
    int np, id;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &np);
    MPI_Comm_rank(MPI_COMM_WORLD, &id);
    char hostname[HOST_NAME_MAX + 1];
    gethostname(hostname, HOST_NAME_MAX + 1);
    if (id == 0) {
    	printf("Hola mundo soy MASTER %d de %d procesos en host %s\n", id, np, hostname);
    } else {
    	printf("Hola mundo mi id es %d en host %s\n", id, hostname);
    }
    MPI_Finalize();
    return 0;
}
