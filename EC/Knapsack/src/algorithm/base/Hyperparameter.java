package algorithm.base;

public class Hyperparameter
{
    public enum Type
    {INT, DOUBLE}

    public String name;
    public double value;
    public double min;
    public double max;
    private Type type;


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
        if (type == Type.INT)
            value = (int) value;

        return new Hyperparameter(name, value, min, max, type);
    }

    public String toString()
    {
        return name + ": " + value;
    }
}
