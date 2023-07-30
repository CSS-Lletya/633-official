package com.rs;

import java.math.BigInteger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Rights;

public final class GameConstants {

	/**
	 * General client and server settings.
	 */
	public static final String SERVER_NAME = GameProperties.getGameProperties().getString("server_name");
	public static final int RECEIVE_DATA_LIMIT = GameProperties.getGameProperties().getInteger("receive_data_limit");
	public static final int PACKET_SIZE_LIMIT = GameProperties.getGameProperties().getInteger("packet_size_limit");
	public static final int CLIENT_REVISION = GameProperties.getGameProperties().getInteger("revision");
	public static final long CONNECTION_TIMEOUT = GameProperties.getGameProperties().getInteger("connection_timeout");
	public static final String SQL_FILE_PATH =  GameProperties.getGameProperties().getString("sql_location");
	public static final boolean SQL_ENABLED = GameProperties.getGameProperties().getBoolean("sql_state");
	
	/**
	 * Player settings
	 */
	public static final WorldTile START_PLAYER_LOCATION = new WorldTile(3222, 3222, 0);
	public static final int COMBAT_XP_RATE = GameProperties.getGameProperties().getInteger("combat_exp_rate");
	public static final int XP_RATE = GameProperties.getGameProperties().getInteger("exp_rate");
	public static final int LAMP_XP_RATE = GameProperties.getGameProperties().getInteger("lamp_exp_rate");
	public static final boolean XP_BONUS_ENABLED = GameProperties.getGameProperties().getBoolean("bonus_exp_enabled");
	public static final int TOTAL_QUEST_POINTS = GameProperties.getGameProperties().getInteger("total_quest_points");
	public static final String MISSING_CONTENT = GameProperties.getGameProperties().getString("missing_content_message");
	public static final short TOLERANCE_SECONDS = GameProperties.getGameProperties().getShort("tolerance");
	
	/**
	 * Launching settings
	 */
	public static boolean DEBUG = GameProperties.getGameProperties().getBoolean("debug_mode");
	public static boolean HOSTED = GameProperties.getGameProperties().getBoolean("is_live");
	public static boolean DAILY_CHARACTER_SAVING = GameProperties.getGameProperties().getBoolean("daily_character_saving");
	public static boolean NEW_YEARS_EVENT = GameProperties.getGameProperties().getBoolean("new_years_event");
	
	/**
	 * Items that are protected upon a Players death by default in the Wilderness
	 */
	public static final String[] PROTECT_ON_DEATH = { "chaotic", "stream", "defender",
			"fire cape", "farseer kiteshield", "eagle-eye kiteshield", "gravite" };
	
	/**
	 * A Players starter kit when joining the game for the first time.
	 */
	public final static ImmutableSet<Item> STATER_KIT = ImmutableSet.of(
			new Item(ItemNames.COINS_995, 10_000), new Item(ItemNames.BRONZE_SCIMITAR_1321), new Item(ItemNames.STAFF_OF_AIR_1381),
			new Item(ItemNames.SHORTBOW_841), new Item(ItemNames.BRONZE_ARROW_882, 250)
	);
	
	/**
	 * An immutable map of Staff members
	 */
	public static final ImmutableMap<String, Rights> STAFF = ImmutableMap.of(
			"Zed", Rights.ADMINISTRATOR,
			"Test", Rights.ADMINISTRATOR
	);

	/**
	 * Represents the world state of PVP
	 * @return
	 */
	public static boolean isPVPWorld() {
		return true;
	}
	
	/**
	 * World settings
	 */
	public static final long WORLD_CYCLE_NS = 600000000L;
	public static final long WORLD_CYCLE_MS = WORLD_CYCLE_NS / 1000000L;
	
	/**
	 * Memory settings
	 */
	public static final Short PLAYERS_LIMIT = 2000;
	public static final Short NPCS_LIMIT = Short.MAX_VALUE;
	public static final byte LOCAL_NPCS_LIMIT = 127;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000;

	/**
	 * Game constants
	 */
	public static final int[] MAP_SIZES = { 104, 120, 136, 168, 72 };

	public static final int[] GRAB_SERVER_KEYS = { 100, 79328, 55571, 46770,
			24563, 299978, 44375, 0, 4173, 2820, 99838, 617461, 155159, 282434,
			329958, 682317, 18859, 19013, 16183, 1244, 6250, 524, 119, 739155,
			813330, 3621, 2908 };

	// an exeption(grab server has his own keyset unlike rest of client)
	public static final BigInteger GRAB_SERVER_PRIVATE_EXPONENT = new BigInteger(
			"85841718464006470839454836619781897739687740809318231193831996660380025422889676223278733529619572421474466540424432365116201466262036779260116487579588025309092277884355330746244882937851596698304162660093117809460890167161229594796675127688779314631686136383237667641862930930283410062092886864440881014337");
	public static final BigInteger GRAB_SERVER_MODULUS = new BigInteger(
			"120684072056280935288427827946427111553241708199336899728637540010539851684827542274005027444025182722373693874630942678750225147898041539436465038752862996523582623683050478903900622900745629235369980114857562631625233381072331798032418279261790337275058300738584974228751698542644837745940989177562329966303");

	public static final BigInteger PRIVATE_EXPONENT = new BigInteger(
			"72097355254232856447691049913560861199871800553034733055741658055384003364250497219347734593899555489356266111078966532473452495715069155559968676764261742228476044257420193568926663071665046174428073288830191026953446568088808917389435275071415275652574533602125129106144025101414104744266670316697396691017");
	public static final BigInteger MODULUS = new BigInteger(
			"113936108878412835789161783853416560016782768509180808282272938775908500602418191899505497385652508213111369682953925143804409254941488925946653496879733372392766486773043079697715731681861313487249634133583720830388725199461616223203479888577328710259826756810447716575537141200187251284798307012938761310363");
}