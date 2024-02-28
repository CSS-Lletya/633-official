package com.rs.game.player.actions;

import com.rs.GameConstants;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.Utility;

public class HomeTeleporting extends Action {

	int ticks;
	private final int[] ANIMATIONS = { 1722, 1723, 1724, 1725, 2798, 2799, 2800, 3195, 4643, 4645, 4646, 4847, 4848,
			4849, 4850, 4851, 4852 };
	private int[] GRAPHICS = {
			800, 801, 802, 1703, 1704, 1705, 1706, 1707, 1708, 1709, 1710, 1711, 1712, 1713 };
	
	@Override
	public boolean start(final Player player) {
		if (!player.getDetails().getHomeDelay().finished()) {
			int minutes = player.getDetails().getHomeDelay().getMinutes();
			player.getPackets().sendGameMessage("You need to wait another " + minutes  + " " + (minutes == 1 ? "minute" : "minutes") + " to cast this spell.");
			return false;
		}
		if (player.getMapZoneManager().execute(zone -> !zone.processMagicTeleport(player, GameConstants.START_PLAYER_LOCATION))) {
			return false;
		}
		return process(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (ticks++ == 0) {
			player.setNextAnimation(new Animation(ANIMATIONS[0]));
			player.setNextGraphics(new Graphics(GRAPHICS[0]));
		} else if (ticks == 1) {
			player.setNextGraphics(new Graphics(GRAPHICS[0]));
			player.setNextAnimation(new Animation(ANIMATIONS[1]));
		} else if (ticks == 2) {
			player.setNextGraphics(new Graphics(GRAPHICS[1]));
			player.setNextAnimation(new Animation(ANIMATIONS[2]));
		} else if (ticks == 3) {
			player.setNextGraphics(new Graphics(GRAPHICS[2]));
			player.setNextAnimation(new Animation(ANIMATIONS[3]));
		} else if (ticks == 4) {
			player.setNextGraphics(new Graphics(GRAPHICS[3]));
			player.setNextAnimation(new Animation(ANIMATIONS[4]));
		} else if (ticks == 5) {
			player.setNextGraphics(new Graphics(GRAPHICS[3]));
			player.setNextAnimation(new Animation(ANIMATIONS[5]));
		} else if (ticks == 6) {
			player.setNextGraphics(new Graphics(GRAPHICS[3]));
			player.setNextAnimation(new Animation(ANIMATIONS[6]));
		} else if (ticks == 7) {
			player.setNextGraphics(new Graphics(GRAPHICS[4]));
			player.setNextAnimation(new Animation(ANIMATIONS[7]));
		} else if (ticks == 8) {
			player.setNextGraphics(new Graphics(GRAPHICS[5]));
			player.setNextAnimation(new Animation(ANIMATIONS[8]));
		} else if (ticks == 9) {
			player.setNextGraphics(new Graphics(GRAPHICS[6]));
			player.setNextAnimation(new Animation(ANIMATIONS[9]));
		} else if (ticks == 10) {
			player.setNextGraphics(new Graphics(GRAPHICS[7]));
			player.setNextAnimation(new Animation(ANIMATIONS[10]));
		} else if (ticks == 11) {
			player.setNextGraphics(new Graphics(GRAPHICS[8]));
			player.setNextAnimation(new Animation(ANIMATIONS[11]));
		} else if (ticks == 12) {
			player.setNextGraphics(new Graphics(GRAPHICS[9]));
			player.setNextAnimation(new Animation(ANIMATIONS[12]));
		} else if (ticks == 13) {
			player.setNextGraphics(new Graphics(GRAPHICS[10]));
			player.setNextAnimation(new Animation(ANIMATIONS[13]));
		} else if (ticks == 14) {
			player.setNextGraphics(new Graphics(GRAPHICS[11]));
			player.setNextAnimation(new Animation(ANIMATIONS[14]));
		} else if (ticks == 15) {
			player.setNextGraphics(new Graphics(GRAPHICS[12]));
			player.setNextAnimation(new Animation(ANIMATIONS[15]));
			player.getAudioManager().sendSound(Sounds.TELEPORING_WARPING);
		} else if (ticks == 15) {
			player.setNextGraphics(new Graphics(GRAPHICS[13]));
			player.setNextAnimation(new Animation(ANIMATIONS[16]));
			
		} else if (ticks == 17) {
			player.setNextWorldTile(GameConstants.START_PLAYER_LOCATION);
			player.setNextGraphics(Graphic.RESET_GRAPHICS);
			player.setNextAnimation(Animations.RESET_ANIMATION);
			player.getDetails().getHomeDelay().start(60 * 30);
			stop(player);
		}
		return 0;
	}

	@Override
	public boolean process(Player player) {
		if (player.getAttackedByDelay() + 10000 > Utility.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You can't home teleport until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}

	@Override
	public void stop(Player player) {
		player.setNextAnimation(new Animation(-1));
		player.setNextGraphics(new Graphics(-1));
	}
}