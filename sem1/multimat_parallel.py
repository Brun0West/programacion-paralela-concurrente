import multiprocessing as mp

MAX = 500

# Inicialización de matrices
a = [[0 for _ in range(MAX)] for _ in range(MAX)]
b = [[100 + i for _ in range(MAX)] for i in range(MAX)]
c = [[0 for _ in range(MAX)] for _ in range(MAX)]

for i in range(MAX):
    a[i][i] = 1  # matriz identidad


def multiply_row(i):
    """Multiplica la fila i de A por la matriz B."""
    row_result = [0] * MAX
    for j in range(MAX):
        s = 0
        for k in range(MAX):
            s += a[i][k] * b[k][j]
        row_result[j] = s
    return i, row_result


if __name__ == "__main__":
    with mp.Pool(processes=mp.cpu_count()) as pool:
        results = pool.map(multiply_row, range(MAX))

    # Insertar resultados en C en el orden correcto
    for i, row in results:
        c[i] = row

    # Impresión de la matriz C
    for i in range(MAX):
        for j in range(MAX):
            print(f"{c[i][j]:5d}", end=" ")
        print()
