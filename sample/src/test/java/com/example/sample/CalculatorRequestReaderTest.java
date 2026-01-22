package com.example.sample;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

public class CalculatorRequestReaderTest {

    @Test
    public void System_in으로_데이터를_읽어들일_수_있다(){
        CalculatorRequestReader calculatorRequestReader = new CalculatorRequestReader();
        System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
        CalculationRequest result = calculatorRequestReader.reader();
        assertEquals(2, result.getNum1());
        assertEquals("+", result.getOperator());
        assertEquals(3, result.getNum2());
    }
}