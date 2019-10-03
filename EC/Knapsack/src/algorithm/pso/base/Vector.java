package algorithm.pso.base;

import algorithm.base.Representation;
import main.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vector
{
    private int x, y, z;
    private int xlim, ylim, zlim;

    private void init()
    {
        double third = Configuration.instance.numberOfItems / 3.0;

        if (Configuration.instance.numberOfItems % 3 == 0)
            xlim = ylim = zlim = (int) Math.pow(2, third);
        else
        {
            xlim = (int) Math.pow(2, Math.floor(third));
            ylim = (int) Math.pow(2, Math.round(third));
            zlim = (int) Math.pow(2, Math.ceil(third));
        }
    }

    public Vector(int xlim, int ylim, int zlim)
    {
        this.xlim = xlim;
        this.ylim = ylim;
        this.zlim = zlim;

        x = Configuration.instance.randomGenerator.nextInt(xlim);
        y = Configuration.instance.randomGenerator.nextInt(ylim);
        z = Configuration.instance.randomGenerator.nextInt(zlim);
    }

    public Vector(Vector other)
    {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;

        this.xlim = other.xlim;
        this.ylim = other.ylim;
        this.zlim = other.zlim;
    }

    public Vector(List<Boolean> rep)
    {
        init();

        double third = Configuration.instance.numberOfItems / 3.0;
        int numX = (int) Math.floor(third);
        int numY = (int) Math.round(third);
        int numZ = (int) Math.ceil(third);

        x = boolListToInt(rep.subList(0, numX));
        y = boolListToInt(rep.subList(numX, numX + numY));
        z = boolListToInt(rep.subList(numX + numY, numX + numY + numZ));
    }

    public static int boolListToInt(List<Boolean> lst)
    {
        int n = 0;
        for (Boolean bit : lst) n = (n << 1) + (bit ? 1 : 0);

        return n;
    }

    public static List<Boolean> intToBoolList(int num)
    {
        List<Boolean> bin = new ArrayList<>();
        while (num > 0)
        {
            bin.add(num % 2 != 0);
            num = Math.floorDiv(num, 2);
        }
        Collections.reverse(bin);
        return bin;
    }

    public void clamp()
    {
        x = Math.min(Math.max(x, 0), xlim);
        y = Math.min(Math.max(y, 0), ylim);
        z = Math.min(Math.max(z, 0), zlim);
    }

    public void wrap()
    {
        x %= xlim;
        y %= ylim;
        z %= zlim;
    }


    public Vector add(Vector other)
    {
        x = other.x + this.x;
        y = other.y + this.y;
        z = other.z + this.z;

        return this;
    }

    public Vector sub(Vector other)
    {
        x = other.x + this.x;
        y = other.y + this.y;
        z = other.z + this.z;

        return this;
    }

    public Vector mul(double n)
    {
        x = (int) (n * x);
        y = (int) (n * y);
        z = (int) (n * z);

        return this;
    }

    public double mag()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector normalize()
    {
        double mag = mag();

        x /= mag;
        y /= mag;
        z /= mag;

        return this;
    }

    public List<Boolean> toBoolList()
    {
        List<Boolean> rep = new ArrayList<>();
        rep.addAll(intToBoolList(x));
        rep.addAll(intToBoolList(y));
        rep.addAll(intToBoolList(z));

        return rep;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getZ() { return z; }
}
