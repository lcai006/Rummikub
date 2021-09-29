package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Meld {
    private String type;
    private ArrayList<Tile> meld;

    public Meld(String str) {
        meld = new ArrayList<>();
        // create tiles from the string
        String[] tiles = str.split("\\s+");

        if (tiles.length >= 3) {
            for (String tile : tiles) {
                Tile t = new Tile(tile);
                meld.add(t);
            }

            if (meld.get(0).number() == meld.get(1).number()) {
                type = "set";
            } else {
                type = "run";
            }

            sort();
        }
    }

    public int size() {
        return meld.size();
    }

    public String type() {
        return type;
    }

    public void sort() {
        meld.sort(Comparator.comparingInt(Tile::colorNum));
        meld.sort(Comparator.comparingInt(Tile::number));

        if (meld.get(0).number() == 1 && meld.get(meld.size() - 1).number() == 13 && meld.size() < 13) {
            int current = 0;
            while (meld.get(0).number() == current + 1) {
                current = meld.get(0).number();
                Tile t = meld.get(0);
                meld.remove(0);
                meld.add(t);
            }
        }
    }

    public void add(String list) {
        if (invalid(list)) {
            return;
        }

        String[] tiles = list.split("\\s+");

        for (String tile: tiles) {
            Tile t = new Tile(tile);
            meld.add(t);
        }

        sort();
    }

    public void remove(String list) {
        String[] tiles = list.split("\\s+");

        for (String tile: tiles) {
            Tile t = new Tile(tile);
            meld.remove(t);
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{");
        for (Tile tile: meld) {
            if (str.length() > 1) {
                str.append(" ");
            }
            str.append(tile.toString());
        }
        str.append("}");

        return str.toString();
    }

    // Validates the meld
    public boolean invalid(String list) {
        String[] tiles = list.split("\\s+");

        if (type.equals("set")) {
            // size of a set is 3 or 4
            if (tiles.length + meld.size() > 4) {
                return true;
            }
            ArrayList<String> colorList = new ArrayList<>();
            for (Tile t: meld) {
                colorList.add(t.color());
            }
            for (String tile: tiles) {
                Tile t = new Tile(tile);
                if (t.number() != meld.get(0).number()) {
                    return true;
                }
                if (colorList.contains(t.color())) {
                    return true;
                }
            }
        } else {
            // size of a run is between 3 and 13
            if (tiles.length + meld.size() > 13) {
                return true;
            }

            ArrayList<Integer> numList = new ArrayList<>();
            for (Tile t: meld) {
                numList.add(t.number());
            }

            if (!list.isEmpty()) {
                for (String tile : tiles) {
                    Tile t = new Tile(tile);
                    if (!t.color().equals(meld.get(0).color())) {
                        return true;
                    }
                    numList.add(t.number());
                }
            }

            // Checks if the sequence is valid
            numList.sort(null);
            int current = 0;
            if (numList.contains(1) && numList.contains(13) && numList.size() < 13) {
                for (int i = 0; i < numList.size(); i++) {
                    if (current == 0) {
                        current = numList.get(i);
                        numList.set(i, current+13);
                    } else if (current + 1 == numList.get(i)) {
                        current = numList.get(i);
                        numList.set(i, current+13);
                    }
                }
                numList.sort(null);
                current = 0;
                for (int num: numList) {
                    if (current == 0) {
                        current = num;
                    } else if (current + 1 == num) {
                        current = num;
                    } else {
                        return true;
                    }
                }
            } else {
                for (int num: numList) {
                    if (current == 0) {
                        current = num;
                    } else if (current + 1 == num) {
                        current = num;
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void newHighLight() {
        for (Tile t: meld) {
            t.newHighlight();
        }
    }

    public void newHighLight(String tiles) {
        ArrayList<String> list = new ArrayList<>(List.of(tiles.split(" ")));
        for (Tile t: meld) {
            if (list.contains(t.value())) {
                t.newHighlight();
            }
        }
    }

    public void moveHighLight(String tiles) {
        for (Tile t: meld) {
            if (tiles.contains(t.value())) {
                t.moveHighlight();
            }
        }
    }

    public void removeHighlight() {
        for (Tile t: meld) {
            t.removeHighlight();
        }
    }

    public String newTiles () {
        StringBuilder str = new StringBuilder();
        for (Tile tile: meld) {
            if (str.length() > 1) {
                str.append(" ");
            }
            if (tile.isNew()) {
                str.append(tile.value());
            }
        }

        return str.toString();
    }

    // Calculate score for a meld
    public int score() {
        int s = 0;
        for (Tile t: meld) {
            s += Math.min(t.number(), 10);
        }
        return s;
    }
}
