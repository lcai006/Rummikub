package model;

import java.util.ArrayList;
import java.util.Comparator;

public class Hand {
    private final ArrayList<Tile> red;
    private final ArrayList<Tile> green;
    private final ArrayList<Tile> blue;
    private final ArrayList<Tile> orange;

    public Hand(String str) {
        red = new ArrayList<>();
        green = new ArrayList<>();
        blue = new ArrayList<>();
        orange = new ArrayList<>();

        // create tiles from the string
        String[] tiles = str.split("\\s+");
        for (String tile: tiles) {
            Tile t = new Tile(tile);
            switch (t.color()) {
                case "R" -> red.add(t);
                case "G" -> green.add(t);
                case "B" -> blue.add(t);
                case "O" -> orange.add(t);
            }
        }

        sort();

    }

    // Sorts tiles in hand
    public void sort() {
        red.sort(Comparator.comparingInt(Tile::number));
        green.sort(Comparator.comparingInt(Tile::number));
        blue.sort(Comparator.comparingInt(Tile::number));
        orange.sort(Comparator.comparingInt(Tile::number));
    }

    public int size() {
        return red.size() + green.size() + blue.size() + orange.size();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Tile tile: red) {
            if (!str.isEmpty())
                str.append(" ");
            str.append(tile.toString());
        }

        for (Tile tile: green) {
            if (!str.isEmpty())
                str.append(" ");
            str.append(tile.toString());
        }

        for (Tile tile: blue) {
            if (!str.isEmpty())
                str.append(" ");
            str.append(tile.toString());
        }

        for (Tile tile: orange) {
            if (!str.isEmpty())
                str.append(" ");
            str.append(tile.toString());
        }

        return str.toString();
    }

    public String display() {
        StringBuilder r = new StringBuilder();
        StringBuilder g = new StringBuilder();
        StringBuilder b = new StringBuilder();
        StringBuilder o = new StringBuilder();

        for (Tile tile: red) {
            if (!r.isEmpty())
                r.append(" ");
            r.append(tile.toString());
        }
        if (!r.isEmpty())
            r.append("\n");

        for (Tile tile: green) {
            if (!g.isEmpty())
                g.append(" ");
            g.append(tile.toString());
        }
        if (!g.isEmpty())
            g.append("\n");

        for (Tile tile: blue) {
            if (!b.isEmpty())
                b.append(" ");
            b.append(tile.toString());
        }
        if (!b.isEmpty())
            b.append("\n");

        for (Tile tile: orange) {
            if (!o.isEmpty())
                o.append(" ");
            o.append(tile.toString());
        }
        if (!o.isEmpty())
            o.append("\n");

        return r.toString() + g + b + o;
    }

    // Adds a tile to the hand
    public void add(String tile) {
        Tile t = new Tile(tile);
        switch (t.color()) {
            case "R" -> red.add(t);
            case "G" -> green.add(t);
            case "B" -> blue.add(t);
            case "O" -> orange.add(t);
        }
        sort();
    }

    public void play(String list) {
        String[] tiles = list.split("\\s+");
        for (String tile: tiles) {
            Tile t = new Tile(tile);
            switch (t.color()) {
                case "R" -> red.remove(t);
                case "G" -> green.remove(t);
                case "B" -> blue.remove(t);
                case "O" -> orange.remove(t);
            }
        }
    }

    public int score() {
        int s = 0;
        for (Tile tile: red) {
            s += Math.min(tile.number(), 10);
        }

        for (Tile tile: green) {
            s += Math.min(tile.number(), 10);
        }

        for (Tile tile: blue) {
            s += Math.min(tile.number(), 10);
        }

        for (Tile tile: orange) {
            s += Math.min(tile.number(), 10);
        }
        return -s;
    }
}
