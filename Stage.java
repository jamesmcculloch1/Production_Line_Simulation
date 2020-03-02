/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//represents the stages in the production line
public class Stage extends ProductionLineNode implements Comparable<Stage> {
    private StageStatus status;
    private Item currentItem;
    private int mean, range;
    private double startStarve, startBlock , starveTime, blockTime;

    public Stage(String name, int mean, int range, ProductionLineNode prev, ProductionLineNode next) {
        super(name, prev, next);
        this.mean = mean;
        this.range = range;
        status = StageStatus.STARVED;
        currentItem = null;
        startStarve = startBlock = starveTime = blockTime = 0.0;
    }

    //called by the production line to get the stage to update itself and the nodes around it
    public Stage run(double currentTime) {
        setTime(currentTime);
        List<Stage> stages;

        //if an item is passed to the next storage
        if (passItem(currentTime)) {
            stages = getNextStages();

            for (Stage stage: stages) {
                if (stage != null) {

                    //if the next stage is starving, run it
                    if (stage.getStatus() == StageStatus.STARVED) {
                        stage.run(currentTime);
                    }
                }
            }


        }
        //if an item is taken from the previous storage
        if (takeItem(currentTime)) {
            //records the name of the stage the item is passed into
            currentItem.addPath(this.getName());

            stages = getPrevStages();

            for (Stage stage: stages) {
                if (stage != null) {
                    //if the prev stage is blocked, run it
                    if (stage.getStatus() == StageStatus.BLOCKED) {
                        stage.run(currentTime);
                    }
                }
            }
        }
        updateStatus(currentTime);

        return this;
    }

    public StageStatus getStatus() {
        return status;
    }

    public void setStatus(StageStatus status) {
        this.status = status;
    }

    //takes an item from the storage previous to the current stage
    public boolean takeItem(double currentTime) {
        if (currentItem == null) {
            Storage prevStorage = (Storage) getPrev().get(0);
            setCurrentItem(prevStorage.takeItem(currentTime));
            if (currentItem != null) {
                Random r = new Random();
                double d = r.nextDouble();

                //calculates the time the item will need to be created
                double timeToFinish = mean + range * (d - 0.5);

                currentItem.setFinishTime(getTime() + timeToFinish);

                //adds it to the production line priorityqueue
                ProductionLine.addToQueue(this);
                return true;
            }
        }

        return false;

    }

    //gives the current item of this stage to the storage next to it
    public boolean passItem(double currentTime) {
        //if the item exists and is finished
        if (currentItem != null && getCurrentItem().isFinished(getTime())) {
            Storage next = (Storage) getNext().get(0);

            //adds the item to the storage
            if (next.addItem(currentItem, currentTime)) {
                currentItem = null;
                return true;
            }
        }
        return false;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item item) {
        this.currentItem = item;


        //calculate time to finish item and add to queue.
        //if currentItem == null set to starved?
    }

    //calculates the valid status for the change after changes have been made
    public void updateStatus(double currentTime) {
        StageStatus prevStatus = status;

        if (getCurrentItem() == null) {
            status = StageStatus.STARVED;
            startStarve = currentTime;
        } else if (getCurrentItem().isFinished(currentTime)) {
            status = StageStatus.BLOCKED;
            startBlock = currentTime;
        } else
            status = StageStatus.ACTIVE;

        //if status has changed
        if (prevStatus != status) {
            if (prevStatus == StageStatus.STARVED) {
                //add the amount of time starving to starveTime
                starveTime += currentTime - startStarve;
            } else if (prevStatus == StageStatus.BLOCKED) {
                //add the amount of time blocked to blockTime
                blockTime += currentTime - startBlock;
            }
        }
    }

    //required for the priority queue. stages with lower completion times come first
    @Override
    public int compareTo(Stage o) {
        if (getCurrentItem() == null) {
            return 1;
        }

        if (o.getCurrentItem() == null) {
            return -1;
        }

        if (getCurrentItem().getFinishTime() < o.getCurrentItem().getFinishTime()) {
            return -1;
        }

        return 1;
    }

    //returns a list of all the stages ahead of this one
    public List<Stage> getNextStages() {
        List<Stage> nextStages = new LinkedList<>();
        for (ProductionLineNode node: getNext().get(0).getNext()) {
            nextStages.add((Stage) node);
        }

        return nextStages;
    }

    //returns a list of al the stages before this one
    public List<Stage> getPrevStages() {
        List<Stage> prevStages = new LinkedList<>();
        for (ProductionLineNode node: getPrev().get(0).getPrev()) {
            prevStages.add((Stage) node);
        }

        return prevStages;
    }

    //returns a string of the time spent working, starved and blocked.
    public String workProportions(double currentTime) {
        double workPercent = 0;
        double starveBlockTime = starveTime + blockTime;

        workPercent = 100 - (starveBlockTime / currentTime) * 100;
        String work = String.format("%5.2f", workPercent);
        String starve = String.format("%5.2f", starveTime);
        String block = String.format("%5.2f", blockTime);

        String string = "";

        string += String.format("%-15s%-15s%-15s%-15s\n", this.getName(), work, starve, block);

        return string;
    }
}
