package TestFolder;
// havde fejl fordi den ikke var IMPORTED rigtigt.
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

public class TestWolf {

    World w;
    Wolf wolf;

    Wolfden wolfden;
    Location location;

    // dette bliver kørt før hver test.
    @BeforeEach
    public void setUp() {
        w = new World(3);
        wolf = new Wolf();
        Wolfden wolfden = new Wolfden();


    }
    // første test virker ulven som kaniner gør.



}