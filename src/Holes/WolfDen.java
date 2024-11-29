package Holes;
import itumulator.executable.DisplayInformation;
import java.awt.Color;

public class WolfDen extends Hole{
    private int packID;

    //Konstruktør
    public WolfDen(int packID){
        this.di = new DisplayInformation(Color.cyan,"hole",true);
        this.packID = packID;
    }

    //Metoder
    /**
     * Returnerer hulens packID.
     * @return hulens packID
     */
    public int getPackID(){
        return packID;
    }
}