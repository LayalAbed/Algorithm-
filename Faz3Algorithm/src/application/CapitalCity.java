package application;

public class CapitalCity {
    private String name;
    private double latitude;
    private double longitude;

    public CapitalCity(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public double[] CityInMap(double mapWidth, double mapHeight) {
        double x = (longitude + 180) * (mapWidth / 360); 
        double y = (90 - latitude) * (mapHeight / 180); 
        return new double[]{x, y};
    }

    @Override
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }
    

}