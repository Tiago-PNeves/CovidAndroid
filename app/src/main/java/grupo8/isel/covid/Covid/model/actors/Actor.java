package grupo8.isel.covid.Covid.model.actors;

import grupo8.isel.covid.Covid.model.Dir;
import grupo8.isel.covid.Covid.model.Level;
import grupo8.isel.covid.Covid.model.cells.Cell;

public abstract class Actor {
    public int x, y;
    public Actor(int dx, int dy){
        this.x = dx;
        this.y = dy;
    }
    public boolean move(Level level, Dir dir, Cell from, Cell to) {
        Actor actor = from.getActor();
        if (to.canHaveActor()) {
            to.setActor(actor);
            from.setActor(null);
            level.updateCell(from);
            level.updateCell(to);
            return true;
        }

        return false;
    }
    public abstract char getType();

}
