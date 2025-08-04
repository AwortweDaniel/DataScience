import java.util.HashMap;

public class hashMap {
    public static void main(String[] args){
        System.out.println("Talking about hashmaps now");

        HashMap<String, Integer> stds = new HashMap<>();
        stds.put("Dan", 123);
        stds.put("Gen", 456);

        System.out.println(stds);
        //getting the value of the key Dan
        System.out.println(stds.get("Dan"));
    }
}
