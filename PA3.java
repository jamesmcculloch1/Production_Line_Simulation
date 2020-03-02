/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

public class PA3 {
    public static void main(String[] args) {
        ProductionLine productionLine;

        //if user provides information
        if (args.length > 2) {
            int userMean = Integer.parseInt(args[0]);
            int userRange = Integer.parseInt(args[1]);
            int userStorageMax = Integer.parseInt(args[2]);
            productionLine = new ProductionLine(userStorageMax, userMean, userRange);
        } else {
            productionLine = new ProductionLine(7, 1000, 1000);
        }
        //runs the simulation
        productionLine.beginProduction();

        //prints out the tables
        System.out.println(productionLine.stageInfo());
        System.out.println(productionLine.storageInfo());
        System.out.println(productionLine.productionPathInfo());
    }
}
