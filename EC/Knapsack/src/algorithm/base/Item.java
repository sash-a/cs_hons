package algorithm.base;

/*
    A gene in the case of GA
 */
public class Item
{
    public int id;
    public int weight;
    public int value;

    public Item(int id, int weight, int value)
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
