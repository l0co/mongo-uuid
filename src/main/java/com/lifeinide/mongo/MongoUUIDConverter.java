package com.lifeinide.mongo;

import org.bson.types.ObjectId;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Converts mongodb {@link ObjectId} to java {@link UUID} in both sides using version 4 (randomly generated) UUIDS.
 *
 * @author Lukasz Frankowski (lifeinide.com)
 */
public class MongoUUIDConverter {

	public static UUID toUUID(ObjectId objectId) {
		byte[] objectidBytes = objectId.toByteArray();
		byte[] uuidBytes = new byte[16];

		System.arraycopy(objectidBytes, 0, uuidBytes, 0, 6);
		System.arraycopy(objectidBytes, 6, uuidBytes, 10, 6);

		uuidBytes[6]  &= 0x0f;  /* clear version        */
		uuidBytes[6]  |= 0x40;  /* set to version 4     */
		uuidBytes[8]  &= 0x3f;  /* clear variant        */
		uuidBytes[8]  |= 0x80;  /* set to IETF variant  */

		ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);

		return new UUID(buffer.getLong(), buffer.getLong());
	}

	public static ObjectId toObjectId(UUID uuid) {
		byte[] uuidBytes = new byte[16];
		((ByteBuffer) ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).position(0))
			.get(uuidBytes);

		if (uuidBytes[6]!=64 || uuidBytes[7]!=0 || uuidBytes[8]!=-128 || uuidBytes[9]!=0)
			throw new IllegalArgumentException(String.format("UUID: %s has not been created from ObjectId", uuid.toString()));

		byte[] objectidBytes = new byte[12];

		System.arraycopy(uuidBytes, 0, objectidBytes, 0, 6);
		System.arraycopy(uuidBytes, 10, objectidBytes, 6, 6);

		return new ObjectId(objectidBytes);
	}

	public static void main(String [] args) {
		ObjectId objectId = new ObjectId();
		UUID uuid = toUUID(objectId);
		System.out.println(String.format("SRC_ID: %s, UUID: %s, REV_ID: %s", objectId.toHexString(), uuid.toString(),
			toObjectId(uuid).toHexString()));
	}

}
