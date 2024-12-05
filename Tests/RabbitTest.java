import Animals.Rabbit;
import Holes.Burrow;
import Holes.WolfDen;
import Plants.Grass;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RabbitTest {
    World world;
    Rabbit rabbit;
    Grass grass;
    Burrow burrow;

    @BeforeEach //Før hver test...
    public void setUp() {
        world = new World(3);
        rabbit = new Rabbit();
        grass = new Grass(); // til når kaninen skal spise græsset.
        burrow = new Burrow();
    }

    @AfterEach //Efter hver test...
    public void tearDown() {
        world = null;
        rabbit = null;
        grass = null;
        burrow = null;
    }

    @Test
    public void RabbitMoves() {
        // checker at kaninen bevæger sig
        Location rabbitlocation = new Location(0, 0);

        world.setTile(rabbitlocation, rabbit);

        world.setCurrentLocation(rabbitlocation);

        Location l = world.getLocation(rabbit);

        // tester givne funktion
        rabbit.moveRandomly(world);
        rabbit.moveRandomly(world);
        rabbit.moveRandomly(world);

        // hvis at kaninen har bevæget sig vil den ikke være den samme som start positionen som lige nu er (0,0)
        assertNotEquals(l, world.getLocation(rabbit));   // her skal start location være anderledes ind slut position.
    }

    @Test
    public void RabbitMoves2() {
        // her tester vi hvad den skal gøre hvis der ikke er plads til at bevæge sig, eller ingen felter er.

        world = new World(1);
        Location rabbitlocation = new Location(0, 0);
        world.setTile(rabbitlocation, rabbit);
        world.setTile(rabbitlocation, grass);
        world.setCurrentLocation(rabbitlocation);


        Location l = world.getLocation(rabbit);
        rabbit.moveRandomly(world);
        Assertions.assertEquals(l, world.getLocation(rabbit));

    }

    @Test
    public void RabbitAges() {
        // Her tester vi om Rabbit ældes rigtigt
        world = new World(3);
        Location rabbitlocation = new Location(0, 0);

        world.setTile(rabbitlocation, rabbit);

        for (int i = 0; i <= 41; i++) {
            world.step();
        }
        assertTrue(1.5 >= rabbit.getAge() && rabbit.getAge() <= 2.5);

        for (int i = 0; i <= 41; i++) {
            world.step();
        }
        assertTrue(3 >= rabbit.getAge() && rabbit.getAge() <= 4);
    }

    // der er 9 cases.
    // hvis kanin er i et af det 4 hjørner, hvis den skal op/ned højre/venstre eller hvis den er på et hul.
    @Test
    public void RabbitMoveto() {
        world = new World(7);

        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);

        // her tester vi om kaninen kan gå til højre flere gange.
        Location location1 = new Location(6, 4);

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);

        // virker også hvis den allerede er der.
        rabbit.moveTowards(world,location1);

        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }


    @Test// der er 9 cases.
    // hvis kanin er i et af det 4 hjørner, hvis den skal op/ned højre/venstre eller hvis den er på et hul.
    public void RabbitMoveto1() {

        world = new World(6);
        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);



        // her tester vi om rabbit kan gå til Venstre EN enkelt gang
        Location location1 = new Location(3, 4);

        rabbit.moveTowards(world,location1);




        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }
    @Test
    public void RabbitMoveto2() {
        // her tester vi om rabbit kan gå til op
        world = new World(7);



        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(4, 6);

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);


        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto3() {
        // her tester vi om rabbit kan gå til ned
        world = new World(7);



        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(4, 2);

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);


        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto4() {
        // her tester vi om rabbit kan gå til til hjørnet
        world = new World(7);



        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(6,2 );

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);


        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }
    @Test
    public void RabbitMoveto5() {
        // her tester vi om rabbit kan gå til højre
        world = new World(9);

        // her hjalp testen mig til at finde fejl i koden tak <3.


        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);

        // den kan bevæge sig flere gange.

        Location location1 = new Location(2,6 );

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);


        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto6() {
        // her tester vi om rabbit kan gå til venstre og ned

        world = new World(7);



        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);

        // den kan bevæge sig en gang

        Location location1 = new Location(3,3 );

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);


        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }
    @Test
    public void RabbitMoveto7() {
        // her tester vi om rabbit kan gå til højre og op.
        world = new World(7);



        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);

        // den kan bevæge sig flere gange.

        Location location1 = new Location(6,6);

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);



        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto8() {
        // her tester vi om rabbit kan gå til højre
        world = new World(7);



        Location rabbitlocation = new Location(4, 4);
        world.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(2,2);

        rabbit.moveTowards(world,location1);

        rabbit.moveTowards(world,location1);



        Assertions.assertEquals(location1, world.getLocation(rabbit));

    }
