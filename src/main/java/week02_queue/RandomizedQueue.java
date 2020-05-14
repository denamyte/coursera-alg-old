package week02_queue;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int n;

    //    public RandomizedQueue()                 // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
    }

    //    public boolean isEmpty()                 // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    //    public int size()                        // return the number of items on the queue
    public int size() {
        return n;
    }

    //    public void enqueue(Item item)           // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (n == a.length) {
            resize(2 * a.length);
        }
        a[n++] = item;
    }

    private void shuffleLast() {
        if (n > 1) {
            int lastIndex = n - 1;
            int indexToExchange = StdRandom.uniform(lastIndex);   // Taking a random number in range [0..lastIndex)
            Item temp = a[indexToExchange];
            a[indexToExchange] = a[lastIndex];
            a[lastIndex] = temp;
        }
    }

    //    public Item dequeue()                    // remove and return a random item
    public Item dequeue() {
        checkOnEmpty();
        shuffleLast();
        Item item = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n <= a.length / 4) {
            resize(a.length / 2);
        }
        return item;
    }

    //    public Item sample()                     // return (but do not remove) a random item
    public Item sample() {
        checkOnEmpty();
        return a[StdRandom.uniform(n)];
    }

    //    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }

    private void checkOnEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
//        System.arraycopy(a, 0, temp, 0, n);
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    private class RQIterator implements Iterator<Item> {

        private int indicesCurrent;
        private int[] indices;

        RQIterator() {
            indices = new int[n];
            for (int j = 0; j < n; j++) {
                indices[j] = j;
            }
            StdRandom.shuffle(indices);
        }

        @Override
        public boolean hasNext() {
            return indicesCurrent < indices.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return a[indices[indicesCurrent++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    //    public static void main(String[] args)   // unit testing
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("A");
        rq.enqueue("B");
        rq.enqueue("C");
        rq.enqueue("D");
        rq.enqueue("E");
        rq.enqueue("F");
        rq.enqueue("G");
        rq.enqueue("H");
        rq.enqueue("I");
        rq.enqueue("J");
        rq.enqueue("K");
        rq.enqueue("L");
        rq.enqueue("M");
        System.out.println("rq size(13): " + rq.size());
        int size = rq.size();
        for (int i = 0; i < size; i++) {
            System.out.print(rq.dequeue());
            System.out.print(" ");
        }
        System.out.println();
        System.out.println("rq size(0): " + rq.size());

        RandomizedQueue<Integer> rqi = new RandomizedQueue<>();
        for (int i = 0; i < 100; i++) {
            rqi.enqueue(i);
        }
        Iterator<Integer> iterator = rqi.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            System.out.println("[" + count++ + "]: " + iterator.next());
        }
        System.out.println();
    }
}