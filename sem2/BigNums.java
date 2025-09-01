import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BigNums {
    SecureRandom sr = new SecureRandom();
    // Pool de hilos personalizado para usar todos los núcleos
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    private static List<BigInteger> factorization(BigInteger n) {
        List<BigInteger> factors = new ArrayList<>();

        // Factoriza por 2
        while (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            factors.add(BigInteger.TWO);
            n = n.divide(BigInteger.TWO);
        }

        // Factoriza por impares
        for (BigInteger i = BigInteger.valueOf(3);
             i.multiply(i).compareTo(n) <= 0;
             i = i.add(BigInteger.TWO)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                n = n.divide(i);
            }
        }

        // Si queda un factor primo mayor
        if (n.compareTo(BigInteger.TWO) > 0) {
            factors.add(n);
        }
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

    public List<BigInteger> listNum(int n, int k) {
        return sr.ints(n, 0, 10).mapToObj(i -> random(k)).toList();
    }

    public static void main(String[] args) {
        System.out.println("Núcleos disponibles: " + NUM_THREADS);

        int n = Integer.parseInt(args[0]); // Number of digits
        int k = Integer.parseInt(args[1]); // Number of numbers
        BigNums b = new BigNums();

        List<BigInteger> listNum = b.listNum(k, n);

        System.out.println("Generando " + k + " números de " + n + " dígitos...\n");
        listNum.forEach(S -> System.out.println("Numero: " + S + "\n"));

        long startTime = System.currentTimeMillis();

        // Opción 1: Usando ExecutorService personalizado
        System.out.println("=== USANDO EXECUTOR SERVICE PERSONALIZADO ===");
        List<CompletableFuture<List<BigInteger>>> futures = listNum.stream()
                .map(number -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("Factorizando " + number + " en hilo: " + Thread.currentThread().getName());
                    return factorization(number);
                }, executor))
                .toList();

        futures.stream()
                .map(CompletableFuture::join)
                .forEach(factors -> System.out.println("FACTORIZACION: " + factors + "\n"));

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo total: " + (endTime - startTime) + " ms");
        executor.shutdown();
        }

}