package org.rossonet.brain.api.message;

import java.io.Serializable;

import org.rossonet.brain.api.UniversallyUniqueObject;

public interface BrainMessage<MESSAGE_TYPE extends Serializable> extends UniversallyUniqueObject {

	public BrainMessageHeader getHeader();

	public MESSAGE_TYPE getPayload();

}
