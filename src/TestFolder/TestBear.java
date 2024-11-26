package TestFolder;
// havde fejl fordi den ikke var IMPORTED rigtigt.
import Animals.Bear;
import Animals.Rabbit;
import Animals.Wolf;
import Holes.Burrow;
import Holes.Wolfden;
import Plants.Grass;
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBear {

    World w;
    Bear bear;

    Wolfden wolfden;
    Location location;

    // dette bliver kørt før hver test.
    @BeforeEach
    public void setUp() {
        w = new World(3);
        bear = new Bear();
        Location TerritoryLocation = new Location(3,4);



    }
    // første test virker bjørnen som kaninen gør.



}