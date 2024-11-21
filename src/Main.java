import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.*;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {
    // Filereader map, med Type som String/Key og integer som
    FileReader fReader;
    HashMap<String,Integer> EntityMap = fReader.getEntityMap();
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fReader = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\week-1\\t1-1c.txt");

        //----------- World Setup --------
        int size = fReader.getWorldSize();
        int delay = 75;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();
        //---------------*----------------

        //-------- Placement of Objects ---------
        //Placér blocking objects
        if ((size * size) < fReader.getEntityAmount()) {
            throw new IllegalArgumentException("*** World is smaller than total animals");
        } else {
            for (Object o : fReader.getEntityList()) {
                //Tildel tilfældig lokation
                Random r = new Random();
                int x = r.nextInt(size);
                int y = r.nextInt(size);

                Location l = new Location(x, y);
                while (!w.isTileEmpty(l)) {  //
                    x = r.nextInt(size);
                    y = r.nextInt(size);
                    l = new Location(x, y);
                }

                //Sæt objektet ind i verden
                w.setTile(l, o);
            }
        }
        //Placér non-blocking objects
        if ((size * size) < fReader.getNBOAmount()) {
            throw new IllegalArgumentException("*** World is smaller than total Objects");
        } else {

            for (Object nbo : fReader.getNboList()) {
                //Tildel tilfældig lokation
                Random r = new Random();
                int x = r.nextInt(size);
                int y = r.nextInt(size);

                Location l = new Location(x, y);
                while (w.getNonBlocking(l) != null) {  //
                    x = r.nextInt(size);
                    y = r.nextInt(size);
                    l = new Location(x, y);
                }

                //Sæt objektet ind i verden
                w.setTile(l, nbo);
            }
        }

        p.show();


    }
}