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

    public void addTile(int i, String list) {
        melds.get(i).add(list);
    }

    public void removeTile(int i, String list) {
        melds.get(i).remove(list);
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

    public void newHighLight(int i) {
        melds.get(i).newHighLight();
    }

    public void newHighLight(int i, String tiles) {
        melds.get(i).newHighLight(tiles);
    }

    public void moveHighLight(int i, String tiles) {
        melds.get(i).moveHighLight(tiles);
    }

    public void removeHighlight() {
        for (Meld m: melds) {
            m.removeHighlight();
        }
    }

    public int size() {
        return melds.size();
    }

}
