package grupo8.isel.covid.Covid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

import grupo8.isel.covid.Covid.model.Dir;
import grupo8.isel.covid.Covid.model.Game;
import grupo8.isel.covid.Covid.model.Level;
import grupo8.isel.covid.Covid.model.Loader;
import grupo8.isel.covid.Covid.model.actors.Virus;
import grupo8.isel.covid.Covid.model.cells.Cell;
import grupo8.isel.covid.Covid.view.CellTile;
import grupo8.isel.covid.R;
import grupo8.isel.covid.TileLib.TilePanel;

public class CovidActivity extends Activity {
    public static final String APP_NAME = "Covid";
    private static final String FILE = "Covid.txt";
    private TextView levels, virus, message;
    private Level.Observer observer;
    private Game model;
    private Level level;
    private TilePanel panel;
    private int levelNumber = 0;
    private LinearLayout gameLayout;
    private Button button;
    private Button idioma;
    private Button save;
    private Button load;
    TextView levelString;
    private boolean isGameOver = false;
    private boolean isEnglish = true;
    private String gameOver = "Game Over";
    private String gameWin = "Level Completed";
    private String noLevels = "No more levels";
    private int levelToSave;
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        levelString = findViewById(R.id.levelID);
        levelString.setText("Level:");
        observer = new LevelObserver();
        levels = findViewById(R.id.levels);
        virus = findViewById(R.id.virus);
        save = findViewById(R.id.save);
        save.setOnClickListener( v-> save());
        load = findViewById(R.id.load);
        load.setOnClickListener( v-> load());
        virus.setText("0");
        message= findViewById(R.id.message);
        model = new Game(getResources().openRawResource(R.raw.levels));
        panel = findViewById(R.id.panel);
        findViewById(R.id.left).setOnClickListener( v-> move(Dir.LEFT) );
        findViewById(R.id.right).setOnClickListener( v-> move(Dir.RIGHT) );
        button = findViewById(R.id.ok);
        panel.setHeartbeatListener(300,(n,t)-> move(Dir.DOWN));
        gameLayout = findViewById(R.id.game_layout);
        idioma = findViewById(R.id.langSelect);
        idioma.setOnClickListener(v -> changeLang());
        loadNextLevel();
        virus.setText(level.getRemainingViruses());
        levels.setText(String.valueOf(levelNumber));
    }

    private void changeLang() {
        if (isEnglish){
            toPortuguese();
            isEnglish = false;
        }

        else {
            toEnglish();
            isEnglish = true;
            }
        }

    private void toEnglish(){
        gameOver = "Game Over";
        gameWin = "Level Completed";
        noLevels = "No more levels";
        levelString.setText("Level:");
        idioma.setText("PORTUGUÊS");
        save.setText("SAVE");
        load.setText("LOAD");
    }

    private void toPortuguese(){
        gameOver = "Fim do jogo";
        gameWin = "Nível Completo";
        noLevels = "Fim dos níveis";
        levelString.setText("Nível:");
        idioma.setText("ENGLISH");
        save.setText("GRAVAR");
        load.setText("CARREGAR");
    }

    private void moveGravity() {
        Dir d = Dir.DOWN;
        LinkedList<Virus> list = level.getViruses();
        for (Virus v : list) {
            int x = v.x;
            int y = v.y;
            int nextY = y+d.dl;
            Cell from = level.getCell(y, x);
            Cell to = level.getCell(nextY, x);
            if (to!=null) if (v.move(level, d, from, to)){
                v.y = nextY;
                virus.setText(String.valueOf(level.getRemainingViruses()));
                }
            }
        }

    private void move(Dir d) {
        if(d==Dir.DOWN)
            moveGravity();
        int x = level.getHeroCell().column;
        int y = level.getHeroCell().line;
        int nextX = x + d.dc;
        int nextY = y + d.dl;
        moveActivePlayer(x, y, nextX, nextY);
        panel.removeHeartbeatListener();
        panel.setHeartbeatListener(300, (n, t) -> move(Dir.DOWN));
    }

    public void save(){
        try (PrintWriter out = new PrintWriter(openFileOutput(FILE, MODE_PRIVATE))) {
            levelToSave= levelNumber;
            model.saveButton(out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        isGameOver = false;
        message.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        findViewById(R.id.left).setOnClickListener(v -> move(Dir.LEFT));
        findViewById(R.id.right).setOnClickListener(v -> move(Dir.RIGHT));

        try (Scanner in = new Scanner(openFileInput(FILE)))  {
            Level levelAux = model.loadButton(in, levelToSave);
            if (levelAux==null) return;
            level = levelAux;
            level.setObserver(observer);
            loadLevel();
        } catch (FileNotFoundException | NullPointerException | Loader.LevelFormatException e) {

            e.printStackTrace();
        }
    }

@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isEnglish", isEnglish);
        outState.putInt("level_number", level.getNumber());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        model.save(bos);
        outState.putByteArray("level", bos.toByteArray());

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        try {
            isEnglish = (savedState.getBoolean("isEnglish"));
            if (!isEnglish) toPortuguese();
            int levelNumber = savedState.getInt("level_number", 1);
            ByteArrayInputStream is = new ByteArrayInputStream(savedState.getByteArray("level"));
            level = model.load(is, levelNumber);
            level.setObserver(observer);
            loadLevel();

        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
        }
    }

    private boolean loadNextLevel() {
       try {
            Level temp = model.loadNextLevel();
            if (temp == null) {
                idioma.setOnClickListener(null);
                message.setText(noLevels);
                button.setOnClickListener(v -> finishAndRemoveTask());
                isGameOver = true;
                return false;
            }
            message.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            findViewById(R.id.left).setOnClickListener( v-> move(Dir.LEFT) );
            findViewById(R.id.right).setOnClickListener( v-> move(Dir.RIGHT) );
            level = temp;
            level.setObserver(observer);
            loadLevel();
            virus.setText(String.valueOf(level.getRemainingViruses()));
            return true;
        } catch (Loader.LevelFormatException e) {
            e.printStackTrace();
            }
        return false;
    }

    private void loadLevel() {
        if (level == null)
            return;

        int lw = level.getWidth(), lh = level.getHeight();
        panel.setSize(lw, lh);
        levelNumber = level.getNumber();
        levels.setText(String.valueOf(levelNumber));
        virus.setText(String.valueOf(level.getRemainingViruses()));
        for (int y = 0; y < lh; ++y) {
            for (int x = 0; x < lw; ++x) {
                Cell cell = level.getCell(y, x);
                panel.setTile(x, y, CellTile.tileOf(this, cell));
            }
        }
    }


    private boolean moveActivePlayer(int xFrom, int yFrom, int xTo, int yTo) {
        Dir dir = calculateDirection(xFrom, yFrom, xTo, yTo);
        if (dir != null) return level.moveMan(dir);

        return false;
    }

    private static Dir calculateDirection(int xFrom, int yFrom, int xTo, int yTo) throws IllegalArgumentException {
        int difX = xTo - xFrom, difY = yTo - yFrom;
        return Dir.fromVector(difX, difY);
    }

    private class LevelObserver implements Level.Observer{
        @Override
        public void onCellUpdated(Cell cell) {
            panel.invalidate(cell.column, cell.line);
        }

        @Override
        public void onPlayerDead() {
            isGameOver = true;
            message.setVisibility(View.VISIBLE);
            message.setText(gameOver);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> finishAndRemoveTask());
            findViewById(R.id.left).setOnClickListener(null);
            findViewById(R.id.right).setOnClickListener(null);
        }

        @Override
        public void onLevelWin() {
            if(!isGameOver) {
                message.setVisibility(View.VISIBLE);
                message.setText(gameWin);
                button.setVisibility(View.VISIBLE);
                findViewById(R.id.left).setOnClickListener(null);
                findViewById(R.id.right).setOnClickListener(null);
                button.setOnClickListener(v -> loadNextLevel());
                }
            }
        }
}


