package codecheck;

import java.util.Scanner;

public class Main {
    public static void main(String... args) {

        // Just to see the format of input.
        System.out.println("TEST_PRINT----");
        System.out.println(args.length);

        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.nextLine());


        for (String str : args) {
            System.out.println(str);
        }

    }
}
