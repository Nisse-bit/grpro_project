import Animals.Rabbit;
import Animals.Wolf;
import Animals.Bear;
import Animals.Carcass;
import Animals.Fungus;

import java.util.*;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.Program;

public class DecompositionTests {
    Rabbit rabbit;
    Wolf wolf;
    Bear bear;
    Carcass carcass;
    Fungus fungus;
    World world;
    Location location;

    @BeforeEach
    public void Setup() {
        rabbit = new Rabbit();
        wolf = new Wolf(123);
        bear = new Bear();
        carcass = new Carcass(false, "big");

        world = new World(10);
        location = new Location(2, 3);
    }

    @AfterEach
    public void takeDown() {
        rabbit = null;
        wolf = null;
        bear = null;
        carcass = null;

        world = null;
        location = null;
    }

    //K3-1a. Opret ådsler, som placeres på kortet når input filerne beskriver dette
    @Test
    public void placeCarcassInWorld() {
        Program program = new Program(10, 10, 10);
        World world = program.getWorld();

        Carcass c1 = new Carcass(false, "big");
        Carcass c2 = new Carcass(true, "small");

        world.setTile(new Location(0, 0), c1);
        world.setTile(new Location(0, 1), c2);

        Assertions.assertTrue(world.contains(c1));
        Assertions.assertTrue(world.contains(c2));
    }

    @Test
    public void placeCarcassFromFile() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\niels\\Documents\\GitHub\\grpro_project\\src\\InputFiles\\inputs_week-3\\week-3\\t3-2ab.txt");
        int size = fr.getWorldSize();
        Program program = new Program(size, 12, 12);
        World world = program.getWorld();

        //Placerer alle blocking-entities (Vi tester kun om Carcass placeres, så NBO'er er ligegyldige)
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

