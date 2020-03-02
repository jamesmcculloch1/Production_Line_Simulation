/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

import java.util.LinkedList;
import java.util.List;

public class Item {
    private double timeEntered;

    //records the names of all the nodes the item went through
    private List<String> path;
    private double finishTime;

    public Item() {
        timeEntered = 0;
        path = new LinkedList<>();
        finishTime = 0;
    }


    public boolean isFinished(double currentTime) {
        if (currentTime >= finishTime) {
            return true;
        }
        return false;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public void addPath(String stageName) {
        path.add(stageName);
    }

    public List<String> getPath() {
        return path;
    }

    public void addTimeEntered(double currentTime) {
        timeEntered = currentTime;
    }

    public double getTimeSpent(double currentTime) {
        return currentTime - timeEntered;
    }
}
