package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.*;
import java.awt.*;
import java.util.*;
public class Bear extends Animal{
    public Location territory;

    //Konstruktører
    //uden territorie
    Set<Location> bearLocations;
    public Bear(){
        di = new DisplayInformation(Color.BLACK,"bear",true);
        territory = null;
        energy = new int[]{150,150};
    }

    //med territorie
    public Bear(Location territory){
        di = new DisplayInformation(Color.BLACK,"bear",true);
        this.territory = territory;
        energy = new int[]{150,150};
    }

    //Metoder
    @Override
    public void act(World world) {

        if(territory == null) {
            //find et territory som den kan knytte sig til.
            // siden bjørne ikke er flok dyr som ulve, har de et individuelt territorie defor "finder den et territoerie der hvor den står" hvis den ikke har et.
            this.CreateTerritory(world);



        } else{
            this.BearMoveLogic(world);// bevæger sig i sit og mod det hvis den er ude af det territory.
        }

    }
/* Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den
’bor’. Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra.
Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet
 */

    /**
     * Returnerer Set med alle de felter hvor bjørnen bevæger sig.
     * @param world
     * @param territory bjønens start territory.
     * @return Set<Location>
     */
    public Set<Location> BearTerritory(World world, Location territory){
        // bjørnen bevæger sig kun inden for 2 radius af sit startfelt.
        int radius = 2;
        return world.getSurroundingTiles(territory,radius);

    }

    /**
     * Bevægelses logik for bjørn
     * den går inde i sit territoerie tilfældigt, hvis den ikke
     *
     * @param world
     * @param
     */
    public void BearMoveLogic(World world) {
        bearLocations = BearTerritory(world,territory);
        while(bearLocations.contains(world.getLocation(this))) {
            this.moveRandomly(world);
        }
        if(territory == null) {
            this.CreateTerritory(world);
        }
        this.moveTowards(world,this.territory);





    }

    public void CreateTerritory(World world) {
        Location location;
        if (this.territory == null) {
            location = world.getLocation(this);
            this.territory = location;
        }else {

        }

    }


}
