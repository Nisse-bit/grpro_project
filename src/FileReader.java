import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import Plants.Grass;
import Animals.Rabbit;
import Holes.Burrow;
import Animals.Wolf;
import Animals.Bear;
import itumulator.world.Location;
import Plants.BerryBush;
import Holes.WolfDen;
import Animals.Carcass;
import Animals.Cordyceps;

public class FileReader{
    private int worldSize; //Første linje i filen; verdenens størrelse
    private List<Object> entityList; //Liste af blocking-entities
    private List<Object> nboList; //Liste af NonBlocking-entities

    //Konstruktør
    /**
     * Behandler data fra en input-fil til brug i forlængelse af itumulator-biblioteket.
     * @param fileLocation stien til filen, hvis' data behandles
     * @throws FileNotFoundException hvis fil-stien er ugyldig
     */
    public FileReader(String fileLocation) throws FileNotFoundException{
        Scanner scanner = new Scanner(new File(fileLocation));
        this.worldSize = Integer.parseInt(scanner.nextLine());
        this.entityList = new ArrayList<>();
        this.nboList = new ArrayList<>();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] splitline = line.split(" ");
            if(splitline.length == 1){return;} //Nogle filer indeholder tomme linjer

            String entityName = splitline[0];
            int entityAmount = 0;

            if(splitline[1].contains("-")){
                int min = Integer.parseInt(splitline[1].split("-")[0]);
                int max = Integer.parseInt(splitline[1].split("-")[1]) + 1;
                entityAmount = new Random().nextInt(min,max);
            }
            else{
                try{entityAmount = Integer.parseInt(splitline[1]);}
                catch(Exception e){/*gør intet hvis der ikke er et tal*/}
            }

            //Grass
            if(entityName.equals("grass")){
                while(entityAmount-- > 0){
                    nboList.add(new Grass());
                }
            }

            //Rabbit
            if(entityName.equals("rabbit")){
                while(entityAmount-- > 0){
                    entityList.add(new Rabbit());
                }
            }

            //Burrow
            if(entityName.equals("burrow")){
                while(entityAmount-- > 0){
                    nboList.add(new Burrow());
                }
            }

            //Wolf
            if(entityName.equals("wolf")){
                int packID = new Random().nextInt(Integer.MAX_VALUE);
                while(entityAmount-- > 0){
                    entityList.add(new Wolf(packID));
                }
                nboList.add(new WolfDen(packID));
            }

            //Bear
            if(entityName.equals("bear")){
                if(splitline.length == 2){
                    while(entityAmount-- > 0){
                        entityList.add(new Bear());
                    }
                }
                else if(splitline.length == 3){
                    //Hvis der er et tredje element i input-linjen, er det en territorie-specifikation
                    splitline[2] = splitline[2].replaceAll("[()]","");
                    int x = Integer.parseInt(splitline[2].split(",")[0]);
                    int y = Integer.parseInt(splitline[2].split(",")[1]);

                    while(entityAmount-- > 0){
                        entityList.add(new Bear(new Location(x,y)));
                    }
                }
            }

            //BerryBush
            if(entityName.equals("berry")){
                while(entityAmount-- > 0){
                    entityList.add(new BerryBush());
                }
            }

            //Carcass
            if(entityName.equals("carcass")){
                boolean infected = false;
                if(splitline[1].equals("fungi")){
                    infected = true;

                    if(splitline[2].contains("-")){
                        int min = Integer.parseInt(splitline[2].split("-")[0]);
                        int max = Integer.parseInt(splitline[2].split("-")[1]) + 1;
                        entityAmount = new Random().nextInt(min,max);
                    }
                    else{
                        entityAmount = Integer.parseInt(splitline[2]);
                    }
                }
                else if(splitline[1].contains("-")){
                    int min = Integer.parseInt(splitline[1].split("-")[0]);
                    int max = Integer.parseInt(splitline[1].split("-")[1]) + 1;
                    entityAmount = new Random().nextInt(min,max);
                }
                else{
                    entityAmount = Integer.parseInt(splitline[1]);
                }

                while(entityAmount-- > 0){
                    String size = (new Random().nextInt(2) == 0)? ("big") : ("small");
                    entityList.add(new Carcass(infected, size));
                }

                //Cordyceps
                if(entityName.equals("cordyceps")){
                    String animal = splitline[1];
                    if(splitline[2].contains("-")){
                        int min = Integer.parseInt(splitline[2].split("-")[0]);
                        int max = Integer.parseInt(splitline[2].split("-")[1]) + 1;
                        entityAmount = new Random().nextInt(min,max);
                    }
                    else{
                        entityAmount = Integer.parseInt(splitline[2]);
                    }

                    while(entityAmount-- > 0){
                        entityList.add(new Cordyceps(animal));
                    }
                }
            }
        }

        if(entityList.size() > worldSize*worldSize){
            throw new IllegalArgumentException("more blocking entities than tiles!");
        }
        if(nboList.size() > worldSize*worldSize){
            throw new IllegalArgumentException("more NonBlocking entities than tiles!");
        }

        scanner.close();
    }

    //Metoder
    /**
     * Returnerer verdenens størrelse.
     * @return verdenens størrelse
     */
    public int getWorldSize(){
        return worldSize;
    }

    /**
     * Returnerer listen af "blocking entities".
     * @return listen af "blocking entities"
     */
    public List<Object> getEntityList(){
        return entityList;
    }

    /**
     * Returnerer listen af "nonblocking entities".
     * @return listen af "nonblocking entities"
     */
    public List<Object> getNonBlockingList(){
        return nboList;
    }
}