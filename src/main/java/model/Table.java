package model;

import java.util.ArrayList;

public class Table {
    private ArrayList<Meld> melds;
    public Table() {
        melds = new ArrayList<>();
    }

    public boolean isEmpty() {
        return melds.isEmpty();
    }

    public void createMeld(String list) {
        Meld m = new Meld(list);
        melds.add(m);
    }

    public void addTile(int num, String list) {
        melds.get(num - 1).add(list);
    }

    public void removeTile(int num, String list) {
        melds.get(num - 1).remove(list);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (Meld m: melds) {
            i++;
            str.append("Meld ").append(i).append(": ");
            str.append(m.toString()).append("\n");
        }
        return str.toString();
    }


}
