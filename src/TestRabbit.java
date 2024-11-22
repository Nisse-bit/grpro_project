
// havde fejl fordi den ikke var IMPORTED rigtigt.
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.Location;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRabbit {

    World w;
    Rabbit rabbit;
    Grass grass;
    Burrow burrow;
    Location location;
    // dette bliver kørt før hver test.
    @BeforeEach
    public void setUp() {
        w = new World(3);
        rabbit = new Rabbit();
        grass = new Grass(); // til når kaninen skal spise græsset.
        burrow = new Burrow();
    }
    // fjern alt?
    @AfterEach
    public void tearDown() {}


    @Test
    public void RabbitMoves() {
        // checker at kaninen bevæger sig
        Location rabbitlocation = new Location(0, 0);

        w.setTile(rabbitlocation, rabbit);

        w.setCurrentLocation(rabbitlocation);

        Location l = w.getLocation(rabbit);

        // tester givne funktion
        rabbit.move(w);
        rabbit.move(w);
        rabbit.move(w);

        // hvis at kaninen har bevæget sig vil den ikke være den samme som start positionen som lige nu er (0,0)
        assertNotEquals(l, w.getLocation(rabbit));   // her skal start location være anderledes ind slut position.
    }

    @Test
    public void RabbitMoves2() {
        // her tester vi hvad den skal gøre hvis der ikke er plads til at bevæge sig, eller ingen felter er.

        w = new World(1);
        Location rabbitlocation = new Location(0, 0);
        w.setTile(rabbitlocation, rabbit);
        w.setTile(rabbitlocation, grass);
        w.setCurrentLocation(rabbitlocation);


        Location l = w.getLocation(rabbit);
        rabbit.move(w);
        Assertions.assertEquals(l, w.getLocation(rabbit));

    }


    @Test
    public void RabbitAges() {


        // Her tester vi om Rabbit ældes rigtigt.
        /*
         if (world.getCurrentTime()%10 == 0) {   hver gang tiden kan divideres med 10 og det har 0 i reminder. vil age stige med 1.
            age = age + 0.5;
        }
        if (age >= 1.5 && age <= 2.5) {  // så efter 40 dage vil rabbit være voksen.
            di = new DisplayInformation(Color.DARK_GRAY, "rabbit-large", false);
        }
        if (age >= 3 && age <= 4) { // efter 40 dage mere vil den have fået en hat på.
            di = new DisplayInformation(Color.black, "rabbit-large-fungi", false);
        }
        if (age > 4) {    // efter 20 dage mere vil kaninen blive slettet fra verden.
            world.delete(this);
        }
         */

        w = new World(3);
        Location rabbitlocation = new Location(0, 0);

        w.setTile(rabbitlocation, rabbit);



        for (int i = 0; i <= 41; i++) {
            w.step();

        }
        assertTrue(1.5 >= rabbit.getAge() && rabbit.getAge() <= 2.5);

        for (int i = 0; i <= 41; i++) {
            w.step();

        }

        assertTrue(3 >= rabbit.getAge() && rabbit.getAge() <= 4);




    }

    // der er 9 cases.
    // hvis kanin er i et af det 4 hjørner, hvis den skal op/ned højre/venstre eller hvis den er på et hul.

    @Test
    public void RabbitMoveto() {
       // her tester vi om kaninen kan gå til højre flere gange.
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);



        Location location1 = new Location(6, 4);  // x < Rx  || y == y dvs den er under kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);

        // virker også hvis den allerede er der.
        rabbit.moveto(w,location1);

        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }


    @Test// der er 9 cases.
    // hvis kanin er i et af det 4 hjørner, hvis den skal op/ned højre/venstre eller hvis den er på et hul.
    public void RabbitMoveto1() {

        w = new World(6);
        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);



        // her tester vi om rabbit kan gå til Venstre
        Location location1 = new Location(4, 5);   // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);




        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }
    @Test
    public void RabbitMoveto2() {
        // her tester vi om rabbit kan gå til op
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(4, 6);   // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);


        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto3() {
        // her tester vi om rabbit kan gå til ned
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(2, 4);   // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);


        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto4() {
        // her tester vi om rabbit kan gå til til hjørnet
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(4,2 );   // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);


        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }
    @Test
    public void RabbitMoveto5() {
        // her tester vi om rabbit kan gå til højre
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(2,2 );   // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);


        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto6() {
        // her tester vi om rabbit kan gå til højre
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(2,2 );   // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);


        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }
    @Test
    public void RabbitMoveto7() {
        // her tester vi om rabbit kan gå til højre
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(6,6); // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);



        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }

    @Test
    public void RabbitMoveto8() {
        // her tester vi om rabbit kan gå til højre
        w = new World(7);



        Location rabbitlocation = new Location(4, 4);
        w.setTile(rabbitlocation, rabbit);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?
        // den kan bevæge sig flere gange.

        Location location1 = new Location(6,6); // x < Rx  || y == y dvs den er til højre for kaninen

        rabbit.moveto(w,location1);

        rabbit.moveto(w,location1);



        Assertions.assertEquals(location1, w.getLocation(rabbit));

    }
    public void RabbitMovesNearestBurrow() {    // work in progress
        w = new World(6);

        Location rabbitlocation = new Location(4, 4);
        // kaninen starter i midten
        // hvad hvis kaninen ikke starter i midten?


        Location location = new Location(2, 4);   // x < Rx  || y == y dvs den er under kaninen
        burrow.PutBurrow(w,location);


        Location location1 = new Location(0, 0);
        Location location2 = new Location(0, 0);
        Location location3 = new Location(0, 0);

    }


}