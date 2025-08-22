import numpy as np
import time
# Creación de variables

inicio = time.perf_counter()

MAX = 500
a = np.arange(MAX * MAX).reshape(MAX, MAX)
b = np.arange(MAX * MAX).reshape(MAX, MAX)
c = np.arange(MAX * MAX).reshape(MAX, MAX)

# inicializacion de matrices

for i in range(0,MAX):
    for j in range(0, MAX):
        a[i][j] = 0
        b[i][j] = 100 + i
    a[i][i] = 1

#multiplicación efectiva de matrices

for i in range(0,MAX):
    for j in range(0, MAX):
        c[i][j] = 0 
        for k in range(0,MAX):
            c[i][j] += a[i][k] * b[k][j]

#Impresión de la matriz (C) resulstado de multiplicación A x B

for i in range(0, MAX):
    for j in range(0, MAX):
        print(f"{c[i][j]} ", end="")
    print()

    
fin = time.perf_counter()
tiempo_total = fin - inicio
minutos = int(tiempo_total // 60)
segundos = int(tiempo_total % 60)
milisegundos = int((tiempo_total * 1000) % 1000)
print(f"\nTiempo de ejecucion: {minutos} min {segundos} s {milisegundos} ms")
