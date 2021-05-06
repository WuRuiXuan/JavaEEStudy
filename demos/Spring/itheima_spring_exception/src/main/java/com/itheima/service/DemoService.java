package com.itheima.service;

import com.itheima.exception.MyException;

import java.io.FileNotFoundException;

public interface DemoService {
    public void show1();

    public void show2();

    public void show3() throws FileNotFoundException;

    public void show4();

    public void show5() throws MyException;
}
