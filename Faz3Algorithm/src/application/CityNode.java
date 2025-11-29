package application;



class CityNode {
    private String cityName;
    private double distance;

    public CityNode(String cityName, double distance) {
        this.cityName = cityName;
        this.distance = distance;
    }

    public String getCityName() {
        return cityName;
    }

    public double getDistance() {
        return distance;
    }
}
