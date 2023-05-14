package com.rs.game.player.content;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.Utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A new way of handling Emotes & Special Emotes such as Linked Queue emotes &
 * Skillcapes.
 * 
 * @author Dennis
 *
 */
public class Emotes {
	
	/**
	 * A list of Emotes for the Player to perform from the Emotes tab
	 * 
	 * @author Dennis
	 *
	 */
	@AllArgsConstructor
	public enum Emote {
		YES(2, Optional.of(Animations.YES), Optional.empty(), Optional.empty()),
		NO(3, Optional.of(Animations.NO), Optional.empty(), Optional.empty()),
		BOW(4, Optional.of(Animations.BOW_DOWN), Optional.empty(), Optional.empty()),
		ANGRY(5, Optional.of(Animations.ANGRY), Optional.empty(), Optional.empty()),
		THINKING(6, Optional.of(Animations.THINKING), Optional.empty(), Optional.empty()),
		WAVE(7, Optional.of(Animations.WAVE), Optional.empty(), Optional.empty()),
		SHRUG(8, Optional.of(Animations.SHRUG), Optional.empty(), Optional.empty()),
		CHEER(9, Optional.of(Animations.CHEER), Optional.empty(), Optional.empty()),
		BECKON(10, Optional.of(Animations.BECKON), Optional.empty(), Optional.empty()),
		LAUGH(12, Optional.of(Animations.LAUGH), Optional.empty(), Optional.empty()),
		JUMP_FOR_JOY(11, Optional.of(Animations.JUMP_FOR_JOY), Optional.empty(), Optional.empty()),
		YAWN(13, Optional.of(Animations.YAWN), Optional.empty(), Optional.empty()),
		DANCE(14, Optional.of(Animations.DANCE), Optional.empty(), Optional.empty()),
		JIG(15, Optional.of(Animations.JIG), Optional.empty(), Optional.empty()),
		TWIRL(16, Optional.of(Animations.TWIRL), Optional.empty(), Optional.empty()),
		HEADBANG(17, Optional.of(Animations.HEAD_BANG), Optional.empty(), Optional.empty()),
		CRY(18, Optional.of(Animations.CRY), Optional.empty(), Optional.empty()),
		BLOW_KISS(19, Optional.of(Animations.BLOW_KISS), Optional.of(Graphic.BLOW_KISS), Optional.empty()),
		PANIC(20, Optional.of(Animations.PANIC), Optional.empty(), Optional.empty()),
		RASPBERRY(21, Optional.of(Animations.RASPBERRY), Optional.empty(), Optional.empty()),
		CLAP(22, Optional.of(Animations.CLAP), Optional.empty(), Optional.empty()),
		SALUTE(23, Optional.of(Animations.SALUTE), Optional.empty(), Optional.empty()),
		GOBLIN_BOW(24, Optional.of(Animations.GOBLIN_BOW), Optional.empty(), Optional.empty()),
		GOBLIN_SALUTE(25, Optional.of(Animations.GOBLIN_SALUTE), Optional.empty(), Optional.empty()),
		GLASS_BOX(26, Optional.of(Animations.GLASS_BOX), Optional.empty(), Optional.empty()),
		CLIMB_ROPE(27, Optional.of(Animations.CLIMB_ROPE), Optional.empty(), Optional.empty()),
		LEAN(28, Optional.of(Animations.LEAN), Optional.empty(), Optional.empty()),
		GLASS_WALL(29, Optional.of(Animations.GLASS_WALL), Optional.empty(), Optional.empty()),
		IDEA(30, Optional.of(Animations.IDEA), Optional.empty(), Optional.empty()),
		STOMP(31, Optional.of(Animations.STOMP), Optional.empty(), Optional.empty()),
		FLAP(32, Optional.of(Animations.FLAP), Optional.empty(), Optional.empty()),
		SLAP_HEAD(33, Optional.of(Animations.SLAP_HEAD), Optional.empty(), Optional.empty()),
		ZOMBIE_WALK(34, Optional.of(Animations.ZOMBIE_WALK), Optional.empty(), Optional.empty()),
		ZOMBIE_DANCE(35, Optional.of(Animations.ZOMBIE_DANCE), Optional.empty(), Optional.empty()),
		ZOMBIE_HAND(36, Optional.of(Animations.ZOMBIE_HAND), Optional.of(Graphic.ZOMBIE_HAND), Optional.empty()),
		SCARED(37, Optional.of(Animations.SCARED), Optional.empty(), Optional.empty()),
		BUNNY_HOP(38, Optional.of(Animations.BUNNY_HOP), Optional.empty(), Optional.empty()),
		//skillcape is 39
		SNOWMAN_DANCE(40, Optional.of(Animations.SNOWMAN_DANCE), Optional.empty(), Optional.empty()),
		AIR_GUITAR(41, Optional.of(Animations.AIR_GUITAR), Optional.of(Graphic.AIR_GUITAR), Optional.of(SpecialEmote.AIR_GUITAR)),
		SAFETY_FIRST(42, Optional.of(Animations.SAFETY_FIRST), Optional.of(Graphic.SAFETY_FIRST), Optional.empty()),
		EXPLORE(43, Optional.of(Animations.EXPLORE), Optional.of(Graphic.EXPLORE), Optional.empty()),
		TRICK(44, Optional.of(Animations.TRICK), Optional.of(Graphic.TRICK), Optional.empty()),
		FREEZE(45, Optional.of(Animations.FREEZE), Optional.of(Graphic.FREEZE), Optional.empty()),
		TURKEY(46, Optional.empty(),  Optional.empty(), Optional.of(SpecialEmote.TURKEY)),
		AROUND_THE_WORLD_IN_EGGTY_DAYS(47, Optional.of(Animations.AROUND_THE_WORLD_IN_EGGTY_DAYS), Optional.of(Graphic.AROUND_THE_WORLD_IN_EGGTY_DAYS), Optional.empty()),
		DRAMATIC_POINT(48, Optional.of(Animations.DRAMATIC_POINT), Optional.empty(), Optional.empty()),
		FAINT(49, Optional.of(Animations.FAINT), Optional.empty(), Optional.empty()),
		PUPPET_MASTER(50, Optional.of(Animations.PUPPET_MASTER), Optional.of(Graphic.PUPPET_MASTER), Optional.empty()),
		TASK_MASTER(51, Optional.of(Animations.TASK_MASTER), Optional.of(Graphic.TASK_MASTER), Optional.empty())
		;

