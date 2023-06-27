package com.rs.game.task.impl;

import java.util.stream.IntStream;

import com.rs.constants.Animations;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.game.task.Task;

import skills.Skills;
import skills.herblore.Potions;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 9-6-2017.
 */
public final class OverloadEffectTask extends Task {
	
	/**
	 * The player this effect is for.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link Task}.
	 */
	public OverloadEffectTask(Player player) {
		super(25, false);
		this.player = player;
	}
	
	/**
	 * The task cycles 40 times which makes up for 6 minutes.
	 * <p>The task is ran every 25 ticks (15 seconds)</p>
	 */
	private int cycle = 10;
	
	private int damageCycle;
	
	@Override
	protected void onSubmit() {
		LinkedTaskSequence seq = new LinkedTaskSequence(true);
		seq.connect(2, () -> {
			if(damageCycle == 5) {
				seq.cancel();
				return;
			}
			player.applyHit(new Hit(player, 100, HitLook.REGULAR_DAMAGE, 0));
			player.setNextAnimation(Animations.OVERLOAD);
			damageCycle++;
		});
		seq.start();
		updateSkills();
	}
	
	/**
	 * A function executed when the {@code counter} reaches the {@code delay}.
	 */
	@Override
	protected void execute() {
		if(WildernessMapZone.isAtWild(player) || cycle < 1) {
			player.getPackets().sendGameMessage("The effects of overload have ran out.");
			this.cancel();
			return;
		}
		cycle--;
	}
	
	@Override
	public void onCancel() {
		restoreSkills();
		resetOverloadEffect(false);
	}
	
	/**
	 * Applies the overload effect for the player.
	 */
	private void updateSkills() {
		Potions.applyOverLoadEffect(player);
	}
	
	/**
	 * Removes the overload effect and restores the skills back to their real state.
	 */
	private void restoreSkills() {
		IntStream.range(0, 6).filter(i -> i != Skills.PRAYER).forEach(i -> player.getSkills().restoreSkill(i));
	}
	
	/**
	 * Applies the overload effect for the specified player.
	 */
	public void applyOverloadEffect() {
		OverloadEffectTask effect = new OverloadEffectTask(player);
		player.setOverloadEffect(effect);
		effect.submit();
	}
	
	/**
	 * Resets the overload effect.
	 */
	public void resetOverloadEffect(boolean stopTask) {
		if(player.getOverloadEffect() != null) {
			if(player.getOverloadEffect().isRunning() && stopTask) {
				player.getOverloadEffect().cancel();
			}
			player.setOverloadEffect(null);
		}
	}
}
