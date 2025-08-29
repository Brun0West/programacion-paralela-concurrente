import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BigNums {
    SecureRandom sr = new SecureRandom();
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


    public BigInteger random(int k){
        byte[] ba = new byte[k];
        ba[0] = (byte) (sr.nextInt(9)+49);
        for (int d = 1;d<k;d++){
            ba[d] = (byte) (sr.nextInt(10)+48);
        }
        return new BigInteger(new String(ba));
    }
    // Lista de n numeros aleatorios de k digitos
    public List<BigInteger> listNum(int n, int k){
        return sr.ints(n,0,10).mapToObj(i -> random(k)).toList();
    }
    public static void main(String[] args) {
        int n = 100; // Number of digits
        int k = 2;
        BigNums b = new BigNums();

        List<BigInteger> listNum = b.listNum(k, n);

        listNum.forEach(S -> System.out.println("Numero: " +S + "\n"));
        // Factorial of a large numbe
        listNum.stream()
                .map(Number -> CompletableFuture.supplyAsync(() -> factorization(Number)))
                .map(CompletableFuture -> CompletableFuture.thenApply(N -> N))
                .map(T -> T.join())
                .forEach(S -> System.out.println("FACTORIZACION: " +S + "\n"));
    }


}
