import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

public class FetchDataTests {
    private static final String ROUTE_URL = "https://api-v3.mbta.com/routes";
    @Test
    public void testFetchData() {
        try {
            final String fetchedData = ParsingUtils.fetchData(new URL(ROUTE_URL));
            Assert.assertFalse(fetchedData.isEmpty());
        } catch (Exception e) {
            Assert.fail("Should have succeeded in fetching data from the Route URL. Failed with error " + e.getMessage());
        }
    }
}
