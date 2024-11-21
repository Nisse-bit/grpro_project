import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {

        try {
            Filereader start = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\week-1\\t1-1a.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //----------- World Setup --------
        int size = start.getWorldSize();
        int delay = 75;
        int display_size = 800;
        Program p = new Program(size, display_size, delay);
        World w = p.getWorld();

        p.show();
        for (int i = 0; i < 10; i++) {
            p.simulate();
        }
    }

    /*public static void displayInformation(Program p) {
        //DisplayInformation - Rabbit
        DisplayInformation diR = new DisplayInformation(Color.white, "rabbit-large");
        p.setDisplayInformation(Rabbit.class, diR);
        //DisplayInformation - Grass
        DisplayInformation diG = new DisplayInformation(Color.green, "grass", true);
        p.setDisplayInformation(Grass.class, diG);

        //DisplayInformation - Hole
        DisplayInformation diH = new DisplayInformation(Color.black, "hole", true);
        p.setDisplayInformation(Grass.class, diH);
    }*/
}
