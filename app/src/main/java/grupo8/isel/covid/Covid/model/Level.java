package grupo8.isel.covid.Covid.model;

import java.io.PrintWriter;
import java.util.LinkedList;
import grupo8.isel.covid.Covid.model.actors.*;
import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.Covid.model.cells.EmptyCell;

public class Level {

    private final int levelNumber;
    private final int height;
    private final int width;
    private Cell heroCell;
    private LinkedList<Virus> viruses = new LinkedList<>();
    private Cell[][] board;
    private Game game;
    private Level.Observer observer;
    private int virus=0;
    private boolean manIsDead;

    /**
     * Constructor for each level
     * @param levelNumber Level number
     * @param height Height of the level
     * @param width Width of the level
     */
    Level(int levelNumber, int height, int width) {
        this.levelNumber = levelNumber;
        this.height = height;
        this.width = width;

        board = new Cell[height][width];

    }

    /**
     * Inits the current level with the game
     * @param game Game
     */
    public void init(Game game) {
        this.game = game;
    }

    public Cell getCell(int l, int c) {
        if (c >= width || c < 0 || l >= height || l < 0)
            return null;

        return board[l][c];
    }

    /**
     * Resets the current level
     */
    public void reset() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                board[j][i] = null;
        }
        virus = 0;
        manIsDead = false;
    }
    public void removeActor(Cell cell) {
        if (cell.hasActor()) {
            cell.setActor(null);
            updateCell(cell);
        }
    }
    public int getNumber() {
        return levelNumber;
    }
    public int getWidth() {
        return width;

    }
    public int getHeight() {
        return height;
    }

    private Actor getActorByType(char type, Cell cell) {
        if(type == Hero.TYPE) {
            heroCell = cell;
            return new Hero(cell.column, cell.line);
        }
        if(type == Virus.TYPE){
            Virus v = new Virus(cell.column, cell.line);
            viruses.add(v);
            ++virus;
            return v;
        }
        return null;
    }

    public boolean moveMan(Dir dir) {

        Cell currCell = getHeroCell();
        int l = currCell.line, c = currCell.column;
        int nextL = l + dir.dl;
        int nextC = c + dir.dc;

        Cell nextCell = getCell(nextL, nextC);
        if (nextCell == null)
            return false;

        Hero player = (Hero) currCell.getActor();
        boolean canMove = player.move(this, dir, currCell, nextCell);
        if (canMove)
            heroCell = nextCell;
        manIsDead = player.isDead();
        if (manIsDead)
            observer.onPlayerDead();
        else if (virus == 0)
            observer.onLevelWin();
        return canMove;
    }

    public void put(int l, int c, char type) {
        Cell cell = Cell.getCellByType(l, c, type);
        if (cell == null) {
            cell = board[l][c];
            if (cell == null) {
                cell = new EmptyCell(l, c);
                board[l][c] = cell;
            }
            cell.setActor(getActorByType(type, cell));
            return;
        }

        if (board[l][c] == null)
            board[l][c] = cell;
    }

    public Cell getHeroCell() { return heroCell; }

    public LinkedList<Virus> getViruses() { return viruses; }

    public void decrementVirusCount(int delta) { virus -= delta; }

    public void setObserver(Observer observer) { this.observer = observer; }

    public String getRemainingViruses() { return String.valueOf(virus); }

    public void saveState(PrintWriter out) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Cell cell = board[y][x];
                char type = cell.getType();
                if (cell.hasActor()) {
                    char actor = cell.getActor().getType();
                    out.print(actor);
                } else {
                    out.print(type);
                }
            }

            out.println();
        }

   }
    public void updateCell(Cell cell) { observer.onCellUpdated(cell); }

    public interface Observer {

        void onCellUpdated(Cell cell);

        void onPlayerDead();

        void onLevelWin();

    }
}
