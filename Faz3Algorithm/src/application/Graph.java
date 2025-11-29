package application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Graph {
    private List<String> vertices;
    private double[][] costMatrix;
    private double[][] timeMatrix;
    private double [][] Disitance;
    private int size;
    private List<CapitalCity> cities;  

    public Graph() {
        this.vertices = new ArrayList<>();
        this.size = 0;
        this.costMatrix = new double[0][0];
        this.timeMatrix = new double[0][0];
        this.cities = new ArrayList<>();  
    }

    public void addVertex(String city) {
        if (!vertices.contains(city)) {
            vertices.add(city);
            size++;
            resizeTHEMatrices();
        }
    }
    

    private void resizeTHEMatrices() {
        if (costMatrix.length == 0 || timeMatrix.length == 0) {
            costMatrix = new double[size][size];
            timeMatrix = new double[size][size];
        } else {
            double[][] newCostMatrix = new double[size][size];
            double[][] newTimeMatrix = new double[size][size];

            for (int i = 0; i < size - 1; i++) {
                System.arraycopy(costMatrix[i], 0, newCostMatrix[i], 0, size - 1);
                System.arraycopy(timeMatrix[i], 0, newTimeMatrix[i], 0, size - 1);
            }

            costMatrix = newCostMatrix;
            timeMatrix = newTimeMatrix;
        }

        for (int i = 0; i < size; i++) {
            Arrays.fill(costMatrix[i], Double.MAX_VALUE);
            Arrays.fill(timeMatrix[i], Double.MAX_VALUE);
        }
    }
// to list the cost and time between the matrix
    public void addEdgebetwenMatrex(String source, String dest, double cost, double time) {
        addVertex(source);
        addVertex(dest);

        int srcIndex = getVertexIndex(source);
        int destIndex = getVertexIndex(dest);

        costMatrix[srcIndex][destIndex] = cost;
        timeMatrix[srcIndex][destIndex] = time;
    }
// to return the matix index
    private int getVertexIndex(String city) {
        return vertices.indexOf(city);
    }


    
    
    public List<String> findShortestPathByMethod(String source, String destination, String method) {
        int srcIndex = getVertexIndex(source);
        int destIndex = getVertexIndex(destination);

        if (srcIndex == -1 || destIndex == -1) {
            throw new IllegalArgumentException("City not found in the graph "); // dos not have any vartix
        }

        double[] distances = new double[size];
        int[] prev = new int[size];
        int[] known = new int[size];
        double[][] matrix = method.equals("Time") ? timeMatrix : costMatrix;

        Arrays.fill(distances, Double.MAX_VALUE);
        Arrays.fill(prev, -1);
        Arrays.fill(known, 0);

        distances[srcIndex] = 0;// the  farst node

        for (int i = 0; i < size; i++) {
            int current = getUNknowntVertex(distances, known);
            if (current == -1) break;
            known[current] = 1;

            for (int NNOD = 0; NNOD < size; NNOD++) {
                if (known[NNOD] == 0 && matrix[current][NNOD] != Double.MAX_VALUE) {
                    double newDist = distances[current] + matrix[current][NNOD];
                    if (newDist < distances[NNOD]) {
                        distances[NNOD] = newDist;// short spath
                        prev[NNOD] = current;
                    }
                }
            }
        }

        List<String> path = reconstructPath(prev, srcIndex, destIndex);
        // if canot each it return emty list 
        return distances[destIndex] == Double.MAX_VALUE ? Collections.emptyList() : path;
    }

    private int getUNknowntVertex(double[] distances, int[] known) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < distances.length; i++) {
            if (known[i] == 0 && distances[i] < minDistance) { // known[i] == 0  not visited
                minDistance = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

 
    
    private List<String> reconstructPathRecursive(int[] prev, int currentIndex) {
        if (currentIndex == -1) {
            return new ArrayList<>();
        }
        List<String> path = reconstructPathRecursive(prev, prev[currentIndex]);
        path.add(vertices.get(currentIndex));
        return path;
    }

    private List<String> reconstructPath(int[] prev, int srcIndex, int destIndex) {
        List<String> path = reconstructPathRecursive(prev, destIndex);
        return path.size() > 1 && path.get(0).equals(vertices.get(srcIndex)) ? path : Collections.emptyList();
    }

    
    
    public double getDistanceBetweenCities(String currentCity, String nextCity, List<CapitalCity> cities) {
        CapitalCity city1 = getCityByName(currentCity, cities);
        CapitalCity city2 = getCityByName(nextCity, cities);

        if (city1 == null || city2 == null) {
            System.out.println("One or both cities not found.");
            return Double.MAX_VALUE; // Return a high value if the cities are not found
        }

        double lat1 = city1.getLatitude();
        double lon1 = city1.getLongitude();
        double lat2 = city2.getLatitude();
        double lon2 = city2.getLongitude();

        final int R = 6371; // Radius of the Earth in kilometers

        // Convert latitudes and longitudes from degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Calculate the Haversine components
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the final distance
        double distance = R * c; // Distance in kilometers

        return distance;
    }


    // Method to get cost between two cities
    public double getCostBetweenCities(String currentCity, String nextCity) {
        int srcIndex = getVertexIndex(currentCity);
        int destIndex = getVertexIndex(nextCity);

        if (srcIndex == -1 || destIndex == -1 || costMatrix[srcIndex][destIndex] == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        }

        return costMatrix[srcIndex][destIndex];
    }

    // Method to get time between two cities
    public double getTimeBetweenCities(String currentCity, String nextCity) {
        int srcIndex = getVertexIndex(currentCity);
        int destIndex = getVertexIndex(nextCity);

        if (srcIndex == -1 || destIndex == -1 || timeMatrix[srcIndex][destIndex] == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        }

        return timeMatrix[srcIndex][destIndex];
    }

    public String[] getVertices() {
        return vertices.toArray(new String[0]);
    }


    public double[] getCityPos(String cityName, double imageWidth, double imageHeight, List<CapitalCity> cities) {
        // Use getCityByName method to fetch the CapitalCity by name from the list of cities
        CapitalCity city = getCityByName(cityName, cities);
        
        if (city != null) {
            // If city is found, return its position on the map
            return city.CityInMap(imageWidth, imageHeight);
        } else {
            // If the city is not found, print 
            System.out.println("City not found: " + cityName);
            return new double[]{0.0, 0.0}; // Return default if city is not found
        }
    }




    public CapitalCity getCityByName(String name, List<CapitalCity> cities) {
        name = name.trim();  // Remove spaces
        for (CapitalCity city : cities) {
            if (city.getName().equalsIgnoreCase(name)) {
                return city;
            }
        }
        return null;
    }




    public void printCities() {
        for (CapitalCity city : cities) {
            System.out.println("City: " + city.getName());
        }
    }

    
}