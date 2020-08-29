package grupo8.isel.covid.Covid.model.actors;

import grupo8.isel.covid.Covid.model.Dir;
import grupo8.isel.covid.Covid.model.Level;
import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.Covid.model.cells.TrashCell;

public class Hero extends Actor{
    public static final char TYPE = '@';
    private boolean dead;

    public Hero(int dx, int dy) {
        super(dx, dy);
    }

    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        if (to.hasActor()) {
            Actor toActor = to.getActor();
            Cell other = level.getCell(to.line + dir.dl, to.column + dir.dc);
            if (other == null) return false;

            if (!toActor.move(level, dir, to, other)) {
                return false;
            }

            toActor.x = other.column;
            toActor.y = other.line;
        }
        if (to.getType() == TrashCell.TYPE) {
            dead = true;
            return false;
        }
        return super.move(level, dir, from, to);
    }


    @Override
    public char getType() {
        return TYPE;
    }

    public boolean isDead() { return dead; }

}
