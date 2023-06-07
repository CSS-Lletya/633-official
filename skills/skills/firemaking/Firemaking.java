package skills.firemaking;


import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

import skills.DestructionSkillAction;
import skills.Skills;

public final class Firemaking extends DestructionSkillAction {
	
	/**
	 * Determines if this firemaking action is being executed because of a familiar.
	 */
	private final boolean familiar;
	
	/**
	 * The definition of the {@link LogType} we're handling.
	 */
	private final LogType log;
	
	/**
	 * The definition of the {@link FireLighter} we're handling.
	 */
	private final FireLighter lighter;
	
	/**
	 * Constructs a new {@link Firemaking} skill action.
	 * @param player the player we're starting this action for.
	 * @param firstItem the first item this player used.
	 * @param secondItem the second item the first item was used on.
	 */
	Firemaking(Player player, Item firstItem, Item secondItem, boolean familiar) {
		super(player, Optional.empty());
		lighter = familiar ? null : FireLighter.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
		log = LogType.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
		this.familiar = familiar;
	}
	
	public static boolean execute(Player player, Item firstItem, Item secondItem, boolean familiar) {
		Firemaking firemaking = new Firemaking(player, firstItem, secondItem, familiar);
		if(familiar) {
			firemaking.start();
			return true;
		}
		if(firemaking.log == null || firemaking.lighter == null) {
			return false;
		}
		if(firstItem.getId() == firemaking.lighter.getItem() && secondItem.equals(firemaking.log.getLog()) || firstItem.equals(firemaking.log.getLog()) && secondItem.getId() == firemaking.lighter.getItem()) {
			player.setNextAnimation(Animations.ATTEMPT_FIRE_LIGHTING);
			firemaking.start();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().setNextAnimation(null);
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(log.getLog().getId()).getName() + "_Lit").addStatistic("Fires_Started");
			if(!familiar) {
				World.get().submit(new FiremakingTask(this));
			}
			if(!familiar && lighter != null && lighter != FireLighter.TINDERBOX) {
				player.getInventory().deleteItem(new Item(lighter.getItem(), 1));
			}
			if(!familiar) {
		        if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
		            if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
		                if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
		                    player.addWalkSteps(player.getX(), player.getY() - 1, 1);
				
			}
		}
		t.cancel();
	}
	
	@Override
	public boolean initialize() {
		return true;
	}
	
	@Override
	public Item destructItem() {
		return log.getLog();
	}
	
	@Override
	public int delay() {
		return RandomUtils.inclusive(2, 6);
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean canExecute() {
		return checkFiremaking();
	}
	
	@Override
	public double experience() {
		return log.getExperience();
	}
	
	@Override
	public int getSkillId() {
		return Skills.FIREMAKING;
	}

	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.ATTEMPT_FIRE_LIGHTING);
	}
	
	private boolean checkFiremaking() {
		if (!player.getInventory().containsItem(590, 1)) {
            player.getPackets().sendGameMessage("You don't have any tinderbox.");
            return false;
        }
        if (player.getSkills().getLevel(Skills.FIREMAKING) < log.getRequirement()) {
            player.getPackets().sendGameMessage("You need a firemaking level of " + log.getRequirement() + " to light this log.");
            return false;
        }
        if (!World.isTileFree(player.getPlane(), player.getX(), player.getY(), 1) 
                || GameObject.getObjectWithSlot(player, Region.OBJECT_SLOT_FLOOR) != null) {
            player.getPackets().sendGameMessage("You can't light a fire here.");
            return false;
        }
		return true;
	}
	
	/**
	 * @return {@link #log}
	 */
	LogType getLogType() {
		return log;
	}
	
	/**
	 * @return {@link #lighter}.
	 */
	FireLighter getFireLighter() {
		return lighter;
	}
}
