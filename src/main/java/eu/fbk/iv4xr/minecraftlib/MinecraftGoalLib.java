package eu.fbk.iv4xr.minecraftlib;

import nl.uu.cs.aplib.mainConcepts.GoalStructure;

import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static eu.iv4xr.framework.Iv4xrEDSL.testgoal;
import static nl.uu.cs.aplib.AplibEDSL.goal;

import java.util.Map;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.spatial.Vec3;

/**
 * Library of goals for an agent in Minecraft
 * 
 * @author Davide Prandi
 */
public class MinecraftGoalLib {

	public final MinecraftTacticLib tacticLib = new MinecraftTacticLib();

	/**
	 * Move to a tag
	 * 
	 * @param tag
	 * @return
	 */
	public GoalStructure tagReached(String tag) {
		return goal("Reached " + tag).toSolve((Boolean arrived) -> arrived != null && arrived)
				.withTactic(tacticLib.moveTo(tag)).lift();
	}

	/**
	 * Move to a tag within a certain distance
	 * 
	 * @param tag
	 * @param distance
	 * @return
	 */
	public GoalStructure tagReachedWithinDistance(String tag, double distance) {
		return goal("Reached " + tag + " (<=" + distance + ")").toSolve((Boolean arrived) -> arrived != null && arrived)
				.withTactic(tacticLib.moveTo(tag, distance)).lift();
	}

	/**
	 * Move to position within distance
	 * 
	 * @param pos
	 * @param distance
	 * @return
	 */
	public GoalStructure reached(Vec3 pos, double distance) {
		return goal("Reached " + pos + " (<=" + distance + ")").toSolve((Boolean arrived) -> arrived != null && arrived)
				.withTactic(tacticLib.moveTo(pos, distance)).lift();
	}

	/**
	 * Mine tag
	 * 
	 * @param tag
	 * @return
	 */
	public GoalStructure mined(String tag) {
		return goal("Mined " + tag).toSolve((Boolean broke) -> broke != null && broke).withTactic(tacticLib.mine(tag))
				.lift();
	}