        //Tæller antal Carcass i verdenen
        int count = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                count++;
            }
        }

        //Har tælleren talt 9-14, har FileReader oversat Carcass rigtig fra fil til verden; der er 9-14 Carcass i filen
        Assertions.assertTrue(9 <= count && count <= 14);
    }

    /*
     * K3-1b. Når dyr dør nu, skal de efterlade et ådsel. Ådsler kan spises ligesom dyr kunne
     * tidligere, dog afhænger mængden af ’kød’ af hvor stort dyret der døde er. Således
     * spises dyr ikke direkte længere når det slås ihjel, i stedet spises ådslet. Alle dyr som er
     * kødædende spiser ådsler.
     */
    @Test
    public void animalsLeaveCarcasses() {

        world.setTile(new Location(0, 0), bear);
        Location BearLocation = world.getLocation(bear);
        bear.die(world);
        Object object = world.getTile(BearLocation);
        Assertions.assertTrue(object instanceof Carcass);


        world.setTile(new Location(2, 2), wolf);
        Location WolfLocation = world.getLocation(wolf);
        wolf.die(world);
        Object object1 = world.getTile(WolfLocation);
        Assertions.assertTrue(object1 instanceof Carcass);


        world.setTile(new Location(4, 4), rabbit);
        Location RabbitLocation = world.getLocation(rabbit);
        rabbit.die(world);
        Object object2 = world.getTile(RabbitLocation);
        Assertions.assertTrue(object2 instanceof Carcass);
    }

    /*
     * K3-1c. Ådsler bliver dårligere med tiden og nedbrydes helt – selvom det ikke er spist
     * op (altså forsvinder det)! Det forsvinder naturligvis også hvis det hele er spist.
     */
    @Test
    public void carcassDecomposes() {
        Location location = new Location(2, 3);
        carcass = new Carcass(false, "big");
        world.setTile(location, carcass);

        Assertions.assertTrue(carcass.getSize().equals("big"));

        for (int i = 0; i < 30; i++) {
            carcass.act(world);
        }
        Assertions.assertFalse(world.contains(carcass));
    }

    /*
     * K3-2a. Udover at ådsler nedbrydes, så hjælper svampene til. Således kan der opstå
     * svampe I et ådsel. Dette kan ikke ses på selve kortet, men svampen lever I selve ådslet.
     * Når ådslet er nedbrudt (og forsvinder), kan den ses som en svamp placeret på kortet,
     * der hvor ådslet lå. For at læse inputfilerne skal du sikre dig, at et ådsel kan indlæses
     * med svamp.
     */
    @Test
    public void infectedCarcassLeavesFungus() {
        carcass = new Carcass(true, "small");
        world.setTile(location, carcass);

        while (world.contains(carcass)) {
            carcass.act(world);
        }

        Fungus fungus = null;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Fungus f) {
                fungus = f;
            }
        }

        Assertions.assertFalse(world.contains(carcass));
        Assertions.assertTrue(world.contains(fungus));
    }

    /*
     * K3-2b. Svampe kan kun overleve, hvis der er andre ådsler den kan sprede sig til i
     * nærheden. Er dette ikke tilfældet, vil svampen også dø efter lidt tid. Desto større ådslet
     * er, desto længere vil svampen leve efter ådslet er væk. Da svampen kan udsende
     * sporer, kan den række lidt længere end kun de omkringliggende pladser.
     */
    @Test
    public void fungusSpreadsInfection() {
        Carcass carcass0 = new Carcass(false, "big");
        world.setTile(new Location(0, 0), carcass0);

        Carcass carcass1 = new Carcass(false, "big");
        world.setTile(new Location(4, 4), carcass1);

        Carcass carcass2 = new Carcass(false, "big");
        world.setTile(new Location(7, 7), carcass2);

        Carcass carcass3 = new Carcass(false, "big");
        world.setTile(new Location(3, 3), carcass3);

        Fungus monkeypox = new Fungus("big");
        world.setTile(new Location(2, 2), monkeypox);
        monkeypox.spreadInfection(world);

        System.out.println(monkeypox.NearestCarcasses(world));

        Assertions.assertTrue(carcass0.getInfected());
        Assertions.assertTrue(carcass1.getInfected());
        Assertions.assertTrue(carcass2.getInfected());
        Assertions.assertTrue(carcass3.getInfected());
    }

    /*
     * [...]. Er dette ikke tilfældet, vil svampen også dø efter lidt tid. Desto
     * større ådslet er, desto længere vil svampen leve efter ådslet er væk.
     */
    @Test
    public void fungusDies() {
        Fungus fungus = new Fungus("small");
        Fungus fungus1 = new Fungus("big");

        world.setTile(new Location(0,0),fungus);
        world.setTile(new Location(8,8),fungus1);

        for(int i = 0; i < 30 ; i++) {
            fungus.act(world);
            fungus1.act(world);
        }

        Assert.assertFalse(world.contains(fungus));
        Assert.assertTrue(world.contains(fungus1));

        for(int i = 0; i < 30 ; i++) {
            fungus1.act(world);
        }

        Assert.assertFalse(world.contains(fungus));
        Assert.assertFalse(world.contains(fungus1));
    }

    @Test
    public void carcassToFungusToDeath() {
        Carcass carcass = new Carcass(true, "big");
        Carcass carcass1 = new Carcass(true, "small");
        Location location = new Location(0,0);
        Location location1 = new Location(8,8);
        world.setTile(location,carcass);
        world.setTile(location1,carcass1);

        for(int i = 0; i < 30 ; i++) {
            carcass.act(world);
            carcass1.act(world);

        }

        Assertions.assertFalse(world.contains(carcass));
        Assertions.assertFalse(world.contains(carcass1));

        Assertions.assertTrue(world.getTile(location) instanceof Fungus);
        Assertions.assertTrue(world.getTile(location1) instanceof Fungus);

        Fungus fungus = (Fungus) world.getTile(location);
        Fungus fungus1 = (Fungus) world.getTile(location1);

        for(int i = 0; i < 30 ; i++) {
            fungus.act(world);
            fungus1.act(world);
        }

        Assert.assertTrue(world.getTile(location) instanceof Fungus); //Den store svamp skulle stadig gerne være i verden
        Assert.assertFalse(world.getTile(location1) instanceof Fungus); //Den lille svamp skulle gerne være "død"

        for(int i = 0; i < 30 ; i++) {
            fungus.act(world);
        }

        Assert.assertFalse(world.getTile(location) instanceof Fungus);
        Assert.assertFalse(world.getTile(location1) instanceof Fungus);
    }
}