package Holes;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import Animals.Wolf;

public class WolfDen extends Hole implements Actor{
    private int packID;
    private List<Wolf> inDen;

    //Konstruktør
    /**
     * Skaber en ny ulve-hule, associeret med et bestemt flok-ID.
     * @param packID ID'en hvis' flok tilhører hulen
     */
    public WolfDen(int packID){
        this.di = new DisplayInformation(Color.cyan,"hole",true);
        this.packID = packID;
        this.inDen = new LinkedList<>();
    }

    //Metoder
    public void act(World world){
        if(world.isNight()){return;} //Ungerne kommer kun ud om dagen

        for(Wolf w : inDen){
            List<Location> tilesForBabies = new LinkedList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
            if(tilesForBabies.isEmpty()){
                for(Wolf q : inDen){
                    System.out.println("["+ this +"] suffocated in its den");
                }
                inDen.removeAll(inDen);
                return;
            }

            int r = new Random().nextInt(tilesForBabies.size());
            Location tileForBaby = tilesForBabies.get(r);
            world.setTile(tileForBaby, w);

            inDen.remove(w);
        }
    }

    /**
     * Returnerer hulens packID.
     * @return hulens packID
     */
    public int getPackID(){
        return packID;
    }

    /**
     * Tilføjer en ulv til hulens liste af huserede ulve.
     * @param wolf ulven der tilføjes til listen
     */
    public void addToDen(Wolf wolf){
        inDen.add(wolf);
    }
}