package model;

public class Tile {
    private final String str;
    private final String color;
    private final int number;
    private boolean newHighlighted = false;
    private boolean moveHighlighted = false;

    public Tile(String val) {
        color = val.substring(0, 1);
        if (val.length() == 2)
            number = Integer.parseInt(val.substring(1, 2));
        else
            number = Integer.parseInt(val.substring(1, 3));

        str = val;
    }

    public String color() {
        return this.color;
    }

    public int number() {
        return this.number;
    }

    public void newHighlight() {
        newHighlighted = true;
    }

    public void moveHighlight() {
        moveHighlighted = true;
    }

    public void removeHighlight() {
        newHighlighted = false;
        moveHighlighted = false;
    }

    public String toString() {
        String text;
        if (newHighlighted)
            text = "*" + str;
        else if (moveHighlighted)
            text = "!" + str;
        else
            text = str;

        return text;
    }
}
