import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Tests that parse JSON into Routes.
 */
public class ParseRouteTests {
    private static final String VALID_JSON = "{\"attributes\":{\"color\":\"DA291C\",\"description\":\"Rapid " +
            "Transit\",\"direction_destinations\":[\"Ashmont/Braintree\",\"Alewife\"],\"direction_names\":[\"South\"" +
            ",\"North\"],\"fare_class\":\"Rapid Transit\",\"long_name\":\"Red Line\",\"short_name\":\"\",\"sort_order\"" +
            ":10010," + "\"text_color\":\"FFFFFF\",\"type\":1},\"id\":\"Red\",\"links\":{\"self\":\"/routes/Red\"},\"relationships\"" +
            ":{\"line\":{\"data\":{\"id\":\"line-Red\",\"type\":\"line\"}}},\"type\":\"route\"}";
    private static final String INVALID_JSON = "\"attributes\":{\"color\":\"DA291C\",\"description\":\"Rapid " +
            "Transit\",\"direction_destinations\":[\"Ashmont/Braintree\",\"Alewife\"],\"direction_names\":[\"South\"" +
            ",\"North\"],\"fare_class\":\"Rapid Transit\",\"long_name\":\"Red Line\",\"short_name\":\"\",\"sort_order\"" +
            ":10010," + "\"text_color\":\"FFFFFF\",\"type\":1},\"id\":\"Red\",\"links\":{\"self\":\"/routes/Red\"},\"relationships\"" +
            ":{\"line\":{\"data\":{\"id\":\"line-Red\",\"type\":\"line\"}}},\"type\":\"route\"";
    private static final String MISSING_FIELDS_JSON = "{\"attributes\":{\"color\":\"DA291C\",\"description\":\"Rapid " +
            "Transit\",\"direction_destinations\":[\"Ashmont/Braintree\",\"Alewife\"],\"direction_names\":[\"South\"" +
            ",\"North\"],\"fare_class\":\"Rapid Transit\",\"long_name\":\"Red Line\",\"short_name\":\"\",\"sort_order\"" +
            ":10010," + "\"text_color\":\"FFFFFF\"},\"links\":{\"self\":\"/routes/Red\"},\"relationships\"" +
            ":{\"line\":{\"data\":{\"id\":\"line-Red\",\"type\":\"line\"}}},\"type\":\"route\"}";
    private static final String MISSING_ATTRIBUTES_JSON = "{\"links\":{\"self\":\"/routes/Red\"},\"relationships\"" +
            ":{\"line\":{\"data\":{\"id\":\"line-Red\",\"type\":\"line\"}}},\"type\":\"route\"}";
    private static final String EMPTY_STRING = "";

    private static final String TWO_VALID_JSON = "[{\"attributes\":{\"color\":\"DA291C\",\"description\":\"Rapid " +
            "Transit\",\"direction_destinations\":[\"Ashmont/Braintree\",\"Alewife\"],\"direction_names\":[\"South\"," +
            "\"North\"],\"fare_class\":\"Rapid Transit\",\"long_name\":\"Red Line\",\"short_name\":\"\"," +
            "\"sort_order\":10010,\"text_color\":\"FFFFFF\",\"type\":1},\"id\":\"Red\"," +
            "\"links\":{\"self\":\"/routes/Red\"},\"relationships\":{\"line\":{\"data\":{\"id\":\"line-Red\"," +
            "\"type\":\"line\"}}},\"type\":\"route\"},{\"attributes\":{\"color\":\"DA291C\",\"description\":\"Rapid " +
            "Transit\",\"direction_destinations\":[\"Mattapan\",\"Ashmont\"],\"direction_names\":[\"Outbound\"," +
            "\"Inbound\"],\"fare_class\":\"Rapid Transit\",\"long_name\":\"Mattapan Trolley\",\"short_name\":\"\"," +
            "\"sort_order\":10011,\"text_color\":\"FFFFFF\",\"type\":0},\"id\":\"Mattapan\"," +
            "\"links\":{\"self\":\"/routes/Mattapan\"}," +
            "\"relationships\":{\"line\":{\"data\":{\"id\":\"line-Mattapan\",\"type\":\"line\"}}},\"type\":\"route\"}]," +
            "\"jsonapi\":{\"version\":\"1.0\"}}";

    private static final Gson GSON =
            new GsonBuilder().registerTypeAdapterFactory(new RouteAdapterFactory()).setPrettyPrinting().create();

    @Test
    public void testParseValidJson() {
        final Route route = GSON.fromJson(VALID_JSON, Route.class);
        Assert.assertEquals("Red", route.getId());
        Assert.assertEquals("Red Line", route.getLongName());
        Assert.assertNull(route.getStops());
        Assert.assertEquals(1, route.getTypeInt());

        final Route routeMissingFields = GSON.fromJson(MISSING_FIELDS_JSON, Route.class);
        Assert.assertNull(routeMissingFields.getId());
        Assert.assertEquals("Red Line", routeMissingFields.getLongName());
        Assert.assertNull(routeMissingFields.getStops());
        Assert.assertEquals(0, routeMissingFields.getTypeInt());

        final Route routeMissingAttributes = GSON.fromJson(MISSING_ATTRIBUTES_JSON, Route.class);
        Assert.assertNull(routeMissingAttributes.getId());
        Assert.assertNull(routeMissingAttributes.getLongName());
        Assert.assertNull(routeMissingAttributes.getStops());
        Assert.assertEquals(0, routeMissingAttributes.getTypeInt());
    }

    @Test
    public void testParseInvalidJson() {
        try {
            final Route route = GSON.fromJson(INVALID_JSON, Route.class);
            Assert.fail("Exception should be thrown when parsing invalid JSON.");
        } catch (JsonSyntaxException e) {
            Assert.assertEquals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line " +
                            "1 column 2 path $",
                    e.getMessage());
        }

        final Route nullRoute = GSON.fromJson(EMPTY_STRING, Route.class);
        Assert.assertNull(nullRoute);

        final Route nullRoute2 = GSON.fromJson((String) null, Route.class);
        Assert.assertNull(nullRoute2);
    }

    @Test
    public void testParseList() {
        final List<Route> singleRoute = ParsingUtils.parse(VALID_JSON, Route.class, new RouteAdapterFactory());
        Assert.assertEquals(1, singleRoute.size());
        final Route route = singleRoute.get(0);
        Assert.assertEquals("Red", route.getId());
        Assert.assertEquals("Red Line", route.getLongName());
        Assert.assertNull(route.getStops());
        Assert.assertEquals(1, route.getTypeInt());

        final List<Route> doubleRoutes = ParsingUtils.parse(TWO_VALID_JSON, Route.class, new RouteAdapterFactory());
        Assert.assertEquals(2, doubleRoutes.size());

        final Route firstRoute = doubleRoutes.get(0);
        Assert.assertEquals("Red", firstRoute.getId());
        Assert.assertEquals("Red Line", firstRoute.getLongName());
        Assert.assertNull(firstRoute.getStops());
        Assert.assertEquals(1, firstRoute.getTypeInt());

        final Route secondRoute = doubleRoutes.get(1);
        Assert.assertEquals("Mattapan", secondRoute.getId());
        Assert.assertEquals("Mattapan Trolley", secondRoute.getLongName());
        Assert.assertNull(secondRoute.getStops());
        Assert.assertEquals(0, secondRoute.getTypeInt());

        final List<Route> emptyList = ParsingUtils.parse(EMPTY_STRING, Route.class, new RouteAdapterFactory());
        Assert.assertTrue(emptyList.isEmpty());

    }
}
