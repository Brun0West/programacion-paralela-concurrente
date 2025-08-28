import numpy as np

MAX = 500

# Inicialización de matrices
a = np.eye(MAX, dtype=int)              # matriz identidad
b = np.array([[100 + i for _ in range(MAX)] for i in range(MAX)], dtype=int)


# Multiplicación de matrices (usa BLAS en C paralelizado)
c = a @ b   # equivalente a np.dot(a, b)

# Impresión de la matriz C
for i in range(MAX):
    for j in range(MAX):
        print(f"{c[i][j]:5d}", end=" ")
    print()


