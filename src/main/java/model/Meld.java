package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Meld {
    private String type;
    private final ArrayList<Tile> meld;

    public Meld(String str) {
        meld = new ArrayList<>();
        // create tiles from the string
        String[] tiles = str.split("\\s+");

        if (tiles.length >= 3) {
            for (String tile : tiles) {
                Tile t = new Tile(tile);
                meld.add(t);
            }

            int num = 0;
            String color = "";
            int n = 0;
            int j = -1;
            for (Tile t: meld) {
                if (t.color().equals("Joker")) {
                    j = n;
                    continue;
                }
                n++;
                if (num == 0) {
                    num = t.number();
                    color = t.color();
                    continue;
                }

                if (t.number() == num && (type == null || type.equals("set"))) {
                    type = "set";
                } else if (t.color().equals(color) && (type == null || type.equals("run"))) {
                    type = "run";
                } else {
                    type = "invalid";
                }
            }
            if (j != -1) {
                if (type.equals("run")) {
                    int number;
                    String c;
                    if (j == 0) {
                        number = meld.get(j + 1).number();
                        c = meld.get(j + 1).color();
                        if (number == 1)
                            number = 13;
                        else
                            number = number - 1;

                    } else {
                        number = meld.get(j - 1).number();
                        c = meld.get(j - 1).color();
                        if (number == 13)
                            number = 1;
                        else
                            number = number + 1;

                    }
                    meld.get(j).setNumber(number);
                    meld.get(j).setColor(c);
                } else if (type.equals("set")) {
                    String c = "RGBO";
                    for (int i = 0; i < meld.size(); i++) {
                        if (i != j) {
                            c = c.replace(meld.get(i).color(), "");
                        }
                    }
                    if (j == 0) {
                        meld.get(j).setNumber(meld.get(j + 1).number());
                    } else {
                        meld.get(j).setNumber(meld.get(j - 1).number());
                    }
                    meld.get(j).setColor(c);
                }
            }

            sort();
        }
    }

    public Meld(Meld m) {
        this.type = m.type;
        this.meld = (ArrayList<Tile>) m.meld.clone();
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
            Tile t;
            if (tile.contains("Joker")) {
                t = new Tile("Joker");
                if (type.equals("run")) {
                    tile = tile.replace("Joker=", "");
                    Tile t1 = new Tile(tile);
                    t.setColor(t1.color());
                    t.setNumber(t1.number());
                } else if (type.equals("set")) {
                    String color = "RGBO";
                    for (Tile setMember: meld) {
                        color = color.replace(setMember.color(), "");
                        t.setNumber(setMember.number());
                    }
                    t.setColor(color);
                }
            } else {
                t = new Tile(tile);
            }
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

        switch (type) {
            case "invalid":
                return true;
            case "set":
                // size of a set is 3 or 4
                if (!list.isEmpty()) {
                    if (tiles.length + meld.size() > 4)
                        return true;
                } else {
                    if (meld.size() < 3 || meld.size() > 4)
                        return true;
                }
                if (meld.size() == 3 && list.equals("Joker"))
                    return false;

                String colorList = "RGBO";
                int num = 0;
                for (Tile t : meld) {
                    if (!colorList.contains(t.color()))
                        return true;
                    if (num == 0)
                        num = t.number();
                    else if (num != t.number())
                        return true;

                    if (t.color().length() > 1) {
                        for (int i = 0; i < t.color().length(); i++) {
                            colorList = colorList.replace(String.valueOf(t.color().charAt(i)), "");
                        }
                    } else {
                        colorList = colorList.replace(t.color(), "");
                    }
                }

                if (!list.isEmpty()) {
                    for (String tile : tiles) {
                        Tile t = new Tile(tile);
                        if (t.number() != meld.get(0).number()) {
                            return true;
                        }
                        if (!colorList.contains(t.color())) {
                            return true;
                        }
                    }
                }
                break;
            case "run":
                // size of a run is between 3 and 13
                if (!list.isEmpty()) {
                    if (tiles.length + meld.size() > 13)
                        return true;
                } else {
                    if (meld.size() < 3 || meld.size() > 13)
                        return true;
                }

                ArrayList<Integer> numList = new ArrayList<>();
                for (Tile t : meld) {
                    numList.add(t.number());
                }

                if (!list.isEmpty()) {
                    for (String tile : tiles) {
                        if (tile.contains("Joker"))
                            tile = tile.replace("Joker=", "");
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
                            numList.set(i, current + 13);
                        } else if (current + 1 == numList.get(i)) {
                            current = numList.get(i);
                            numList.set(i, current + 13);
                        }
                    }
                    numList.sort(null);
                    current = 0;
                    for (int number : numList) {
                        if (current == 0) {
                            current = number;
                        } else if (current + 1 ==  number) {
                            current = number;
                        } else {
                            return true;
                        }
                    }
                } else {
                    for (int number : numList) {
                        if (current == 0) {
                            current = number;
                        } else if (current + 1 == number) {
                            current = number;
                        } else {
                            return true;
                        }
                    }
                }
                break;
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
            } else if(tiles.contains("Joker") && t.value().equals("Joker")) {
                t.newHighlight();
            }
        }
    }

    public void moveHighLight(String tiles) {
        ArrayList<String> list = new ArrayList<>(List.of(tiles.split(" ")));
        for (Tile t: meld) {
            if (list.contains(t.value())) {
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

    public Tile getJoker() {
        if (toString().contains("Joker")) {
            for (Tile t: meld) {
                if (t.value().equals("Joker")) {
                    return t;
                }
            }
        }
        return null;
    }
}
