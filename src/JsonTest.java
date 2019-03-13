import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonTest {
	public static void main(String[] args) {
		ObjectMapper mp = new ObjectMapper();
		MyValue val = null;
		try {
			val = mp.readValue(new File("test.json"), MyValue.class);
		}
		catch(IOException e) {
			System.out.println("Get borked!");
		}
		if(val == null) System.out.println("BORKf");
		else System.out.println(val);
	}
}
