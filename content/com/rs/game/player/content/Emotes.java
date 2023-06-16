package com.rs.game.player.content;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.game.task.Task;
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
					player.getTreasureTrailsManager().useEmote(buttonId);
					player.getDetails().getStatistics().addStatistic("Emotes_Performed");
					if (!isDoingEmote(player))
					    setNextEmoteEnd(player);
				}
				useBookEmote(player, buttonId);
			}
		}
		
	    public static void setNextEmoteEnd(Player player) {
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
	

    public static void useBookEmote(Player player, int id) {
        player.getMovement().stopAll(false);
        if (id == 39) {
            final int capeId = player.getEquipment().getCapeId();
            switch (capeId) {
                case 9747:
                case 9748:
                case 10639: // Attack cape
                    player.setNextAnimation(new Animation(4959));
                    player.setNextGraphics(new Graphics(823));
                    break;
                case 9753:
                case 9754:
                case 10641: // Defence cape
                    player.setNextAnimation(new Animation(4961));
                    player.setNextGraphics(new Graphics(824));
                    break;
                case 9750:
                case 9751:
                case 10640: // Strength cape
                    player.setNextAnimation(new Animation(4981));
                    player.setNextGraphics(new Graphics(828));
                    break;
                case 9768:
                case 9769:
                case 10647: // Hitpoints cape
                    player.setNextAnimation(new Animation(14242));
                    player.setNextGraphics(new Graphics(2745));
                    break;
                case 9756:
                case 9757:
                case 10642: // Ranged cape
                    player.setNextAnimation(new Animation(4973));
                    player.setNextGraphics(new Graphics(832));
                    break;
                case 9762:
                case 9763:
                case 10644: // Magic cape
                    player.setNextAnimation(new Animation(4939));
                    player.setNextGraphics(new Graphics(813));
                    break;
                case 9759:
                case 9760:
                case 10643: // Prayer cape
                    player.setNextAnimation(new Animation(4979));
                    player.setNextGraphics(new Graphics(829));
                    break;
                case 9801:
                case 9802:
                case 10658: // Cooking cape
                    player.setNextAnimation(new Animation(4955));
                    player.setNextGraphics(new Graphics(821));
                    break;
                case 9807:
                case 9808:
                case 10660: // Woodcutting cape
                    player.setNextAnimation(new Animation(4957));
                    player.setNextGraphics(new Graphics(822));
                    break;
                case 9783:
                case 9784:
                case 10652: // Fletching cape
                    player.setNextAnimation(new Animation(4937));
                    player.setNextGraphics(new Graphics(812));
                    break;
                case 9798:
                case 9799:
                case 10657: // Fishing cape
                    player.setNextAnimation(new Animation(4951));
                    player.setNextGraphics(new Graphics(819));
                    break;
                case 9804:
                case 9805:
                case 10659: // Firemaking cape
                    player.setNextAnimation(new Animation(4975));
                    player.setNextGraphics(new Graphics(831));
                    break;
                case 9780:
                case 9781:
                case 10651: // Crafting cape
                    player.setNextAnimation(new Animation(4949));
                    player.setNextGraphics(new Graphics(818));
                    break;
                case 9795:
                case 9796:
                case 10656: // Smithing cape
                    player.setNextAnimation(new Animation(4943));
                    player.setNextGraphics(new Graphics(815));
                    break;
                case 9792:
                case 9793:
                case 10655: // Mining cape
                    player.setNextAnimation(new Animation(4941));
                    player.setNextGraphics(new Graphics(814));
                    break;
                case 9774:
                case 9775:
                case 10649: // Herblore cape
                    player.setNextAnimation(new Animation(4969));
                    player.setNextGraphics(new Graphics(835));
                    break;
                case 9771:
                case 9772:
                case 10648: // Agility cape
                    player.setNextAnimation(new Animation(4977));
                    player.setNextGraphics(new Graphics(830));
                    break;
                case 9777:
                case 9778:
                case 10650: // Thieving cape
                    player.setNextAnimation(new Animation(4965));
                    player.setNextGraphics(new Graphics(826));
                    break;
                case 9786:
                case 9787:
                case 10653: // Slayer cape
                    player.setNextAnimation(new Animation(4967));
                    player.setNextGraphics(new Graphics(1656));
                    break;
                case 9810:
                case 9811:
                case 10611: // Farming cape
                    player.setNextAnimation(new Animation(4963));
                    player.setNextGraphics(new Graphics(825));
                    break;
                case 9765:
                case 9766:
                case 10645: // Runecrafting cape
                    player.setNextAnimation(new Animation(4947));
                    player.setNextGraphics(new Graphics(817));
                    break;
                case 9789:
                case 9790:
                case 10654: // Construction cape
                    player.setNextAnimation(new Animation(4953));
                    player.setNextGraphics(new Graphics(820));
                    break;
                case 12169:
                case 12170:
                case 12524: // Summoning cape
                    player.setNextAnimation(new Animation(8525));
                    player.setNextGraphics(new Graphics(1515));
                    break;
                case 9948:
                case 9949:
                case 10646: // Hunter cape
                    player.setNextAnimation(new Animation(5158));
                    player.setNextGraphics(new Graphics(907));
                    break;
                case 9813:
                case 10662: // Quest cape
                    player.setNextAnimation(new Animation(4945));
                    player.setNextGraphics(new Graphics(816));
                    break;
                case 18508:
                case 18509: // Dungeoneering cape
                    final int rand = (int) (Math.random() * (2 + 1));
                    player.setNextAnimation(new Animation(13190));
                    player.setNextGraphics(new Graphics(2442));

                    LinkedTaskSequence seq = new LinkedTaskSequence();
                    seq.connect(1, () -> {
                        player.setNextAnimation(new Animation(((rand > 0 ? 13192 : (rand == 2 ? 13193 : 13194)))));
                    }).connect(2, () -> {
                        player.getAppearance().transformIntoNPC((rand == 0 ? 11229 : (rand == 2 ? 11228 : 11227)));
                    }).connect(3, () -> {
                        player.getAppearance().transformIntoNPC(-1);
                    }).start();
                    break;
                case 19709:
                case 19710: // Master dungeoneering cape
                	World.get().submit(new Task(2) {
                		int step;
    					private NPC dung1, dung2, dung3, dung4;
						@Override
						protected void execute() {
							if (step == 1) {
								player.getAppearance().transformIntoNPC(11229);
								player.setNextAnimation(new Animation(14608));
								dung1 = new NPC(-1, new WorldTile(player.getX(), player.getY() - 1, player.getPlane()), null, false);
								player.faceEntity(dung1);
								dung1.setLocation(dung1);
								dung1.setNextGraphics(new Graphics(2777));
								dung2 = new NPC(-1, new WorldTile(player.getX() + 1, player.getY() - 1, player.getPlane()), null, false);
							}
							if (step == 2) {
								dung1.deregister();
								player.getAppearance().transformIntoNPC(11228);
								dung2.setLocation(dung2);
								player.setNextAnimation(new Animation(14609));
								player.setNextGraphics(new Graphics(2782));
								dung2.setNextGraphics(new Graphics(2778));
								dung3 = new NPC(-1, new WorldTile(player.getX(), player.getY() - 1, player.getPlane()), null, false);
								dung4 = new NPC(-1, new WorldTile(player.getX(), player.getY() + 1, player.getPlane()), null, false);
							}
							if (step == 3) {
								dung2.deregister();
								player.getAppearance().transformIntoNPC(11227);
								dung3.setLocation(dung3);
								dung4.setLocation(dung4);
								dung4.faceEntity(player);
								player.setNextAnimation(new Animation(14610));
								dung3.setNextGraphics(new Graphics(2779));
								dung4.setNextGraphics(new Graphics(2780));
							}
							if (step > 4) {
								player.getAppearance().transformIntoNPC(-1);
								dung3.deregister();
								dung4.deregister();
								cancel();
							}
							step++;
						}
					});
                    break;
            }
            return;
        }
        if (!Emote.isDoingEmote(player))
            Emote.setNextEmoteEnd(player);
    }
}