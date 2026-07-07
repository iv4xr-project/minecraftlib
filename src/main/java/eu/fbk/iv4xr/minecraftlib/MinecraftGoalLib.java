package eu.fbk.iv4xr.minecraftlib;

import nl.uu.cs.aplib.mainConcepts.GoalStructure;

import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static eu.iv4xr.framework.Iv4xrEDSL.testgoal;
import static nl.uu.cs.aplib.AplibEDSL.goal;

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
	 * Select an item
	 * 
	 * @param tag
	 * @param face
	 * @return
	 */
	public GoalStructure placed(String tag, String face) {
		return goal("Placed on " + tag + "/" + face).toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.place(tag, face)).lift();
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
	 * Click
	 * 
	 * @param tag
	 * @return
	 */
	public GoalStructure clicked(String tag) {
		return goal("Clicked " + tag).toSolve((Boolean ok) -> ok != null && ok).withTactic(tacticLib.click(tag)).lift();
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
		return goal("Anvil op at " + tag).toSolve((Boolean ok) -> ok != null && ok)
				.withTactic(tacticLib.anvil(tag, itemOne, itemTwo, customName)).lift();
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
	public GoalStructure assertBlockIs(TestAgent ta, String tag, String expected, String nbt) {
		String info = "block@" + tag + "==" + expected;
		return testgoal("Assert " + info, ta).toSolve((Boolean checked) -> true)
				.oracle(ta, (Boolean checked) -> assertTrue_("block-check", info, checked != null && checked))
				.withTactic(tacticLib.checkBlock(tag, expected, nbt)).lift();
	}

	/**
	 * Assert the block at given position is the expected one
	 * @param ta
	 * @param pos
	 * @param expected
	 * @param nbt
	 * @return
	 */
	public GoalStructure assertBlockIs(TestAgent ta, Vec3 pos, String expected, String nbt) {
		String info = "block@" + pos + "==" + expected;
		return testgoal("Assert " + info, ta).toSolve((Boolean checked) -> true)
				.oracle(ta, (Boolean checked) -> assertTrue_("block-check", info, checked != null && checked))
				.withTactic(tacticLib.checkBlock(pos, expected, nbt)).lift();
	}
	
	/**
	 * Assert the inventory contains the correct number of a given item 
	 * @param ta
	 * @param item
	 * @param count
	 * @return
	 */
    public GoalStructure assertHasItem(TestAgent ta, String item, Integer count) {
        String info = "inventory has " + (count == null ? 1 : count) + "x " + item;
        return testgoal("Assert " + info, ta)
                .toSolve((Boolean checked) -> true)
                .oracle(ta, (Boolean checked) -> assertTrue_("inventory-check", info, checked != null && checked))
                .withTactic(tacticLib.checkInventory(item, count))
                .lift();
    }
    
    /**
     * Assert the health is as expected
     * @param ta
     * @param target
     * @param health
     * @return
     */
    public GoalStructure assertEntityHealth(TestAgent ta, String target, Float health) {
        String info = "entity " + target + (health == null ? " present" : " health==" + health);
        return testgoal("Assert " + info, ta)
                .toSolve((Boolean checked) -> true)
                .oracle(ta, (Boolean checked) -> assertTrue_("entity-check", info, checked != null && checked))
                .withTactic(tacticLib.checkEntity(target, health))
                .lift();
    }

}
