package code;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class TestDataBuilder {
    public static void main(String args[]) {
        Random r = new Random();
        String fileName = "testData.txt";
        String testData = "";
        int t;
        int n;
        int d;
        int numOfpurchase;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input minimum t: ");
        int t_min = scanner.nextInt();
        System.out.println("Input maximum t: ");
        int t_max = scanner.nextInt();

        System.out.println("Input minimum n: ");
        int n_min = scanner.nextInt();
        System.out.println("Input maximum n: ");
        int n_max = scanner.nextInt();

        System.out.println("Input minimum d: ");
        int d_min = scanner.nextInt();
        System.out.println("Input maximum d: ");
        int d_max = scanner.nextInt();

        System.out.println("Number of Test Data: ");
        numOfpurchase = scanner.nextInt();

        System.out.println("Your input: ");
        System.out.println(t_min + " <= t <= " + t_max);
        System.out.println(n_min + " <= n <= " + n_max);
        System.out.println(d_min + " <= d <= " + d_max);
        System.out.println("Number of Test Data: " + numOfpurchase);

        for (int i = 1; i <= numOfpurchase; i++) {
            t = r.nextInt(t_max + 1 - t_min) + t_min;
            testData += t + ",";
            n = r.nextInt(n_max + 1 - n_min) + n_min;
            testData += n + ",";
            d = r.nextInt(d_max + 1 - d_min) + d_min;
            testData += d + "\n";
        }

        try {
            FileWriter fw = new FileWriter("C:\\Users\\User\\Downloads\\AABCL-Group-Assignment-master\\" + fileName);
            fw.write(testData);
            fw.close();
        } catch (
                Exception e) {
            System.out.println(e);
        }
        System.out.println("Success");
    }
}