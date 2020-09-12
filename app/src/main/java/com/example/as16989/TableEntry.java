package com.example.as16989;

public class TableEntry {

    int ID;
    String name;
    String desc;
    float score;
    boolean busy;

    public TableEntry(int ID, String name, String desc, float score, boolean busy) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.score = score;
        this.busy = busy;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isBusy() {
        return busy;
    }
    public int getID() {
        return ID;
    }
    public String getDesc() { return desc; }
    @Override
    public String toString() {
        return "TableEntry{" +
                "ID = " + ID +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", score=" + score +
                ", flag=" + busy +
                '}';
    }
}
