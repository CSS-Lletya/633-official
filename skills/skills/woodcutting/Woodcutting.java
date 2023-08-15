package skills.woodcutting;


import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Equipment;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtility;

import lombok.val;
import skills.HarvestingSkillAction;
import skills.Skills;
import skills.TransformableObject;

public class Woodcutting extends HarvestingSkillAction {
	
	/**
	 * The definition for the hatchet being used.
	 */
	private final Hatchet hatchet;
	
	/**
	 * The definition for the tree being cut.
	 */
	private final Tree tree;
	
	/**
	 * The object we're interfering with.
	 */
	private final GameObject object;
	
	/**
	 * The object's name.
	 */
	private final String objectName;
	
	/**
	 * Constructs a new {@link Woodcutting} skill.
	 */
	public Woodcutting(Player player, Tree tree, GameObject object) {
		super(player, Optional.of(object));
		this.tree = tree;
		this.objectName = object.getDefinitions().getName().toLowerCase();
		this.hatchet = Hatchet.getDefinition(player).orElse(null);
		this.object = object;
	}
	
	@Override
	public boolean successful() {
		assert hatchet.getSpeed() > 0;
		val level = player.getSkills().getLevel(Skills.WOODCUTTING);
		val advancedLevels = level - hatchet.getSpeed();
		return Math.min(Math.round(advancedLevels * 0.8F) + 20, 70) > RandomUtility.random(100);
	}

	@Override
	public Optional<Item[]> removeItems() {
		return Optional.empty();
	}
	
	@Override
	public Item[] harvestItems() {
		return (tree.getItem().getId() != -1) ? new Item[]{tree.getItem()} : new Item[]{};
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean isIgnoreResourceGather() {
		return (RandomUtility.random(5) == 0 && hatchet == Hatchet.INFERNO_ADZE) || tree == Tree.IVY;
	}
	
	@Override
	public boolean initialize() {
		if(!checkWoodcutting()) {
			return false;
		}
		if(object.getLife() <= 0) {
			object.setLife(tree.getLogCount());
		}
		getPlayer().getPackets().sendGameMessage("You begin to cut the " + objectName + "...");
		getPlayer().setNextAnimation(hatchet.getAnimation());
		return true;
	}
	
	@Override
	public void onSequence(Task t) {
		if(object.isDisabled()) {
			this.onStop();
			t.cancel();
		}
	}
	
	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			if (RandomUtility.random(5) == 0 && hatchet == Hatchet.INFERNO_ADZE && tree != Tree.IVY) {
				player.getSkills().addExperience(Skills.FIREMAKING, experience());
				player.getPackets().sendGameMessage("The adze's heat instantly incinerates the " + tree.getItem().getDefinitions().getName() + ".");
				player.setNextGraphics(new Graphics(1776, 0 , 150));
			}
			randomEvent();
			BirdNest.drop(getPlayer());
			player.getDetails().getStatistics()
			.addStatistic(ItemDefinitions.getItemDefinitions(tree.getItem().getId()).getName() + "_Chopped")
			.addStatistic("Logs_Cut");
			object.setLife(object.getLife() - 1);
		}
		if(object.getLife() <= 0 && !tree.isObstacle()) {
			TransformableObject obj = null;
			for(TransformableObject ob : tree.getObject()) {
				if(ob.getObjectId() == object.getId()) {
					obj = ob;
					break;
				}
			}
			if(obj != null) {
				player.getAudioManager().sendSound(Sounds.FALLING_TREE);
				GameObject.spawnTempGroundObject(new GameObject(obj.getTransformable(), 10, 0, object), tree.getRespawnTime());
				t.cancel();
			}
		}
	}
	
	@Override
	public boolean canExecute() {
		return checkWoodcutting();
	}
	
	@Override
	public void onStop() {
		getPlayer().setNextAnimation(Animations.RESET_ANIMATION);
	}
	
	@Override
	public double experience() {
		return fullLumberJack(getPlayer()) ? (tree.getExperience() * 1.05) : tree.getExperience();
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(hatchet.getAnimation());
	}
	
	@Override
	public int getSkillId() {
		return Skills.WOODCUTTING;
	}
	
	private boolean checkWoodcutting() {
		if(tree == null) {
			return false;
		}
		if(Hatchet.getDefinition(player).orElse(null) == null) {
			getPlayer().getPackets().sendGameMessage("You don't have a hatchet.");
			return false;
		}
		if(player.getSkills().getLevel(Skills.WOODCUTTING) < tree.getRequirement()) {
			getPlayer().getPackets().sendGameMessage("You need a woodcutting level of " + tree.getRequirement() + " to cut this tree!");
			return false;
		}
		if(player.getSkills().getLevel(Skills.WOODCUTTING) < hatchet.getRequirement()) {
			getPlayer().getPackets().sendGameMessage("You need a level of " + hatchet.getRequirement() + " to use this hatchet!");
			return false;
		}
		if(getPlayer().getInventory().getFreeSlots() < 1 && !tree.isObstacle()) {
			getPlayer().getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private static boolean fullLumberJack(Player player) {
		return player.getEquipment() != null && player.getEquipment().containsAll(10933, 10939, 10940, 10941);
	}
	
	private void randomEvent() {
		if (hatchet == Hatchet.INFERNO_ADZE)
			return;
		if((RandomUtility.nextInt(1000) - (hatchet.ordinal() * 10)) > 900) {
	        if(RandomUtility.nextBoolean()) {
				if(getPlayer().getEquipment().containsAny(hatchet.getHatchet().getId())) {
					player.getEquipment().getItems().set(Equipment.SLOT_WEAPON, null);
					player.getEquipment().refresh(Equipment.SLOT_WEAPON);
					player.getAppearance().generateAppearenceData();
				} else {
					player.getInventory().deleteItem(hatchet.getHatchet());
				}
				if(getPlayer().getInventory().hasFreeSlots()) {
					getPlayer().getInventory().addItem(new Item(462));
				} else {
					FloorItem.addGroundItem(new Item(462), getPlayer().getLastWorldTile(), player, true, 180);
				}
				FloorItem.addGroundItem(new Item(hatchet.getHead()), getPlayer().getLastWorldTile(), player, true, 180);
				getPlayer().getPackets().sendGameMessage("Your hatchet dismantled during the chopping process.");
				player.getAudioManager().sendSound(Sounds.BROKEN_AXE);
			}
		}
	}
}