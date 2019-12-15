package com.marcotte.blockhead.util;

public class Utils
{
    public static boolean almostEqual( double a, double b)
    {
        return almostEqual( a, b, 1E-7);
    }

    public static boolean almostEqual( double a, double b, double eps)
    {
        return Math.abs(a-b) < eps;
    }
}
