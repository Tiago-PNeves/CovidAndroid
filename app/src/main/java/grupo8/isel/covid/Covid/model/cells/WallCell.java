package grupo8.isel.covid.Covid.model.cells;

public class WallCell extends Cell {

    public static final char TYPE = 'X';

    public WallCell(int l, int c) {
        super(l, c);
    }

    @Override
    public char getType() {
        return TYPE;
    }
}
