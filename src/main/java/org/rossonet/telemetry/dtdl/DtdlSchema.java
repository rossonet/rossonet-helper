package org.rossonet.telemetry.dtdl;

import org.json.JSONObject;

public interface DtdlSchema {

	public void exportDtmlSchema();

	public void importDtmlSchema(JSONObject rootSchema);

}
