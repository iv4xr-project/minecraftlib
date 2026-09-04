package eu.fbk.iv4xr.minecraftlib;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import nl.uu.cs.aplib.mainConcepts.Environment;

public class MinecraftAgent extends TestAgent {
	private String serverUrl;
	public MinecraftAgent(String name, String serverUrl) {
		super(name, null);
		this.setServerUrl(serverUrl);
	}


	
	@Override
	public TestAgent attachEnvironment(Environment env) {
		// TODO Auto-generated method stub
		if (env instanceof MinecraftEnv) {
			((MinecraftEnv) env).joinServer(this);
			return super.attachEnvironment(env);
		}
		throw new IllegalStateException("Can only accept MinecraftEnv");
	}

	public String getServerUrl() {
		return serverUrl;
	}


	public void setServerUrl(String serverUrl) {
		if (this.serverUrl == serverUrl) {
			return;
		}
		this.serverUrl = serverUrl;
		if (state() != null && state().env() != null) {
			((MinecraftEnv) state().env()).joinServer(this);			
		}
	}
	
	public void quit() {
		if (state() != null && state().env() != null) {
			((MinecraftEnv) state().env()).quit(this);			
		}
	}
}
