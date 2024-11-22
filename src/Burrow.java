import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import java.util.List;
import java.util.LinkedList;

public class Burrow implements DynamicDisplayInformationProvider{
    private DisplayInformation di;
    private List<Rabbit> associatedRabbits;

    //Konstruktør
    public Burrow(){
        di = new DisplayInformation(Color.orange,"hole-small",true);
        associatedRabbits = new LinkedList<>();
    }

    //Metoder
    @Override public DisplayInformation getInformation(){
        return di;
    }

    //Metoder til associates-listen
    public void addRabbit(Rabbit rabbit){
        associatedRabbits.add(rabbit);
    }
    public List<Rabbit> getAssociates(){return associatedRabbits;}
}
