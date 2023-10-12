import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests that parse JSON into Stops.
 */
public class ParseStopTests {
    private static final String VALID_JSON = "{\"attributes\":{\"address\":null,\"at_street\":null," +
            "\"description\":\"Downtown Crossing - Red Line - Alewife\",\"latitude\":42.355518,\"location_type\":0," +
            "\"longitude\":-71.060225,\"municipality\":\"Boston\",\"name\":\"Downtown Crossing\",\"on_street\":null," +
            "\"platform_code\":null,\"platform_name\":\"Alewife\",\"vehicle_type\":1,\"wheelchair_boarding\":1}," +
            "\"id\":\"70078\",\"links\":{\"self\":\"/stops/70078\"}," +
            "\"relationships\":{\"facilities\":{\"links\":{\"related\":\"/facilities/?filter[stop]=70078\"}}," +
            "\"parent_station\":{\"data\":{\"id\":\"place-dwnxg\",\"type\":\"stop\"}}," +
            "\"zone\":{\"data\":{\"id\":\"RapidTransit\",\"type\":\"zone\"}}},\"type\":\"stop\"}";
    private static final String INVALID_JSON = "\"attributes\":{\"address\":null,\"at_street\":null," +
            "\"description\":\"Downtown Crossing - Red Line - Alewife\",\"latitude\":42.355518,\"location_type\":0," +
            "\"longitude\":-71.060225,\"municipality\":\"Boston\",\"name\":\"Downtown Crossing\",\"on_street\":null," +
            "\"platform_code\":null,\"platform_name\":\"Alewife\",\"vehicle_type\":1,\"wheelchair_boarding\":1}," +
            "\"id\":\"70078\",\"links\":{\"self\":\"/stops/70078\"}," +
            "\"relationships\":{\"facilities\":{\"links\":{\"related\":\"/facilities/?filter[stop]=70078\"}}," +
            "\"parent_station\":{\"data\":{\"id\":\"place-dwnxg\",\"type\":\"stop\"}}," +
            "\"zone\":{\"data\":{\"id\":\"RapidTransit\",\"type\":\"zone\"}}},\"type\":\"stop\"";
    private static final String MISSING_FIELDS = "{\"attributes\":{\"at_street\":null," +
            "\"description\":\"Downtown Crossing - Red Line - Alewife\",\"latitude\":42.355518,\"location_type\":0," +
            "\"longitude\":-71.060225,\"municipality\":\"Boston\",\"on_street\":null," +
            "\"platform_code\":null,\"platform_name\":\"Alewife\",\"vehicle_type\":1,\"wheelchair_boarding\":1}," +
            "\"id\":\"70078\",\"links\":{\"self\":\"/stops/70078\"}," +
            "\"relationships\":{\"facilities\":{\"links\":{\"related\":\"/facilities/?filter[stop]=70078\"}}," +
            "\"parent_station\":{\"data\":{\"id\":\"place-dwnxg\",\"type\":\"stop\"}}," +
            "\"zone\":{\"data\":{\"id\":\"RapidTransit\",\"type\":\"zone\"}}},\"type\":\"stop\"}";
    private static final String MISSING_ATTRIBUTES = "{\"address\":null,\"at_street\":null," +
            "\"description\":\"Downtown Crossing - Red Line - Alewife\",\"latitude\":42.355518,\"location_type\":0," +
            "\"longitude\":-71.060225,\"municipality\":\"Boston\",\"name\":\"Downtown Crossing\",\"on_street\":null," +
            "\"platform_code\":null,\"platform_name\":\"Alewife\",\"vehicle_type\":1,\"wheelchair_boarding\":1," +
            "\"id\":\"70078\",\"links\":{\"self\":\"/stops/70078\"}," +
            "\"relationships\":{\"facilities\":{\"links\":{\"related\":\"/facilities/?filter[stop]=70078\"}}," +
            "\"parent_station\":{\"data\":{\"id\":\"place-dwnxg\",\"type\":\"stop\"}}," +
            "\"zone\":{\"data\":{\"id\":\"RapidTransit\",\"type\":\"zone\"}}},\"type\":\"stop\"}";

