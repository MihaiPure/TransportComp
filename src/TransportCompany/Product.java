package TransportCompany;

public class Product {
    private int id;
    private String name;
    private double weight;
    private int amount;
    private boolean fragile;

    public Product() {
    }

    public Product(String name, int amount) {
        this.name = name;
        this.amount = amount;
        this.fragile = false;
    }

    public Product(String name, double weight, int amount) {
        this.name = name;
        this.weight = weight;
        this.amount = amount;
        this.fragile = false;
    }

    public Product(String name, double weight, int amount, boolean fragile) {
        this.name = name;
        this.weight = weight;
        this.amount = amount;
        this.fragile = fragile;
    }

    public Product(Product otherProduct) {
        this.name = otherProduct.name;
        this.weight = otherProduct.weight;
        this.amount = otherProduct.amount;
        this.fragile = otherProduct.fragile;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name +
                ", weight=" + weight +
                ", amount=" + amount +
                ", fragile=" + fragile;
    }
}
