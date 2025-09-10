import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JavaParallel_2 {
    public static void main(String[] args) {
        try{
            List<Integer> MyList= Arrays.asList(2,3,5);
            MyList.stream()
                    .map(Number -> CompletableFuture.supplyAsync(() -> Number * 2))
                    .map(CompletableFuture -> CompletableFuture.thenApply(N -> N * N))
                    .map(T -> T.join())
                    .forEach(S -> System.out.println(S));
        }catch(Exception e){}
    }
    private static int GetNumber(int a){
        return a+a;
    }
}
