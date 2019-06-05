package TransportCompany;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

public class SwingUI {
    private static JFrame appFrame;
    private static JButton showVehicles;
    private static JButton showProducts;
    private static JButton loadProductOntoAnyVehicle;
    private static JButton loadProductOntoSpecificVehicle;
    private static JButton checkServiceability;
    private static JButton setShortestRoute;
    private static JButton buildDeposit;
    private static JButton addNewProduct;
    private static JButton addNewVehicle;
    private static JButton addNewCity;

    public SwingUI(TransportService ts){
        appFrame = new JFrame("Transport Service");
        appFrame.setSize(300, 370);

        showVehicles = new JButton("Show All Vehicles");
        showVehicles.setBounds(0, 0, 300, 30);

        showProducts = new JButton("Show All Products");
        showProducts.setBounds(0, 30, 300, 30);

        addNewProduct = new JButton("Add New Product");
        addNewProduct.setBounds(0, 60, 300, 30);

        addNewVehicle = new JButton("Add New Vehicle");
        addNewVehicle.setBounds(0, 90, 300, 30);

        addNewCity = new JButton("Add New City");
        addNewCity.setBounds(0, 120, 300, 30);

        buildDeposit = new JButton("Build a New Deposit");
        buildDeposit.setBounds(0, 150, 300, 30);

        checkServiceability = new JButton("Check if a City Can Be Serviced");
        checkServiceability.setBounds(0, 180, 300, 30);

        loadProductOntoAnyVehicle = new JButton("Load Product Onto Any Vehicle");
        loadProductOntoAnyVehicle.setBounds(0, 210, 300, 30);

        loadProductOntoSpecificVehicle = new JButton("Load Product Onto Specific Vehicle");
        loadProductOntoSpecificVehicle.setBounds(0, 240, 300, 30);

        setShortestRoute = new JButton("Set Shortest Route from a Deposit to a City");
        setShortestRoute.setBounds(0, 270, 300, 30);


        showVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame list = new JFrame("Vehicles");
                list.setSize(500, 600);
                list.setLayout(null);
                list.setVisible(true);

                ArrayList<Vehicle> vehicles = ts.getVehicles();
                int n = vehicles.size();
                String[] elements = new String[n];
                for(int i = 0; i<n;i++){
                    elements[i] = vehicles.get(i).toString();
                }
                JList listContent = new JList(elements);
                listContent.setBounds(0,0,500,400);

                list.add(listContent);
            }
        });

        showProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame list = new JFrame("Vehicles");
                list.setSize(500, 600);
                list.setLayout(null);
                list.setVisible(true);

                Set<Product> products = ts.getProducts();
                int n = products.size();
                String[] elements = new String[n];
                int i=0;
                for(Product p : products){
                    elements[i] = p.toString();
                    i++;
                }
                JList listContent = new JList(elements);
                listContent.setBounds(0,0,500,400);

                list.add(listContent);
            }
        });

        loadProductOntoAnyVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Load Product Onto Any Vehicle");
                container.setSize(300, 300);
                container.setLayout(null);
                container.setVisible(true);

                JLabel idLabel = new JLabel("Product ID: ");
                idLabel.setBounds(30, 0, 100, 30);
                JTextField idField = new JTextField();
                idField.setBounds(130, 0, 100, 30);


                JLabel amountLabel = new JLabel("Amount: ");
                amountLabel.setBounds(30, 30, 100, 30);
                JTextField amountField = new JTextField();
                amountField.setBounds(130, 30, 100, 30);

                JButton submit = new JButton("Load Vehicle");
                submit.setBounds(120, 80, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 140, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int id = Integer.parseInt(idField.getText());
                        int amount = Integer.parseInt(amountField.getText());

                        Vehicle v = ts.loadAnyVehicle(id, amount);
                        if(v == null){
                            System.out.println("Operation Failed");
                            result.setText("Something Went Wrong; Nothing Happened");
                        }
                        else{
                            System.out.println("Successfully Loaded onto" + v);
                            result.setText(v.toString());
                        }
                    }
                });

                container.add(result);
                container.add(idField);
                container.add(idLabel);
                container.add(amountField);
                container.add(amountLabel);
                container.add(submit);

            }
        });

        loadProductOntoSpecificVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Load Product Onto Specific Vehicle");
                container.setSize(300, 300);
                container.setLayout(null);
                container.setVisible(true);

                JLabel prodIdLabel = new JLabel("Product ID: ");
                prodIdLabel.setBounds(30, 0, 100, 30);
                JTextField prodIdField = new JTextField();
                prodIdField.setBounds(130, 0, 100, 30);


                JLabel amountLabel = new JLabel("Amount: ");
                amountLabel.setBounds(30, 30, 100, 30);
                JTextField amountField = new JTextField();
                amountField.setBounds(130, 30, 100, 30);

                JLabel vehIdLabel = new JLabel("Vehicle ID: ");
                vehIdLabel.setBounds(30, 60, 100, 30);
                JTextField vehIdField = new JTextField();
                vehIdField.setBounds(130, 60, 100, 30);

                JButton submit = new JButton("Load Vehicle");
                submit.setBounds(120, 110, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 140, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int prodId = Integer.parseInt(prodIdField.getText());
                        int amount = Integer.parseInt(amountField.getText());
                        int vehId = Integer.parseInt(vehIdField.getText());

                        int check = ts.loadSpecificVehicle(vehId,prodId, amount);
                        if(check == -1){
                            System.out.println("Operation Failed");
                            result.setText("Something Went Wrong; Nothing Happened");
                        }
                        else{
                            System.out.println("Operation Successful");
                            result.setText("Operation Successful");
                        }
                    }
                });

                container.add(result);
                container.add(prodIdField);
                container.add(prodIdLabel);
                container.add(amountField);
                container.add(amountLabel);
                container.add(vehIdField);
                container.add(vehIdLabel);
                container.add(submit);
            }
        });

        checkServiceability.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Check City Availability");
                container.setSize(300, 300);
                container.setLayout(null);
                container.setVisible(true);

                JLabel idLabel = new JLabel("City Name: ");
                idLabel.setBounds(30, 0, 100, 30);
                JTextField idField = new JTextField();
                idField.setBounds(130, 0, 100, 30);

                JButton submit = new JButton("Check");
                submit.setBounds(120, 50, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 140, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = idField.getText();

                        boolean check = ts.isInRange(name);
                        if(!check){
                            System.out.println("City Not In Range");
                            result.setText("City Not In Range");
                        }
                        else{
                            System.out.println("City In Range");
                            result.setText("City In Range");
                        }
                    }
                });

                container.add(result);
                container.add(idField);
                container.add(idLabel);
                container.add(submit);
            }
        });

        setShortestRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Set Shortest Path");
                container.setSize(300, 300);
                container.setLayout(null);
                container.setVisible(true);

                JLabel firstIdLabel = new JLabel("Deposit ID: ");
                firstIdLabel.setBounds(30, 0, 120, 30);
                JTextField firstIdField = new JTextField();
                firstIdField.setBounds(150, 0, 100, 30);

                JLabel secondIdLabel = new JLabel("Destination ID: ");
                secondIdLabel.setBounds(30, 30, 120, 30);
                JTextField secondIdField = new JTextField();
                secondIdField.setBounds(150, 30, 100, 30);

                JButton submit = new JButton("Calculate");
                submit.setBounds(140, 80, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 140, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int dep = Integer.parseInt(firstIdField.getText());
                        int dest = Integer.parseInt(secondIdField.getText());

                        Route route = ts.configureNewRoute(dep, dest);
                        if(route == null){
                            result.setText("A route could not be configured");
                        }else{
                            result.setText(route.toString());
                        }

                    }
                });

                container.add(result);
                container.add(firstIdField);
                container.add(firstIdLabel);
                container.add(secondIdField);
                container.add(secondIdLabel);
                container.add(submit);
            }
        });

        buildDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Build New Deposit in City");
                container.setSize(300, 300);
                container.setLayout(null);
                container.setVisible(true);

                JLabel idLabel = new JLabel("City ID: ");
                idLabel.setBounds(30, 0, 100, 30);
                JTextField idField = new JTextField();
                idField.setBounds(130, 0, 100, 30);

                JButton submit = new JButton("Build");
                submit.setBounds(120, 50, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 140, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int id = Integer.parseInt(idField.getText());

                        City dep = ts.addDeposit(id);
                        if(dep != null){
                            result.setText(dep.toString());
                        }
                        else {
                            result.setText("Could not find city with said ID");
                        }
                    }
                });

                container.add(result);
                container.add(idField);
                container.add(idLabel);
                container.add(submit);
            }
        });

        addNewProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Add New Product to Database: ");
                container.setSize(300, 300);
                container.setLayout(null);
                container.setVisible(true);

                JLabel nameLabel = new JLabel("Name: ");
                nameLabel.setBounds(30, 0, 120, 30);
                JTextField nameField = new JTextField();
                nameField.setBounds(150, 0, 100, 30);

                JLabel weightLabel = new JLabel("Weight: ");
                weightLabel.setBounds(30, 30, 120, 30);
                JTextField weightField = new JTextField();
                weightField.setBounds(150, 30, 100, 30);

                JLabel amountLabel = new JLabel("Amount: ");
                amountLabel.setBounds(30, 60, 120, 30);
                JTextField amountField = new JTextField();
                amountField.setBounds(150, 60, 100, 30);

                JLabel fragileLabel = new JLabel("Fragile: ");
                fragileLabel.setBounds(30, 90, 120, 30);
                String[] variants = {"true", "false"};
                JComboBox fragile = new JComboBox(variants);
                fragile.setBounds(150, 90, 100, 30);
                fragile.setSelectedIndex(0);

                JButton submit = new JButton("Add Product");
                submit.setBounds(120, 140, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 160, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        double weight = Double.parseDouble(weightField.getText());
                        int amount = Integer.parseInt(amountField.getText());
                        String isFragile = fragile.getSelectedItem().toString();
                        boolean isFrag = isFragile.equals("true");

                        Product newProd = ts.handleProductGeneration(name, weight, amount, isFrag);
                        if(newProd == null){
                            result.setText("Something went wrong; Nothing happened");
                        }
                        else {
                            result.setText(newProd.toString());
                        }
                    }
                });

                container.add(result);
                container.add(nameField);
                container.add(nameLabel);
                container.add(weightField);
                container.add(weightLabel);
                container.add(amountField);
                container.add(amountLabel);
                container.add(fragileLabel);
                container.add(fragile);
                container.add(submit);
            }
        });

        addNewVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Add New Vehicle to Database: ");
                container.setSize(300, 400);
                container.setLayout(null);
                container.setVisible(true);

                JLabel typeLabel = new JLabel("Type: ");
                typeLabel.setBounds(30, 0, 120, 30);
                String[] variants = {"Truck", "CargoVan"};
                JComboBox type = new JComboBox(variants);
                type.setBounds(150, 0, 100, 30);
                type.setSelectedIndex(0);

                JLabel plateLabel = new JLabel("License Plate: ");
                plateLabel.setBounds(30, 30, 120, 30);
                JTextField plateField = new JTextField();
                plateField.setBounds(150, 30, 100, 30);

                JLabel rangeLabel = new JLabel("Range: ");
                rangeLabel.setBounds(30, 60, 120, 30);
                JTextField rangeField = new JTextField();
                rangeField.setBounds(150, 60, 100, 30);

                JLabel speedLabel = new JLabel("Top Speed: ");
                speedLabel.setBounds(30, 90, 120, 30);
                JTextField speedField = new JTextField();
                speedField.setBounds(150, 90, 100, 30);

                JLabel capLabel = new JLabel("Cargo Capacity: ");
                capLabel.setBounds(30, 120, 120, 30);
                JTextField capField = new JTextField();
                capField.setBounds(150, 120, 100, 30);

                JLabel fragileLabel = new JLabel("Handles Fragile: ");
                fragileLabel.setBounds(30, 150, 120, 30);
                String[] fragVariants = {"true", "false"};
                JComboBox fragile = new JComboBox(fragVariants);
                fragile.setBounds(150, 150, 100, 30);
                fragile.setSelectedIndex(0);

                JLabel refLabel = new JLabel("Refrigerated: ");
                refLabel.setBounds(30, 180, 120, 30);
                String[] refVariants = {"true", "false"};
                JComboBox refrigerated = new JComboBox(refVariants);
                refrigerated.setBounds(150, 180, 100, 30);
                refrigerated.setSelectedIndex(0);

                JLabel tempLabel = new JLabel("Lowest Temp.: ");
                tempLabel.setBounds(30, 210, 120, 30);
                JTextField tempField = new JTextField();
                tempField.setBounds(150, 210, 100, 30);

                JButton submit = new JButton("Add Vehicle");
                submit.setBounds(120, 260, 110, 30);

                JLabel result = new JLabel();
                result.setBounds(0, 160, 300, 60);

                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String plate = plateLabel.getText();
                        int range = Integer.parseInt(rangeLabel.getText());
                        int cap = Integer.parseInt(capLabel.getText());
                        int speed = Integer.parseInt(speedLabel.getText());
                        if(type.getSelectedItem().toString().equals("Truck")){
                            boolean ref = Boolean.parseBoolean(refLabel.getText());
                            int temp = Integer.parseInt(tempLabel.getText());
                            ts.addVehicle(new Truck(plate, range, speed, cap, ref, temp));
                        }
                        else{
                            boolean frag = Boolean.parseBoolean(fragile.getSelectedItem().toString());
                            ts.addVehicle(new CargoVan(plate, range, speed, cap, frag));
                            result.setText("Vehicle added");
                        }
                    }
                });

                container.add(result);
                container.add(typeLabel);
                container.add(type);
                container.add(plateField);
                container.add(plateLabel);
                container.add(rangeField);
                container.add(rangeLabel);
                container.add(speedField);
                container.add(speedLabel);
                container.add(capLabel);
                container.add(capField);
                container.add(fragileLabel);
                container.add(fragile);
                container.add(refLabel);
                container.add(refrigerated);
                container.add(tempField);
                container.add(tempLabel);
                container.add(submit);
            }
        });

        addNewCity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame container = new JFrame("Add New City to Database: ");
                container.setSize(360, 200);
                container.setLayout(null);
                container.setVisible(true);

                JLabel message = new JLabel("City Network Cannot Be Modified in This Interface");
                message.setBounds(30, 0, 300, 30);

                container.add(message);
            }
        });


        appFrame.add(showVehicles);
        appFrame.add(showProducts);
        appFrame.add(addNewProduct);
        appFrame.add(addNewVehicle);
        appFrame.add(addNewCity);
        appFrame.add(loadProductOntoAnyVehicle);
        appFrame.add(loadProductOntoSpecificVehicle);
        appFrame.add(checkServiceability);
        appFrame.add(setShortestRoute);
        appFrame.add(buildDeposit);
        appFrame.add(addNewProduct);
        appFrame.add(addNewVehicle);
        appFrame.add(addNewCity);

        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        appFrame.setLayout(null);
        appFrame.setVisible(true);
    }
}
