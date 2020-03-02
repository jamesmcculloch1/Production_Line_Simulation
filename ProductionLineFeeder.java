/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

//used to supply unlimited items to the first stage
public class ProductionLineFeeder extends Storage {

    public ProductionLineFeeder(Stage firstStage) {
        super();
    }

    @Override
    public boolean addItem(Item item, double currentTime) {
        throw new IllegalArgumentException("Shouldnt be accessing this method");
    }

    @Override
    public Item takeItem(double currentTime) {
        //create an item based on the specifications to feed into the production line
        return new Item();
    }


}
