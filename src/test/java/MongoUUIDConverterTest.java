import com.lifeinide.mongo.MongoUUIDConverter;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @author Lukasz Frankowski (lifeinide.com)
 */
public class MongoUUIDConverterTest {

	@Test
	public void test() {
		for (int i=0; i<256; i++) {
			ObjectId objectId = new ObjectId(new Date(), i);
			UUID uuid = MongoUUIDConverter.toUUID(objectId);
			Assert.assertEquals(objectId, MongoUUIDConverter.toObjectId(uuid));
		}

	}

}
