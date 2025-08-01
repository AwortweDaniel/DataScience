import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {

        String textfilePath = "E:\\Java Programming Files\\DataScienceWithJava\\DanielText.txt";

        try(BufferedReader reader = new BufferedReader(new FileReader(textfilePath)))
        {
            String line;
            int ID =0;

            while((line= reader.readLine()) != null)
            {  ID ++;
//                            System.out.println(ID +". " + line);
                String[] lineSplit = line.split(" ");
                System.out.println(ID + ". " + Arrays.toString(lineSplit));

                for( String word : lineSplit){
                    System.out.println(ID + ". "+word);
                }

            }

        }
    }
}