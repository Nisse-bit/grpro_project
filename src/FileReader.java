import Animals.Rabbit;
import Holes.Burrow;
import Plants.Grass;
import Animals.Wolf;
import Plants.BerryBush;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class FileReader {
    private File file; //Input-filen vi ønsker at læse fra
    private int worldSize; //Første linje i filen, verdenens størrelse
    private Map<String, Integer> entityMap; //Map over entities der skal skabes (key), samt hvor mange (value)
    private List<Object> entityList; //List til at opbevare blocking entities
    private List<Object> nboList; //List til at opbevare nonblocking entities

    //Konstruktør
    /**
     * Konstruktøren laver en FileReader, som dertil laver et HashMap over "Entity" og amount.
     * Konstruktøren laver gemmer også en int worldSize fra første linje i læste fil.
     * @param fileLocation i string form.
     * @throws FileNotFoundException
     */
    public FileReader(String fileLocation) throws FileNotFoundException {
        this.file = new File(fileLocation);
        Scanner scanner = new Scanner(file);

        this.worldSize = Integer.parseInt(scanner.nextLine());
        this.entityMap = new HashMap<>();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] splitline = line.split(" ");
            if(splitline.length == 1){return;} //Nogle filer indeholder tomme linjer

            String entityName = splitline[0];
            int entityAmount;

            if(splitline[1].contains("-")){
                int min = Integer.parseInt(splitline[1].split("-")[0]);
                int max = Integer.parseInt(splitline[1].split("-")[1]) + 1;
                entityAmount = new Random().nextInt(min,max);
            }
            else{
                entityAmount = Integer.parseInt(splitline[1]);
            }

            entityMap.put(entityName, entityAmount);
        }

        this.makeLists();
    }

    //makeLists
    /**
     * Denne metode laver 2 lister af objekter. 1 Liste af NBO-Objekter & 1 Liste af Actors.
     */
    private void makeLists(){
        this.entityList = new ArrayList<>();
        this.nboList = new ArrayList<>();
        for(String key : entityMap.keySet()){
            //rabbits
            if(key.equalsIgnoreCase("rabbit")){
                int amount = entityMap.get(key);
                for(int i=amount; i-- >0;){
                    entityList.add(new Rabbit());
                }
            }
            //grass
            if(key.equalsIgnoreCase("grass")){
                int amount = entityMap.get(key);
                for(int i=amount; i-- >0;){
                    nboList.add(new Grass());
                }
            }
            //burrow
            if(key.equalsIgnoreCase("burrow")){
                int amount = entityMap.get(key);
                for(int i=amount; i-- >0;){
                    nboList.add(new Burrow());
                }
            }
            //wolf
                if(key.equalsIgnoreCase("wolf")){
                    int amount = entityMap.get(key);
                    for(int i=amount; i-- >0;){
                        entityList.add(new Wolf());
                }
            }
            //berry
            if(key.equalsIgnoreCase("berry")){
                int amount = entityMap.get(key);
                for(int i=amount; i-- >0;){
                    entityList.add(new BerryBush());
                }
            }
        }
    }

    //getEntityList
    public List<Object> getEntityList(){
        if(entityList == null){this.makeLists();}
        return entityList;
    }
    //getEntityAmount
    /**
     * En simpel metode der returnere summen af objekter i EntityListen
     * @return int sum
     */
    public int getEntityAmount(){
        int sum = 0;
        for(Object o : this.getEntityList()){
            sum += 1;
        }
        return sum;
    }

    //getNboList
    public List<Object> getNboList(){
        if(entityList == null){this.makeLists();}
        return nboList;
    }

    //getNBOAmount
    /**
     * En simpel metode der returnere summen af objekter i NBOListen
     * @return int sum
    */
    public int getNBOAmount(){
        int sum = 0;
        for(Object o : this.getNboList()){
            sum += 1;
        }
        return sum;
    }

    //getEntityMap
    public Map<String,Integer> getEntityMap(){
        return entityMap;
    }

    //getFile
    public File getFile() {
        return file;
    }

    //getWorldSize
    public int getWorldSize(){
        return worldSize;
    }

    //printInfo
    /**
    * En simpel metode primært brugt til debugging af FileReader konstruktøren.
    */
    public void printInfo(){
        System.out.println("Worldsize: "+ worldSize);
        for(String entity : entityMap.keySet()){
            System.out.println("Entity: "+ entity +", amount: "+ entityMap.get(entity));
        }
        System.out.println();
    }
}