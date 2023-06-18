package com.rs.game.player.content;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.player.FriendsIgnores;
import com.rs.game.player.Player;
import com.rs.io.OutputStream;
import com.rs.net.AccountCreation;
import com.rs.net.encoders.other.ChatMessage;
import com.rs.net.encoders.other.QuickChatMessage;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class FriendChatsManager {

	private String owner;
	private String ownerDisplayName;
	private FriendsIgnores settings;
	private CopyOnWriteArrayList<Player> players;
	private Object2ObjectOpenHashMap<String, Long> bannedPlayers;
	private byte[] dataBlock;

	private static Object2ObjectOpenHashMap<String, FriendChatsManager> cachedFriendChats;

	public static void init() {
		cachedFriendChats = new Object2ObjectOpenHashMap<String, FriendChatsManager>();
	}

	public int getRank(int rights, String username) {
		if (rights == 2)
			return 127;
		if (username.equals(owner))
			return 7;
		return settings.getRank(username);
	}

	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}

	public int getWhoCanKickOnChat() {
		return settings.getWhoCanKickOnChat();
	}

	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}

	public String getOwnerName() {
		return owner;
	}

	public String getChannelName() {
		return settings.getChatName().replaceAll("<img=", "");
	}

	private void joinChat(Player player) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.hasRankToJoin(player.getUsername())
					&& !player.getDetails().getRights().isStaff()) {
				player.getPackets().sendGameMessage("You do not have a enough rank to join this clan chat channel.");
				return;
			}
			if (players.size() >= 100) {
				player.getPackets().sendGameMessage("This chat is full.");
				return;
			}
			Long bannedSince = bannedPlayers.get(player.getUsername());
			if (bannedSince != null) {
				if (bannedSince + 3600000 > Utility.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You have been banned from this channel.");
					return;
				}
				bannedPlayers.remove(player.getUsername());
			}
			joinChatNoCheck(player);
		}
	}

	public void leaveChat(Player player, boolean logout) {
		synchronized (this) {
			player.setCurrentFriendChat(null);
			players.remove(player);
			if (players.size() == 0) { // no1 at chat so uncache it
				synchronized (cachedFriendChats) {
					cachedFriendChats.remove(owner);
				}
			} else
				refreshChannel();
			if (!logout) {
				player.getDetails().setCurrentFriendChatOwner(null);
				player.getInterfaceManager().closeInterfaces();
				player.getPackets().sendGameMessage("You have left the channel.");
				player.getPackets().sendFriendsChatChannel();
				player.getDetails().setToogleLootShare(false);
				player.getVarsManager().forceSendVarBit(4071, 0);
			}
			getLocalMembers().remove(player);
		}
	}

	public Player getPlayerByDisplayName(String username) {
		String formatedUsername = Utility.formatPlayerNameForProtocol(username);
		for (Player player : players) {
			if (player.getUsername().equals(formatedUsername) || player.getDisplayName().equals(username))
				return player;
		}
		return null;
	}

	public void kickPlayerFromChat(Player player, String username) {
		String name = "";
		for (char character : username.toCharArray()) {
			name += Utility.containsInvalidCharacter(character) ? " " : character;
		}
		synchronized (this) {
			int rank = getRank(player.getDetails().getRights().getValue(), player.getUsername());
			if (rank < getWhoCanKickOnChat())
				return;
			Player kicked = getPlayerByDisplayName(name);
			if (kicked == null) {
				player.getPackets().sendGameMessage("This player is not this channel.");
				return;
			}
			if (rank <= getRank(kicked.getDetails().getRights().getValue(), kicked.getUsername()))
				return;
			kicked.setCurrentFriendChat(null);
			kicked.getDetails().setCurrentFriendChatOwner(null);
			players.remove(kicked);
			bannedPlayers.put(kicked.getUsername(), Utility.currentTimeMillis());
			kicked.getPackets().sendFriendsChatChannel();
			kicked.getPackets().sendGameMessage("You have been kicked from the clan chat channel.");
			player.getPackets()
					.sendGameMessage("You have kicked " + kicked.getUsername() + " from clan chat channel.");
			refreshChannel();

		}
	}

	private void joinChatNoCheck(Player player) {
		synchronized (this) {
			players.add(player);
			player.setCurrentFriendChat(this);
			player.getDetails().setCurrentFriendChatOwner(owner);
			player.getPackets()
					.sendGameMessage("You are now talking in the clan chat channel " + settings.getChatName());
			refreshChannel();
			getLocalMembers().add(player);
		}
	}

	public void destroyChat() {
		synchronized (this) {
			for (Player player : players) {
				player.setCurrentFriendChat(null);
				player.getDetails().setCurrentFriendChatOwner(null);
				player.getPackets().sendFriendsChatChannel();
				player.getPackets().sendGameMessage("You have been removed from this channel!");
			}
		}
		synchronized (cachedFriendChats) {
			cachedFriendChats.remove(owner);
		}

	}

	public void sendQuickMessage(Player player, QuickChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player) && !player.getDetails().getRights().isStaff()) {
				player.getPackets()
						.sendGameMessage("You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			String formatedName = Utility.formatPlayerNameForDisplay(player.getUsername());
			String displayName = player.getDisplayName();
			int rights = ChatMessage.getMessageIcon(player);
			for (Player p2 : players)
				p2.getPackets().receiveFriendChatQuickMessage(formatedName, displayName, rights, settings.getChatName(),
						message);
		}
	}

	public void sendMessage(Player player, ChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player) && !player.getDetails().getRights().isStaff()) {
				player.getPackets()
						.sendGameMessage("You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			String formatedName = Utility.formatPlayerNameForDisplay(player.getUsername());
			String displayName = player.getDisplayName();
			int rights = ChatMessage.getMessageIcon(player);
			for (Player p2 : players)
				p2.getPackets().receiveFriendChatMessage(formatedName, displayName, rights, settings.getChatName(),
						message);
		}
	}

	public void sendDiceMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player) && !player.getDetails().getRights().isStaff()) {
				player.getPackets()
						.sendGameMessage("You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			for (Player p2 : players) {
				p2.getPackets().sendGameMessage(message);
			}
		}
	}

	private void refreshChannel() {
		synchronized (this) {
			OutputStream stream = new OutputStream();
			stream.writeString(ownerDisplayName);
			String ownerName = Utility.formatPlayerNameForDisplay(owner);
			stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
			if (!getOwnerDisplayName().equals(ownerName))
				stream.writeString(ownerName);
			stream.writeLong(Utility.stringToLong(getChannelName()));
			int kickOffset = stream.getOffset();
			stream.writeByte(0);
			stream.writeByte(getPlayers().size());
			for (Player player : getPlayers()) {
				String displayName = player.getDisplayName();
				String name = Utility.formatPlayerNameForDisplay(player.getUsername());
				stream.writeString(displayName);
				stream.writeByte(displayName.equals(name) ? 0 : 1);
				if (!displayName.equals(name))
					stream.writeString(name);
				stream.writeShort(1);
				int rank = getRank(player.getDetails().getRights().getValue(), player.getUsername());
				stream.writeByte(rank);
				stream.writeString(GameConstants.SERVER_NAME);
			}
			dataBlock = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.getBytes(dataBlock, 0, dataBlock.length);
			for (Player player : players) {
				dataBlock[kickOffset] = (byte) (player.getUsername().equals(owner) ? 0 : getWhoCanKickOnChat());
				player.getPackets().sendFriendsChatChannel();
			}
		}
	}

	public byte[] getDataBlock() {
		return dataBlock;
	}

	public FriendChatsManager(Player player) {
		owner = player.getUsername();
		ownerDisplayName = player.getDisplayName();
		settings = player.getFriendsIgnores();
		players = new CopyOnWriteArrayList<Player>();
		bannedPlayers = new Object2ObjectOpenHashMap<String, Long>();
		this.localMembers = new CopyOnWriteArrayList<Player>();
	}

	public static void destroyChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.destroyChat();
			player.getPackets().sendGameMessage("Your clan chat channel has now been disabled!");
		}
	}

	public static void linkSettings(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.settings = player.getFriendsIgnores();
		}
	}

	public static void refreshChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.refreshChannel();
		}
	}

    public static List<Player> getLootSharingPeople(Player player) {
        FriendChatsManager chat = player.getCurrentFriendChat();
        if (chat == null)
            return null;
        List<Player> players = new ArrayList<Player>();
        for (Player p2 : player.getCurrentFriendChat().getLocalMembers()) {
            if (p2.withinDistance(player, 12))
                players.add(p2);
        }
        return players;
    }
    
    private CopyOnWriteArrayList<Player> localMembers;
    
    public CopyOnWriteArrayList<Player> getLocalMembers() {
        return localMembers;
    }

    public static void toogleLootShare(Player player) {
        if (player.getCurrentFriendChat() == null) {
            player.getPackets().sendGameMessage("You need to be in a clan chat channel to activate LootShare.");
            player.refreshToogleLootShare();
            return;
        }
        if (!player.getUsername().equals(player.getCurrentFriendChat().getOwnerName())) {
            player.getPackets().sendGameMessage("Only the owner of the clan chat can toggle Lootshare.");
            player.refreshToogleLootShare();
            return;
        }
        player.getCurrentFriendChat().players.forEach(cm -> cm.toogleLootShare());
        player.getPackets().sendGameMessage("LootShare is now " + (player.getDetails().isToogleLootShare() ? "active." :"deactivated."));
    }
	public static void joinChat(String ownerName, Player player) {
		synchronized (cachedFriendChats) {
			if (player.getCurrentFriendChat() != null)
				return;
			player.getPackets().sendGameMessage("Attempting to join channel...");
			String formatedName = Utility.formatPlayerNameForProtocol(ownerName);
			FriendChatsManager chat = cachedFriendChats.get(formatedName);
			Player owner = World.getPlayerByDisplayName(ownerName);
			if (chat == null) {
				if (owner == null)
					owner = World.getLobbyPlayerByDisplayName(ownerName);
				if (owner == null) {
					if (!AccountCreation.exists(formatedName)) {
						player.getPackets().sendGameMessage("The channel you tried to join does not exist.");
						return;
					}
					owner = AccountCreation.loadPlayer(formatedName);
					if (owner == null) {
						player.getPackets().sendGameMessage("The channel you tried to join does not exist.");
						return;
					}
				}
				FriendsIgnores settings = owner.getFriendsIgnores();
				if (!settings.hasFriendChat()) {
					player.getPackets().sendGameMessage("The channel you tried to join does not exist.");
					return;
				}
				if (!player.getUsername().equals(ownerName) && !settings.hasRankToJoin(player.getUsername())
						&& !player.getDetails().getRights().isStaff()) {
					player.getPackets()
							.sendGameMessage("You do not have a enough rank to join this clan chat channel.");
					return;
				}
				
				chat = new FriendChatsManager(owner);
				cachedFriendChats.put(chat.owner, chat);
				chat.joinChatNoCheck(player);
				player.getCurrentFriendChat().getLocalMembers().add(player);
				player.getVarsManager().forceSendVarBit(4071, owner.getDetails().isToogleLootShare() ? 1 : 0);
			} else {
				chat.joinChat(player);
				if (owner.getDetails().isToogleLootShare()) {
					player.getDetails().setToogleLootShare(true);
					player.getVarsManager().forceSendVarBit(4071, 1);
				}
			}
		}

	}
	
	public void kickPlayerFromFriendsChannel(Player player, String name) {
		if (player.getCurrentFriendChat() == null)
			return;
		player.getCurrentFriendChat().kickPlayerFromChat(player, name);
	}

	public void sendFriendsChannelMessage(Player player,ChatMessage message) {
		if (player.getCurrentFriendChat() == null)
			return;
		player.getCurrentFriendChat().sendMessage(player, message);
	}

	public void sendFriendsChannelQuickMessage(Player player,QuickChatMessage message) {
		if (player.getCurrentFriendChat() == null)
			return;
		player.getCurrentFriendChat().sendQuickMessage(player, message);
	}
}
