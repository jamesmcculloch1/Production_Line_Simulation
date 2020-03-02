/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//simulates the inter-stage storage on the production line
public class Storage extends ProductionLineNode {
    private Queue items;
    private final int storageMax;

    //used to record how many items were in the storage and for
    //how long (to calculate average items in storage)
    private Map<Integer, Double> itemInfo;

    private List<Double> avgTimeInStorage;
    private double timeLastAccessed;

    public Storage() {
        this("", null,null);
    }

    public Storage(String name, ProductionLineNode prev, ProductionLineNode next) {
        super(name, prev, next);
        storageMax = 1;
        timeLastAccessed = 0;

        //using an unlimited queue (for finished items storage
        items = new Queue();
        itemInfo = new HashMap<Integer, Double>();
        avgTimeInStorage = new LinkedList<>();
    }

    public Storage(String name, int storageMax, ProductionLineNode prev, ProductionLineNode next) {
        super(name, prev, next);
        this.storageMax = storageMax;
        timeLastAccessed = 0;

        if (storageMax < 1) {
            throw new IllegalArgumentException("Cannot create Storage objects with no available storage");
        }

        //using a limited queue of size storageMax
        items = new FixedQueue(storageMax);

        itemInfo = new HashMap<Integer, Double>();
        avgTimeInStorage = new LinkedList<>();
    }

    public boolean addItem(Item item, double currentTime) {
        recordChange(currentTime);

        boolean added = items.addToTail(item);

        if (added) {
            item.addTimeEntered(currentTime);
        }

        return added;
    }

    public Item takeItem(double currentTime) {
        recordChange(currentTime);

        Item removed = items.removeFromHead();

        if (removed != null) {
            //records how long the removed item was in the storage
            avgTimeInStorage.add(removed.getTimeSpent(currentTime));
        }

        return removed;
    }

    //updates the itemInfo with how long a certain number of items has been in the queue
    public void recordChange(double currentTime) {
        int index = items.getSize();

        //if key doesnt exist, create it
        if (!itemInfo.containsKey(index)) {
            itemInfo.put(index, 0.0);
        }

        if (index >= 0) {
            //updates the amount of time the storage has held the current amount of items
            double prevTime = itemInfo.get(index);
            double timeWithItems = currentTime - timeLastAccessed;
            double newTime = timeWithItems + prevTime;
            itemInfo.put(items.getSize(), newTime);
            timeLastAccessed = currentTime;
        }
    }

    //returns a string with the average amount of time this storage has an item for
    public String getAvgTime() {
        String string = "";

        double totalTime = 0;

        //gets total time
        for (Double d: avgTimeInStorage) {
            totalTime += d;
        }


        double avgTime = totalTime / avgTimeInStorage.size();
        String avgT = String.format("%5.2f", avgTime);

        string += String.format("%-15s%-15s%-15s\n", getName(), avgT, avgItems());

        return string;
    }

    //returns a string with the average amount of items in the storage at any given time
    public String avgItems() {
        String string;
        double totalTime = 0.0;
        double itemAvgTotal = 0.0;

        for (int i = 0; i < storageMax; i++) {
            //makes sure that key is in the map before accessing
            if (!itemInfo.containsKey(i))
                itemInfo.put(i, 0.0);

            //gets the proportion of total time a certain amount of items were in the storage
            double proportionTotalTime = itemInfo.get(i) / timeLastAccessed;

            itemAvgTotal += proportionTotalTime * i;
        }

        return String.format("%5.2f", itemAvgTotal);
    }

    public boolean hasItem() {
        if (items.getSize() > 0) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (items.getSize() == storageMax) {
            return true;
        }
        return false;
    }

    public int getSize() {
        return items.getSize();
    }

    public Queue getQueue() {
        return items;
    }


}
