package com.rs.game.player.content;

import java.util.Optional;

import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.Utility;

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
	public enum Emote {
		YES((byte) 2, Optional.of(new Animation(855)), Optional.empty(), Optional.empty()),
		NO((byte) 3, Optional.of(new Animation(856)), Optional.empty(), Optional.empty()),
		BOW((byte) 4, Optional.of(new Animation(858)), Optional.empty(), Optional.empty()),
		ANGRY((byte) 5, Optional.of(new Animation(859)), Optional.empty(), Optional.empty()),
		THINKING((byte) 6, Optional.of(new Animation(857)), Optional.empty(), Optional.empty()),
		WAVE((byte) 7, Optional.of(new Animation(863)), Optional.empty(), Optional.empty()),
		SHRUG((byte) 8, Optional.of(new Animation(2113)), Optional.empty(), Optional.empty()),
		CHEER((byte) 9, Optional.of(new Animation(862)), Optional.empty(), Optional.empty()),
		BECKON((byte) 10, Optional.of(new Animation(864)), Optional.empty(), Optional.empty()),
		LAUGH((byte) 12, Optional.of(new Animation(861)), Optional.empty(), Optional.empty()),
		JUMP_FOR_JOY((byte) 11, Optional.of(new Animation(2109)), Optional.empty(), Optional.empty()),
		YAWN((byte) 13, Optional.of(new Animation(2111)), Optional.empty(), Optional.empty()),
		DANCE((byte) 14, Optional.of(new Animation(866)), Optional.empty(), Optional.empty()),
		JIG((byte) 15, Optional.of(new Animation(2106)), Optional.empty(), Optional.empty()),
		TWIRL((byte) 16, Optional.of(new Animation(2107)), Optional.empty(), Optional.empty()),
		HEADBANG((byte) 17, Optional.of(new Animation(2108)), Optional.empty(), Optional.empty()),
		CRY((byte) 18, Optional.of(new Animation(860)), Optional.empty(), Optional.empty()),
		BLOW_KISS((byte) 19, Optional.of(new Animation(1374)), Optional.of(new Graphics(1702)), Optional.empty()),
		PANIC((byte) 20, Optional.of(new Animation(2105)), Optional.empty(), Optional.empty()),
		RASPBERRY((byte) 21, Optional.of(new Animation(2110)), Optional.empty(), Optional.empty()),
		CLAP((byte) 22, Optional.of(new Animation(865)), Optional.empty(), Optional.empty()),
		SALUTE((byte) 23, Optional.of(new Animation(2112)), Optional.empty(), Optional.empty()),
		GOBLIN_BOW((byte) 24, Optional.of(new Animation(0x84F)), Optional.empty(), Optional.empty()),
		GOBLIN_SALUTE((byte) 25, Optional.of(new Animation(0x850)), Optional.empty(), Optional.empty()),
		GLASS_BOX((byte) 26, Optional.of(new Animation(1131)), Optional.empty(), Optional.empty()),
		CLIMB_ROPE((byte) 27, Optional.of(new Animation(1130)), Optional.empty(), Optional.empty()),
		LEAN((byte) 28, Optional.of(new Animation(1129)), Optional.empty(), Optional.empty()),
		GLASS_WALL((byte) 29, Optional.of(new Animation(1128)), Optional.empty(), Optional.empty()),
		IDEA((byte) 30, Optional.of(new Animation(4275)), Optional.empty(), Optional.empty()),
		STOMP((byte) 31, Optional.of(new Animation(1745)), Optional.empty(), Optional.empty()),
		FLAP((byte) 32, Optional.of(new Animation(4280)), Optional.empty(), Optional.empty()),
		SLAP_HEAD((byte) 33, Optional.of(new Animation(4276)), Optional.empty(), Optional.empty()),
		ZOMBIE_WALK((byte) 34, Optional.of(new Animation(3544)), Optional.empty(), Optional.empty()),
		ZOMBIE_DANCE((byte) 35, Optional.of(new Animation(3543)), Optional.empty(), Optional.empty()),
		ZOMBIE_HAND((byte) 36, Optional.of(new Animation(7272)), Optional.of(new Graphics(1244)), Optional.empty()),
		SCARED((byte) 37, Optional.of(new Animation(2836)), Optional.empty(), Optional.empty()),
		BUNNY_HOP((byte) 38, Optional.of(new Animation(6111)), Optional.empty(), Optional.empty()),
		//skillcape is 39
		SNOWMAN_DANCE((byte) 40, Optional.of(new Animation(7531)), Optional.empty(), Optional.empty()),
		AIR_GUITAR((byte) 41, Optional.of(new Animation(2414)), Optional.of(new Graphics(1537)), Optional.of(SpecialEmote.AIR_GUITAR)),
		SAFETY_FIRST((byte) 42, Optional.of(new Animation(8770)), Optional.of(new Graphics(1553)), Optional.empty()),
		EXPLORE((byte) 43, Optional.of(new Animation(9990)), Optional.of(new Graphics(1734)), Optional.empty()),
		TRICK((byte) 44, Optional.of(new Animation(10530)), Optional.of(new Graphics(1864)), Optional.empty()),
		FREEZE((byte) 45, Optional.of(new Animation(11044)), Optional.of(new Graphics(1973)), Optional.empty()),
		TURKEY((byte) 46, Optional.empty(),  Optional.empty(), Optional.of(SpecialEmote.TURKEY)),
		AROUND_THE_WORLD_IN_EGGTY_DAYS((byte) 47, Optional.of(new Animation(11542)), Optional.of(new Graphics(2037)), Optional.empty()),
		DRAMATIC_POINT((byte) 48, Optional.of(new Animation(12658)), Optional.empty(), Optional.empty()),
		FAINT((byte) 49, Optional.of(new Animation(14165 )), Optional.empty(), Optional.empty()),
		PUPPET_MASTER((byte) 50, Optional.of(new Animation(14869)), Optional.of(new Graphics(2837)), Optional.empty()),
		TASK_MASTER((byte) 51, Optional.of(new Animation(15033)), Optional.of(new Graphics(2930)), Optional.empty())
		;

		/**
		 * The button Id (slot id).
		 */
		@Getter
		private final byte buttonId;
		
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
		 * Constructs a new Player Emote.
		 * 
		 * @param buttonId
		 * @param animation
		 * @param graphics
		 * @param specialEmote
		 */
		private Emote(byte buttonId, Optional<Animation> animation, Optional<Graphics> graphics,
				Optional<SpecialEmote> specialEmote) {
			this.buttonId = buttonId;
			this.animation = animation;
			this.graphics = graphics;
			this.specialEmote = specialEmote;
		}

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
					player.setNextAnimation(new Animation(10994));
					player.setNextGraphics(new Graphics(86));
				});
				turkeySeq.connect(2, () -> {
					player.setNextAnimation(new Animation(10996));
					player.getAppearance().transformIntoNPC(8499);
				});
				turkeySeq.connect(6, () -> {
					player.setNextAnimation(new Animation(10995));
					player.setNextGraphics(new Graphics(86));
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