		/**
		 * The button Id (slot id).
		 */
		@Getter
		private final int buttonId;
		
		/**
		 * The Animation being performed.
		 */
		@Getter
		private final Optional<Animation> animation;
		
		/**
		 * The Graphics being performed.
		 */
		@Getter
		private final Optional<Graphics> graphics;
		
		/**
		 * The Special Emote being performed.
		 */
		@Getter
		private final Optional<SpecialEmote> specialEmote;

		/**
		 * Executes the Emote.
		 * 
		 * @param player
		 * @param buttonId
		 */
		public static void executeEmote(Player player, int buttonId) {
			if (isDoingEmote(player))
	    		return;
			for (Emote emote : Emote.values()) {
				if (buttonId == emote.getButtonId()) {
					emote.getSpecialEmote().ifPresent(user -> user.handleSpecialEmote(player));
					emote.getAnimation().ifPresent(player::setNextAnimation);
					emote.getGraphics().ifPresent(player::setNextGraphics);
					if (!isDoingEmote(player))
					    setNextEmoteEnd(player);
				}
			}
		}
		
	    private static void setNextEmoteEnd(Player player) {
	    	player.setNextEmoteEnd(player.getLastAnimationEnd() - 600);
	    }

	    public void setNextEmoteEnd(Player player, long delay) {
	    	player.setNextEmoteEnd(Utility.currentTimeMillis() + delay);
	    }

	    public static boolean isDoingEmote(Player player) {
	    	return player.getNextEmoteEnd() >= Utility.currentTimeMillis();
	    }
	}
	
	/**
	 * A list of Special Emote events to take place (Skillcapes, Linked Queue events, etc..).
	 * @author Dennis
	 *
	 */
	private enum SpecialEmote {
		AIR_GUITAR {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				player.getPackets().sendMusicEffect(302);
				return true;
			}
		},
		TURKEY {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				LinkedTaskSequence turkeySeq = new LinkedTaskSequence();
				turkeySeq.connect(1, () -> {
					player.setNextAnimation(Animations.TURKEY_PART_1);
					player.setNextGraphics(Graphic.SMALL_CLOUD_COVERING_PLAYER);
				});
				turkeySeq.connect(2, () -> {
					player.setNextAnimation(Animations.TURKEY_PART_2);
					player.getAppearance().transformIntoNPC(8499);
				});
				turkeySeq.connect(6, () -> {
					player.setNextAnimation(Animations.TURKEY_PART_3);
					player.setNextGraphics(Graphic.SMALL_CLOUD_COVERING_PLAYER);
					player.getAppearance().transformIntoNPC(-1);
				});
				turkeySeq.start();
				return true;
			}
		};
		
		/**
		 * The execution method for the Special Emote.
		 * @param player
		 * @return state
		 */
		protected boolean handleSpecialEmote(Player player) {
			return false;
		}
	}
}