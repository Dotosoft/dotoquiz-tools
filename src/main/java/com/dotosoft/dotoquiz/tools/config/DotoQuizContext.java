package com.dotosoft.dotoquiz.tools.config;

import org.apache.commons.chain.impl.ContextBase;

import com.dotosoft.dotoquiz.tools.util.SyncState;
import com.google.api.client.auth.oauth2.Credential;

public class DotoQuizContext extends ContextBase {

	private SyncState syncState;
	private Settings settings;
	private Credential credential;

	public DotoQuizContext() {
		syncState = new SyncState();
		settings = new Settings();
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public SyncState getSyncState() {
		return syncState;
	}

	public void setSyncState(SyncState syncState) {
		this.syncState = syncState;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}