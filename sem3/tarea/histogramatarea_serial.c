#include <stdio.h>
#include <stdlib.h>

int find_bin(double measurement, double* bin_maxes, int bin_count, double min_meas) {
    if (measurement < bin_maxes[0]) {
        return 0;  // Caso especial para bin 0: min_meas <= measurement < bin_maxes[0]
    }
    for (int b = 1; b < bin_count; b++) {
        if (bin_maxes[b - 1] <= measurement && measurement < bin_maxes[b]) {
            return b;
        }
    }
    return bin_count - 1;  // Último bin si measurement == max_meas
}

int main() {
    // Datos de ejemplo del documento
    double data[] = {1.3, 2.9, 0.4, 0.3, 1.3, 4.4, 1.7, 0.4, 3.2, 0.3, 4.9, 2.4, 3.1, 4.4, 3.9, 0.4, 4.2, 4.5, 4.9, 0.9};
    int data_count = 20;
    double min_meas = 0.0;
    double max_meas = 5.0;
    int bin_count = 5;

    double bin_width = (max_meas - min_meas) / bin_count;

    // Inicializar bin_maxes
    double* bin_maxes = (double*)malloc(bin_count * sizeof(double));
    for (int b = 0; b < bin_count; b++) {
        bin_maxes[b] = min_meas + (b + 1) * bin_width;
    }

    // Inicializar bin_counts a cero
    int* bin_counts = (int*)calloc(bin_count, sizeof(int));

    // Bucle principal: determinar bins y contar
    for (int i = 0; i < data_count; i++) {
        int b = find_bin(data[i], bin_maxes, bin_count, min_meas);
        bin_counts[b]++;
    }

    // Imprimir resultados (debería dar: 6 3 2 3 6)
    printf("Histograma serial:\n");
    for (int b = 0; b < bin_count; b++) {
        printf("Bin %d: %d\n", b, bin_counts[b]);
    }

    // Liberar memoria
    free(bin_maxes);
    free(bin_counts);

    return 0;
}
