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
        }
    }

    //getEntityList
    public List<Object> getEntityList(){
        if(entityList == null){this.makeLists();}
        return entityList;
    }
    //getEntityAmount
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
    public void printInfo(){
        System.out.println("Worldsize: "+ worldSize);
        for(String entity : entityMap.keySet()){
            System.out.println("Entity: "+ entity +", amount: "+ entityMap.get(entity));
        }
        System.out.println();
    }
}