package skills.thieving.impl;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.task.Task;

import lombok.val;
import skills.Skills;
import skills.thieving.ChestData;
import skills.thieving.Thieving;

/**
 * @author Corey
 * @author Dennis
 * @since 23/11/19
 * @since 8/0/23
 */
public class ChestThieving extends Action {
    
    private final ChestData chest;
    private final GameObject object;
    private final boolean searchForTraps;
    
    public ChestThieving(final ChestData chest, final GameObject object, final boolean searchForTraps) {
        this.chest = chest;
        this.object = object;
        this.searchForTraps = searchForTraps;
    }
    
    public static boolean handleChest(final Player player, final GameObject object, final boolean searchForTraps) {
        val chest = ChestData.getChest(object.getId());
        if (chest == null) {
            return false;
        }
        player.getAction().setAction(new ChestThieving(chest, object, searchForTraps));
        return true;
    }
    
    @Override
    public boolean start(Player player) {
    	
        if (player.getSkills().getLevel(Skills.THIEVING) < chest.getLevel()) {
            player.getPackets().sendGameMessage("You need a Thieving level of at least " + chest.getLevel() + " to steal from this chest.");
            return false;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.getPackets().sendGameMessage("You need some free inventory slots to steal from this chest.");
            return false;
        }
        player.setNextAnimation(Animations.LOCKPICKING);
    
        if (searchForTraps) {
            searchForTraps(player);
        } else {
            if (chest.isTrapped()) {
            	World.get().submit(new Task(1) {
            		@Override
            		protected void execute() {
            			chest.onTriggerTrap(player);
            			cancel();
            		}
            	});
                return false;
            }
            open(player);
        }
    
        player.getMovement().lock(14);
        return true;
    }
    
    @Override
    public boolean process(Player player) {
        return true;
    }
    
    @Override
    public int processWithDelay(Player player) {
        return -1;
    }
    
    private void open(Player player) {
        player.getPackets().sendGameMessage("You attempt to picklock the chest.");
        player.setNextAnimation(Animations.LEANING_FORWARD_USING_BOTH_HANDS);
    }
    
    private void searchForTraps(Player player) {
        player.getPackets().sendGameMessage("You search the chest for traps.");
        World.get().submit(new Task(1) {
        	int tick;
    		@Override
    		protected void execute() {
    			switch(tick++) {
    			case 1:
    				player.getPackets().sendGameMessage("You find a trap on the chest.");
    				break;
    			case 3:
    				player.getPackets().sendGameMessage("You disable the trap.");
    				break;
    			case 4:
    				open(player);
    				break;
    			case 6:
    				open(player);
    				val hasLockpick = player.getInventory().containsItem(new Item(ItemNames.LOCKPICK_1523));
    		        val requirement = Math.max(1, chest.getLevel() - (hasLockpick ? 8 : 0));
    		    
    		        if (Thieving.success(player, requirement)) {
    		            success(player);
    		        } else {
    		            failure(player);
    		        }
    		        cancel();
    				break;
    			}
    		}
    	});
    }
    
    private void success(Player player) {
        player.getPackets().sendGameMessage("You manage to unlock the chest.");
        player.getAudioManager().sendSound(Sounds.FINDING_TREASURE);
        player.setNextAnimation(Animations.CHEST_LOOTING);
        World.get().submit(new Task(2) {
    		@Override
    		protected void execute() {
    			player.getSkills().addExperience(Skills.THIEVING, chest.getExperience());
                addLoot(player);
                spawnEmptyChest();
                chest.onSuccess(player);
    			cancel();
    		}
    	});
    }
    
    private void failure(Player player) {
    	player.getAudioManager().sendSound(Sounds.LOCKED);
        player.getPackets().sendGameMessage("You fail to picklock the chest.");
        player.getMovement().unlock();
        chest.onFailure(player);
    }
    
    private void spawnEmptyChest() {
    	GameObject.spawnTempGroundObject(new GameObject(chest.getOpenId(), object.getType(), object.getRotation(), object), chest.getTime());
    }
    
    private void addLoot(Player player) {
        if (chest.getLoot() != null) {
            final Item[] loot = new Item[chest.getLoot().length];
            for (int i = 0; i < chest.getLoot().length; i++) {
                loot[i] = chest.getLoot()[i].generateResult();
            }
            player.getInventory().addItems(loot);
        } else {
            player.getInventory().addItems(chest.getLootTable().generateLoot());
        }
        player.getPackets().sendGameMessage("You steal some loot from the chest.");
        World.get().submit(new Task(1) {
    		@Override
    		protected void execute() {
    	        player.getMovement().unlock();
    			cancel();
    			stop(player);
    		}
    	});
    }

	@Override
	public void stop(Player player) {
	}
}