import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class frequentItemSetMiner {
    public static void main(String[] args) throws FileNotFoundException, IOException, NumberFormatException {

        final long startTime = System.currentTimeMillis();
        final Runtime runtime = Runtime.getRuntime();

        //Criteria of interestingness
        int minsup = 2;

        //Create a hashmap to store the unique items and their transaction ids

        ConcurrentHashMap<Set<Integer>, Set<Integer>> FreqItemSets = new ConcurrentHashMap<>();

        // Create String variable to represent file name
        String inputfile = "E:\\Java Programming Files\\DataScienceWithJava\\DanielText.txt";

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
                String[] transactionSplit = line.trim().split("\\s+");
                System.out.println(Arrays.toString(transactionSplit));

                //loop through transactionSplit Array to get each unique item
                for(String uniqueItem: transactionSplit) {
                    //parse uniqueItem as an integer
                    Integer item = Integer.parseInt(uniqueItem);

                    //Store uniqueItem in a set of integers
                    Set<Integer> itemset = new HashSet<>();

                    //Create a set of integers to store transactionID
                    Set<Integer> itemID = new HashSet<>();

                    //add transaction ID to the itemId
                    itemset.add(item);
                    Set<Integer> TIDS = FreqItemSets.get(itemset);

                    //Check if  itemset exists or not in the map
                    if(TIDS==null){
                        //Key (itemset) does not exist in map
                        //Insert(itemset and itemID) into hashmap
                        //Add transactionID to set TID
                        itemID.add(transactionId);

                        //Store uniqueItem in a set of integers
//                        itemset.add(item);
                        FreqItemSets.put(itemset, itemID);
                    }else{
                        //key (itemset) exists in map
                        //get old TIDS, Add new TID
                        //Update the map with new TIDS
                        Set<Integer> newtransactionID = new HashSet<>();
                        newtransactionID.add(transactionId);
//                        itemset.add(item);

                        TIDS.addAll(newtransactionID);
                        FreqItemSets.put(itemset, TIDS);

                    }



                }
//                System.out.println(FreqItemSets);
            }
            //close reader
            reader.close();

            System.out.println("Before Pruning");
            System.out.println(FreqItemSets);
            //Remove infrequent length-1 itemset from FrequentItemsets Map

            System.out.println("After Pruning");
            //Create a list (set of integers) to enable candidate
            //Generation using Apriori (Store frequent length-1 first)
            List <Set<Integer>> loopingList = new ArrayList<>();

            //For each entry in freqItemSet Map
            //Remove those entries that are not frequent
            for (Map.Entry<Set<Integer>, Set<Integer>> entry:FreqItemSets.entrySet()){
                if(entry.getValue().size()>=minsup){
                    //Itemset is frequent
                    loopingList.add(entry.getKey());
                }else{
                    //Itemset is infrequent, Remove from frequentItemSet Map
                    FreqItemSets.remove(entry.getKey());
                }
            }
            System.out.println(FreqItemSets);
            System.out.println("Before sorting");
            System.out.println(loopingList);

            //Sort loopingList in order to enable Apriori Candidate Generation
            Collections.sort(loopingList, new Comparator<Set<Integer>>() {
                @Override
                public int compare(Set<Integer> set1, Set<Integer> set2) {
                    Integer val1 = set1.iterator().next();
                    Integer val2 = set2.iterator().next();
                    return Integer.compare(val1, val2);
                }
            });
            System.out.println("After sorting");
            System.out.println(loopingList);

            //Loop through the loopinglist to genrate the remaining frequent itemset
            while(loopingList.size() > 1){
                //Create a list for swapping to store
                //Generate candidate/frequent itemsets temp.
                List<Set<Integer>> swapList = new ArrayList<>();

                //Outer for loop
                for(int i = 0; i< loopingList.size();i++){
                    //Create an arrayList to store the itemset
                    //From the looping list to be used in generating
                    //The frequent itemset, sort to enable obtain
                    //Prefix and suffix for apriori candidate generation
                    List<Integer>firstHalf = new ArrayList<>();

                    //Add item at index i to firstHalf
                    firstHalf.addAll(loopingList.get(i));

                    //Sort to enable extract suffix
                    Collections.sort(firstHalf);

                    //Create sets to store prefix and suffix
                    Set<Integer> prefixFH = new HashSet<>();
                    Set<Integer> suffixFH = new HashSet<>();

                    //Extract suffix from sorted first half
                    suffixFH.add(firstHalf.get(firstHalf.size() -1));

                    //Get prefix from firstHalf
                    prefixFH.addAll(firstHalf);

                    //Remove suffix from prefix
                    prefixFH.removeAll(suffixFH);

                    //Inner for loop
                    for(int j = i+1; j<loopingList.size();j++){
                        //Create an arrayList to store the itemset
                        //From the looping list to be used in generating
                        //The frequent itemset, sort to enable obtain
                        //Prefix and suffix for apriori candidate generation
                        List<Integer>secondHalf = new ArrayList<>();

                        //Add item at index i to firstHalf
                        secondHalf.addAll(loopingList.get(j));

                        //Sort to enable extract suffix
                        Collections.sort(secondHalf);

                        //Create sets to store prefix and suffix
                        Set<Integer> prefixSH = new HashSet<>();
                        Set<Integer> suffixSH = new HashSet<>();

                        //Extract suffix from sorted first half
                        suffixSH.add(secondHalf.get(secondHalf.size() -1));

                        //Get prefix from firstHalf
                        prefixSH.addAll(secondHalf);

                        //Remove suffix from prefix
                        prefixSH.removeAll(suffixSH);

                        System.out.println(prefixFH + " "+prefixSH);
                        System.out.println(suffixFH + " "+suffixSH);

                        //Create data structures to store TIDS of firstHalf and secondHalf
                        Set<Integer> FHTIDS = new HashSet<>();
                        Set<Integer> SHTIDS = new HashSet<>();

                        //Get TIDS from FreqItemSets and add to FHTIDS and SHTIDS
                        FHTIDS.addAll(FreqItemSets.get(loopingList.get(i)));
                        SHTIDS.addAll(FreqItemSets.get(loopingList.get(j)));

                        //Create a set to store the intersection TIDS
                        Set<Integer> IntersectionTID = new HashSet<>();
                        IntersectionTID.addAll(FHTIDS);
                        IntersectionTID.retainAll(SHTIDS);

                        //Check if the itemSet is frequent using intersectionTID
                        if(IntersectionTID.size()>= minsup){
                            //Potentially frequent itemSet
                            //Check if the itemSet meets the apriori property
                            if (prefixFH.equals(prefixSH) && !(suffixFH.equals(suffixSH))){
                                //Generate frequent itemset
                                Set<Integer> FItemSet = new HashSet<>();
                                FItemSet.addAll(prefixFH);
                                FItemSet.addAll(suffixFH);
                                FItemSet.addAll(suffixSH);

                                //Add generated frequent itemset (FItemSet)
                                // And the TIDS( IntersectionTID) to freqItemSets
                                FreqItemSets.put(FItemSet, IntersectionTID);

                                //Add only the generated frequent itemSet (FItemSet)
                                //To swapping list for next round of candidate generation
                                swapList.add(FItemSet);
                            }else{
                                //Apriori candidate generation not met
                                //Break from inner loop
                                break;
                            }
                        }


                    }
                }
                //Clear content of loopingList
                //Add all content in the swappinglist to loopingList
                loopingList.clear();
                loopingList.addAll(swapList);

            }
            long endTime = System.currentTimeMillis();
            long memory = runtime.totalMemory() - runtime.freeMemory();
            System.out.println("\t Memory and Runtime");
            System.out.println("=============================================");

            //System.out.println("Memory used: "+ memory/(1024*1024)+ "Mb");
            System.out.println("Memory used: "+ memory/(1024*1024)+ "Mb");
            System.out.println("Runtime: "+ (endTime-startTime));
            System.out.println("Dataset size: "+ transactionId);
            System.out.println("Minimum support: "+minsup);
            System.out.println("Frequent itemsets: "+ FreqItemSets.size());
            System.out.println("===============================================");
            for(Map.Entry<Set<Integer>, Set<Integer>> entry: FreqItemSets.entrySet() ){
                System.out.println("Frequent Itemset: "+ entry.getKey()+" support count: "+ entry.getValue().size());
                System.out.println("Frequent Itemset: "+ entry.getKey()+" support: "+ Math.round(entry.getValue().size()/(double)transactionId)*1000.00/1000.00);
                System.out.println("Frequent Itemset: "+ entry.getKey()+" support count "+ entry.getValue());
            }
        }
    }
}
