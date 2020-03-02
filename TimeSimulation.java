/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

//used to keep track of the current time and the time limit
public class TimeSimulation {
    private double timeLimit, currentTime;

    public TimeSimulation(double timeLimit) {
        currentTime = 0;
        this.timeLimit = timeLimit;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isPastTimeLimit() {
        return timeLimit <= currentTime;
    }
}
