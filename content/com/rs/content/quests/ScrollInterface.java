package com.rs.content.quests;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.player.Player;

/**
 * old Quest interface. junk code dont mind.
 *
 * @author Dennis
 */
public class ScrollInterface {
	
    /**
     * Sends the quest interface to the player with the parameterized title and
     * list of messages. The messages will be formatted to never overlap one
     * line, but to go to the next one if it passes the limit of characters on a
     * line.
     *
     * @param player      The player
     * @param title       The title of the quest interface
     * @param messageList The list of messages to send. a {@code String} {@code Array}
     *                    {@code Object}
     */
    public static void sendQuestScroll(Player player, String title, String... messageList) {
        player.getInterfaceManager().closeInterfaces();

        final List<String> messages = new ArrayList<String>();
        final int interfaceId = 275;
        final int endLine = 315;
        final int maxLength = 70;
        final int lineCount = 315;

        int startLine = 16;

        for (String message : messageList) {
            char[] unformatted = message.toCharArray();
            String[] newMessage = new String[(int) Math.ceil((double) unformatted.length / maxLength) + 1];
            for (int j = 0; j < unformatted.length; j++) {
                int index = j == 0 ? 1 : (int) Math.ceil((double) j / maxLength);
                char character = unformatted[j];
                newMessage[index] += character;
            }
            for (String m : newMessage) {
                if (m == null)
                    continue;
                messages.add(m.replaceAll("null", ""));
            }
        }
        for (int k = 0; k < lineCount; k++) {
            player.getPackets().sendIComponentText(interfaceId, k, "");
        }

        for (String message : messages) {
            if (startLine > endLine)
                break;
            player.getPackets().sendIComponentText(interfaceId, startLine, message);
            startLine++;
        }


//        player.getPackets().sendHideIComponent(interfaceId, 14, true);
        player.getPackets().sendRunScript(1207, new Object[]{messages.size()});
        player.getPackets().sendIComponentText(interfaceId, 2, title);
        player.getInterfaceManager().sendFullscreenInterface(0, interfaceId);
    }
}
