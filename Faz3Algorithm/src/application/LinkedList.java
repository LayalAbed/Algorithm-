package application;



public class LinkedList {
    static class Node {
        private double cost;
        private double time;
        Node next;

        public Node(double cost, double time) {
            this.cost = cost;
            this.time = time;
            this.next = null;
        }

        // Getter and Setter for cost
        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        // Getter and Setter for time
        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }

    private Node head;

    public LinkedList() {
        head = null;
    }

    public void add(double cost, double time) {
        Node newNode = new Node(cost, time);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public boolean remove(double cost, double time) {
        if (head == null) {
            return false;
        }
        if (head.getCost() == cost && head.getTime() == time) {
            head = head.next;
            return true;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.getCost() == cost && current.next.getTime() == time) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void printList() {
        if (head == null) {
            System.out.println("القائمة فارغة");
            return;
        }
        Node current = head;
        while (current != null) {
            System.out.print("Cost: " + current.getCost() + ", Time: " + current.getTime() + " | ");
            current = current.next;
        }
        System.out.println();
    }
}