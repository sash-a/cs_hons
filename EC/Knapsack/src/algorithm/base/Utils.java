package algorithm.base;

public class Utils
{
    public static double sig(double n) { return sig(n, 0, 1); }

    public static double sig(double n, double min, double max) { return (max - min) / (1 + Math.exp(-n)) + min; }

    public static double sig(double n, double min, double max, double lb, double ub)
    {
        double a = (lb + ub) / 2;
        double b = 2 / Math.abs(lb - ub);
        double c = min;
        double d = max - min;

        return d / (1 + Math.exp(b * (a - n))) + c;
    }


    public static double clamp(double n, double min, double max) { return Math.min(Math.max(n, min), max); }

    public static double squash(double n, double min, double max) { return (n - min) / (max - min); }
}
