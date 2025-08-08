import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main2 {
    public static void main(String[] args) throws IOException {

        int minsup =4;

        ConcurrentHashMap<Set<Integer>, Set<Integer>> FreqItemsets =new ConcurrentHashMap<>();

        String inputFile ="DanielText.txt";
        FileReader read = new FileReader(inputFile);

        BufferedReader reader = new BufferedReader(read);

        String line;

        //create a counter to count the line number (transaction ID)
        int transactionID=0;
        //read the File
        while ((line=reader.readLine())!=null){
            transactionID++;

            //split line into a unique items
            String[] transactionSplit = line.split(" ");
            System.out.println(Arrays.toString(transactionSplit));

            for(String uniqueItem: transactionSplit){
                Integer item = Integer.parseInt(uniqueItem);
                //store uniqueItem in a set of integers
                Set<Integer> itemset = new HashSet<>();
                //create a set of integers to store transactionID
                Set<Integer> itemID = new HashSet<>();
                // add transaction ID to ItemID
                itemset.add(item);
                //to get the transaction IDS of the itemset from FreqItemsets map
                Set<Integer>TIDS =FreqItemsets.get(itemset);

                if(TIDS ==null){

                    itemID.add(transactionID);

                    FreqItemsets.put(itemset,itemID);
                }else {

                    Set<Integer>newtransactionID = new HashSet<>();
                    newtransactionID.add(transactionID);

                    //itemset.add(item);
                    TIDS.addAll(newtransactionID);
                    FreqItemsets.put(itemset,TIDS);
                }
            }

        }
        reader.close();
        System.out.println("BEFORE PRUNING");
        System.out.println(FreqItemsets);
        //REMOVE INFREQUENT LENGTH-1 ITEMSETS FROM

        System.out.println("After Pruning");
        //CREATE A LIST (SET OF INTEGERS) TO ENABLE CANDIDATE
        //GENERATION USING APRIORI(store frequent length-1)
        List<Set<Integer>>loopinglist =new ArrayList<>();
        //for each entry in frequentset


        for(Map.Entry<Set<Integer>, Set<Integer>>entry: FreqItemsets.entrySet()){
            if(entry.getValue().size()>=minsup){
                //ITEMSET IS FREQUENT
                loopinglist.add(entry.getKey());

            }else {
                //ITEMSET IS INFREQUENT REMOVE FROM FreqItemset MAP
                FreqItemsets.remove(entry.getKey());

            }
        }
        System.out.println(FreqItemsets);
        System.out.println(loopinglist);
        //SORT loopinglist TO ENABLE APRIORI CANDIDATE
        Collections.sort(loopinglist, new Comparator<Set<Integer>>() {
            @Override
            public int compare(Set<Integer> set1, Set<Integer> set2) {
                Integer val1 =set1.iterator().next();
                Integer val2 =set2.iterator().next();
                return Integer.compare(val1,val2);
            }
        });
    }
}