package TransportCompany;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private int id;
    private String licensePlate;
    private int range;
    private int topSpeed;
    private int cargoCapacity;
    private int currentCapacity;
    private static int count = 0;
    private static final double gasPrice = 4.31;
    private List<Product> load = new ArrayList<>();

    @Override
    public String toString() {
        return  licensePlate +
                ", range=" + range +
                ", topSpeed=" + topSpeed;
    }

    public Vehicle() {
        this.id = count++;
    }

    public Vehicle(String licensePlate, int range, int topSpeed) {
        this.licensePlate = licensePlate;
        this.range = range;
        this.topSpeed = topSpeed;
        this.id = count++;
    }

    public Vehicle(String licensePlate, int range, int topSpeed, int cargoCapacity) {
        this.licensePlate = licensePlate;
        this.range = range;
        this.topSpeed = topSpeed;
        this.cargoCapacity = cargoCapacity;
        this.currentCapacity = cargoCapacity;
        this.id = count++;
    }

    public Vehicle(Vehicle otherVehicle) {
        this.licensePlate = otherVehicle.getLicensePlate();
        this.range = otherVehicle.getRange();
        this.topSpeed = otherVehicle.getTopSpeed();
        this.cargoCapacity = otherVehicle.getCargoCapacity();
        this.currentCapacity = otherVehicle.getCurrentCapacity();
        this.load = otherVehicle.load;
        this.id = count++;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(int avgSpeed) {
        this.topSpeed = avgSpeed;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public void setCargoCapacity(int cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public static double getGasPrice() {
        return gasPrice;
    }

    public void addLoad(Product product) {
        for (Product p : load) {
            if (p.getName().toLowerCase().equals(product.getName().toLowerCase())) {
                p.addAmount(product.getAmount());
                return;
            }
        }
        load.add(product);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class CargoVan extends Vehicle {
    private Boolean handlesFragile;

    public Boolean getHandlesFragile() {
        return handlesFragile;
    }

    public void setHandlesFragile(Boolean handlesFragile) {
        this.handlesFragile = handlesFragile;
    }

    @Override
    public String toString() {
        return  getLicensePlate() +
                ", range=" + getRange() +
                ", topSpeed=" + getTopSpeed() +
                ", handlesFragile=" + handlesFragile +
                ", CargoVan";
    }

    public CargoVan(String licensePlate, int range, int topSpeed, int cargoCapacity, Boolean handlesFragile) {
        super(licensePlate, range, topSpeed, cargoCapacity);
        this.handlesFragile = handlesFragile;
    }

    public CargoVan(CargoVan otherVan) {
        super(otherVan);
        this.handlesFragile = otherVan.handlesFragile;
    }

    public CargoVan() {
    }
}

class Truck extends Vehicle {
    private boolean refrigerated;
    private int lowestTemp;

    public Truck(String licensePlate, int range, int topSpeed, int cargoCapacity, boolean refrigerated, int lowestTemp) {
        super(licensePlate, range, topSpeed, cargoCapacity);
        this.refrigerated = refrigerated;
        this.lowestTemp = lowestTemp;
    }

    public boolean isRefrigerated() {
        return refrigerated;
    }

    @Override
    public String toString() {
        return  getLicensePlate() +
                ", range=" + getRange() +
                ", topSpeed=" + getTopSpeed() +
                ", refrigerated=" + refrigerated +
                ", lowestTemp=" + lowestTemp +
                ", Truck";
    }

    public void setRefrigerated(boolean refrigerated) {
        this.refrigerated = refrigerated;
    }

    public int getLowestTemp() {
        return lowestTemp;
    }

    public void setLowestTemp(int lowestTemp) {
        this.lowestTemp = lowestTemp;
    }
}