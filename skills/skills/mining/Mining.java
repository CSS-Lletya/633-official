package skills.mining;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

import skills.HarvestingSkillAction;
import skills.Skills;

public final class Mining extends HarvestingSkillAction {

	/**
	 * The definition of the {@link PickaxeData} being used.
	 */
	private final PickaxeData pickaxe;

	/**
	 * The definition of the {@link RockData} being mined.
	 */
	private final RockData rock;

	/**
	 * The object we're interfering with.
	 */
	private final GameObject object;

	/**
	 * An array holding all the possible gems which can be obtained while mining or during Gem Rocks event
	 */
	public static final Item[] GEMS = Item.toList(1623, 1621, 1603, 1617, 1625, 1627, 1629);

	/**
	 * Constructs a new {@link Mining}.
	 * @param player {@link #player}.
	 * @param rock the mining rock.
	 * @param object the rock object.
	 */
	public Mining(Player player, RockData rock, GameObject object) {
		super(player, Optional.of(object));
		if(rock == RockData.ESSENCE && player.getSkills().getLevel(Skills.MINING) >= 30)
			rock = RockData.PURE_ESSENCE;
		this.rock = rock;
		this.pickaxe = PickaxeData.getDefinition(player).orElse(null);
		this.object = object;
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
		if (rock == RockData.LRC_COAL || rock == RockData.LRC_GOLD || rock == RockData.LRC_MINERALS ||rock == RockData.ESSENCE || rock == RockData.PURE_ESSENCE)
			return;
		if (success) {
			randomEvent();
			object.setLife(object.getLife() - 1);
		}
		if (object.getLife() <= 0 && !object.isDisabled()) {
			for (int ob : rock.getObject()) {
				if (ob == object.getId())
					GameObject.spawnTempGroundObject(new GameObject(rock == RockData.GEM_ROCK ? 11193: 450 , 10, 0, object), rock.getRespawnTime());
			}
			this.onStop();
			t.cancel();
		}
	}

	@Override
	public double successFactor() {
		double successFactor = pickaxe.getSpeed() + rock.getSuccess() - RandomUtils.nextDouble(1.0);
		if(successFactor < 0) {
			return 0;
		}
		return successFactor;
	}
	
	@Override
	public int getSkillId() {
		return Skills.MINING;
	}

	@Override
	public Optional<Item[]> removeItems() {
		return Optional.empty();
	}

	@Override
	public Item[] harvestItems() {
		return new Item[]{rock == RockData.GEM_ROCK ? RandomUtils.random(rock.getItem()): rock.getItem()[0]};
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public boolean initialize() {
		if(!checkMining()) {
			return false;
		}
		if(object.getLife() <= 0) {
			object.setLife(rock.getOreCount());
		}
		getPlayer().getPackets().sendGameMessage("You begin to mine the rock...");
		getPlayer().setNextAnimation(pickaxe.getAnimation());
		return true;
	}

	@Override
	public boolean canExecute() {
		return !object.isDisabled() && checkMining();
	}

	@Override
	public double experience() {
		return rock.getExperience();
	}


	@Override
	public void onStop() {
		getPlayer().setNextAnimation(Animations.RESET_ANIMATION);
	}

	@Override
	public Optional<Animation> animation() {
		return Optional.of(pickaxe.getAnimation());
	}

	private void randomEvent() {
		if((RandomUtils.nextInt(1000) - (pickaxe.ordinal() * 10)) > 900) {
	        if(RandomUtils.nextBoolean()) {
				if(getPlayer().getEquipment().containsAny(pickaxe.getItem().getId())) {
					player.getEquipment().getItems().set(Equipment.SLOT_WEAPON, null);
					player.getEquipment().refresh(Equipment.SLOT_WEAPON);
					player.getAppearance().generateAppearenceData();
				} else {
					player.getInventory().deleteItem(pickaxe.getItem());
				}
				if(getPlayer().getInventory().hasFreeSlots()) {
					getPlayer().getInventory().addItem(PickaxeData.PICKAXE_HANDLE);
				} else {
					FloorItem.addGroundItem(PickaxeData.PICKAXE_HANDLE, getPlayer().getLastWorldTile(), player, true, 180);
				}
				FloorItem.addGroundItem(pickaxe.getHead(), getPlayer().getLastWorldTile(), player, true, 180);
				getPlayer().getPackets().sendGameMessage("Your pickaxe dismantled during the mining process.");
				player.getAudioManager().sendSound(Sounds.PICKAXE_LOST);
			}
		}
		return;
	}

	private boolean checkMining() {
		if(rock == null) {
			return false;
		}
		if(PickaxeData.getDefinition(getPlayer()).orElse(null) == null) {
			getPlayer().getPackets().sendGameMessage("You don't have a pickaxe.");
			return false;
		}
		if(getPlayer().getSkills().getLevel(Skills.MINING) < rock.getRequirement()) {
			getPlayer().getPackets().sendGameMessage("You need a level of " + rock.getRequirement() + " to mine this rock!");
			return false;
		}
		if(getPlayer().getSkills().getLevel(Skills.MINING) < pickaxe.getRequirement()) {
			getPlayer().getPackets().sendGameMessage("You need a level of " + pickaxe.getRequirement() + " to use this pickaxe!");
			return false;
		}
		if(getPlayer().getInventory().getFreeSlots() < 1) {
			getPlayer().getPackets().sendGameMessage("You do not have any space left in your inventory.");
			return false;
		}
		
		int chance = 282;
		if (player.getEquipment().getItem(Equipment.SLOT_RING).getId() == 2572) {
			chance /= 1.5; 
		}
		Item necklace = player.getEquipment().getItem(Equipment.SLOT_AMULET);
		if (necklace != null && (necklace.getId() > 1705 && necklace.getId() < 1713)) {
			chance /= 1.5;
		}
		if (RandomUtils.random(chance) == 0) {
			Item gem = RandomUtils.random(GEMS);
			player.getAudioManager().sendSound(Sounds.FINDING_GEM);
			player.getPackets().sendGameMessage("You find a " + gem.getName() + "!");
			if (!player.getInventory().addItem(gem)) {
				player.getPackets().sendGameMessage("You do not have enough space in your inventory, so you drop the gem on the floor.");
			}
		}
		return true;
	}
}