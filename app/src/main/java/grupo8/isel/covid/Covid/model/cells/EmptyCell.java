package grupo8.isel.covid.Covid.model.cells;

import grupo8.isel.covid.Covid.model.Dir;
import grupo8.isel.covid.Covid.model.actors.Actor;

public class EmptyCell extends Cell {

    public static final char TYPE = '.';
    public EmptyCell(int l, int c) {
        super(l, c);
    }
    @Override
    public boolean canHaveActor() { return true; }

    @Override
    public char getType() { return TYPE; }

}

