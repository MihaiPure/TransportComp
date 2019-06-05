package TransportCompany;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Uprunner {

    private static void showOptions() {
        System.out.println("1 - show all vehicles");
        System.out.println("2 - show all products");
        System.out.println("3 - load product onto an available vehicle");
        System.out.println("4 - checks if a city can be serviced");
        System.out.println("5 - try to load product onto a specific vehicle");
        System.out.println("6 - sets the shortest route from a deposit to a city");
        System.out.println("7 - build new deposit");
        System.out.println();
        System.out.println("these operations will also alter the database (CSV files)");
        System.out.println("11 - add new product");
        System.out.println("12 - add new vehicle");
        System.out.println("13 - add new city");
        System.out.println();
        System.out.println("0 - exit");
        System.out.println("What do you want to do? Operation: ");
    }

    public static void main(String[] args) {
        TransportService service = TransportService.getInstance();
        service.initializeService();

        //For Demonstration Purposes
        //Should be removed for production
        service.addDeposit(0);

        SwingUI ui = new SwingUI(service);

        /*
        int op;
        Scanner scanner = new Scanner(System.in);
        showOptions();

        op = scanner.nextInt();
        while (op != 0) {
            switch (op) {
                case 1: {
                    service.showVehicles();
                    System.out.println();
                    break;
                }
                case 2: {
                    service.showProducts();
                    System.out.println();
                    break;
                }
                case 3: {
                    scanner.nextLine();

                    System.out.println("which kind of product? (feed id): ");
                    int prodId = scanner.nextInt();
                    System.out.println("amount: ");
                    int prodAmount = scanner.nextInt();
                    Vehicle loadedInto = service.loadAnyVehicle(prodId, prodAmount);
                    System.out.println("Successfully loaded into " + loadedInto);

                    service.addToLog("Loaded Vehicle " + loadedInto.getLicensePlate() + " with product with ID=" +
                            prodId + "(x" + prodAmount + ")");
                    break;
                }
                case 4: {
                    scanner.nextLine();

                    System.out.println("City name:");
                    String cityName = scanner.nextLine();
                    boolean answer = service.isInRange(cityName);
                    System.out.println(answer);
                    System.out.println();
                    service.addToLog("checked to see if " + cityName + " can be serviced. Result=" + answer);
                    break;
                }
                case 5: {
                    System.out.println("which product? (feed id): ");
                    int prodId = scanner.nextInt();
                    System.out.println("which vehicle? (feed id): ");
                    int vehicleId = scanner.nextInt();
                    System.out.println("amount: ");
                    int prodAmount = scanner.nextInt();
                    int err = service.loadSpecificVehicle(vehicleId, prodId, prodAmount);
                    if (err != 0) {
                        System.out.println("Warning! vehicle could not be loaded");
                    }
                    service.addToLog("Tried to load product with ID=" + prodId + "(x" + prodAmount + ")" +
                            "into Vehicle with ID=" + vehicleId + ". Operation " + (err == 0 ? "succeeded" : "failed"));
                    break;
                }
                case 6: {
                    scanner.nextLine();
                    System.out.println("From which deposit would you like the vehicles to depart? (feed id): ");
                    boolean ok = false, ans = false;
                    int id = 0;
                    while (!ok){
                        ok = true;
                        id = Integer.parseInt(scanner.nextLine());
                        if(!service.isDeposit(id)){
                            ok = false;
                            System.out.println("ERROR: city with said id is not a deposit or doesn't exist");
                        }
                    }
                    System.out.println("Which city would you like to be the destination? (feed id): ");
                    ok = false;
                    int otherId = 0;
                    while (!ok){
                        ok = true;
                        otherId = Integer.parseInt(scanner.nextLine());
                        if(otherId == id){
                            ok = false;
                            System.out.println("ERROR: can't set a route from and to the same city");
                        }
                        if(!service.isCity(otherId)){
                            ok = false;
                            System.out.println("ERROR: there's no city with that name");
                        }
                    }
                    Route route = service.configureNewRoute(id, otherId);
                    service.addToLog("Added new route from city with id=" + id + " to city with id" + otherId);
                    break;
                }
                case 7:{
                    System.out.println("In which city would you like to build the new deposit? (feed id): ");
                    int id = Integer.parseInt(scanner.nextLine());
                    service.addDeposit(id);
                    break;
                }
                case 11: {
                    service.handleNewProductInput(scanner);
                    break;
                }
                case 12: {
                    service.handleNewVehicleInput(scanner);
                    break;
                }
                case 13: {
                    service.handleCityAddition(scanner);
                    break;
                }
                default:
                    break;
            }
            showOptions();
            op = scanner.nextInt();
        }
        */
    }
}
