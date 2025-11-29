package application;

public class CostTimeSouDes {
    private String source;
    private String destination;
    private double cost;
    private double time;

    public CostTimeSouDes(String source, String destination, double cost, double time) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getCost() {
        return cost;
    }

    public double getTime() {
        return time;
    }
}