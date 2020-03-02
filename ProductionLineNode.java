/*
    Name: James McCulloch
    Student No: 3291441
    Date: 04/06/2019
    Course: SENG2200
 */

import java.util.LinkedList;
import java.util.List;

//represents a node on the production line.
public class ProductionLineNode {
    private List<ProductionLineNode> prev, next;
    private double time;
    private String name;

    public ProductionLineNode() {
        prev = new LinkedList<>();
        next = new LinkedList<>();
        time = 0;
        name = "";
    }

    public ProductionLineNode(String name, ProductionLineNode prev, ProductionLineNode next) {
        this.prev = new LinkedList<>();
        if (prev != null) {
            this.prev.add(prev);
        }
        this.next = new LinkedList<>();
        if (next != null) {
            this.next.add(next);
        }
        time = 0;
        this.name = name;
    }

    public List<ProductionLineNode> getNext() {
        return next;
    }

    public void setNext(ProductionLineNode next) {
        this.next.add(next);
    }

    public List<ProductionLineNode> getPrev() {
        return prev;
    }

    public void setPrev(ProductionLineNode prev) {
        this.prev.add(prev);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
