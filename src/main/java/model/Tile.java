package model;

public class Tile {
    private final String str;
    private String color;
    private int color_num;
    private int number;
    private boolean newHighlighted = false;
    private boolean moveHighlighted = false;

    public Tile(String val) {
        if (val.equals("Joker")) {
            color = "Joker";
        } else {
            color = val.substring(0, 1);
            // use color number to organize tiles in "RGBO" order.
            switch (color) {
                case "R" -> color_num = 0;
                case "G" -> color_num = 1;
                case "B" -> color_num = 2;
                case "O" -> color_num = 3;
                default -> color_num = 4;
            }
            if (val.length() == 2)
                number = Integer.parseInt(val.substring(1, 2));
            else
                number = Integer.parseInt(val.substring(1, 3));
        }

        str = val;
    }

    public String color() {
        return this.color;
    }

    public int number() {
        return this.number;
    }

    public void setColor(String color) {
        this.color = color;
        switch (color) {
            case "R" -> color_num = 0;
            case "G" -> color_num = 1;
            case "B" -> color_num = 2;
            case "O" -> color_num = 3;
            default -> color_num = 4;
        }
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public int colorNum() {
        return this.color_num;
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
        if (moveHighlighted)
            text = "!" + str;
        else if (newHighlighted)
            text = "*" + str;
        else
            text = str;

        return text;
    }

    // Value without highlight
    public String value() {
        return str;
    }

    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        return (this.toString().equals(obj.toString()));
    }

    public boolean isNew() {
        return newHighlighted;
    }
}
