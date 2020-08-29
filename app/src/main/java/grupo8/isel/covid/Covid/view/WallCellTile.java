package grupo8.isel.covid.Covid.view;

import android.content.Context;

import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.R;
import grupo8.isel.covid.TileLib.Img;

public class WallCellTile extends CellTile {

    private static final int RESID = R.drawable.wall;

    private static Img image;

    /**
     * Creates a new DoorCellTile object
     * @param ctx Context
     * @param cell associated door cell
     */
    WallCellTile(Context ctx, Cell cell) {
        super(ctx, cell);
        image = generateImage(image, ctx, RESID);
        setTileBackgroundImage(image);
    }

}


