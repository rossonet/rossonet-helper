package org.rossonet.telemetry.dtdl;

import org.json.JSONObject;

public interface DtdlSchema {

	public JSONObject exportDtmlSchema();

	public void importDtmlSchema(JSONObject rootSchema);

}