    private static final String EMPTY_STRING = "";
    private static final String TWO_VALID_JSON = "[{\"attributes\":{\"address\":null,\"at_street\":null," +
            "\"description\":\"Downtown Crossing - Red Line - Alewife\",\"latitude\":42.355518,\"location_type\":0," +
            "\"longitude\":-71.060225,\"municipality\":\"Boston\",\"name\":\"Downtown Crossing\",\"on_street\":null," +
            "\"platform_code\":null,\"platform_name\":\"Alewife\",\"vehicle_type\":1,\"wheelchair_boarding\":1}," +
            "\"id\":\"70078\",\"links\":{\"self\":\"/stops/70078\"}," +
            "\"relationships\":{\"facilities\":{\"links\":{\"related\":\"/facilities/?filter[stop]=70078\"}}," +
            "\"parent_station\":{\"data\":{\"id\":\"place-dwnxg\",\"type\":\"stop\"}}," +
            "\"zone\":{\"data\":{\"id\":\"RapidTransit\",\"type\":\"zone\"}}},\"type\":\"stop\"}," +
            "{\"attributes\":{\"address\":null,\"at_street\":null,\"description\":null,\"latitude\":42.253283," +
            "\"location_type\":0,\"longitude\":-71.170494,\"municipality\":\"Dedham\",\"name\":\"Dedham Mall @ South " +
            "Side\",\"on_street\":null,\"platform_code\":null,\"platform_name\":null,\"vehicle_type\":3," +
            "\"wheelchair_boarding\":1},\"id\":\"10835\",\"links\":{\"self\":\"/stops/10835\"}," +
            "\"relationships\":{\"facilities\":{\"links\":{\"related\":\"/facilities/?filter[stop]=10835\"}}," +
            "\"parent_station\":{\"data\":null},\"zone\":{\"data\":{\"id\":\"LocalBus\",\"type\":\"zone\"}}}," +
            "\"type\":\"stop\"}],\"jsonapi\":{\"version\":\"1.0\"}}";
    private static final Gson GSON =
            new GsonBuilder().registerTypeAdapterFactory(new StopAdapterFactory()).setPrettyPrinting().create();

    @Test
    public void testParseValidJson() {
        final Stop stop = GSON.fromJson(VALID_JSON, Stop.class);
        Assert.assertEquals("Downtown Crossing", stop.getName());

        final Stop stopMissingFields = GSON.fromJson(MISSING_FIELDS, Stop.class);
        Assert.assertNull(stopMissingFields.getName());

        final Stop stopMissingAttributes = GSON.fromJson(MISSING_ATTRIBUTES, Stop.class);
        Assert.assertNull(stopMissingAttributes.getName());
    }

    @Test
    public void testParseInvalidJson() {
        try {
            final Stop stop = GSON.fromJson(INVALID_JSON, Stop.class);
            Assert.fail("Exception should be thrown when parsing invalid JSON.");
        } catch (JsonSyntaxException e) {
            Assert.assertEquals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line " +
                            "1 column 2 path $",
                    e.getMessage());
        }

        final Stop nullStop = GSON.fromJson(EMPTY_STRING, Stop.class);
        Assert.assertNull(nullStop);

        final Stop nullStop2 = GSON.fromJson((String) null, Stop.class);
        Assert.assertNull(nullStop2);
    }

    @Test
    public void testParseList() {
        final List<Stop> singleStop = ParsingUtils.parse(VALID_JSON, Stop.class, new StopAdapterFactory());
        Assert.assertEquals(1, singleStop.size());
        final Stop stop = singleStop.get(0);
        Assert.assertEquals("Downtown Crossing", stop.getName());

        final List<Stop> doubleStops = ParsingUtils.parse(TWO_VALID_JSON, Stop.class, new StopAdapterFactory());
        Assert.assertEquals(2, doubleStops.size());

        final Stop firstStop = doubleStops.get(0);
        Assert.assertEquals("Downtown Crossing", firstStop.getName());

        final Stop secondStop = doubleStops.get(1);
        Assert.assertEquals("Dedham Mall @ South Side", secondStop.getName());

        final List<Stop> emptyList = ParsingUtils.parse(EMPTY_STRING, Stop.class, new StopAdapterFactory());
        Assert.assertTrue(emptyList.isEmpty());

    }
}
