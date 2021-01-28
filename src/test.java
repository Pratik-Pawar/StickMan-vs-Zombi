
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author prati
 */
public class test {

    public static void main(String[] args) {
        int a[] = {12, 17, 22, 26, 31, 31, 46, 56, 58, 58, 58, 58, 51, 45, 39, 32, 25, 19, 12};
        for (int i = 0; i < a.length; i++) {
            int j = a[i];
            System.out.print((j - 12) + ",");
        }
    }
}
