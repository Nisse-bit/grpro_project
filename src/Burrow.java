import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import java.awt.Color;

public class Burrow implements DynamicDisplayInformationProvider{
    private DisplayInformation di;

    //Konstruktør
    public Burrow(){
        di = new DisplayInformation(Color.darkGray,"hole-small",true);
    }

    //Metoder
    @Override public DisplayInformation getInformation(){
        return di;
    }
}
