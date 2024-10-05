import java.util.NoSuchElementException;

public class HW02_20220808038 {
    public static void main(String[] args) {

        MusicPlayer mp = new MusicPlayer("./Musics");

    }
}

interface INode <T> { // storage unit
    // Constructor (T data, Node<T> prev, Node<T> next)
    T getData(); // returns the data
    Node<T> getNext(); // returns the next of this storage unit
    Node<T> getPrev(); // returns the previous storage unit of this unit
    void setNext(Node<T> next); // sets next pointer of this node
    void setPrev(Node<T> prev); // sets the prev pointer of this node
    String toString(); // string representation
}

interface IDoublyCircularLinkedList <T> {
    // must have the data field current
    // Constructor ()
    void addFirst(T data); // adds an element to the head of the list. If first element in list, must also be last element
    // if only element in the list its next and prev should point to itself
    void addLast(T data); // adds an element to the tail of the list. If first element in list, must also be last element
    // if only element in the list its next and prev should point to itself
    T removeFirst() throws NoSuchElementException; // removes the first element in the list,
    // throw exception if list is empty, if only element remaining it should be first and last and its next and prev,
    // should be itself
    T removeLast() throws NoSuchElementException; // removes the last element in the list,
    // throw exception if list is empty, if only element remaining it should be first and last and its next and prev,
    // should be itself
    T get(int index) throws IndexOutOfBoundsException; // gets the ith element in the list,
    // should throw exception if out of bounds
    T first() throws NoSuchElementException; // should set current, returns the first data
    T last() throws NoSuchElementException; // should set current, returns the last data
    boolean remove(T data); // should return false if data doesnt exists, returns true and removes if exists
    boolean isEmpty();
    int size();
    T next() throws NoSuchElementException; // if empty, throws exception, should change current correctly
    // if current is null should return head and set it to head
    T previous() throws NoSuchElementException; // if empty, throws exception, should change current correctly
    // if current is null should return tail data and set it
    T getCurrent() throws NoSuchElementException; // Retruns the current pointer, if no element exits throws exception
    // if current is null returns heads data
    Node<T> getHead(); // returns the head of the list, if is empty returns null
    // any other method needed

}

class Node<T> implements INode<T> {

    public T data;
    public Node<T> prev;
    public Node<T> next;

    public Node(T data) {
        this.data = data;
        this.prev = this;
        this.next = this;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public Node<T> getNext() {
        return next;
    }

    @Override
    public Node<T> getPrev() {
        return prev;
    }

    @Override
    public void setNext(Node<T> next) {
        this.next = next;
    }

    @Override
    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}

class DoublyCircularLinkedList<T> implements IDoublyCircularLinkedList<T> {

    public Node<T> head;
    public Node<T> tail;
    public Node<T> current;
    public int size;

    public DoublyCircularLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.data = data;

        if (isEmpty()) {
            newNode.next = newNode;
            newNode.prev = newNode;
            head = tail = newNode;
        }
        else {
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            tail.next = newNode;
        }
    }

    @Override
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.data = data;

        if (isEmpty()) {
            newNode.next = newNode;
            newNode.prev = newNode;
            head = tail = newNode;
        }
        else {
            newNode.next = head;
            newNode.prev = tail;
            head.prev = newNode;
            tail.next = newNode;
            tail = newNode;
        }
    }

    @Override
    public T removeFirst() throws NoSuchElementException {

        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }

        T data = head.getData();

        if (size() == 1) {
            head.prev = null;
            head.next = null;
            head = tail = null;
        }
        else {
            head = head.next;
            head.prev = tail;
            tail.next = head;
        }

        return data;
    }

    @Override
    public T removeLast() throws NoSuchElementException {

        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }

        T data = tail.getData();

        if (size() == 1) {
            head.prev = null;
            head.next = null;
            head = tail = null;
        }
        else {
            tail = tail.prev;
            tail.next = head;
            head.prev = tail;
        }

        return data;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {

        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.getData();
    }

    @Override
    public T first() throws NoSuchElementException {

        if(isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }

        return head.getData();
    }

    @Override
    public T last() throws NoSuchElementException {

        if(isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }

        return tail.getData();
    }

    @Override
    public boolean remove(T data) {

        if (isEmpty()) {
            return false;
        }

        Node<T> tempNode = head;
        do {
            if (tempNode.getData().equals(data)) {
                if (tempNode == head) {
                    removeFirst();
                } else if (tempNode == tail) {
                    removeLast();
                } else {
                    tempNode.prev.next = tempNode.next;
                    tempNode.next.prev = tempNode.prev;
                }
                return true;
            }
            tempNode = tempNode.next;
        } while (tempNode != head);

        return false;
    }

    @Override
    public boolean isEmpty() {

        if(head == null) {
            return true;
        }

        return false;
    }

    @Override
    public int size() {

        if(head == null) {
            return 0;
        }

        //  If it is not empty, there is at least 1 node
        int count = 1;
        Node<T> current = head;

        while (current.next != head) {
            count++;
            current = current.next;
        }

        return count;
    }

    @Override
    public T next() throws NoSuchElementException {

        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        if (current == null) {
            current = head;
        } else {
            current = current.next;
        }
        return current.getData();
    }

    @Override
    public T previous() throws NoSuchElementException {

        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        if (current == null) {
            current = tail;
        } else {
            current = current.prev;
        }
        return current.getData();
    }

    @Override
    public T getCurrent() throws NoSuchElementException {

        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        if (current == null) {
            current = head;
        }
        return current.getData();
    }

    @Override
    public Node<T> getHead() {

        if(head == null) {
            return null;
        }

        return head;
    }
}