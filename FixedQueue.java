/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

//a first in first out queue of limited size
public class FixedQueue extends Queue {

    //negative value for unlimited storage
    private int storageMax;

    public FixedQueue(int storageMax) {
        super();
        this.storageMax = storageMax;
    }

    @Override
    public boolean addToTail(Item item) {
        //if the queue has room to add an item, it adds one
        if (getSize() < storageMax && item != null) {
            getItems().add(item);
            return true;
        }

        return false;
    }

    @Override
    public boolean getFull() {
        if (getSize() < storageMax) {
            return true;
        }
        return false;
    }
}
