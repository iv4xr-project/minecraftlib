package eu.fbk.iv4xr.minecraftlib;

import static nl.uu.cs.aplib.AplibEDSL.action;

import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Tactic;

/**
 * Tactics for interacting with Minecraft
 * 
 * @author Davide Prandi
 */
public class MinecraftTacticLib {

	/**
	 * Get the agent from the Minecraft State
	 * 
	 * @param S
	 * @return
	 */
	private static String getAgentId(MinecraftState S) {
		return S.worldmodel().agentId;
	}

	/**
	 * Refresh the world model
	 * 
	 * @param S
	 */
	private static void refreshWorldModel(MinecraftState S) {
		S.worldmodel = S.env().observe(getAgentId(S));
	}

	/**
	 * Move to a tagged object
	 * 
	 * @param tag
	 * @return
	 */
	public Tactic moveTo(String tag) {
		return moveTo(tag, null);
	}

	/**
	 * Move to a tagged object within distance
	 * 
	 * @param tag
	 * @param distance
	 * @return
	 */
	public Tactic moveTo(String tag, Double distance) {
		return action("move_to " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().moveTo(getAgentId(S), tag, distance);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Move to explicit coordinates
	 * 
	 * @param pos
	 * @param distance
	 * @return
	 */
	public Tactic moveTo(Vec3 pos, Double distance) {
		return action("move_to " + pos).do1((MinecraftState S) -> {
			boolean ok = S.env().moveTo(getAgentId(S), pos, distance);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Mine a tagged object
	 * 
	 * @param tag
	 * @return
	 */
	public Tactic mine(String tag) {
		return action("break " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().mine(getAgentId(S), tag);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Select an object in the main hand
	 * 
	 * @param item
	 * @return
	 */
	public Tactic select(String item) {
		return action("select " + item).do1((MinecraftState S) -> {
			boolean ok = S.env().selectItem(getAgentId(S), item);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Place on a tagged object
	 * 
	 * @param tag
	 * @param face
	 * @return
	 */
	public Tactic placeOn(String tag, String face) {
		return action("place_on " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().placeOn(getAgentId(S), tag, face);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}
	
	/**
	 * Place a tagged object
	 * 
	 * @param tag
	 * @return
	 */
	public Tactic rawPlace(String tag) {
		return action("place " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().rawPlace(getAgentId(S), tag);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Attack target entity
	 * 
	 * @param target
	 * @return
	 */
	public Tactic attack(String target) {
		return action("attack " + target).do1((MinecraftState S) -> {
			boolean ok = S.env().attack(getAgentId(S), target);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Activate object with tag
	 * 
	 * @param tag
	 * @return
	 */
	public Tactic click(String tag) {
		return action("click " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().click(getAgentId(S), tag);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}
	
	/**
	 * Use an item on a tagged entity
	 * 
	 * @param tag
	 * @return
	 */
	public Tactic useOnEntity(String tag) {
		return action("use_on_entity " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().useOnEntity(getAgentId(S), tag);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Pick up the loot
	 * 
	 * @param tag
	 * @return
	 */
	public Tactic pickUpLoot() {
		return action("pick_up_loot ").do1((MinecraftState S) -> {
			boolean ok = S.env().pickUpLoot(getAgentId(S));
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Toggle sneak (on/off)
	 * 
	 * @param state
	 * @return
	 */
	public Tactic sneak(boolean state) {
		return action("sneak " + state).do1((MinecraftState S) -> {
			boolean ok = S.env().sneak(getAgentId(S), state);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}
	
	/**
	 * jump
	 * 
	 * @return
	 */
	public Tactic jump() {
		return action("jump").do1((MinecraftState S) -> {
			boolean ok = S.env().jump(getAgentId(S));
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Wait
	 * 
	 * @param ticks
	 * @return
	 */
	public Tactic waitTicks(int ticks) {
		return action("wait " + ticks).do1((MinecraftState S) -> {
			boolean ok = S.env().waitTicks(getAgentId(S), ticks);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	/**
	 * Use the anvil
	 * 
	 * @param tag
	 * @param itemOne
	 * @param itemTwo
	 * @param customName
	 * @return
	 */
	public Tactic anvil(String tag, String itemOne, String itemTwo, String customName) {
		return action("anvil " + tag).do1((MinecraftState S) -> {
			boolean ok = S.env().anvil(getAgentId(S), tag, itemOne, itemTwo, customName);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}
	
	/**
	 * Craft an item with a crafting table
	 * 
	 * @param tag (crafting table location if applicable)
	 * @param item (to craft)
	 * @param count
	 * @return
	 */
	public Tactic craft(String tag, String item, Integer count) {
		return action("craft " + item).do1((MinecraftState S) -> {
			boolean ok = S.env().craft(getAgentId(S), tag, item, count);
			refreshWorldModel(S);
			return (Object) ok;
		}).lift();
	}

	////////////////////
	///
	/// Checks
	///
	///////////////////

	/**
	 * Check the type of a block specified by a tag
	 * 
	 * @param tag
	 * @param expected
	 * @param nbt
	 * @return
	 */
	public Tactic checkBlock(String tag, String expected, String nbt) {
		return action("check_block " + tag)
				.do1((MinecraftState S) -> (Object) S.env().checkBlock(getAgentId(S), tag, expected, nbt)).lift();
	}

	/**
	 * Check the type o a block a given position
	 * 
	 * @param pos
	 * @param expected
	 * @param nbt
	 * @return
	 */
	public Tactic checkBlock(Vec3 pos, String expected, String nbt) {
		return action("check_block " + pos)
				.do1((MinecraftState S) -> (Object) S.env().checkBlock(getAgentId(S), pos, expected, nbt)).lift();
	}

	/**
	 * Check if the correct amount of an item is present in the inventory
	 * 
	 * @param item
	 * @param count
	 * @return
	 */
	public Tactic checkInventory(String item, Integer count) {
		return action("check_inventory " + item)
				.do1((MinecraftState S) -> (Object) S.env().checkInventory(getAgentId(S), item, count)).lift();
	}

	/**
	 * Check the health of a target entity
	 * 
	 * @param target
	 * @param health
	 * @return
	 */
	public Tactic checkEntity(String target, Float health) {
		return action("check_entity " + target)
				.do1((MinecraftState S) -> (Object) S.env().checkEntity(getAgentId(S), target, health)).lift();
	}
}
