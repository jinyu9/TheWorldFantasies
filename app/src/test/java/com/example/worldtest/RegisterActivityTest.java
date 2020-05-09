package com.example.worldtest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterActivityTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("开始测试");
    }
/*
    @Test
    public void onCreate() {
    }

    @Test
    public void onClick() {
    }
*/
    @Test
    public void isContainAll() {
        String test="Aa1";
        String test1="AAA";
        String test2="aaa";
        String test3="111";
        String test4="啊";
        String test5="!";
        String test6="";
        assertEquals(true,RegisterActivity.isContainAll(test));
        assertEquals(false,RegisterActivity.isContainAll(test1));
        assertEquals(false,RegisterActivity.isContainAll(test2));
        assertEquals(false,RegisterActivity.isContainAll(test3));
        assertEquals(false,RegisterActivity.isContainAll(test4));
        assertEquals(false,RegisterActivity.isContainAll(test5));
        assertEquals(false,RegisterActivity.isContainAll(test6));
        assertEquals(false,RegisterActivity.isContainAll(null));
    }

    @Test
    public void isNumber() {
        String test="123456";
        String test1="12345a";
        String test2="12345A";
        String test3="12345啊";
        String test4="12345!";
        String test5="";
        assertEquals(true,RegisterActivity.IsNumber(test));
        assertEquals(false,RegisterActivity.IsNumber(test1));
        assertEquals(false,RegisterActivity.IsNumber(test2));
        assertEquals(false,RegisterActivity.IsNumber(test3));
        assertEquals(false,RegisterActivity.IsNumber(test4));
        assertEquals(false,RegisterActivity.IsNumber(test5));
        assertEquals(false,RegisterActivity.IsNumber(null));
    }
}