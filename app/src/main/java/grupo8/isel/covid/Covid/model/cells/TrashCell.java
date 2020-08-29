package grupo8.isel.covid.Covid.model.cells;

import grupo8.isel.covid.Covid.model.Dir;
import grupo8.isel.covid.Covid.model.actors.Actor;

public class TrashCell extends Cell {

    public static final char TYPE = 'V';

    public TrashCell(int l, int c) {
        super(l, c);
    }
    @Override
    public boolean canHaveActor() { return true; }
    @Override
    public char getType() {
        return TYPE;
    }
}
