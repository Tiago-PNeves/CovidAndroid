package grupo8.isel.covid.Covid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.DrawableRes;

import grupo8.isel.covid.Covid.model.Level;
import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.Covid.model.cells.TrashCell;
import grupo8.isel.covid.Covid.model.cells.WallCell;
import grupo8.isel.covid.TileLib.Img;
import grupo8.isel.covid.TileLib.Tile;

public abstract class CellTile implements Tile {

    protected static final Paint paint = new Paint();
    protected Context ctx;
    protected Cell cell;
    protected Level level;
    private int defaultBackground;
    private Img img;

    CellTile(Context ctx, Cell cell) { this(ctx, cell, Color.BLACK); }

    CellTile(Context ctx, Cell cell, int defaultBackground) {
        this.ctx = ctx;
        this.cell = cell;
        this.defaultBackground = defaultBackground;
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawColor(defaultBackground);

        if (img != null)
            img.draw(canvas, side, side, paint);
    }

    @Override
    public boolean setSelect(boolean selected) { return false; }

    /**
     * Sets the background image
     * @param img image to be set
     */
    public void setTileBackgroundImage(Img img) { this.img = img; }

    /**
     * Generates a new image or uses one that already exists
     * This method is used to prevent creation of Img objects for
     * every single CellTile, since they can occupy some memory
     * @param cache Cache for the Image object (null if the cache is empty)
     * @param ctx Context
     * @param resId an Android drawable resource
     * @see Img
     * @return a new Image object (that can be saved on the cache), or the cache
     */
    static Img generateImage(Img cache, Context ctx, @DrawableRes int resId) {

        return cache == null ? new Img(ctx, resId) : cache;
    }

    public static CellTile tileOf(Context ctx, Cell cell) {
        if (cell == null)
            return null;
        if (cell instanceof TrashCell)
            return new TrashCellTile(ctx, cell);
        else if (cell instanceof WallCell)
            return new WallCellTile(ctx, cell);
        else
            return new EmptyCellTile(ctx, cell);
    }
}


