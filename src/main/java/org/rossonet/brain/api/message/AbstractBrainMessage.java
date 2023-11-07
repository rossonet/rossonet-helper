package org.rossonet.brain.api.message;

import java.io.Serializable;
import java.util.UUID;

public class AbstractBrainMessage<MESSAGE_TYPE extends Serializable> implements BrainMessage<MESSAGE_TYPE> {

	private final UUID uuid = UUID.randomUUID();
	private MESSAGE_TYPE payload;
	private BrainMessageHeader header;

	@Override
	public BrainMessageHeader getHeader() {
		return header;
	}

	@Override
	public MESSAGE_TYPE getPayload() {
		return payload;
	}

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	public void setHeader(BrainMessageHeader header) {
		this.header = header;
	}

	public void setPayload(MESSAGE_TYPE payload) {
		this.payload = payload;
	}

}
