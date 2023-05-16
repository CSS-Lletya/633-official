package com.rs.game.player;

import com.rs.game.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * TODO: Combat rework to eventually remove old hitlooks, atm just archival.
 * @author Dennis
 *
 */
@Data
public final class Hit {

	//new hitmarkers will be updated when a combat system is reworked.
	@AllArgsConstructor
	public enum HitLook {

		MISSED(1), 
		REGULAR_DAMAGE(2), MELEE_DAMAGE(2), RANGE_DAMAGE(2), MAGIC_DAMAGE(2), REFLECTED_DAMAGE(2), ABSORB_DAMAGE(2), 
		LARGE_DAMAGE(3),
		POISON_DAMAGE(4), DESEASE_DAMAGE(4);

//		MISSED(1), 
//		REGULAR_DAMAGE(2), 
//		LARGE_DAMAGE(3), 
//		POISON_DAMAGE(4),
//		LARGE_POISON_DAMAGE(5),
//		POISON_LIKE_DAMAGE(6), //dung maybe?
//		LARGE_POISON_LIKE_DAMAGE(7), //dung maybe?
//		NPC_MISSED(8), //maybe this is for npcs? its similar to damage though. 
//		LARGE_NPC_MISSED(9), //maybe this is for npcs? its similar to damage though. 
//		HEAL_DUNG(10)//gotta be healed dung (cross shaped) 11 is same thing/size
		@Getter
		private int mark;
	}

	private Entity source;
	private HitLook look;
	private int damage;
	private boolean critical;
	private Hit soaking;
	private int delay;
	
	public Hit(Entity source, int damage, HitLook look) {
		this(source, damage, look, 0);
	}

	public Hit(Entity source, int damage, HitLook look, int delay) {
		this.source = source;
		this.damage = damage;
		this.look = look;
		this.delay = delay;
	}

	public boolean missed() {
		return damage == 0;
	}

	public int getMark(Player player, Entity victm) {
		if (missed())
			return HitLook.MISSED.getMark();
		if (getDamage() > 99)
			return HitLook.LARGE_DAMAGE.getMark();
		return HitLook.REGULAR_DAMAGE.getMark();
	}
}