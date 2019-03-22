import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Item {
	public String name;
	public int weight;
	public int maxDurability;

	@JsonIgnore
	private int curDurability;
	
	public Item(@JsonProperty("name") String name, @JsonProperty("weight") int weight, @JsonProperty("maxDurability") int maxDurability) {
		this.name = name;
		this.weight = weight;
		this.maxDurability = maxDurability;
		curDurability = this.maxDurability;
	}

	@Override
	public String toString() {
		String str = String.format("===%s===\n\tWeight = %d\n\tDurability = %d / %d\n", name, weight, curDurability, maxDurability);
		return str;
	}

	public void useItem(int x) { curDurability -= x; }
}
