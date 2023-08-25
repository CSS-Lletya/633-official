package skills.agility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rs.game.map.GameObject;

/**
 * A collection of immutable data that help builds an {@link Obstacle} interaction.
 * Using Annotations also allows for much cleaner inner classes.
 * @author Dennis
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AgilitySignature {
	
	/**
	 * A brief description of the Agility obstacle 
	 * @return
	 */
	String info();
	
	/**
	 * The current stage of the course, prevents repeated actions
	 * @return
	 */
	int stage();
	
	/**
	 * The {@link GameObject} we're interacting with
	 * @return
	 */
	int[] object();
	
	/**
	 * The duration of the interaction delay when interacting with the Agility {@link Obstacle}.
	 * @return
	 */
	int duration();
	
	/**
	 * The Agility level required to interact with this {@link Obstacle}
	 * @return
	 */
	int levelRequired();
	
	/**
	 * The rewarded experience of completing an Agility {@link Obstacle}
	 * @return
	 */
	double completionExperience();
}