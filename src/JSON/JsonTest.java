import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;
import java.io.IOException;

public class JsonTest {
	public static void main(String[] args) throws IOException {
		ObjectMapper mp = new ObjectMapper();
		ArrayList<Item> myItems = mp.readValue(new File("assets/items.json"), new TypeReference<List<Item>>(){});
		
		Random rand = new Random();
		for(Item i : myItems) {
			i.useItem(rand.nextInt(i.maxDurability));	
			System.out.println(i);
		}
	}
}
