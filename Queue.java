/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

import java.util.LinkedList;
import java.util.List;

//a first in first out linked list
public class Queue {
    private List<Item> items;

    public Queue() {
        items = new LinkedList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    //returns null if list is queue is empty
    public Item removeFromHead() {
        Item item = null;
        if (!items.isEmpty()) {
            item = items.remove(0);
        }

        return item;
    }

    public boolean addToTail(Item item) {
        if (item != null) {
            items.add(item);
            return true;
        }

        return false;
    }

    public int getSize() {
        return items.size();
    }

    public boolean getFull() {
        return false;
    }
}
