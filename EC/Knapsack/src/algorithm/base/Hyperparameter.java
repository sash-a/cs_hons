package algorithm.base;

public class Hyperparameter
{
    public enum Type
    {INT, DOUBLE}

    public String name;
    public double value;
    public double min;
    public double max;
    public Type type;


    public Hyperparameter(String name, double value, double min, double max, Type type)
    {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.type = type;
    }

    public Hyperparameter change(double value)
    {
        value = Utils.clamp(value, min, max);
//        value = Utils.sig(value, min, max, min / 4, max / 4);
        if (type == Type.INT)
            value = (int) Math.round(value);

        return new Hyperparameter(name, value, min, max, type);
    }

    public String toString()
    {
        return name + ": " + value;
    }
}
