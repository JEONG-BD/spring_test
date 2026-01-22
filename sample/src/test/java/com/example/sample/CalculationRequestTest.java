package com.example.sample;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalculationRequestTest {

    @Test
    public void 유효한_숫자를_파싱할_수_있다(){
        //given
        String[] parts = new String[]{"1", "+", "3"};
        //when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        //then
        assertEquals(1, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperator());
        assertEquals(3, calculationRequest.getNum2());
    }

    @Test
    public void 세자리_숫자가_넘어가는_유효한_숫자를_파싱할_수_있다(){
        //given
        String[] parts = new String[]{"123", "+", "321"};
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        //then
        assertEquals(123, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperator());
        assertEquals(321, calculationRequest.getNum2());
    }

    @Test
    public void 문자열_길이가_유효하지_않으면_에러를_던진다(){
        //given
        String[] parts = new String[]{"5", "+-", ""};
        //when
        assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }

    @Test
    public void 유효하지_않은_연산자가_입력되면_에러를_던진다(){
        //given
        String[] parts = new String[]{"5", "+-", ""};
        //when
        assertThrows(InvalidOperatorException.class, () -> {
            new CalculationRequest(parts);
        });
    }
}