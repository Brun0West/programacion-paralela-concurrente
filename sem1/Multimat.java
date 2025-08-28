public class Multimat {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        final int MAX = 500;
        int a[][] = new int[MAX][MAX];
        int b[][] = new int[MAX][MAX];
        int c[][] = new int[MAX][MAX];
        int i, j, k;
        for (i = 0; i < MAX; i++) {
            for (j = 0; j < MAX; j++) {
                a[i][j] = 0;
                b[i][j] = 100 + i;
            }
            a[i][i] = 1;
        }

        /**
         * Multiplicación de las matrices
         */
        for (i = 0; i < MAX; i++) {
            for (j = 0; j < MAX; j++) {
                c[i][j] = 0;
                for (k = 0; k < MAX; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        /**
         * Impresión de la matriz c
         */
        for (i = 0; i < MAX; i++) {
            for (j = 0; j < MAX; j++)
                System.out.printf("%5d ", c[i][j]);
            System.out.printf("\n");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}
