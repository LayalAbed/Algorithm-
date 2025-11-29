package application;

class Vertex {
    private String cityName;
    public double dist;

    public Vertex(String cityName, double dist) {
        this.cityName = cityName;
        this.dist = dist;
    }

    public String getCityName() {
        return cityName;
    }

    public double getDist() {
        return dist;
    }
}