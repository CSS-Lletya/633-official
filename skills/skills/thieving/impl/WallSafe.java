package skills.thieving.impl;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

import skills.Skills;
import skills.thieving.Thieving;

public class WallSafe extends Thieving {
	
	public WallSafe(Player player, Optional<WorldTile> position) {
		super(player, position);
	}

	@Override
	public int requirement() {
		return 50;
	}

	@Override
	public Item loot() {
		return getLoot();
	}

	@Override
	public boolean canInit() {
		if (getPlayer().getSkills().getLevel(Skills.THIEVING) < requirement()) {
			getPlayer().getPackets().sendGameMessage("You need a thieving level of " + requirement() + " to steal from this Wall safe.");
			return false;
		}
		if(!getPlayer().getInventory().hasFreeSlots()) {
			getPlayer().getPackets().sendGameMessage("You don't have enough inventory space for the loot.");
			return false;
		}
		if (!player.getDetails().getThievingStun().elapsed(1800)) {
			return false;
		}
		player.getDetails().getThievingStun().reset();
		return true;
	}
	
	@Override
	public void onSubmit() {
		if (!player.getInventory().containsAny(5560)) {
			player.getPackets().sendGameMessage("You need a " + ItemDefinitions.getItemDefinitions(5560).getName() + " in order to do this.");
			return;
		}
		player.getPackets().sendGameMessage("You attempt to pick the lock...");
		player.setNextAnimation(new Animation(2247));
		player.getAudioManager().sendSound(Sounds.PICKING_LOCK_MANY);
	}
	
	@Override
	public boolean failure() {
		return (RandomUtils.inclusive(getPlayer().getSkills().getLevel(Skills.THIEVING) + RandomUtils.inclusive(5)) < (RandomUtils.inclusive(requirement()) + RandomUtils.inclusive((5))));
	}
	
	@Override
	public void onExecute(Task t) {
		if (failure()) {
			player.getPackets().sendGameMessage("You fail and trigger a trap!");
			player.setNextAnimation(new Animation(3170));
			player.applyHit(
					new Hit(player, RandomUtils.random(player.getSkills().getLevel(Skills.HITPOINTS) <= 30 ? 40 : 100),
							Hit.HitLook.REGULAR_DAMAGE));
			t.cancel();
		} else {
			player.getPackets().sendGameMessage("You successfully crack the safe!");
			player.getDetails().getStatistics()
			.addStatistic(ItemDefinitions.getItemDefinitions(getLoot().getId()).getName() + "_Taken_From_Safe")
			.addStatistic("Safes_Cracked");
		}
		t.cancel();
	}

	@Override
	public int getSkillId() {
		return Skills.THIEVING;
	}

	@Override
	public double experience() {
		return 70;
	}

	@Override
	public boolean canExecute() {
		return player.getInventory().containsAny(5560);
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public int delay() {
		return 3;
	}

	private final int EMERALD = 1621;
	private final int RUBY = 1619;
	private final int SAPPHIRE = 1623;
	private final int DIAMOND = 1617;
	
	public Item getLoot() {
		Item[] ITEMS = { new Item(SAPPHIRE), new Item(RUBY), new Item(EMERALD), new Item(DIAMOND),
				new Item(995, RandomUtils.random(20, 40)), };
		return RandomUtils.random(ITEMS);
	}
}