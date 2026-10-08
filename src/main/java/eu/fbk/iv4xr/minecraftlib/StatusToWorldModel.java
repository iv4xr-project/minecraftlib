package eu.fbk.iv4xr.minecraftlib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide the utility for translation of the JSON returned by the
 * MineflayerTestbench
 * 
 * @author Davide Prandi
 */
public class StatusToWorldModel {

	private StatusToWorldModel() {
	}

	public static final String AGENT_TYPE = "player";
	public static final String BLOCK_ID_PREFIX = "block:";
	public static final String ENTITY_ID_PREFIX = "entity:";
	public static final String INVENTORY_PROP = "inventory";
	public static final String HELD_ITEM_PROP = "heldItem";
	public static final String HELD_ITEM_DURABILITY_USED_PROP = "heldItemDurabilityUsed";
	public static final String ITEM_DURABILITY_USED_PROP = "itemDurabilityUsed";
	public static final String ITEM_MAX_DURABILITY_PROP = "itemMaxDurability";

	public static final String HEALTH = "health";
	public static final String FOOD = "food";
	public static final String VERSION = "version";
	public static final String STATUS = "status";
	public static final String LAST_ACTION_RESULT = "lastActionResult";
	public static final String INVENTORY = "inventory";
	public static final String DEATHS = "deaths";
	
	/**
	 * Covert the MineflyerTestbech json into a iv4xr world model.
	 * 
	 * @param agentId
	 * @param status
	 * @param timestamp
	 * @return
	 */
	public static WorldModel convert(String agentId, JsonObject status, long timestamp) {
		WorldModel wom = new WorldModel();
		wom.agentId = agentId;
		wom.timestamp = timestamp;

		Vec3 pos = vec3(status.getAsJsonObject("position"));
		wom.position = pos;
		wom.velocity = new Vec3(0, 0, 0); // TODO This could be refined
 
		// Agent entity
		WorldEntity agent = new WorldEntity(agentId, AGENT_TYPE, true);
		agent.position = pos;
		agent.timestamp = timestamp;
		if (has(status, HEALTH)) {
			agent.properties.put(HEALTH, (float) status.get(HEALTH).getAsDouble());
		}
		
		if (has(status, FOOD)) {
			agent.properties.put(FOOD, (float) status.get(FOOD).getAsDouble());
		}
		
		if (has(status, VERSION)) {
			agent.properties.put(VERSION, status.get(VERSION).getAsString());
		}
		
		if (has(status, STATUS)) {
			agent.properties.put("botStatus", status.get(STATUS).getAsString());
		}

		if (has(status, DEATHS)) {
			agent.properties.put(DEATHS, status.get(DEATHS).getAsInt());
		}
		
        if (has(status, LAST_ACTION_RESULT)) {
            agent.properties.put(LAST_ACTION_RESULT, status.get(LAST_ACTION_RESULT).getAsBoolean());
        }

		// simplify inventory management creating a map
        HashMap<String, Integer> inventory = new HashMap<>();
        // durability of the items that wear out: item name -> used durability of each item with
        // that name (two pickaxes are two entries), and item name -> durability when new
        HashMap<String, List<Integer>> durabilityUsed = new HashMap<>();
        HashMap<String, Integer> maxDurability = new HashMap<>();
        if (has(status, "inventory")) {
        	for (JsonElement el : status.getAsJsonArray("inventory")) {
        		JsonObject item = el.getAsJsonObject();
        		String name = item.get("name").getAsString();
        		int count = has(item, "count") ? item.get("count").getAsInt() : 1;
        		inventory.merge(name, count, Integer::sum);
        		if (has(item, "durabilityUsed") && has(item, "maxDurability")) {
        			durabilityUsed.computeIfAbsent(name, n -> new ArrayList<>()).add(item.get("durabilityUsed").getAsInt());
        			maxDurability.put(name, item.get("maxDurability").getAsInt());
        		}
        	}
        }
        
        agent.properties.put(INVENTORY_PROP, inventory);
        agent.properties.put(ITEM_DURABILITY_USED_PROP, durabilityUsed);
        agent.properties.put(ITEM_MAX_DURABILITY_PROP, maxDurability);

        // name of the item in the hand of the agent, absent when the hand is empty
        if (has(status, "heldItem")) {
        	JsonObject held = status.getAsJsonObject("heldItem");
        	agent.properties.put(HELD_ITEM_PROP, held.get("name").getAsString());
        	// used durability of the held item, absent when it does not wear out
        	if (has(held, "durabilityUsed")) {
        		agent.properties.put(HELD_ITEM_DURABILITY_USED_PROP, held.get("durabilityUsed").getAsInt());
        	}
        }
        wom.elements.put(agentId, agent);
        
        // near blocks
        if (has(status, "nearbyBlocks")) {
            for (JsonElement el : status.getAsJsonArray("nearbyBlocks")) {
            	JsonObject o = el.getAsJsonObject();
            	Vec3 v = vec3(o.getAsJsonObject("position"));
            	String id = BLOCK_ID_PREFIX + coordKey(v);
            	WorldEntity we = new WorldEntity(id, o.get("id").getAsString(), false);
            	we.position = v;
            	copyProperties(o, we);
            	we.timestamp = timestamp;
            	wom.elements.put(id, we);            	
            }
        }
        
        // near entities
        if (has(status, "nearbyEntities")) {
        	for (JsonElement el : status.getAsJsonArray("nearbyEntities")) {
        		JsonObject o = el.getAsJsonObject();
        		String name = has(o, "name") ? o.get("name").getAsString() : "jdoe";
        		String uuid = has(o, "uuid") ? o.get("uuid").getAsString() : null;
        		Vec3 v = vec3(o.getAsJsonObject("position"));
        		// if available use uuid else create it 
        		// TODO  evalute if it moves
        		String id = uuid != null ? uuid : ENTITY_ID_PREFIX + name + ":" + coordKey(v);
        		WorldEntity we = new WorldEntity(id, name, true);
        		we.position = v;
        		if (has(o, "velocity")) {
        			we.velocity = vec3(o.getAsJsonObject("velocity"));
        		}
        		we.timestamp = timestamp;
        		copyProperties(o, we);
        		we.properties.put("name", name);
        		if (uuid != null) {
        			we.properties.put("uuid", uuid);
        		}
        		if (has(o, "id")) {
        			we.properties.put("mcEntityId", o.get("id").getAsInt());
        		}
        		wom.elements.put(id, we);
        	}
        }
        
		return wom;
	}

