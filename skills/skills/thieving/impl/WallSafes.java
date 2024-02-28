package skills.thieving.impl;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;

import skills.ProducingSkillAction;
import skills.Skills;
import skills.thieving.Thieving;

public class WallSafes extends ProducingSkillAction {

	public WallSafes(Player player) {
		super(player, Optional.empty());
	}

	@Override
	public boolean manualMode() {
		return true;
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if (success) {
			player.getMovement().lock(1);
			if (Thieving.success(player, 50)) {
				player.setNextAnimation(Animations.WALLSAFE_CHECKING_SHORT);
				player.getPackets().sendGameMessage("You get some loot.");
				player.getInventory().addItem(getLoot());
				player.getAudioManager().sendSound(Sounds.WALLSAFE_SUCCESS);
			} else {
				player.setNextAnimation(Animations.WALLSAFE_FAILURE);
				player.setNextGraphics(Graphic.STUN_GRAPHIC);
				player.getPackets().sendGameMessage("You slip and trigger a trap!");
				player.getAudioManager().sendSound(Sounds.WALLSAFE_FAILURE);
				player.applyHit(new Hit(player, RandomUtility.random(20, 60), Hit.HitLook.REGULAR_DAMAGE));
			}
			t.cancel();
		}
	}

	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.WALLSAFE_CHECKING);
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.empty();
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.empty();
	}

	@Override
	public int delay() {
		return 3;
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public boolean initialize() {
		player.getPackets().sendGameMessage("You start cracking the safe.");
		player.getAudioManager().sendSound(Sounds.WALLSAFE_CRACKING);
		return true;
	}

	@Override
	public boolean canExecute() {
		if (getPlayer().getSkills().getLevel(Skills.THIEVING) < 50) {
			getPlayer().getPackets().sendGameMessage("You need a thieving level of 50 to crack the wall safes.");
			return false;
		}
		if (!getPlayer().getInventory().hasFreeSlots()) {
			getPlayer().getPackets().sendGameMessage("You need some free space to crack the safe.");
			return false;
		}
		return true;
	}

	@Override
	public double experience() {
		return 70;
	}

	@Override
	public int getSkillId() {
		return Skills.THIEVING;
	}

	private final int EMERALD = 1621;
	private final int RUBY = 1619;
	private final int SAPPHIRE = 1623;
	private final int DIAMOND = 1617;

	public Item getLoot() {
		Item[] ITEMS = { new Item(SAPPHIRE), new Item(RUBY), new Item(EMERALD), new Item(DIAMOND),
				new Item(995, RandomUtility.random(100, 400)), };
		return RandomUtility.random(ITEMS);
	}
}