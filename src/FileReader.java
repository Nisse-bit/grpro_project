import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileReader {
    private File file; //input-filen vi ønsker at læse fra
    private int worldSize; //første linje i filen, verdenens størrelse
    private HashMap<String, Integer> entityMap; //Map over entities der skal skabes (key), samt hvor mange (value)
    private List<Object> entityList;
    private List<Object> nboList;

    //Konstruktør
    public FileReader(String fileLocation) throws FileNotFoundException {
        this.file = new File(fileLocation);
        Scanner scanner = new Scanner(file); //Scanner til at læse fra filen

        this.worldSize = Integer.parseInt(scanner.nextLine());
        this.entityMap = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitline = line.split(" ");
            if(splitline.length == 1){return;}
            // ^^Nogle linjer i input-filerne er tomme

            String entityName = splitline[0];
            int entityAmount;

            if (splitline[1].contains("-")) {
                int min = Integer.parseInt(splitline[1].split("-")[0]);
                int max = Integer.parseInt(splitline[1].split("-")[1]);
                entityAmount = new Random().nextInt(min, max);
            } else {
                entityAmount = Integer.parseInt(splitline[1]);
            }

            entityMap.put(entityName, entityAmount);
        }
    }

    //Metoder
    //printInfo
    public void printInfo() {
        System.out.println("Worldsize: " + worldSize);
        for (String entity : entityMap.keySet()) {
            System.out.println("Entity: " + entity + ", amount: " + entityMap.get(entity));
        }
        System.out.println();
    }
    //makeEntityList
    private void makeEntityList(){
        this.entityList = new ArrayList<>();
        for(String key : entityMap.keySet()){
            //rabbits
            if(key.equalsIgnoreCase("rabbit")){
                int amount = entityMap.get(key);
                for(int i=amount; i-- >0;){
                    entityList.add(new Rabbit());
                }
            }
        }
    }
    //getEntityList
    public List<Object> getEntityList(){
        if(entityList == null){
            this.makeEntityList();
        }
        return entityList;
    }

    //makeNboList
    private void makeNboList(){
        this.nboList = new ArrayList<>();
        for(String key : entityMap.keySet()){
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
    //getNboList
    public List<Object> getNboList(){
        if(nboList == null){
            this.makeNboList();
        }
        return nboList;
    }
    
    //getFile
    public File getFile() {
        return file;
    }

    //getWorldSize returnerer størrelsen af verden. som er første linje i input filen.
    public int getWorldSize() {
        return worldSize;
    }

    //get SumEntity
    public int getEntityAmount(){
        int sum = 0;
        for (Object a : entityList){
           sum++;
        }
        return sum;
    }
    //get SumNBO
    public int getNBOAmount(){
        int sum = 0;
        for (Object a : nboList){
            sum++;
        }
        return sum;
    }
    //getEntityMap   Returnere Hashmap med, String type af object og integer mængden den skal lave af dem.
    public HashMap<String, Integer> getEntityMap() {
        return entityMap;
    }
}