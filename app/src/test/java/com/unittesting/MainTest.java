package com.unittesting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainTest extends Main {
    @Test
    public void  twoPlusTwo(){
        MainTest mainTest = new MainTest();
        assertEquals(4, add(2,2));
    }


}