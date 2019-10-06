package algorithm.base;

public class Utils
{
    public static double sig(double n) { return 1 / (1 + Math.exp(-n)); }

    public static double clamp(double n, double min, double max) { return Math.min(Math.max(n, min), max); }
}
