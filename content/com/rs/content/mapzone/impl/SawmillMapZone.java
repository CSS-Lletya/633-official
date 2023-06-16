package com.rs.content.mapzone.impl;

import com.rs.constants.Sounds;
import com.rs.content.mapzone.MapZone;
import com.rs.content.mapzone.ZoneRestriction;
import com.rs.game.Entity;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.impl.Overseer;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.Utility;

import skills.Skills;
import skills.woodcutting.sawmill.ConveyorBeltHopper;
import skills.woodcutting.sawmill.CutPlank;
import skills.woodcutting.sawmill.PlanksTake;
import skills.woodcutting.sawmill.ReturnLogs;
import skills.woodcutting.sawmill.Sawmill;
import skills.woodcutting.sawmill.StackOfLogs;

public class SawmillMapZone extends MapZone {

	public static final int OVERSEER = 8904;

	public SawmillMapZone() {
		super("Sawmill", MapZoneSafetyCondition.SAFE, MapZoneType.NORMAL, ZoneRestriction.FIRES,
				ZoneRestriction.CANNON);
	}

	@Override
	public void start(Player player) {
		if (this.getArguments(player) == null || this.getArguments(player).length == 0) {
			setJob(player, getJob(player));
			return;
		}
	}

	@Override
	public boolean login(Player player) {
		refreshVars(player);
		sendInterfaces(player);
		return true;
	}

	@Override
	public boolean logout(Player player) {
		return false; // so doesnt remove script
	}

	@Override
	public void finish(Player player) {

	}

	@Override
	public boolean canMove(Player player, int dir) {
		return true;
	}

	@Override
	public void sendInterfaces(Player player) {
		if (getJob(player) != null)
			player.getInterfaceManager().sendOverlay(766, false);
	}

