package Animals;

import itumulator.executable.DisplayInformation;
import java.awt.Color;

public class Wolf extends Animal {

    //Konstruktør
    public Wolf() {
        di = new DisplayInformation(Color.RED,"wolf",true);
    }
}