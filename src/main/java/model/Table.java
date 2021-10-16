package model;

import java.util.ArrayList;

public class Table {
    private final ArrayList<Meld> melds;
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

    public Meld getMeld(int i) {
        return melds.get(i);
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

    public void checkMelds() {
        for (Meld m: new ArrayList<>(melds)) {
            if (m.size() == 0) {
                melds.remove(m);
                continue;
            }
            if (m.type().equals("run") && m.invalid("")) {
                // Split run
                String newTiles = m.newTiles();
                m.removeHighlight();
                String list = m.toString();
                list = list.replace("{", "");
                list = list.replace("}", "");
                ArrayList<String> newMelds = new ArrayList<>();
                StringBuilder tiles = new StringBuilder();
                int num = 0;
                for (String tile: list.split(" ")) {
                    Tile t = new Tile(tile);
                    if (num == 0) {
                        num = t.number();
                        if (!tiles.isEmpty())
                            tiles.append(" ");
                        tiles.append(t.value());
                    } else if (num != t.number() - 1 && num != 13) {
                        newMelds.add(tiles.toString());
                        tiles.setLength(0);
                        tiles.append(t.value());
                        num = t.number();
                    } else {
                        if (!tiles.isEmpty())
                            tiles.append(" ");
                        tiles.append(t.value());
                        num = t.number();
                    }
                }
                newMelds.add(tiles.toString());
                for (int i = 0; i < newMelds.size(); i++) {
                    if (i != 0) {
                        m.remove(newMelds.get(i));
                        createMeld(newMelds.get(i));
                        newHighLight(size() - 1, newTiles);
                    }
                }
            }
        }
    }
}
