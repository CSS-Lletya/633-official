package com.rs.plugin.impl.interfaces;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.RandomUtility;

@RSInterfaceSignature(interfaceId = { 429 })
public class NPCContactInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		NPCContact contact = NPCContact.forId(componentId);
		if (contact == NPCContact.RANDOM) {
			contact = NPCContact.values()[RandomUtility.random(NPCContact.values().length)];
		}
		if (contact == null) {
			return;
		}
		player.setNextAnimation(Animations.NPC_CONTACT);
		player.setNextGraphics(Graphic.NPC_CONTACT);
		player.getAudioManager().sendSound(Sounds.NPC_CONTACT_SPELL);
		player.getInterfaceManager().closeInterfaces();
		player.getPackets().sendGameMessage("This npc is unable to be contacted at this moment.");
	}

	/**
	 * Represents the npc's to contact.
	 * @author 'Vexia
	 */
	public enum NPCContact {
		HONEST_JIMM(38, 4362),
		BERT_THE_SANDMAN(39, 3108),
		ADVISOR_GHRIM(40, 1375),
		TURAEL(41, 70),
		LANTHUS(42, 1526),
		MAZCHNA(43, 1596),
		DURADEL(44, 1599),
		VANNAKA(45, 1597),
		MURPHY(46, 466),
		CHAELDAR(47, 1598),
		CYRISUS(48, 5893),
		LARRY(49, 5424),
		RANDOM(51, -1);

		/**
		 * Constructs a new {@code NPContactSpell} {@code Object}.
		 * @param button the button.
		 * @param npc the npc.
		 */
		NPCContact(int button, int npc) {
			this.button = button;
			this.npc = npc;
		}

		/**
		 * Represents the button of the npc to contact.
		 */
		private int button;

		/**
		 * Represents the npc to contact.
		 */
		private int npc;

		/**
		 * Gets the button.
		 * @return The button.
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Sets the button.
		 * @param button The button to set.
		 */
		public void setButton(int button) {
			this.button = button;
		}

		/**
		 * Gets the npc.
		 * @return The npc.
		 */
		public int getNpc() {
			return npc;
		}

		/**
		 * Sets the npc.
		 * @param npc The npc to set.
		 */
		public void setNpc(int npc) {
			this.npc = npc;
		}

		/**
		 * Gets the npc.
		 * @param id the id. 
		 * @return the npc contact.
		 */
		public static NPCContact forId(int id) {
			for (NPCContact npc : NPCContact.values()) {
				if (npc.getButton() == id) {
					return npc;
				}
			}
			return null;
		}
	}
}