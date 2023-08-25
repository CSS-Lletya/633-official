package skills.agility;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

/**
 * Handles a Player interacting with an Agility {@link Obstacle} to train their
 * respective skill.
 * 
 * @author Dennis
 *
 */
public class AgilityHandler {

	/**
	 * Executes the interaction with an Agility {@link Obstacle}
	 * @param player
	 * @param object
	 */
	public static void execute(Player player, GameObject object) {
		getObstacle(object).ifPresent(inter -> inter.execute(player, object));
	}

	/**
	 * Checks for the correct {@link GameObject} id and compares it with the Agility {@link Obstacle} to ensure proper actions are taken and then returns it as a value
	 * @param object
	 * @return
	 */
	private static Optional<Obstacle> getObstacle(GameObject object) {
		return AGILITY_COURSES.values().stream().filter(inventoryType -> isCorrectId(inventoryType, object))
				.findFirst();
	}

	/**
	 * Checks the {@link AgilitySignature} for any matching ids with the {@link GameObject}, returns true or false
	 * @param obstacle
	 * @param object
	 * @return
	 */
	private static boolean isCorrectId(Obstacle obstacle, GameObject object) {
		AgilitySignature signature = obstacle.getClass().getAnnotation(AgilitySignature.class);
		return Arrays.stream(signature.object()).anyMatch(agilityObstacle -> object.getId() == agilityObstacle);
	}

	/**
	 * Gets the Agility level required to interact with this {@link Obstacle}
	 * @param obstacle
	 * @param object
	 * @return
	 */
	protected static int getLevelRequired(Obstacle obstacle, GameObject object) {
		AgilitySignature signature = obstacle.getClass().getAnnotation(AgilitySignature.class);
		return signature.levelRequired();
	}

	/**
	 * Gets the Agiltiy {@link Obstacle} duration (delay of this event taking)
	 * @param obstacle
	 * @param object
	 * @return
	 */
	protected static int getDuration(Obstacle obstacle, GameObject object) {
		AgilitySignature signature = obstacle.getClass().getAnnotation(AgilitySignature.class);
		return signature.duration();
	}

	/**
	 * Gets the rewarded experience given upon completing the {@link Obstacle}
	 * @param obstacle
	 * @param object
	 * @return
	 */
	protected static double getRewardedExperience(Obstacle obstacle, GameObject object) {
		AgilitySignature signature = obstacle.getClass().getAnnotation(AgilitySignature.class);
		return signature.completionExperience();
	}

	/**
	 * Gets the Agility {@link Obstacle} stage specific id. This helps us prevent false experience giving (spamming interaction)
	 * @param obstacle
	 * @param object
	 * @return
	 */
	protected static int getStageId(Obstacle obstacle, GameObject object) {
		AgilitySignature signature = obstacle.getClass().getAnnotation(AgilitySignature.class);
		return signature.stage();
	}

	/**
	 * The object map which contains all the Agility {@link Obstacle} in the world.
	 */
	private static final Object2ObjectArrayMap<AgilitySignature, Obstacle> AGILITY_COURSES = new Object2ObjectArrayMap<>();

	/**
	 * Loads all the Agility courses into the {@link #AGILITY_COURSES} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<Obstacle> courses = Utility.getClassesInDirectory("skills.agility.gnome").stream()
				.map(clazz -> (Obstacle) clazz).collect(Collectors.toList());
		courses.forEach(course -> AGILITY_COURSES.put(course.getClass().getAnnotation(AgilitySignature.class), course));
	}
}