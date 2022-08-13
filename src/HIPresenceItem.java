import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class HIPresenceItem {

	@Getter
	private final String itemName;

	@Getter
	private final String itemValue;

	public HIPresenceItem(String itemName) {
		this(itemName, itemName);
	}

	@Override
	public String toString() {
		return itemName;
	}

}
