package com.rs.plugin.impl.objects.region;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.Skills;

@ObjectSignature(objectId = {52316, 52317, 52296, 52299}, name = {})
public class ThievingGuildRegionObjectPlugin extends ObjectListener{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		int id = object.getId();
        if (player.getSkills().getLevel(Skills.THIEVING) >= 30) {
            player.getPackets().sendGameMessage("Perhaps you've learned enough now, time to try something else.");
            return;
        }
        if (optionId == 1) {
            switch (id) {
                case 52317:
                    player.getMovement().lock(2);
                    player.setNextAnimation(new Animation(881));
                    player.getSkills().addExperience(Skills.THIEVING, 3);
                    break;
                case 52316:
                    player.getPackets().sendGameMessage(
                            "Surves no purpose attacking this dummy without any real reason behind it.");
                    break;
                    //Optionall could've gone with wiki, but choose to simplify it
                    //Link: https://runescape.wiki/w/Thieves%27_Guild#Practice_Chest_Area
                case 52296://red
                    player.getMovement().lock(2);
                    player.setNextAnimation(Animations.SIMPLE_GRAB);
                    player.getSkills().addExperience(Skills.THIEVING, 3);
                    break;
                case 52299://blue
                	player.getMovement().lock(2);
                    player.setNextAnimation(Animations.SIMPLE_GRAB);
                    player.getSkills().addExperience(Skills.THIEVING, 3);
                	break;
            }
        } else if (optionId == 3) {
            switch (id) {
                case 52316:
                    player.getMovement().lock(2);
                    player.setNextAnimation(Animations.PICKPOCKET);
                    player.getSkills().addExperience(Skills.THIEVING, 0.4);
                    break;
            }
        }
	}
}