	@Override
	public boolean processNPCClick1(Player player, NPC npc) {
		if (npc.getId() == OVERSEER) {
			player.dialog(new Overseer(player, npc, this));
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick2(Player player, final NPC npc) {
		if (npc.getId() == OVERSEER) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			if (isOrderCompleted(player)) {
				finishJob(player);
				player.dialog(new DialogueEventListener(player, npc) {
					@Override
					public void start() {
						npc(happy, "Good job! I'll fill in the paperwork and send them on their way.");
					}
				});
			} else {
				player.dialog(new DialogueEventListener(player, npc) {
					@Override
					public void start() {
						npc(happy, "I don't think you've got all the planks you need for this order. Come back when you're done.");
					}
				});
			}
			
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(Player player, GameObject object) {
		if (object.getId() == 46317) {
			if (player.getInventory().getFreeSlots() == 0) {
				player.getPackets()
						.sendGameMessage("This crate is full of saws, but you don't have enough space to take one.");
				return false;
			}
			player.getMovement().lock(2);
			player.getInventory().addItem(8794, 1);
			player.getPackets().sendGameMessage("You search in the crate to find a saw.");
			player.getAudioManager().sendSound(Sounds.PICK_FLAX);
			return false;
		}
		if (object.getId() == 46296) { // job board
			openSawmillBoard(player);
			return false;
		}
		if (object.getId() == 46297) {// take logs
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			player.getAction().setAction(new StackOfLogs(28));
			return false;
		}
		if (object.getId() == 46304) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			player.getAction().setAction(new ConveyorBeltHopper(28, this));
			return false;
		}
		if (object.getId() == 46309) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			player.getAction().setAction(new PlanksTake(28, this));
			return false;
		}
		if (object.getId() == 46307 && object.getX() == 3311 && object.getY() == 3491) {
			if (Sawmill.hasPlanksOrLogs(player)) {
				player.dialog(new DialogueEventListener(player, Entity.findNPC(SawmillMapZone.OVERSEER)) {
					@Override
					public void start() {
						npc(angry_2, "Oi! That's our wood you've got there! Give it  back!");
					}
				});
				return false;
			}
			if (player.getInventory().containsAny(8794)) {
				player.getInventory().deleteItem(new Item(8794));
				player.getPackets().sendGameMessage("You return the Saw back to the Overseer.");
			}
			player.getMovement().lock(2);
			player.addWalkSteps(object.getX(), object.getY(), 1, false);
			leave(player);
			player.getInterfaceManager().removeOverlay();
			return false;
		}
		if (object.getId() == 46303) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			inspectCart(player);
			return false;
		}
		if (object.getId() == 46300) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			openCutPlanks(player);
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(Player player, final GameObject object) {
		if (object.getId() == 46296) { // job board
			takeJob(player, true);
			return false;
		}
		if (object.getId() == 46297) {// take logs
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			player.getAction().setAction(new StackOfLogs(1));
			return false;
		}
		if (object.getId() == 46304) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			player.getAction().setAction(new ConveyorBeltHopper(1, this));
			return false;
		}
		if (object.getId() == 46309) {
			if (!hasJob(player)) {
				player.dialogue(d -> d.mes("You should get a job from the job board first."));
				return false;
			}
			player.getAction().setAction(new PlanksTake(1, this));
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick3(Player player, GameObject object) {
		if (object.getId() == 46296) { // job board
			takeJob(player, false);
			return false;
		}
		if (object.getId() == 46297) {
			player.getAction().setAction(new ReturnLogs());
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(Player player, int type) {
		if (player.getInventory().containsAny(1511)) {
			int amountOfLogs = player.getInventory().getNumberOf(1511);
			player.getInventory().deleteItem(new Item(1511, amountOfLogs));
			player.getPackets().sendGameMessage("You return the Logs back to the Overseer.");
		}
		if (player.getInventory().containsAny(8794)) {
			int amountOfSaws = player.getInventory().getNumberOf(8794);
			player.getInventory().deleteItem(new Item(8794, amountOfSaws));
			player.getPackets().sendGameMessage("You return the Saw back to the Overseer.");
		}
		if (player.getInventory().containsAny(960)) {
			int amountOfPlanks = player.getInventory().getNumberOf(960);
			player.getInventory().deleteItem(new Item(960, amountOfPlanks));
			player.getPackets().sendGameMessage("You return the planks back to the Overseer.");
		}
		player.getInterfaceManager().removeOverlay();
	}

	@Override
	public void forceClose(Player player) {
		leave(player);
		player.getInterfaceManager().removeOverlay();
	}

	@Override
	public boolean sendDeath(Player player) {
		leave(player);
		return true;
	}

	public boolean isPlanksFull() {
		return planks >= 100;
	}

	public boolean hasPlanks() {
		return planks > 0;
	}

	public void removePlank(Player player) {
		planks--;
		refreshPlanks(player);
	}

	public void addPlank(Player player) {
		planks += 2;
		refreshConveyorAnimation(player);
		refreshPlanks(player);
	}

	private void refreshConveyorAnimation(Player player) {
		if (Utility.currentWorldCycle() > lastLogAnimation + 5) {
			lastLogAnimation = Utility.currentWorldCycle();
			GameObject.sendObjectAnimation(player, CONVEYOR_BELT, new Animation(12394));
			GameObject.sendObjectAnimation(player, CONVEYOR_BELT, new Animation(12395));
		}
	}

	private void refreshPlanks(Player player) {
		player.getVarsManager().sendVarBit(4214, planks);
	}

	public boolean hasJob(Player player) {
		return getJob(player) != null;
	}

	private void leave(Player player) {
		player.getInterfaceManager().closeInterfaces();
	}

	private void openCutPlanks(Player player) {
		player.getInterfaceManager().sendInterface(902);
	}

	private void inspectCart(Player player) {
		player.getInterfaceManager().sendInterface(903);
		for (int i = 0; i < 6; i++)
			refreshCart(player, i);
	}

	private void refreshCart(Player player, int i) {
		Job job = getJob(player);
		String colour;
		if (job.planks[i] < job.jobDetails.planks[i])
			colour = "<col=FF0000>";
		else if (job.planks[i] > job.jobDetails.planks[i])
			colour = "<col=00FF00>";
		else
			colour = "<col=FFFF00>";
		player.getPackets().sendIComponentText(903, INVESTIGATE_COMPONENT_IDS[i], colour + "x" + job.planks[i]);
	}

	public void finishJob(Player player) {
		Job job = getJob(player);
		if (job == null)
			return;
		player.getSkills().addXp(Skills.WOODCUTTING, job.jobDetails == Jobs.QUICK ? 4395 / 3 : 8580 / 3);
		setJob(player, null);
		player.getInterfaceManager().closeInterfaces();
		player.getInterfaceManager().removeOverlay(false);
//        player.addToInventoryOrDrop(player, new Item(995, job.jobDetails == Jobs.QUICK ? 75_000 : 150_000));
		player.getDetails().getStatistics().addStatistic("Sawmill_Jobs_Completed");
	}

	public boolean isOrderCompleted(Player player) {
		Job job = getJob(player);
		if (job == null)
			return false;
		for (int i = 0; i < job.planks.length; i++) {
			if (job.planks[i] < job.jobDetails.planks[i])
				return false;
		}
		return true;

	}

	public void withdrawFromCart(Player player, int type, int quantity) {
		Job job = getJob(player);
		if (quantity > job.planks[type]) {
			quantity = job.planks[type];
			player.getPackets().sendGameMessage("You have no planks to withdraw.");
		}
		job.planks[type] -= quantity;
		player.getVarsManager().sendVarBit(type == 5 ? 5008 : 4216 + type, job.planks[type]);
		player.getInventory().addItem(960, quantity);
		refreshCart(player, type);
	}

	private void openSawmillBoard(Player player) {
		player.getInterfaceManager().sendInterface(901);
		Job job = getJob(player);
		if (job != null) {
			player.getPackets().sendHideIComponent(901, 25, true);
			player.getPackets().sendHideIComponent(901, 26, true);

			player.getPackets().sendHideIComponent(901, 34, true);
			player.getPackets().sendHideIComponent(901, 35, true);

			player.getPackets().sendHideIComponent(901, job.jobDetails == Jobs.QUICK ? 22 : 20, true);

			player.getPackets().sendIComponentText(901, job.jobDetails == Jobs.QUICK ? 33 : 24,
					job.planks[0] + "/" + job.jobDetails.planks[0] + " Short plank" + "<br>" + job.planks[1] + "/"
							+ job.jobDetails.planks[1] + " Long plank" + "<br>" + job.planks[2] + "/"
							+ job.jobDetails.planks[2] + " Diagonal plank" + "<br>" + job.planks[3] + "/"
							+ job.jobDetails.planks[3] + " Tooth plank" + "<br>" + job.planks[4] + "/"
							+ job.jobDetails.planks[4] + " Groove plank" + "<br>" + job.planks[5] + "/"
							+ job.jobDetails.planks[5] + " Curved plank");
		}

	}

	public void takeJob(Player player, boolean quick) {
		if (getJob(player) != null) {
			player.getPackets().sendGameMessage("You already have a job!");
			return;
		}
		this.setJob(player, new Job(quick ? Jobs.QUICK : Jobs.LARGE));
		refreshVars(player);
		sendInterfaces(player);
	}

	public void addPlank(Player player, int type, int amount) {
		Job job = getJob(player);
		if (job == null)
			return;
		job.planks[type] += amount;
		player.getVarsManager().sendVarBit(type == 5 ? 5008 : 4216 + type, job.planks[type]);
	}

	private void refreshVars(Player player) {
		Job job = getJob(player);
		if (job == null)
			return;
		player.getVarsManager().sendVarBit(5007, job.jobDetails == Jobs.QUICK ? 0 : 1);
		for (int i = 0; i < job.jobDetails.planks.length; i++)
			player.getVarsManager().sendVarBit(4208 + i, ((job.jobDetails.planks[i] - 5) / 5));
		for (int i = 0; i < job.planks.length; i++)
			player.getVarsManager().sendVarBit(i == 5 ? 5008 : 4216 + i, job.planks[i]);
	}

	public void cutPlank(Player player, int type, int amount) {
		player.getInterfaceManager().closeInterfaces();
		player.getAction().setAction(new CutPlank(type, amount, this));
	}

	private void setJob(Player player, Job job) {
		setArguments(player, job == null ? null : new Object[] { job });
	}

	private Job getJob(Player player) {
		if (this.getArguments(player) == null || this.getArguments(player).length == 0)
			return null;
		return (Job) this.getArguments(player)[0];
	}

	private static final GameObject CONVEYOR_BELT = new GameObject(46298, 10, 0, 3324, 3496, 0);
	private static long lastLogAnimation;
	public static int[] INVESTIGATE_COMPONENT_IDS = { 75, 67, 57, 49, 41, 33 };
	public static int[] logs = { 1511, 1521, 6333, 6332 };
	public static int[] plank = { 960, 8778, 8780, 8782 };
	public static int[] prices = { 100, 250, 500, 1500 };
	private transient int planks;

	public static int[] getPlankForLog(int item) {
		for (int i = 0; i < logs.length; i++) {
			if (item == logs[i])
				return new int[] { plank[i], prices[i] };
		}
		return null;
	}

	private class Job {

		private Jobs jobDetails;
		private int[] planks;

		private Job(Jobs jobDetails) {
			this.jobDetails = jobDetails;
			planks = new int[jobDetails.planks.length];
		}
	}

	private enum Jobs {
		QUICK(15, 15, 20, 5, 10, 5), LARGE(30, 45, 20, 30, 30, 15);

		private int[] planks;

		private Jobs(int shortPlank, int longPlank, int diagonalPlank, int toothPlank, int groovePlank,
				int curvedPlank) {
			planks = new int[] { shortPlank, longPlank, diagonalPlank, toothPlank, groovePlank, curvedPlank };
		}
	}
}