// intuition

    @Test
    public void rabbitFindsBurrow(){
        /*
         * Der er 4 måder en kanin finder en Burrow:
         *  1) Der er en Burrow indenfor radius 3 af kaninen.
         *  2) Der er ingen Burrow i radius, så kaninen graver en.
         *  3) Der er ikke mulighed for at grave, så kaninen finder nærmeste Burrow i hele verdenen.
         *  4) Der er ingen Burrows i verdenen og kaninen står på et NBO, bevæger kaninen sig tilfældigt,
         *     og prøver at finde en Burrow igen næste step.
         */

        //(1)
        world = new World(3);
        Rabbit r1 = new Rabbit();
        Burrow b1 = new Burrow();
        world.setTile(new Location(1,1), r1);
        world.setTile(new Location(1,2), b1);
        world.setNight();

        Assertions.assertNull(r1.getBurrow());
        r1.act(world);
        Assertions.assertEquals(r1.getBurrow(), b1);
        world = null;

        //(2)
        world = new World(9);
        Rabbit r2 = new Rabbit();
        Burrow b2 = new Burrow();
        world.setTile(new Location(1,1), r2);
        world.setTile(new Location(1,5), b2);
        world.setNight();

        Assertions.assertNull(r2.getBurrow());
        r2.act(world);
        Assertions.assertNotNull(r2.getBurrow());
        Assertions.assertNotEquals(r2.getBurrow(), b2);
        world = null;

        //(3)
        world = new World(9);
        Rabbit r3 = new Rabbit();
        Burrow b3 = new Burrow();
        world.setTile(new Location(0,0), new WolfDen(123));
        world.setTile(new Location(0,0), r3);
        world.setTile(new Location(8,8), b3);
        world.setNight();

        Assertions.assertNull(r3.getBurrow());
        r3.act(world);
        Assertions.assertEquals(r3.getBurrow(), b3);
        world = null;

        //(4)
        world = new World(2);
        Rabbit r4 = new Rabbit();
        world.setTile(new Location(0,0), r4);
        world.setTile(new Location(0,0), new WolfDen(123));
        world.setNight();

        Assertions.assertNull(r4.getBurrow());
        r4.act(world);
        Assertions.assertNull(r4.getBurrow());
        r4.act(world);
        Assertions.assertNotNull(r4.getBurrow());
        world = null;
    }

    // Den her tester om kaniner, går imod locationer og også gemmer sig i dem.
    @Test
    public void RabbitMovesNearestBurrow() {    // work in progress
        World world = new World(6);

        Location rabbitlocation = new Location(4, 4);
        // kaninen starter i midten



        Location location = new Location(3, 4);   // x < Rx  && y == y dvs den er under kaninen
        // sætter burrow ved location 3,4


        world.setTile(location, burrow);
        world.setTile(rabbitlocation,rabbit);



        world.setCurrentLocation(location);
        System.out.println(world.getNonBlocking(location));


        
            

        System.out.println(world.getCurrentTime());
        boolean onMap = rabbit.getOnMap();
        // nu skal kaninen gå i mod burrow.
        // det burde den gøre
        Assertions.assertNotNull(burrow);
      ;
        rabbit.act(world);



        
        
        rabbit.act(world);
        onMap = rabbit.getOnMap();
        // rabbit goes into hole and dissapears from map.
        Assert.assertFalse(onMap);







    }

}