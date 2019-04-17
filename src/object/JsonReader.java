package object;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.io.IOException;

import game.*;

public class JsonReader {
	
	/*public static void main(String[] args) {
		JsonReader read = new JsonReader();
		try {
			read.readItems();
		}
		catch(IOException e) {
			System.out.println("File error");
		}
	}*/

	public ArrayList<UsableItem> readItems() throws IOException {
		ObjectMapper mp = new ObjectMapper();

		//TODO: Add equippables functionality
		ArrayList<UsableItem> listOfItems = null; 

		try {
			 listOfItems= mp.readValue(new File("../data/items.json"), new TypeReference<List<UsableItem>>(){});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return listOfItems;
	}

}

