import Animals.Carcass;
import Animals.Rabbit;
import Holes.Burrow;
import Holes.WolfDen;
import Plants.Grass;
import itumulator.executable.Program;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;

import java.io.FileNotFoundException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test //K1-2a. Kaniner kan placeres på kortet når input filerne beskriver dette. Kaniner skal blot tilfældigt placeres.
    public void placeRabbitFromFile() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\InputFiles\\week-1\\t1-2fg.txt");
        int size = fr.getWorldSize();
        Program program = new Program(size, 12, 12);
        World world = program.getWorld();

        //Placerer alle blocking-entities (Vi tester kun om Rabbit placeres, så NBO'er er ligegyldige)
        for (Object o : fr.getEntityList()) {
            Random r = new Random();
            int x = r.nextInt(size);
            int y = r.nextInt(size);

            Location l = new Location(x, y);
            while (!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }
            world.setTile(l, o);
        }

        //Tæller antal Rabbit i verdenen
        int count = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Rabbit) {
                count++;
            }
        }

        //Har tælleren talt 4, har FileReader oversat Rabbit rigtig fra fil til verden; der er 4 Rabbit i filen
        Assertions.assertTrue(count == 4);
    }

    @Test //K1-2b. Kaniner kan dø, hvilket resulterer I at de fjernes fra verdenen.
    public void rabbitDies() {
        /* Kaninerne kan dø på et par forskellige måder, men uanset hvad "dør" den idet dens die-metode kaldes.
         * Derfor er det nemt at teste om kaninen dør; vi kører bare die-metoden, og ser hvorvidt kaninen
         * stadig eksiterer. */

        world.setTile(new Location(0,0), rabbit);
        rabbit.die(world);
        Assertions.assertFalse(world.contains(rabbit));
    }

    @Test //K1-2c. Kaniner lever af græs som de spiser i løbet af dagen, uden mad dør en kanin.
    public void rabbitLivesOffGrass() {
        //Uden græs, og uden at kaninen sover om natten, dør den af sult efter 50 steps
        Rabbit rabbit1 = new Rabbit();
        world.setTile(new Location(1,1), rabbit1);

        for(int i=0; i < 50; i++){
            rabbit1.act(world);
        }
        Assertions.assertFalse(world.contains(rabbit1));

        //Med græs, lever kaninen længere end 50 steps
        Rabbit rabbit2 = new Rabbit();
        world.setTile(new Location(1,1), rabbit2);

        for(int i = 0; i < world.getSize(); i++){
            for(int j = 0; j < world.getSize(); j++) {
                world.setTile(new Location(i,j), new Grass());
            }
        }
        for(int i=0; i < 50; i++){
            rabbit2.act(world);
        }
        Assertions.assertTrue(world.contains(rabbit2));
    }

    @Test //K1-2d. Kaniners alder bestemmer hvor meget energi de har.
    public void rabbitLosesEnergy() {
        world.setTile(new Location(0,0), rabbit);
        Assertions.assertTrue(rabbit.getEnergy()[0] == 50);

        for(int i=0; i < 30; i++){
            rabbit.act(world);
        }
        Assertions.assertTrue(30 <= rabbit.getEnergy()[0] && rabbit.getEnergy()[0] <= 40);
    }

    @Test //K1-2e. Kaniner kan reproducere.
    public void rabbitReproduces() {

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
    public void rabbitAges() {
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