import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class frequentItemSetMiner {
    public static void main(String[] args) throws FileNotFoundException, IOException, NumberFormatException {
        //Criteria of interestingness
        int minsup = 4;

        //Create a hashmap to store the unique items and their transaction ids

        ConcurrentHashMap<Set<Integer>, Set<Integer>> FreqItemSets = new ConcurrentHashMap<>();

        // Create String variable to represent file name
        String inputfile = "DanielText.txt";

        //File object to point to the text file
        FileReader read = new FileReader(inputfile);

        //Create a buffer reader to read the file line by line
        try(BufferedReader reader = new BufferedReader(read)) {

            //Create a string variable to represent each line in dataset
            String line;

            //Create a counter to count the line number(transaction id)
            int transactionId = 0;

            //Read the file
            while ((line = reader.readLine()) != null) {
                //Increment the transaction ID
                transactionId++;

                //split line into unique items
                String[] transactionSplit = line.split("");

                //loop through transactionSplit Array to get each unique item
                for(String uniqueItem: transactionSplit) {
                    //parse uniqueItem as an integer
                    Integer item = Integer.parseInt(uniqueItem);

                    //Store uniqueItem in a set of integers
                    Set<Integer> itemset = new HashSet<>();

                    //Create a set of integers to store transactionID
                    Set<Integer> itemID = new HashSet<>();

                    //add transaction ID to the itemId
                    itemID.add(transactionId);
                    Set<Integer> TIDS = FreqItemSets.get(itemset);

                    //Check if  itemset exists or not in the map
                    if(TIDS==null){
                        //Key (itemset) does not exist in map
                        //Insert(itemset and itemID) into hashmap
                        //Add transactionID to set TID
                        itemID.add(transactionId);

                        //Store uniqueItem in a set of integers
                        itemset.add(item);
                        FreqItemSets.put(itemset, itemID);
                    }else{
                        //key (itemset) exists in map
                        //get old TIDS, Add new TID
                        //Update the map with new TIDS
                        Set<Integer> newtransactionID = new HashSet<>();
                        newtransactionID.add(transactionId);
                        itemset.add(item);
                        TIDS.addAll(newtransactionID);
                        FreqItemSets.put(itemset, TIDS);

                    }



                }
            }
        }
    }
}
