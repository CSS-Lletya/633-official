package skills.runecrafting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PouchType {

	SMALL(3), MEDIUM(5), LARGE(7), GIANT(12);

	@Getter
	private int maxAmount;
}