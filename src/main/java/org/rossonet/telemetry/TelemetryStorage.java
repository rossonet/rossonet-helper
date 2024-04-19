package org.rossonet.telemetry;

import java.util.Collection;

import org.apache.jena.rdf.model.Model;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace;
import org.rossonet.rules.base.CachedMemory;
import org.rossonet.telemetry.dtdl.DtdlFragment;
import org.rossonet.telemetry.dtdl.DtdlSchema;

public interface TelemetryStorage extends CachedMemory, DtdlSchema, DtdlFragment {

	public Model getJenaModel();

	public ManagedNamespace getNamespace();

	public SshServerStorage getSshServerStorage();

	public Collection<TelemetryData<?>> getTelemetries();

}
