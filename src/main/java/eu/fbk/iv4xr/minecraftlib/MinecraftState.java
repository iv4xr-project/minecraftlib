package eu.fbk.iv4xr.minecraftlib;

import java.util.HashMap;
import java.util.Map;

import eu.iv4xr.framework.extensions.pathfinding.Navigatable;
import eu.iv4xr.framework.mainConcepts.Iv4xrAgentState;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Environment;

/**
 * Agent state for bridging aplib and Minecraft using the
 * <a href="https://github.com/se-fbk/MineflayerTestbench">
 * MineflayerTestbench</a>.
 * <p>
 * No navigation is needed, as pathfinding is implemented in Mineflayer.
 * 
 * @author Davide Prandi
 */
public class MinecraftState extends Iv4xrAgentState<Void> {

	@Override
	public MinecraftEnv env() {
		return (MinecraftEnv) super.env();
	}

	@Override
	public MinecraftState setEnvironment(Environment env) {
		super.setEnvironment(env);
		return this;
	}

	/**
	 * Navigation is not managed. Throw an error.
	 */
	@Override
	public Navigatable<Void> worldNavigation() {
		throw new UnsupportedOperationException("navigation is delegated to the testbench");
	}

	@Override
	public MinecraftState setWorldNavigation(Navigatable<Void> nav) {
		throw new UnsupportedOperationException("navigation is delegated to the testbench");
	}

	/**
	 * New state replace old state (no merge)
	 */
	@Override
	public void updateState(String agentId) {
		this.worldmodel = env().observe(agentId);
	}

	/////////////////////////////////////////////////////
	///
	/// Utilities
	///
	/////////////////////////////////////////////////////

	public WorldEntity getAgent() {
		if (worldmodel == null) {
			return null;
		}
		return worldmodel.elements.get(worldmodel.agentId);
	}

	public Float getHealth() {
		WorldEntity a = getAgent();
		return a == null ? null : (Float) a.properties.get("health");
	}

	public boolean isAgentAlive() {
		Float hp = getHealth();
		return hp != null && hp > 0f;
	}

	public Vec3 getAgentPosition() {
		return worldmodel == null ? null : worldmodel.position;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getInventory() {
		WorldEntity a = getAgent();
		if (a == null) {
			return new HashMap<>();
		}
		Map<String, Integer> inv = (Map<String, Integer>) a.properties.get(StatusToWorldModel.INVENTORY_PROP);
		return inv == null ? new HashMap<>() : inv;
	}

	public int getItemCount(String itemName) {
		return getInventory().getOrDefault(itemName, 0);
	}

	public boolean hasItem(String itemName, int count) {
		return getItemCount(itemName) >= count;
	}

	public Boolean lastActionResult() {
		WorldEntity a = getAgent();
		return a == null ? null : (Boolean) a.properties.get("lastActionResult");
	}

	/**
	 * Euclidian distance of the agent from a tagged object
	 * 
	 * @param tag
	 * @return
	 */
	public double distanceToTag(String tag) {
		Vec3 target = env().tagPosition(tag);
		Vec3 me = getAgentPosition();
		if (target == null || me == null) {
			return Double.POSITIVE_INFINITY;
		}
		return Vec3.dist(me, target);
	}

}
