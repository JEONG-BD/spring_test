package com.example.sample;

import java.util.Scanner;

public class CalculatorRequestReader {

    public CalculationRequest reader(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter two number and an operator (e.g 1+2):");
        String result = sc.nextLine();
        return new CalculationRequest(result.split(" "));
    }
}
