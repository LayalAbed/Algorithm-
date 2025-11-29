package application;

public class LinkList {
    private Node head;
    private Node tail;
    int count = 0;

    public void addLast(int data) {
        Node node = new Node(data);
        if (count != 0) {
            tail.next = node;
            tail = node;
            count++;
        } else {
            head = node;
            tail = node;
            count++;
        }
    }

    public void delete(int data) {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        if (head.data == data) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
            count--;
            System.out.println("Deleted " + data + " from the list.");
            return;
        }

        Node current = head;
        while (current != null && current.next != null) {
            if (current.next.data == data) {
                current.next = current.next.next;
                if (current.next == null) {
                    tail = current;
                }
                count--;
                System.out.println("Deleted " + data + " from the list.");
                return;
            }
            current = current.next;
        }

        System.out.println(data + " not found in the list.");
    }

    public void display() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return head == null;
    }
}
