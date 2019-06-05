package TransportCompany;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static java.lang.String.valueOf;

public class TransportService {
    private static final String fileName = "./data";
    private ArrayList<Vehicle> vehicles = new ArrayList<>(0);
    private ArrayList<Deposit> deposits = new ArrayList<>(0);
    private CityNetwork cityNetwork = new CityNetwork(fileName);
    private ArrayList<Route> routes = new ArrayList<>(0);
    private Set<Product> products = new HashSet<>();


    private static final TransportService service = new TransportService();

    public static TransportService getInstance() {
        return service;
    }

    public void initializeService() {
        readProductsFromCSV();
        readProductsFromDB();
        readVehiclesFromCSV();
        readCityNetworkFromCSV();
        determineCityOptimalDistances();
        //readDepositsFromCSV();
    }

    private void readCityNetworkFromCSV() {
        cityNetwork.readCityNetworkFromCSV(fileName);
    }

    private void readProductsFromDB(){
        String DB_URL = "jdbc:mysql://localhost:3306/myprettylittlecutieschema";
        String USERNAME = "root";
        String PASSWORD = "cisco12345";

        Connection con;
        try{
            con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sqlQuery = "select productName, Weight, amount, fragile from products";

            PreparedStatement readStatement = con.prepareStatement(sqlQuery);
            ResultSet resultSet = readStatement.executeQuery();
            while (resultSet.next()){
                String prodName = resultSet.getString("productName");
                double weight = resultSet.getDouble("weight");
                int amount = resultSet.getInt("amount");
                boolean fragile = resultSet.getBoolean("fragile");
                service.addProduct(new Product(prodName, weight, amount, fragile));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void readDepositsFromCSV(){
        Path myPath = Paths.get( fileName + "/deposits.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.US_ASCII)) {
            //First line should be ignored because it's the description of each CSV
            br.readLine();
            //reading products 1 by 1
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");
                //process read data
                deposits.add(new Deposit(attributes[0], Integer.parseInt(attributes[1])));

                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readProductsFromCSV() {
        Path myPath = Paths.get(fileName + "/products.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.US_ASCII)) {
            //First line should be ignored because it's the description of each CSV
            br.readLine();
            //reading products 1 by 1
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");
                //process read data
                boolean productIsFragile = attributes[3].equals("TRUE");
                service.addProduct(new Product(attributes[0], Double.valueOf(attributes[1] + "d"), Integer.parseInt(attributes[2]), productIsFragile));

                //add processed data into database

                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readVehiclesFromCSV() {
        Path myPath = Paths.get(fileName + "/vehicles.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.US_ASCII)) {
            //First line should be ignored because it's the description of each CSV
            br.readLine();

            //reading vehicles 1 by 1
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");

                //process read data
                if (attributes[0].equals("CargoVan")) {
                    boolean vehicleHandlesFragile = (attributes[5].equals("TRUE"));
                    //add processed data into database
                    vehicles.add(new CargoVan(attributes[1], Integer.parseInt(attributes[2]), Integer.parseInt(attributes[3]), Integer.parseInt(attributes[4]), vehicleHandlesFragile));
                } else if (attributes[0].equals("Truck")) {
                    boolean vehicleIsRefrigerated = (attributes[6].equals("TRUE"));
                    //add processed data into database
                    vehicles.add(new Truck(attributes[1], Integer.parseInt(attributes[2]), Integer.parseInt(attributes[3]), Integer.parseInt(attributes[4]), vehicleIsRefrigerated, Integer.parseInt(attributes[7])));
                }

                //read next line
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void determineCityOptimalDistances(){
        cityNetwork.floydWarshall();
    }

    public void addVehicle(Vehicle newVehicle) {
        vehicles.add(newVehicle);
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProduct(Product newProduct) {
        if (newProduct.getAmount() <= 0) {
            throw new ArithmeticException("amount of product cannot be a negatve number");
        }
        String newProductName = newProduct.getName().toLowerCase();
        for (Object product : products) {
            Product elem = (Product) product;
            if (elem.getName().toLowerCase().equals(newProductName)) {
                if ((elem.getWeight() != newProduct.getWeight()) || (elem.isFragile() != newProduct.isFragile())) {
                    System.out.println("Warning: tried to redefine attributes of already existing product");
                }
                elem.setAmount(elem.getAmount() + newProduct.getAmount());
                return;
            }
        }
        if (newProduct.getWeight() <= 0) {
            throw new ArithmeticException("New product cannot have weight less or equal to 0 (check constructor type)");
        }
        newProduct.setId(products.size());
        products.add(newProduct);

    }

    public Route configureNewRoute(Deposit deposit, City destination, ArrayList<City> stops, int avgSpeed) {
        Route newRoute = new Route();
        newRoute.setDep(deposit);
        newRoute.setDest(destination);
        //newRoute.setStops(stops);
        newRoute.setAvgSpeedOnRoute(avgSpeed);
        return newRoute;
    }

    public Route configureNewRoute(Deposit deposit, City destination){
        Route newRoute = new Route();
        newRoute.setDep(deposit);
        newRoute.setDest(destination);
        ArrayList<City> stops = cityNetwork.path(deposit.getId(), destination.getId());
        if(stops == null){
            return null;
        }
        else{
            stops.remove(0);
            stops.remove(stops.size()-1);
            newRoute.setStops(stops);
            return newRoute;
        }
    }

    public Route configureNewRoute(int depositId, int destinationId){
        Deposit deposit = getDeposit(depositId);
        City destination = cityNetwork.getCity(destinationId);
        if(deposit != null && destination != null){
            return configureNewRoute(deposit, destination);
        }
        else{
            return null;
        }
    }

    public void showVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    public Vehicle loadAnyVehicle(int id, int amount) {
        for (Product product : products) {
            if (product.getId() == id)
                return loadAnyVehicle(product, amount);
        }
        System.out.println("no such product");
        return null;
    }

    public int loadSpecificVehicle(int vehicleId, int productId, int amount) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == vehicleId) {
                for (Product product : products) {
                    if (product.getId() == productId) {
                        return loadSpecificVehicle(vehicle, product, amount);
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public int loadSpecificVehicle(Vehicle vehicle, Product product, int amount) {
        if (amount > product.getAmount() || amount > vehicle.getCurrentCapacity()) {
            return -1;
        }
        product.setAmount(product.getAmount() - amount);
        if ((product.isFragile() && vehicle instanceof CargoVan && ((CargoVan) vehicle).getHandlesFragile())
                || (!product.isFragile())) {
            Product moveProduct = new Product(product);
            moveProduct.setAmount(amount);
            vehicle.addLoad(moveProduct);
            return 0;
        }
        return -1;
    }

    public Vehicle loadAnyVehicle(Product product, int amount) {
        if (amount > product.getAmount()) {
            throw new IllegalArgumentException("amount set is higher than stock");
        }
        product.setAmount(product.getAmount() - amount);
        if (product.isFragile()) {
            for (Vehicle vehicle : vehicles) {
                if (vehicle instanceof CargoVan) {
                    if (((CargoVan) vehicle).getHandlesFragile()) {
                        if (vehicle.getCurrentCapacity() >= amount * product.getWeight()) {
                            Product moveProduct = new Product(product);
                            moveProduct.setAmount(amount);
                            vehicle.addLoad(moveProduct);
                            return vehicle;
                        }
                    }
                }
            }
            System.out.println("could not load any vehicle at this time");
            return null;
        } else {
            for (Vehicle vehicle : vehicles) {
                if (vehicle.getCurrentCapacity() >= amount * product.getWeight()) {
                    Product moveProduct = new Product(product);
                    moveProduct.setAmount(amount);
                    vehicle.addLoad(moveProduct);
                    return vehicle;
                }
            }
            System.out.println("could not load any vehicle at this time");
            return null;
        }
    }

    public void addDeposit(Deposit deposit) {
        deposits.add(deposit);
    }

    public boolean isCity(int id){
        return cityNetwork.isCity(id);
    }

    public boolean isDeposit(int id){
        for (Deposit dep : deposits){
            if(dep.getId() == id){
                return true;
            }
        }
        return false;
    }

    public Deposit getDeposit(int id){
        for(Deposit deposit : deposits){
            if(deposit.getId() == id){
                return deposit;
            }
        }
        return null;
    }

    public City addDeposit(int id){
        City c = cityNetwork.getCity(id);
        if(c == null){
            return null;
        }
        Deposit newDep = new Deposit(c);

        try (FileWriter fw = new FileWriter(fileName + "/deposits.csv", true)) {
            fw.append(newDep.getName());
            fw.append(";");
            fw.append(valueOf(newDep.getId()));
            fw.append("\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        deposits.add(newDep);
        return newDep;
    }

    public void showRoutes() {
        for (Route r : routes) {
            System.out.println(r);
        }
    }

    public void showProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public Boolean isInRange(City city) {
        return cityNetwork.isReachable(deposits, city);
    }

    public Boolean isInRange(String name) {
        System.out.println(name);
        for (City city : cityNetwork.getCities()) {
            if (city.getName().toLowerCase().equals(name.toLowerCase())) {
                return isInRange(city);
            }
        }
        return false;
    }

    public void handleNewProductInput(Scanner scanner) {
        //garbage line
        scanner.nextLine();

        //predefining arguments
        String name = "";
        int amount = 0;
        double weight = 0d;
        boolean fragile = false, ok = false;

        //handling input for product name
        while (!ok) {
            ok = true;
            System.out.println("Product name: ");
            name = scanner.nextLine();
            for (Product prod : products) {
                if (prod.getName().toLowerCase().equals(name.toLowerCase())) {
                    ok = false;
                    System.out.println("ERROR: new product can't have same name as existing product");
                }
            }
            if (name.equals("")) {
                ok = false;
                System.out.println("ERROR: name attribute can't be missing");
            }
        }

        //handling input for product amount
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Amount: ");
            amount = Integer.parseInt(scanner.nextLine());
            if (amount < 0) {
                ok = false;
                System.out.println("ERROR: can't have negative amount");
            }
        }

        //handling input for product weight
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Weight: ");
            weight = Double.parseDouble(scanner.nextLine() + "d");
            if (weight <= 0) {
                ok = false;
                System.out.println("ERROR: weight must be strictly positive");
            }
        }

        //handling input for product fragile property
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Is it fragile? (y/n): ");
            String answer = scanner.nextLine().toLowerCase();
            if (!(answer.equals("y") || answer.equals("n") || answer.equals("yes") || answer.equals("no"))) {
                ok = false;
                System.out.println("ERROR: please respect input value (y/n)");
            }
            fragile = (answer.equals("y") || answer.equals("yes"));
        }

        //processing input
        handleProductGeneration(name, weight, amount, fragile);
    }

    public Product handleProductGeneration(String name, double weight, int amount, boolean fragile) {
        Product newProd = new Product(name, weight, amount, fragile);
        products.add(newProd);

        try (FileWriter fw = new FileWriter(fileName + "/products.csv", true)) {
            fw.append(name);
            fw.append(";");
            fw.append(valueOf(weight));
            fw.append(";");
            fw.append(valueOf(amount));
            fw.append(";");
            fw.append(Boolean.toString(fragile).toUpperCase());
            fw.append("\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        service.addToLog("Added " + name + " to product library (x" + amount + ")");
        return newProd;
    }



    public void handleNewVehicleInput(Scanner scanner) {
        //garbage line
        scanner.nextLine();

        //predefine arguments
        String type = "";
        String licensePlate = "";
        int range = 0, topSpeed = 0, cargoCapacity = 0, lowestTemp = 0;
        boolean handlesFragile = false, refrigerated = false, ok = false;

        //handling input for vehicle type
        while (!ok) {
            ok = true;
            System.out.println("Vehicle type (CargoVan/Truck): ");
            type = scanner.nextLine();
            if (!type.toLowerCase().equals("cargovan") && !type.toLowerCase().equals("truck")) {
                ok = false;
                System.out.println("ERROR: please enter a valid type; (CargoVan/Truck)");
            }
        }

        //handling input for vehicle license plate
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Vehicle license plate: ");
            licensePlate = scanner.nextLine().toUpperCase();
            if (licensePlate.equals("")) {
                ok = false;
                System.out.println("ERROR: license plate field can't be missing");
            }
            if (licensePlate.length() > 20) {
                ok = false;
                System.out.println("ERROR: license plate can't be longer than 20 characters");
            }
        }

        //handling input for vehicle range
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Vehicle range: ");
            range = Integer.parseInt(scanner.nextLine());
            if (range <= 0) {
                ok = false;
                System.out.println("ERROR: vehicle can't have negative range");
            }
            if (range > 10000) {
                System.out.println("WARNING: vehicle range over 10000; might be typo");
            }
        }

        //handling input for vehicle top speed
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Vehicle top speed:");
            topSpeed = Integer.parseInt(scanner.nextLine());
            if (topSpeed <= 0) {
                ok = false;
                System.out.println("ERROR: can't have negative top speed");
            }
            if (topSpeed >= 500) {
                System.out.println("WARNING: top speed over 500km/h; might be typo");
            }
        }

        //handling input for vehicle cargo capacity
        ok = false;
        while (!ok) {
            ok = true;
            System.out.println("Vehicle cargo capacity: ");
            cargoCapacity = Integer.parseInt(scanner.nextLine());
            if (cargoCapacity <= 0) {
                ok = false;
                System.out.println("ERROR: cargo capacity can't be negative");
            }
        }

        if (type.toLowerCase().equals("cargovan")) {
            //handling input for fragile property
            ok = false;
            while (!ok) {
                ok = true;
                System.out.println("Does this vehicle handle fragile content? (y/n): ");
                String answer = scanner.nextLine();
                if (!(answer.equals("y") || answer.equals("n") || answer.equals("yes") || answer.equals("no"))) {
                    ok = false;
                    System.out.println("ERROR: please respect input value (y/n)");
                }
                handlesFragile = (answer.equals("y") || answer.equals("yes"));
            }
        } else if (type.toLowerCase().equals("truck")) {
            //handling input for refrigerated property
            ok = false;
            while (!ok) {
                ok = true;
                System.out.println("Is this vehicle refrigerated? (y/n): ");
                String answer = scanner.nextLine();
                if (!(answer.equals("y") || answer.equals("n") || answer.equals("yes") || answer.equals("no"))) {
                    ok = false;
                    System.out.println("ERROR: please respect input value (y/n)");
                }
                refrigerated = (answer.equals("y") || answer.equals("yes"));
            }

            //handling input for lowestTemp
            ok = false;
            while (!ok) {
                ok = true;
                System.out.println("This vehicle's cargo area's lowest temperature: ");
                lowestTemp = Integer.parseInt(scanner.nextLine());
                if (lowestTemp < -150 || lowestTemp > 100) {
                    ok = false;
                    System.out.println("ERROR: input out of bounds");
                }
            }
        }

        //processing input
        if (type.toLowerCase().equals("cargovan")) {
            handleCargoVanGeneration(licensePlate, range, topSpeed, cargoCapacity, handlesFragile);
        } else {
            handleTruckGeneration(licensePlate, range, topSpeed, cargoCapacity, refrigerated, lowestTemp);
        }
    }

    public void handleCargoVanGeneration(String licensePlate, int range, int topSpeed, int cargoCapacity, boolean handlesFragile) {
        vehicles.add(new CargoVan(licensePlate, range, topSpeed, cargoCapacity, handlesFragile));

        try (FileWriter fw = new FileWriter(fileName + "/vehicles.csv", true)) {
            fw.append("CargoVan");
            fw.append(";");
            fw.append(licensePlate);
            fw.append(";");
            fw.append(valueOf(range));
            fw.append(";");
            fw.append(valueOf(topSpeed));
            fw.append(";");
            fw.append(valueOf(cargoCapacity));
            fw.append(";");
            fw.append(Boolean.toString(handlesFragile).toUpperCase());
            fw.append(";");
            fw.append("-");
            fw.append(";");
            fw.append("-");
            fw.append("\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        service.addToLog("Added CargoVan " + licensePlate + " to vehicle garage");

    }

    public void handleTruckGeneration(String licensePlate, int range, int topSpeed, int cargoCapacity, boolean refrigerated, int lowestTemp) {
        vehicles.add(new Truck(licensePlate, range, topSpeed, cargoCapacity, refrigerated, lowestTemp));

        try (FileWriter fw = new FileWriter(fileName + "/vehicles.csv", true)) {
            fw.append("Truck");
            fw.append(";");
            fw.append(licensePlate);
            fw.append(";");
            fw.append(valueOf(range));
            fw.append(";");
            fw.append(valueOf(cargoCapacity));
            fw.append(";");
            fw.append(Boolean.toString(refrigerated).toUpperCase());
            fw.append(";");
            fw.append(valueOf(lowestTemp));
            fw.append(";");
            fw.append("-");
            fw.append(";");
            fw.append("-");
            fw.append("\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        service.addToLog("Added Truck " + licensePlate + " to vehicle garage");
    }

    public void handleCityAddition(Scanner scanner) {
        String logMessage = cityNetwork.handleCityAddition(scanner);
        addToLog(logMessage);
    }

    public void addToLog(String message){

        ActionLogger actionLogger = new ActionLogger(fileName, message);
        Thread t = new Thread(actionLogger);
        t.start();
    }
}
