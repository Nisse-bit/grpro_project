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
        FileReader fReader = new FileReader("");

        //----------- World Setup --------
        int size = fReader.getWorldSize();
        int delay = 75;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();
        //---------------*----------------

        //-------- Placement of Objects ---------
        EntityMap = fReader.getEntityMap(); // virker ikke fordi metoden er stati



        int amount = 2;
        int amountNBO = 3;
        // skal slettes på et tidspunkt siden den skal tage det fra filereaderen.
        // giver en location ud fra amount og [putter dem ned der også]
        giveRandomLocation(amount, p); // + Type type eller Object object
        giveRandomLocation(amount, p); // + Type type eller Object object

        p.show();


    }



    public static void giveRandomLocation(int amount, Program program) {
        World w = program.getWorld();
        int size = w.getSize();
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);

            Location l = new Location(x, y);
            while (!w.isTileEmpty(l)) {  //
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }
        }
        //Mangler at set tiles og at checke om det er et ledigt tile.
        // .setTile(l, XXX );
        //----------*----------------
        // w.setTile(new Location(0, 0), new <MyClass>());
    }
}