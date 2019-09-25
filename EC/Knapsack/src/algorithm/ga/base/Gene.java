package algorithm.ga.base;

public class Gene
{
    public int id;
    public int weight;
    public int value;

    public Gene(int id, int weight, int value)
    {
        this.id = id;
        this.weight = weight;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "id: " + this.id + " weight: " + this.weight + " value: " + this.value;
    }
}