	/////////////////////////////////////////////////////
	///
	/// Utilities
	///
	/////////////////////////////////////////////////////

	/**
	 * Get a key from 3D coords
	 * @param p
	 * @return
	 */
	static String coordKey(Vec3 p) {
		return Math.round(p.x) + "_" + Math.round(p.y) + "_" + Math.round(p.z);
	}

	/**
	 * Parse json to get 3S coords
	 * @param o
	 * @return
	 */
	static Vec3 vec3(JsonObject o) {
		return new Vec3((float) o.get("x").getAsDouble(), (float) o.get("y").getAsDouble(),
				(float) o.get("z").getAsDouble());
	}

	/**
	 * Copy the primitive values of the "properties" json object, if present,
	 * into the properties of a world entity
	 * @param o
	 * @param we
	 */
	static void copyProperties(JsonObject o, WorldEntity we) {
		if (!has(o, "properties"))
			return;

		for (Map.Entry<String, JsonElement> p : o.getAsJsonObject("properties").entrySet()) {
			if (!p.getValue().isJsonPrimitive())
				continue;

			JsonPrimitive val = p.getValue().getAsJsonPrimitive();
			if (val.isBoolean())
				we.properties.put(p.getKey(), val.getAsBoolean());
			else if (val.isNumber())
				we.properties.put(p.getKey(), (float) val.getAsDouble());
			else
				we.properties.put(p.getKey(), val.getAsString());
		}
	}

	/**
	 * Check if a key is present in a json object
	 * @param o
	 * @param key
	 * @return
	 */
	static boolean has(JsonObject o, String key) {
		return o.has(key) && !o.get(key).isJsonNull();
	}

}
