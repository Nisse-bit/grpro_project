import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileReader {
    private File file; //input-filen vi ønsker at læse fra
    private int worldSize; //første linje i filen, verdenens størrelse
    private HashMap<String, Integer> entityMap; //Map over entities der skal skabes (key), samt hvor mange (value)
    private List<Object> entityList;

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

        this.entityList = new LinkedList<>();
        //Laver entityMap om til en liste med objekter der skal placeres
        for (String key : entityMap.keySet()) {
            if(key.equals("rabbit")) {
                for(int i=0; i<=entityMap.get(key); i++){
                    entityList.add(new Rabbit());
                }
            }
            if(key.equals("grass")) {
                for(int i=0; i<=entityMap.get(key); i++){
                    entityList.add(new Grass());
                }
            }
            if(key.equals("burrow")) {
                for(int i=0; i<=entityMap.get(key); i++){
                    entityList.add(new Burrow());
                }
            }
        }
    }

    //Metoder
    public void printInfo() {
        System.out.println("Worldsize: " + worldSize);
        for (String entity : entityMap.keySet()) {
            System.out.println("Entity: " + entity + ", amount: " + entityMap.get(entity));
        }
        System.out.println();
    }

    //getFile
    public File getFile() {
        return file;
    }

    //getWorldSize Returnere størrelsen af verden. som er første linje i input filen.
    public int getWorldSize() {
        return worldSize;
    }

    //getEntityMap   Returnere Hashmap med, String type af object og integer mængden den skal lave af dem.
    public HashMap<String, Integer> getEntityMap() {
        return entityMap;
    }
}