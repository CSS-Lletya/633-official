package skills;

import lombok.Data;

/**
 * Acts as a transformable object which switches between states.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
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