/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

//class is used to connect all the nodes (stages and storages) together and calls methods on them
public class ProductionLine {
    private Stage s0, s1, s2a, s2b, s3, s4a, s4b, s5;
    private Storage productionLineFeeder ,q01, q12, q23, q34, q45, finishedItems;
    private int mean, range;
    private TimeSimulation ts;
    private static PriorityQueue<Stage> priorityQueue;
    List<Stage> stages;
    //all storages except the first and last
    List<Storage> middleStorages;


    public ProductionLine(int qMax, int mean, int range) {
        ts = new TimeSimulation(10000000);
        priorityQueue = new PriorityQueue<>();
        stages  = new LinkedList<Stage>();
        middleStorages = new LinkedList<Storage>();

        qMax = 7;
        this.mean = mean;
        this.range = range;

        //USED FOR S2A, S2B, S4A AND S4B
        //doubles the mean and range of these stages to keep the line balanced
        //REMOVE * 2 FOR SIMILAR OUTPUT TO SAMPLE OUTPUTS
        int parallelMean = mean * 2;
        int parallelRange = range * 2;


        //this part instantiates all the stages and storages and connects them
        s0 = new Stage("s0", mean, range, null, null);
        q01 = new Storage("q1", qMax, s0, null);
        s0.setNext(q01);

        //creating item feeder for first stage
        productionLineFeeder = new ProductionLineFeeder(s0);
        s0.setPrev(productionLineFeeder);

        s1 = new Stage("s1", mean, range, q01, null);
        q01.setNext(s1);
        q12 = new Storage("q2",qMax, s1, null);
        s1.setNext(q12);

        s2a = new Stage("s2a", parallelMean, parallelRange, q12, null);
        q12.setNext(s2a);
        q23 = new Storage("q3",qMax, s2a, null);
        s2a.setNext(q23);

        //adding alternate stage
        s2b = new Stage("s2b", parallelMean, parallelRange, q12, q23);
        q12.setNext(s2b);
        q23.setPrev(s2b);


        s3 = new Stage("s3", mean, range, q23, null);
        q23.setNext(s3);
        q34 = new Storage("q4",qMax, s3, null);
        s3.setNext(q34);

        s4a = new Stage("s4a", parallelMean, parallelRange, q34, null);
        q34.setNext(s4a);
        q45 = new Storage("q5",qMax, s4a, null);
        s4a.setNext(q45);

        s4b = new Stage("s4b", parallelMean, parallelRange, q34, q45);
        q34.setNext(s4b);
        q45.setPrev(s4b);

        s5 = new Stage("s5", mean, range, q45, null);
        q45.setNext(s5);
        finishedItems = new Storage("q6",s5, null);
        s5.setNext(finishedItems);


        //adds all stages to a list
        stages.add(s0);
        stages.add(s1);
        stages.add(s2a);
        stages.add(s2b);
        stages.add(s3);
        stages.add(s4a);
        stages.add(s4b);
        stages.add(s5);

        //adds all storages between stages to a list
        middleStorages.add(q01);
        middleStorages.add(q12);
        middleStorages.add(q23);
        middleStorages.add(q34);
        middleStorages.add(q45);
    }

    //called to run the production line
    public void beginProduction() {
        //adds an item to the first stage and adds it to the queue
        addToQueue(s0.run(ts.getCurrentTime()));

        //runs until user entered time limit is reached
        while (!ts.isPastTimeLimit()) {
            Stage currentStage = priorityQueue.remove();

            //updates the time with the finish time of the item removed from the priority queue
            ts.setCurrentTime(currentStage.getCurrentItem().getFinishTime());

            currentStage.run(ts.getCurrentTime());
        }
    }

    //adds a stage to the priority queue
    public static void addToQueue(Stage stage) {
        //if this stage already has a time associated, remove it
        priorityQueue.remove(stage);

        Item item = stage.getCurrentItem();
        if (item == null || item.getFinishTime() <= 0) {
            return;
        } else {
            priorityQueue.add(stage);
        }
    }

    //returns a string with the information about the path through the stages the item took
    public String productionPathInfo() {
        String format = "Production Paths:\n";
        format += "------------------\n";
        int s2as4a = 0, s2as4b = 0, s2bs4a = 0, s2bs4b = 0;

        //gets how many took each path from the finishedItems list
        for (Item item: finishedItems.getQueue().getItems()) {
            List<String> path = item.getPath();
            if (path.contains("s2a") && path.contains("s4a")) {
                s2as4a++;
            } else if (path.contains("s2a") && path.contains("s4b")) {
                s2as4b++;
            } else if (path.contains("s2b") && path.contains("s4a")) {
                s2bs4a++;
            } else {
                s2bs4b++;
            }
        }

        format += "s2a -> s4a:  " + s2as4a + "\n";
        format += "s2a -> s4b:  " + s2as4b + "\n";
        format += "s2b -> s4a:  " + s2bs4a + "\n";
        format += "s2b -> s4b:  " + s2bs4b + "\n";

        format += "Total:  " + (s2as4a + s2as4b + s2bs4a + s2bs4b);

        return format;
    }

    //returns a string with information about the storages
    public String storageInfo() {
        String string = "Storage Queues:\n";
        string += "--------------------------------\n";

        string += String.format("%-15s%-15s%-15s\n", "Store", "AvgTime[t]", "AvgItems");

        for (Storage storage: middleStorages) {
            string += storage.getAvgTime();
        }

        return string;
    }

    //returns a string with information related to the stages
    public String stageInfo() {
        String string = "Production Stations:\n";
        string += "--------------------------------------------------------\n";

        string += String.format("%-15s%-15s%-15s%-15s\n", "Stage:", "Work[%]", "Starve[t]", "Block[t]");
        for (Stage stage: stages) {
            string += stage.workProportions(ts.getCurrentTime());
        }

        return string;
    }
}
