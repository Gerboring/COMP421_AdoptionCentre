import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class ParseCSV
{
    //TODO: IO exception try catch
    public static String[] getAnimalCSVStatements() throws IOException, SQLException {
        BufferedReader csvReader = new BufferedReader(new FileReader("./resources/animal_intake.csv"));

        //String[] animalInsertStatements = new String[(int) (csvReader.lines().count() - 1)];
        String[] animalInsertStatements = new String[500];

        csvReader.readLine(); //skip header line
        int i = 0;
        String row = "";
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");

            if(data[0].contains("months") || data[0].contains("week"))
            {
                data[0] = "0"; //0 years old
            }
            data[0] = data[0].replaceAll("[^0-9]", "");

            //change animalID to just integers
            data[1] = data[1].replaceAll("[^0-9]", "");
            //change sex field to just male or female char
            if(data[11].toLowerCase().contains("male")){
                data[11] = "m";
            } else if (data[11].toLowerCase().contains("female")){
                data[11] = "f";
            } else{
                data[11] = null;
            }

            //TODO: populate medical file
            //0 = age, 1 = id, 2 = species, 3 = breed, 10 = name, isAdopted = false, medicalFile = empty, 11 = sex
            String stmt = InitializeReset.getInsertAnimalStatement(Integer.parseInt(data[1]), data[10].trim(), data[2], Integer.parseInt(data[0]), data[3], data[11].charAt(0), false, "");
            animalInsertStatements[i] = stmt;
            i++;
        }
        csvReader.close();

        return animalInsertStatements;
    }
}
