package skills;

import lombok.Data;

@Data
public final class TransformableObject {
	
	/**
	 * The original state of this object.
	 */
	private final int objectId;
	
	/**
	 * The transformable states this object can transform into.
	 */
	private final int transformable;
}
