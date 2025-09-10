import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaParallel {
    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> list = new ArrayList<>(Arrays.asList(intArray));

        System.out.println("Original List usando secuencialemente: " + list);

        list.stream().forEach(num -> System.out.print(num + " "));
        System.out.println();

        System.out.println("Parallel Stream Output:");
        list.parallelStream().forEach(num -> System.out.print(num + " "));
        System.out.println();
    }
}