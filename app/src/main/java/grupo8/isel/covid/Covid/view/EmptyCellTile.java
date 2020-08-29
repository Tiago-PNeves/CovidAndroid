package grupo8.isel.covid.Covid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import grupo8.isel.covid.Covid.model.cells.Cell;

public final class EmptyCellTile extends PlayableCellTile {

    private static final int BACKGROUND_COLOR = Color.parseColor("#2D5F7A");

    /**
     * Creates a new FloorCellTile object
     * @param ctx Context
     * @param cell associated floor cell
     */
    EmptyCellTile(Context ctx, Cell cell) { super(ctx, cell, BACKGROUND_COLOR); }

}