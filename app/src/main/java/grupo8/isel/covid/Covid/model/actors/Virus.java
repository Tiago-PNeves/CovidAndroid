package grupo8.isel.covid.Covid.model.actors;

import grupo8.isel.covid.Covid.model.Dir;
import grupo8.isel.covid.Covid.model.Level;
import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.Covid.model.cells.TrashCell;

public class Virus extends Actor {

    public static final char TYPE = '*';

    public Virus(int dx, int dy) {
        super(dx, dy);
    }

    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        if (to.hasActor())
            return false;

        if (super.move(level, dir, from, to)) {
            if (to.getType() == TrashCell.TYPE) {
                level.removeActor(to);
                level.decrementVirusCount(1);
            }
            return true;
        }
        return false;
    }

        @Override
        public char getType() {
            return TYPE;
        }
    }

