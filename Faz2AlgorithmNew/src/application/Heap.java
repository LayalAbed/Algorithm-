package application;

public class Heap {

	private Node[] heap;
	private int size;
	private int capacity;

	public Heap() {
		this(1);
	}

	public Heap(int capacity) {
		this.capacity = capacity;
		heap = new Node[capacity];
		size = 0;
	}

	public Node[] getHeap() {
		Node[] target = new Node[size + 1];
		for (int i = 0; i < size + 1; i++)
			target[i] = heap[i];
		return target;
	}

	public int getSize() {
		return size;
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isEmpty() {
		if(size ==0)
			return true;
		else
			return false;
	}

	public void addHeap(Node element) {
		int i = ++size;
		while ((i > 1) && heap[i / 2].getFreq() > element.getFreq()) {
			heap[i] = heap[i / 2];
			i /= 2;
		}
		heap[i] = element;
	}

	public Node deleteHeap() {
		int child, i;
		Node last, min = null;
		if (size != 0) {
			min = heap[1];
			last = heap[size--];
			for (i = 1; i * 2 <= size; i = child) {
				child = i * 2;
				if (child < size
						&& heap[child].getFreq() > heap[child + 1].getFreq())
					child++;
				if (last.getFreq() > heap[child].getFreq())
					heap[i] = heap[child];
				else
					break;
			}
			heap[i] = last;
		}
		return min;
	}

}
