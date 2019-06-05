package TransportCompany;

import java.util.ArrayList;

public class City {
    String name;
    int id;

    public City() {
    }

    public City(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

class Deposit extends City {
    private ArrayList<Product> stock;

    public Deposit() {
    }

    public Deposit(String name, int id) {
        super(name, id);
    }

    public Deposit(String name, int id, ArrayList<Product> stock) {
        super(name, id);
        this.stock = stock;
    }

    public Deposit(City city){
        name = city.name;
        id = city.id;
    }

    private int isInStock(Product prod) {
        for (int i = 0; i < stock.size(); i++) {
            if (prod.getName().equals(stock.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    void addStock(Product newStock) {
        int idx = this.isInStock(newStock);
        if (idx == -1) {
            stock.add(newStock);
        } else {
            stock.get(idx).addAmount(newStock.getAmount());
        }

    }
}