	/**
	 * Select an item
	 * 
	 * @param item
	 * @return
	 */
	public GoalStructure selected(String item) {
		return goal("Selected " + item).toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.select(item))
				.lift();
	}

	/**
	 * Place a block on a tagged block
	 * 
	 * @param tag
	 * @param face
	 * @return
	 */
	public GoalStructure placedOn(String tag, String face) {
		return goal("Placed on " + tag + "/" + face).toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.placeOn(tag, face)).lift();
	}
	
	/**
	 * Place a block at a tagged position
	 * 
	 * @param tag
	 * @return
	 */
	public GoalStructure placed(String tag) {
		return goal("Placed " + tag).toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.rawPlace(tag)).lift();
	}

	/**
	 * Attack
	 * 
	 * @param target
	 * @return
	 */
	public GoalStructure attacked(String target) {
		return goal("Attacked " + target).toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.attack(target))
				.lift();
	}
	
	/**
	 * Sneak
	 * 
	 * @param state
	 * @return
	 */
	public GoalStructure sneaked(boolean state) {
		return goal("Sneaked " + state).toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.sneak(state))
				.lift();
	}
	
	/**
	 * Jump
	 * 
	 * @return
	 */
	public GoalStructure jumped() {
		return goal("Jumped").toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.jump())
				.lift();
	}

	/**
	 * Click
	 * 
	 * @param tag
	 * @return
	 */
	public GoalStructure clicked(String tag) {
		return goal("Clicked " + tag).toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.click(tag)).lift();
	}
	
	/**
	 * UseOnEntity
	 * 
	 * @param tag
	 * @return
	 */
	public GoalStructure usedOnEntity(String tag) {
		return goal("Used on entity " + tag).toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.useOnEntity(tag)).lift();
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
	public GoalStructure usedAnvil(String tag, String itemOne, String itemTwo, String customName) {
		return goal("Anvil op @ " + tag).toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.anvil(tag, itemOne, itemTwo, customName)).lift();
	}
	
	/**
	 * Craft an item with a crafting table
	 * 
	 * @param tag (crafting table location if applicable)
	 * @param item (to craft)
	 * @param count
	 * @return
	 */
	public GoalStructure crafted(String tag, String item, Integer count) {
		return goal("Craft " + item + " @ " + tag).toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.craft(tag, item, count)).lift();
	}

	/**
	 * Wait some ticks
	 * 
	 * @param ticks
	 * @return
	 */
	public GoalStructure waited(int ticks) {
		return goal("Waited " + ticks + " ticks").toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.waitTicks(ticks)).lift();
	}

	/**
	 * Assert that the the tagged block is the expected one
	 * 
	 * @param ta
	 * @param tag
	 * @param expected
	 * @param nbt
	 * @return
	 */
	public GoalStructure assertBlockIs(TestAgent ta, String tag, String expected, String nbt, Boolean result) {
		String info = "block@" + tag + "==" + expected;
		return testgoal("Assert " + info, ta).toSolve((Boolean checked) -> true)
				.oracle(ta, (Boolean checked) -> assertTrue_("block-check", info, checked != null && checked))
				.withTactic(tacticLib.checkBlock(tag, expected, nbt, result)).lift();
	}

	/**
	 * Assert the block at given position is the expected one
	 * @param ta
	 * @param pos
	 * @param expected
	 * @param nbt
	 * @return
	 */
	public GoalStructure assertBlockIs(TestAgent ta, Vec3 pos, String expected, String nbt, Boolean result) {
		String info = "block@" + pos + "==" + expected;
		return testgoal("Assert " + info, ta).toSolve((Boolean checked) -> true)
				.oracle(ta, (Boolean checked) -> assertTrue_("block-check", info, checked != null && checked))
				.withTactic(tacticLib.checkBlock(pos, expected, nbt, result)).lift();
	}
	
	/**
	 * Assert the inventory contains the correct number of a given item 
	 * @param ta
	 * @param item
	 * @param count
	 * @return
	 */
    public GoalStructure assertHasItem(TestAgent ta, String item, Integer count, Boolean result) {
        String info = "inventory has " + (count == null ? 1 : count) + "x " + item;
        return testgoal("Assert " + info, ta)
                .toSolve((Boolean checked) -> true)
                .oracle(ta, (Boolean checked) -> assertTrue_("inventory-check", info, checked != null && checked))
                .withTactic(tacticLib.checkInventory(item, count, result))
                .lift();
    }
    
	/**
	 * Assert the inventory contains an item with the exact components 
	 * @param ta
	 * @param components
	 * @return
	 */
    public GoalStructure assertItemComponents(TestAgent ta, String item, Map<String, Object> components, Boolean result) {
        String info = "inventory has " + item + " with components " + components;
        return testgoal("Assert " + info, ta)
                .toSolve((Boolean checked) -> true)
                .oracle(ta, (Boolean checked) -> assertTrue_("inventory-check", info, checked != null && checked))
                .withTactic(tacticLib.checkItemComponents(item, components, result))
                .lift();
    }
    
    /**
     * Assert the health is as expected
     * @param ta
     * @param target
     * @param health
     * @return
     */
    public GoalStructure assertEntity(TestAgent ta, String target, Float health, String nbt, Boolean result) {
        String info = "entity " + target + (health == null ? " present" : " health==" + health);
        return testgoal("Assert " + info, ta)
                .toSolve((Boolean checked) -> true)
                .oracle(ta, (Boolean checked) -> assertTrue_("entity-check", info, checked != null && checked))
                .withTactic(tacticLib.checkEntity(target, health, nbt, result))
                .lift();
    }
    
    
    /**
     * Assert the bot has the advancement
     * @param advancement
     * @return
     */
    public GoalStructure assertAdvancement(TestAgent ta, String advancement, Boolean result) {
        String info = result == null || !result ? "has" : "does not have" + " advancement " + advancement;
        return testgoal("Assert " + info, ta)
                .toSolve((Boolean checked) -> true)
                .oracle(ta, (Boolean checked) -> assertTrue_("advancement-check", info, checked != null && checked))
                .withTactic(tacticLib.checkAdvancment(advancement, result))
                .lift();
    }

}
