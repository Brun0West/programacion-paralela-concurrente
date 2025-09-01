import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class BigNum2 {
    SecureRandom sr = new SecureRandom();

    // Configurar el número de hilos para el parallel stream
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    private static List<BigInteger> factorization(BigInteger n) {
        List<BigInteger> factors = new ArrayList<>();
        BigInteger original = n;

        // Factoriza por 2
        while (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            factors.add(BigInteger.TWO);
            n = n.divide(BigInteger.TWO);
        }

        // Optimización: calcular sqrt una vez y actualizar cuando sea necesario
        BigInteger sqrt = sqrt(n);

        // Factoriza por impares
        for (BigInteger i = BigInteger.valueOf(3);
             i.compareTo(sqrt) <= 0;
             i = i.add(BigInteger.TWO)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                n = n.divide(i);
                sqrt = sqrt(n); // Recalcular sqrt después de encontrar un factor
            }
        }

        // Si queda un factor primo mayor
        if (n.compareTo(BigInteger.TWO) > 0) {
            factors.add(n);
        }

        // Debug: mostrar qué hilo está procesando
        System.out.println("Factorizando " + original + " en hilo: " + Thread.currentThread().getName());

        return factors;
    }

    // Metodo auxiliar para calcular raíz cuadrada de BigInteger
    private static BigInteger sqrt(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }

        BigInteger x = n;
        BigInteger y = n.add(BigInteger.ONE).divide(BigInteger.TWO);

        while (y.compareTo(x) < 0) {
            x = y;
            y = x.add(n.divide(x)).divide(BigInteger.TWO);
        }
        return x;
    }

    public BigInteger random(int k) {
        byte[] ba = new byte[k];
        ba[0] = (byte) (sr.nextInt(9) + 49);
        for (int d = 1; d < k; d++) {
            ba[d] = (byte) (sr.nextInt(10) + 48);
        }
        return new BigInteger(new String(ba));
    }

    // Lista de n numeros aleatorios de k digitos
    public List<BigInteger> listNum(int n, int k) {
        return sr.ints(n, 0, 10).mapToObj(i -> random(k)).toList();
    }

    public static void main(String[] args) {
        System.out.println("Núcleos disponibles: " + NUM_THREADS);
        System.out.println("Parallelism del ForkJoinPool común: " +
                ForkJoinPool.commonPool().getParallelism());

        int n = Integer.parseInt(args[0]); // Number of digits
        int k = Integer.parseInt(args[1]); // Number of numbers
        BigNum2 b = new BigNum2();

        List<BigInteger> listNum = b.listNum(k, n);

        System.out.println("\n=== PROCESAMIENTO SERIAL ===");
        long startTime = System.currentTimeMillis();

        listNum.forEach(S -> System.out.println("Numero: " + S));
        System.out.println();
        listNum.forEach(S -> System.out.println("FACTORIZACION: " + factorization(S) + "\n"));

        long serialTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo serial: " + serialTime + " ms\n");

        System.out.println("=== PROCESAMIENTO PARALELO (ForkJoinPool común) ===");
        startTime = System.currentTimeMillis();

        // Usar parallel stream con el pool común
        listNum.parallelStream()
                .map(number -> {
                    List<BigInteger> factors = factorization(number);
                    return "Numero: " + number + " -> FACTORIZACION: " + factors;
                })
                .forEach(System.out::println);

        long parallelTime = System.currentTimeMillis() - startTime;
        System.out.println("\nTiempo paralelo (pool común): " + parallelTime + " ms");
        System.out.println("Speedup: " + String.format("%.2f", (double)serialTime / parallelTime) + "x\n");

        System.out.println("=== PROCESAMIENTO PARALELO (ForkJoinPool personalizado) ===");

        // Crear un ForkJoinPool personalizado con todos los núcleos
        ForkJoinPool customThreadPool = new ForkJoinPool(NUM_THREADS);
        try {
            startTime = System.currentTimeMillis();

            customThreadPool.submit(() ->
                    listNum.parallelStream()
                            .map(number -> {
                                List<BigInteger> factors = factorization(number);
                                return "Numero: " + number + " -> FACTORIZACION: " + factors;
                            })
                            .forEach(System.out::println)
            ).get();

            long customParallelTime = System.currentTimeMillis() - startTime;
            System.out.println("\nTiempo paralelo (pool personalizado): " + customParallelTime + " ms");
            System.out.println("Speedup vs serial: " + String.format("%.2f", (double)serialTime / customParallelTime) + "x");
            System.out.println("Speedup vs pool común: " + String.format("%.2f", (double)parallelTime / customParallelTime) + "x");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customThreadPool.shutdown();
        }
    }
}