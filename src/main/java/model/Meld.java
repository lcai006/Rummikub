package model;

import java.util.ArrayList;
import java.util.Comparator;

public class Meld {
    private String type;
    private ArrayList<Tile> meld;

    public Meld(String str) {
        meld = new ArrayList<>();
        // create tiles from the string
        String[] tiles = str.split("\\s+");

        for (String tile: tiles) {
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
            Tile t = meld.get(0);
            meld.remove(0);
            meld.add(t);
        }
    }

    public void add(String list) {
        if (!validate(list)) {
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

    // Validates if the add action is valid
    public boolean validate(String list) {
        String[] tiles = list.split("\\s+");

        if (type.equals("set")) {
            // size of a set is 3 or 4
            if (tiles.length + meld.size() > 4) {
                return false;
            }
            ArrayList<String> colorList = new ArrayList<>();
            for (Tile t: meld) {
                colorList.add(t.color());
            }
            for (String tile: tiles) {
                Tile t = new Tile(tile);
                if (t.number() != meld.get(0).number()) {
                    return false;
                }
                if (colorList.contains(t.color())) {
                    return false;
                }
            }
        } else {
            // size of a run is between 3 and 13
            if (tiles.length + meld.size() > 13) {
                return false;
            }
            ArrayList<Integer> numList = new ArrayList<>();
            for (Tile t: meld) {
                numList.add(t.number());
            }
            for (String tile: tiles) {
                Tile t = new Tile(tile);
                if (!t.color().equals(meld.get(0).color())) {
                    return false;
                }
                numList.add(t.number());
            }

            // Checks if the sequence is valid
            numList.sort(null);
            int current = 0;
            if (numList.contains(1) && numList.contains(13)) {
                // if a run contains 1 and 13, ignore 1
                for (int num: numList) {
                    if (current == 0) {
                        current = num;
                    } else if (current == 1) {
                        current = num;
                    } else if (current + 1 == num) {
                        current = num;
                    } else {
                        return false;
                    }
                }
            } else {
                for (int num: numList) {
                    if (current == 0) {
                        current = num;
                    } else if (current + 1 == num) {
                        current = num;
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
