package week02_queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int sz;

    //    public Deque()                           // construct an empty deque
    public Deque() {
    }

    //    public boolean isEmpty()                 // is the deque empty?
    public boolean isEmpty() {
        return sz == 0;
    }

    //    public int size()                        // return the number of items on the deque
    public int size() {
        return sz;
    }

    //    public void addFirst(Item item)          // add the item to the front
    public void addFirst(Item item) {
        checkNotNull(item);
        if (!addIfEmpty(item)) {
            Node<Item> newNode = new Node<>(item);
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        sz++;
    }

    //    public void addLast(Item item)           // add the item to the end
    public void addLast(Item item) {
        checkNotNull(item);
        if (!addIfEmpty(item)) {
            Node<Item> newNode = new Node<>(item);
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        sz++;
    }
    //    public Item removeFirst()                // remove and return the item from the front
    public Item removeFirst() {
        checkRemoveOnEmpty();
        Item item = removeIfOne();
        if (item == null) {
            Node<Item> newFirst = first.next;
            newFirst.prev = null;
            item = first.item;
            eliminateNode(first);
            first = newFirst;
        }
        sz--;
        return item;
    }

    //    public Item removeLast()                 // remove and return the item from the end
    public Item removeLast() {
        checkRemoveOnEmpty();
        Item item = removeIfOne();
        if (item == null) {
            Node<Item> newLast = last.prev;
            newLast.next = null;
            item = last.item;
            eliminateNode(last);
            last = newLast;
        }
        sz--;
        return item;
    }

    private void eliminateNode(Node<Item> node) {
        node.next = null;
        node.prev = null;
        node.item = null;
    }


    private void checkNotNull(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    private boolean addIfEmpty(Item item) {
        if (sz == 0) {
            first = new Node<>(item);
            last = first;
            return true;
        }
        return false;
    }

    private Item removeIfOne() {
        if (sz == 1) {
            Item item = first.item;
            first.item = null;
            first = null;
            last = null;
            return item;
        }
        return null;
    }

    private void checkRemoveOnEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    //    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator<>(first);
    }

    private static class Node<Item> {
        private Item item;
        private Node<Item> prev;
        private Node<Item> next;
        private Node(Item item) {
            this.item = item;
        }
    }

    private static class DequeIterator<Item> implements Iterator<Item> {

        private Node<Item> nextNode;

        DequeIterator(Node<Item> nextNode) {
            this.nextNode = nextNode;
        }

        public boolean hasNext() {
            return nextNode != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = nextNode.item;
            nextNode = nextNode.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    //    public static void main(String[] args)   // unit testing
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        // check size
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        System.out.println("deque size(3): " + deque.size());
        System.out.println("deque removeLast(1): " + deque.removeLast());
        System.out.println("deque removeFirst(3): " + deque.removeFirst());
        System.out.println("deque size(1): " + deque.size());
        System.out.println("deque removeFirst(2): " + deque.removeFirst());
        System.out.println("deque size(0): " + deque.size());

        deque = new Deque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        System.out.println("deque size(3): " + deque.size());
        System.out.println("deque removeFirst(1): " + deque.removeFirst());
        System.out.println("deque size(2): " + deque.size());
        System.out.println("deque removeFirst(3): " + deque.removeLast());
        System.out.println("deque removeFirst(2): " + deque.removeLast());
        System.out.println("deque size(0): " + deque.size());

        deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()) {
            System.out.println("Iterated value: " + iterator.next());
        }
    }
}
