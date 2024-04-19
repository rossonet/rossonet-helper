package org.rossonet.telemetry;

import java.util.Collection;

import org.apache.jena.rdf.model.Model;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace;
import org.json.JSONObject;
import org.rossonet.ext.rules.api.Facts;

public abstract class AbstractTelemetryStorage implements TelemetryStorage {

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject exportDtmlFragment(final String position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject exportDtmlSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Facts getFacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getJenaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedNamespace getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SshServerStorage getSshServerStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TelemetryData<?>> getTelemetries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importDtmlFragment(final String position, final JSONObject partialSchema) {
		// TODO Auto-generated method stub

	}

	@Override
	public void importDtmlSchema(final JSONObject rootSchema) {
		// TODO Auto-generated method stub

	}

}
