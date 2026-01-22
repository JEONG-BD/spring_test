package com.example.sample;

public class SampleApplication {

    public static void main(String[] args) {

        CalculationRequest calculationRequest = new CalculatorRequestReader().reader();
        long answer = new Calculator().calculate(
                calculationRequest.getNum1(),
                calculationRequest.getOperator(),
                calculationRequest.getNum2());
        System.out.println("answer = " + answer);

    }
}
