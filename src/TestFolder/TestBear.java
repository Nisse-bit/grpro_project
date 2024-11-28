package TestFolder;
// havde fejl fordi den ikke var IMPORTED rigtigt.
import Animals.Bear;

import java.util.*;
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBear {

    World world;
    Bear bear;
    Location tlocation;

    Location location;

    Set<Location> territory;

    // dette bliver kørt før hver test.
    @BeforeEach
    public void setUp() {
        world = new World(9);
        world.setDay();
        bear = new Bear();
        Location tlocation = new Location(3,4);
        world.setTile(tlocation, bear);
        territory = bear.getTerritoryTiles(world,tlocation);


    }
    // første test virker bjørnen som kaninen gør.

    @Test
    public void BearMoveTest(){
       tlocation = new Location(3,4);
        System.out.println(tlocation);
        System.out.println(bear.getTerritoryTiles(world,tlocation));

        Location Startlocation = world.getLocation(bear);
        // bevæger sig forhåbenligt
        bear.createTerritory(world);
        bear.bearBrain(world);





        assertNotEquals(Startlocation, world.getLocation(bear));

        assertTrue(territory.contains(world.getLocation(bear)));

    }

    @Test
    public void BearCreateTerritoryTest(){
        Bear bear1 = new Bear();
        Location location = new Location(5,5);
        world.setTile(location, bear1);
        bear.createTerritory(world);

        assertNotNull(bear.territory);



    }
    @Test
    public void TerritoryTest(){

        Bear bear1 = new Bear();
        Location location = new Location(5,5);
        world.setTile(location, bear1);
        bear.createTerritory(world);
        System.out.println(bear.getTerritoryTiles(world,bear.territory));
        assertNotNull(bear.getTerritoryTiles(world,bear.territory));
    }
    @Test
    public void Bearkonstruktortest(){
        Location location = new Location(5,5);
        Bear bear1 = new Bear(location);
        world.setTile(location, bear1);
        assertNotNull(bear.territory);


    }
}