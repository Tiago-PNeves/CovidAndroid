package grupo8.isel.covid.Covid.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Game {

    private int levelNumber = 0;
    private Level curLevel = null;
    private final int width=9, height=9;
    private final InputStream input;

    public Game(InputStream levelsFile) {
        input = levelsFile.markSupported() ? levelsFile : new BufferedInputStream(levelsFile);

    }

    public Level loadNextLevel() throws Loader.LevelFormatException {
        Level temp = new Loader(createScanner()).load(levelNumber + 1);
        if (temp != null) {
            curLevel = temp;
            ++levelNumber;
            curLevel.init(this);
        }

        return temp;
    }

    public Level loadButton(Scanner is, int levelToLoad) throws Loader.LevelFormatException {
        if (levelToLoad==0) return null;
        this.levelNumber = levelToLoad;
        curLevel = new Loader(is).load(levelToLoad);
        if (curLevel != null)
            curLevel.init(this);

        return curLevel;
    }

    public void saveButton(PrintWriter out){
        int lw = curLevel.getWidth(), lh = curLevel.getHeight();
        out.println("#" + levelNumber + " " + lh + " x " + lw);
        curLevel.saveState(out);
        out.close();
    }


    public Level load(InputStream is, int levelNumber) throws Loader.LevelFormatException {
        this.levelNumber = levelNumber;
        curLevel = new Loader(new Scanner(is)).load(levelNumber);
        if (curLevel != null)
            curLevel.init(this);

        return curLevel;
    }

    public void save(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        int lw = curLevel.getWidth(), lh = curLevel.getHeight();
        pw.println("#" + levelNumber + " " + lh + " x " + lw);
        curLevel.saveState(pw);
        pw.close();
    }

    private Scanner createScanner() {
        try {
            input.reset();
            return new Scanner(input);
        } catch (IOException e) {
            throw new RuntimeException("IOException",e);
        }
    }
}
