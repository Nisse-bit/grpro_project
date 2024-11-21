import itumulator.world.Location;
import itumulator.world.World;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Rabbittest {
    World w;
    Rabbit rabbit;
    Grass grass;
    Burrow burrow;

    // dette bliver kørt før hver test.
    @BeforeEach
    public void setUp() {
        w = new World(3);
        rabbit = new Rabbit();
        grass = new Grass(); // til når kaninen skal spise græsset.
        burrow = new Burrow();
    }

    @Test
    public void RabbitMoves() {
        // checker at kaninen bevæger sig
        Location rabbitlocation = new Location(0, 0);
        w.setTile(rabbitlocation, rabbit);

        w.setCurrentLocation(rabbitlocation);

        Location l = w.getLocation(rabbit);

        rabbit.act(w);
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
        rabbit.act(w);

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

        w = new World(1);
        Location rabbitlocation = new Location(0, 0);
        w.setTile(rabbitlocation, rabbit);
        w.setTile(rabbitlocation, grass);
        w.setCurrentLocation(rabbitlocation);


        Location l = w.getLocation(rabbit);
        rabbit.act(w);

        for (int i = 0; i <= 41; i++) {
            w.step();

        }
        assertTrue(1.5 >= rabbit.getAge() && rabbit.getAge() <= 2.5);

        for (int i = 0; i <= 41; i++) {
            w.step();

        }

        assertTrue(3 >= rabbit.getAge() && rabbit.getAge() <= 4);




    }
}