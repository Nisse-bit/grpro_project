package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.*;
import java.awt.*;
import java.util.*;
public class Bear extends Animal{
    Location territory;
    //Konstruktør
    Set<Location> bearLocations;
    public Bear(){
        super();
        di = new DisplayInformation(Color.BLACK,"bear",true);

    }
/* Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den
’bor’. Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra.
Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet
 */

    /**
     * REtunere Set med alle de felter hvor bjørnen bevæger sig.
     * @param world
     * @param territory bjønens start territory.
     * @return Set<Location>
     */
    public Set<Location> BearTerritory(World world, Location territory){
        // bjørnen bevæger sig kun inden for 2 radius af sit startfelt.
        int radius = 2;
        return world.getSurroundingTiles(territory,radius);

    }

    public void BearMoveinsideTerritory(World world,Location Territory) {
        bearLocations = BearTerritory(world,territory);
        //this.Bear.Move


    }


}
