package TransportCompany;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.valueOf;

public class CityNetwork {
    private static final String fileName = "./data";
    private List<City> cities = new ArrayList<>();
    private int V;
    private int E;
    private ArrayList<ArrayList<Edge>> lst;
    private int[][] dist;
    private int[][] next;
    private static int INF = 999999999;

    private void readCitiesFromCSV(String fileName) {
        Path myPath = Paths.get(fileName + "/cities.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.US_ASCII)) {
            //First line should be ignored because it's the description of each CSV
            br.readLine();

            //reading vehicles 1 by 1
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");

                //process read data
                cities.add(new City(attributes[0], Integer.parseInt(attributes[1])));

                //read next line
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readRoadsFromCSV(String fileName) {
        Path myPath = Paths.get(fileName + "/cityNetwork.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.US_ASCII)) {
            //First line should be ignored because it's the description of each CSV
            br.readLine();

            //reading vehicles 1 by 1
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");

                //process read data
                addEdge(new Edge(Integer.parseInt(attributes[0]), Integer.parseInt(attributes[1]), Integer.parseInt(attributes[2])));

                //read next line
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected void readCityNetworkFromCSV(String fileName) {
        readCitiesFromCSV(fileName);
        V = cities.size();
        E = 0;
        lst = new ArrayList<ArrayList<Edge>>();
        for (int vertex = 0; vertex < V; vertex++) {
            lst.add(new ArrayList<Edge>());
        }
        readRoadsFromCSV(fileName);

    }

    @Nullable
    public City getCity(int id){
        for ( City city : cities){
            if(city.getId() == id){
                return city;
            }
        }
        return null;
    }

    private int addCity(String name){
        int id = V;
        cities.add(new City(name, V));
        V = cities.size();
        lst.add(new ArrayList<>());
        return id;
    }

    public boolean isCity(int id){
        for (City city : cities){
            if(city.getId() == id){
                return true;
            }
        }
        return false;
    }

    public CityNetwork(String fileName) {
        /*
         *   13 16
         *   0  2  140
         *   0  6  204
         *   0  9  108
         *   0  11 97
         *   1  7  118
         *   1  8  130
         *   2  3  226
         *   2  7  114
         *   2  9  105
         *   3  10 114
         *   3  12 165
         *   4  7  226
         *   4  8  144
         *   5  9  104
         *   7  9  118
         *   11 12 66
         * */
        readCityNetworkFromCSV(fileName);
    }

    public int getV() {
        return V;
    }

    public int getE() {
        return E;
    }

    public List<City> getCities() {
        return cities;
    }

    public ArrayList<ArrayList<Edge>> getLst() {
        return lst;
    }

    public void addEdge(Edge e) {
        int v = e.getFirstVertex();
        int w = e.getSecondVertex(v);
        lst.get(v).add(e);
        lst.get(w).add(e);
        E++;
    }

    private void DFSUtil(int v, boolean[] viz) {
        viz[v] = true;
        for (Edge e : lst.get(v)) {
            if (!viz[e.getSecondVertex(v)]) {
                DFSUtil(e.getSecondVertex(v), viz);
            }
        }
    }

    private boolean[] DFS(int v) {
        boolean[] viz = new boolean[V];
        DFSUtil(v, viz);
        return viz;
    }

    public Boolean isReachable(ArrayList<Deposit> deposits, City city) {
        int vertex = city.getId();
        if (vertex >= V) {
            return false;
        }
        for (Deposit deposit : deposits) {
            int index = deposit.getId();
            boolean[] marked = DFS(index);
            if (marked[vertex]) {
                return true;
            }
        }
        return false;
    }

    protected void floydWarshall() {
        /* Once the city network is read and processed, this will cache all optimal distances in the graph
         * (between any two cities) using the Floyd-Warshall algorithm (complexity O(V^3));
         *
         * This function  - stores distances in the dist[][] matrix and
         *                - offers the possibility of reconstructing paths using the next[][] matrix;
         *
         * It is supposed to pe called only once at the beginning of the program
         */

        //initializing distance matrix with inf
        dist = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = INF;
            }
        }
        //initializing next matrix with null
        next = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                next[i][j] = -1;
            }
        }

        //adding distances and next for base cases
        for (int i = 0; i < V; i++) {
            for (Edge e : lst.get(i)) {
                int u = e.getFirstVertex(), v = e.getSecondVertex(u);
                dist[u][v] = e.getWeight();
                next[u][v] = v;
            }
        }
        for (int i = 0; i < V; i++) {
            dist[i][i] = 0;
            next[i][i] = i;
        }

        //calculating distances between any 2 vertices in the graph
        int i, j, k;
        for (k = 0; k < V; k++) {
            for (i = 0; i < V; i++) {
                for (j = 0; j < V; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    ArrayList<City> path(int u, int v){
        if(next[u][v] == -1){
            return null;
        }
        ArrayList<City> reconstructed = new ArrayList<>();
        reconstructed.add(getCity(u));
        while (u != v){
            u = next[u][v];
            reconstructed.add(getCity(u));
        }
        return reconstructed;
    }

    String handleCityAddition(Scanner scanner){
        //garbage line
        scanner.nextLine();

        //predefine arguments
        String name = "";
        boolean ok = false;

        //handling input for city name
        while (!ok) {
            ok = true;
            System.out.println("New city name: ");
            name = scanner.nextLine();
            for (City existingCity : cities){
                if(existingCity.getName().toLowerCase().equals(name.toLowerCase())){
                    ok = false;
                    System.out.println("New city name can't be the same as existing city name");
                }
            }
        }
        int newId = addCity(name);

        System.out.println("Configure roads from this new city: ");
        System.out.println("cities - show all cities");
        System.out.println("add - add new road to another city");
        System.out.println("exit - exit road configuration mode");

        String op = scanner.nextLine();
        while (!op.equals("exit")){
            switch (op){
                case "cities":{
                    for (City city : cities){
                        System.out.println(city);
                    }
                    break;
                }
                case "add":{
                    ok = false;
                    int otherId = 0;
                    while (!ok) {
                        ok = true;
                        System.out.println("City to link to id: ");
                        otherId = Integer.parseInt(scanner.nextLine());
                        if (otherId == newId) {
                            ok = false;
                            System.out.println("ERROR: there can't be a road leading to and from the same city");
                        }
                        if(otherId < 0){
                            ok = false;
                            System.out.println("ERROR: id must be positive");
                        }
                        if(otherId >= V){
                            ok = false;
                            System.out.println("ERROR: id too big. no city with that id");
                        }
                    }
                    ok = false;
                    int weight = 0;
                    while(!ok){
                        ok = true;
                        System.out.println("distance between the two cities: ");
                        weight = Integer.parseInt(scanner.nextLine());
                        if(weight <= 0){
                            ok = false;
                            System.out.println("ERROR: distance between two cities can't be negative or zero");
                        }
                    }
                    addEdge(new Edge(newId, otherId, weight));
                    break;
                }
                default:{
                    System.out.println("Bad input (case sensitive)");
                    break;
                }
            }

            System.out.println("Configure roads from this new city: ");
            System.out.println("cities - show all cities");
            System.out.println("add - add new road to another city");
            System.out.println("exit - exit road configuration mode");
            op = scanner.nextLine();
        }
        handleCityGeneration(newId, name);
        return "Added " + name + " to the range of cities, with all associated roads (check .cityNetwork for more information)";
    }

    private void handleCityGeneration(int id, String name){
        try (FileWriter fw = new FileWriter(fileName + "/cities.csv", true)) {
            fw.append(name);
            fw.append(";");
            fw.append(valueOf(id));
            fw.append("\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try (FileWriter fw = new FileWriter(fileName + "/cities.csv", true)) {
            for (Edge e : lst.get(id)){
                fw.append(valueOf(e.getFirstVertex()));
                fw.append(";");
                fw.append(valueOf(e.getSecondVertex(e.getFirstVertex())));
                fw.append(";");
                fw.append(valueOf(e.getWeight()));
                fw.append("\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Iterable<Edge> lst(int v) {
        return lst.get(v);
    }

    @Override
    public String toString() {
        return "CityNetwork{" +
                "cities=" + cities +
                '}';
    }
}

class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final int weight;

    public Edge(int v, int w, int weight) {
        if (v < 0 || w < 0) {
            throw new IllegalArgumentException("vertex index out of bounds");
        }
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("Length of road is NaN");
        }
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int getFirstVertex() {
        return v;
    }

    public int getSecondVertex(int vertex) {
        if (vertex == v) {
            return w;
        } else if (vertex == w) {
            return v;
        } else throw new IllegalArgumentException("No such vertex in graph");
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return "{v=" + v +
                ", w=" + w +
                ", weight=" + weight +
                '}';
    }
}
