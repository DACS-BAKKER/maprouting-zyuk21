/*
 Date: 13/01/2020
 Name: Alex Yuk
 File: LinkedList
 */

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {

    private Node start;
    private int size;

    public LinkedList() {
        this.start = null;
        this.size = 0;
    }

    public LinkedList(T item) {
        this.start = null;
        this.size = 0;
        add(item);
    }

    public void add(T item) {
        if (this.start == null) {
            this.start = new Node();
            this.start.item = item;
        } else {
            Node temp = this.start;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = new Node();
            temp.next.item = item;
        }
        this.size++;
    }

    public T get(int index) {
        if (this.size() - 1 < index) {
            System.out.println("Out of bounds.");
        } else {
            Node temp = this.start;
            for (int i = 0; i < index; i++)
                temp = temp.next;
            return temp.item;
        }
        return null;
    }

    public void clear() {
        this.start = null;
    }

    public int size() {
        return this.size;
    }

    public Boolean isEmpty() {
        return this.start == null;
    }

    public void set(int index, T number) {
        if (this.size - 1 < index) {
            System.out.println("Out of bounds.");
        } else {
            Node temp = this.start;
            for (int i = 0; i < index; i++)
                temp = temp.next;
            temp.item = number;
        }
    }

    private class Node {
        T item;
        Node next;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> {

        private Node temp = start;

        public boolean hasNext() {
            return temp != null;
        }

        public void remove() {
        }

        public T next() {
            T item = temp.item;
            temp = temp.next;
            return item;
        }
    }

    public String toString() {
        String returned = "";
        Node temp = this.start;
        while (temp != null) {
            returned += temp.item + " ";
            temp = temp.next;
        }
        return returned;
    }

    public void recursivelyReverse() {
        this.start = recursivelyReverse(this.start);
    }

    public void iterativelyReverse() {
        this.start = iterativelyReverse(this.start);
    }

    private Node iterativelyReverse(Node n) {
        Node current = n;
        Node previous = null;
        Node next = null;
        while (current != null) {
            next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        n = previous;
        return n;
    }

    private Node recursivelyReverse(Node n) {
        if (n != null && n.next == null)
            return n;
        else if (n != null && n.next != null) {
            Node temp = recursivelyReverse(n.next);
            n.next.next = n;
            n.next = null;
            return temp;
        }
        return null;
    }


    public static void main(String[]args) {

    }
}
