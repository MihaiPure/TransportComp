package TransportCompany;

import java.util.ArrayList;

public class Route {
    Deposit dep;
    City dest;
    ArrayList<City> stops;
    int avgSpeedOnRoute;

    public Route() {
    }

    public Route(Deposit dep, City dest, ArrayList<City> stops, int avgSpeedOnRoute) {
        this.dep = dep;
        this.dest = dest;
        //this.stops = stops;
        this.avgSpeedOnRoute = avgSpeedOnRoute;
    }

    public Route(Deposit dep, City dest){
        this.dep = dep;
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "Deposit=" + dep +
                ", Destination=" + dest +
                ", Stops=" + stops +
                ", avgSpeedOnRoute=" + avgSpeedOnRoute;
    }

    public Deposit getDep() {
        return dep;
    }

    public void setDep(Deposit dep) {
        this.dep = dep;
    }

    public City getDest() {
        return dest;
    }

    public void setDest(City dest) {
        this.dest = dest;
    }

    public ArrayList<City> getStops() {
        return stops;
    }

    public void setStops(ArrayList<City> stops) {
        this.stops = stops;
    }

    public int getAvgSpeedOnRoute() {
        return avgSpeedOnRoute;
    }

    public void setAvgSpeedOnRoute(int avgSpeedOnRoute) {
        this.avgSpeedOnRoute = avgSpeedOnRoute;
    }
}
