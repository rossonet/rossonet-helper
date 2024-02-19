package org.rossonet.telemetry.dtdl;

import org.json.JSONObject;

public interface DtdlFragment {

	public JSONObject exportDtmlFragment(String position);

	public void importDtmlFragment(String position, JSONObject partialSchema);

}
