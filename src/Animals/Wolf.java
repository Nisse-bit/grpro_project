package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class Wolf extends Animal {
    private List<Wolf> pack = new LinkedList<>();
    private boolean hasMadePack;

    //Konstruktør
    public Wolf(int packSize) {
        di = new DisplayInformation(Color.RED,"wolf-small",true);
        energy = new int[]{100,100};
        hasMadePack = false;

        while(packSize-- > 0){
            pack.add(new Wolf(0));
        }
    }

    //Metoder
    public void makePack(World world){
        List<Location> packPlacementTiles = new LinkedList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        for(Wolf w : pack){
            int r = new Random().nextInt(packPlacementTiles.size());
            Location l = packPlacementTiles.get(r);
            packPlacementTiles.remove(r);

            world.setTile(l,w);
        }
        hasMadePack = true;
    }

    @Override public void act(World world){
        this.older(world);
    }

    @Override public void older(World world){
        age++;

        if(age == 40){
            di = new DisplayInformation(Color.RED,"wolf",true);
        }
        if(age == 120){
            dies = true;
        }
    }
}