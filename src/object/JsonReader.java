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

	public GameObject readItems() throws IOException {
		ObjectMapper mp = new ObjectMapper();

		UsableItem item = null;

		try {
			 item = mp.readValue(new File("object/testfile.json"), UsableItem.class);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Item loaded, name: " + item.name);
		return item;
	}

}

