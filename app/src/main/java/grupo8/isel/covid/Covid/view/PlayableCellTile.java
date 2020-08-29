package grupo8.isel.covid.Covid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import grupo8.isel.covid.Covid.CovidActivity;
import grupo8.isel.covid.Covid.model.Level;
import grupo8.isel.covid.Covid.model.actors.Hero;
import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.R;
import grupo8.isel.covid.TileLib.Img;

public class PlayableCellTile extends CellTile {

        private static final int PLAYER_RES = R.drawable.nurse;
        private static final int VIRUS_RES = R.drawable.virus;
        private static final int DEAD_RES = R.drawable.dead;
        protected static Img deadImg;
        protected static Img playerImg;
        protected static Img virusImg;
        /**
         * Creates a new PlayableCellTile object
         * @param ctx Context
         * @param cell associated playable cell
         */
        PlayableCellTile(Context ctx, Cell cell) { super(ctx, cell); }

        /**
         * Creates a new PlayableCellTile object
         * @param ctx Context
         * @param cell associated playable cell
         */
        PlayableCellTile(Context ctx, Cell cell, int bgColor) { super(ctx, cell, bgColor); }

        {
            playerImg = generateImage(playerImg, ctx, PLAYER_RES);
            virusImg = generateImage(virusImg, ctx, VIRUS_RES);
        }

        @Override
        public void draw(Canvas canvas, int side) {
            super.draw(canvas, side);
            if (!cell.hasActor())
                return;

            String actorName = cell.getActor().getClass().getSimpleName();
            try {
                PlayableCellTile.class
                        .getDeclaredMethod("draw" + actorName, Canvas.class, int.class)
                        .invoke(this, canvas, side);
            } catch (IllegalAccessException |
                    InvocationTargetException |
                    NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        /**
         * Draws a player on this cell
         * @param canvas Canvas where to draw
         * @param side side length
         */

        protected void drawHero(Canvas canvas, int side) {
            Hero p = (Hero) cell.getActor();
            playerImg.draw(canvas, side, side, paint );
        }

        /**
         * Draws a box on this cell
         * @param canvas Canvas where to draw
         * @param side side length
         */
        protected void drawVirus(Canvas canvas, int side) { virusImg.draw(canvas, side, side, paint); }

        public void drawDeadHero(Canvas canvas, int side){
            level.getHeroCell();
            deadImg.draw(canvas, side, side, paint);
        }
    }



