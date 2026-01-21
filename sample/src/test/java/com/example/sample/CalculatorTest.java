package com.example.sample;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class CalculatorTest{

    @Test
    public void 덧셈_연산을_할_수_있다(){

        String operator = "+";
        long num1 = 2;
        long num2 = 1;

        Calculator calculator = new Calculator();
        long result = calculator.calculate(num1, operator, num2);

        assertEquals(3, result); // junit
        assertThat(result).isEqualTo(3); // assertj
    }

    @Test
    public void 뺄셈_연산을_할_수_있다(){

        String operator = "-";
        long num1 = 2;
        long num2 = 1;

        Calculator calculator = new Calculator();
        long result = calculator.calculate(num1, operator, num2);

        assertEquals(1, result); // junit
        assertThat(result).isEqualTo(1); // assertj
    }

    @Test
    public void 곱셈_연산을_할_수_있다(){

        String operator = "*";
        long num1 = 2;
        long num2 = 1;

        Calculator calculator = new Calculator();
        long result = calculator.calculate(num1, operator, num2);

        assertEquals(2, result); // junit
        assertThat(result).isEqualTo(2); // assertj
    }

    @Test
    public void 나눗셈_연산을_할_수_있다(){

        String operator = "/";
        long num1 = 2;
        long num2 = 1;

        Calculator calculator = new Calculator();
        long result = calculator.calculate(num1, operator, num2);

        assertEquals(2, result); // junit
        assertThat(result).isEqualTo(2); // assertj
    }

    @Test
    public void 잘못된_연산자가_요청으로_들어올_경우_실패한다(){

        String operator = "x";
        long num1 = 2;
        long num2 = 1;
        Calculator calculator = new Calculator();

        assertThrows(InvalidOperatorException.class,() -> {
            calculator.calculate(num1, operator, num2);
        });
    }
}
