package grupo8.isel.covid.Covid.model.cells;

import grupo8.isel.covid.Covid.model.actors.Actor;

public abstract class Cell {
    protected Actor actor;
    public final int line;
    public final int column;

    public Cell(int l, int c) {
        line = l;
        column = c;
    }
    public boolean canHaveActor() { return false; }

    public final void setActor(Actor actor) { this.actor = actor; }

    public final Actor getActor() {
        return actor;
    }

    public final boolean hasActor() {
        return actor != null;
    }

    public abstract char getType();

    public static Cell getCellByType(int l, int c, char type) {
        switch (type) {
            case WallCell.TYPE:
                return new WallCell(l, c);
            case TrashCell.TYPE:
                return new TrashCell(l, c);
            case EmptyCell.TYPE:
                return new EmptyCell(l, c);
            default:
                return null;
        }
    }

}
