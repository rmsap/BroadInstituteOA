import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * This class contains utility methods for parsing JSON.
 */
public class ParsingUtils {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String USERNAME = "saperstein.r@northeastern.edu";
    private static final String PASSWORD = "Welcome@1234";

    /**
     * Fetch JSON data from the given URL.
     * @param url          URL from which data should be fetched
     * @return             JSON data from URL, as a String
     * @throws IOException if we fail to fetch data
     */
    public static String fetchData(final URL url) throws IOException {
        // Set up HTTP request
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        final String userAndPass = USERNAME + ":" + PASSWORD;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userAndPass.getBytes()));
        connection.setRequestProperty ("Authorization", basicAuth);

        // Read input from request and return it
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            final StringBuilder json = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
            return json.toString();
        } else {
            throw new RuntimeException("Failed to get stop data from MBTA.");
        }
    }

    /**
     * Parse the JSON into the given class using the given TypeAdapterFactory.
     * @param json           JSON to parse
     * @param classOfT       class to parse JSON into
     * @param adapterFactory TypeAdapterFactory to use for parsing
     * @return               List of parsed objects of the given class
     */
    public static <T> List<T> parse(final String json, final Class<T> classOfT,
                                   final TypeAdapterFactory adapterFactory) {
        final Gson gson =
                new GsonBuilder().registerTypeAdapterFactory(adapterFactory).setPrettyPrinting().create();
        final String[] jsonOfT = prepareJsonForParsing(json);

        final List<T> parsedTs = new ArrayList<>();
        for (int i = 1; i < jsonOfT.length; i++) {
            final T t = gson.fromJson(jsonOfT[i], classOfT);
            if (t != null) {
                parsedTs.add(gson.fromJson(jsonOfT[i], classOfT));
            }
        }
        return parsedTs;
    }

    /**
     * Prepare the given Stop or Route JSON to be parsed.
     * @param json JSON to prepare for parsing.
     * @return     An array of Strings containing each cleaned JSON object description
     */
    private static String[] prepareJsonForParsing(final String json) {
        final String[] splitJson = json.split("attributes");
        for (int i = 1; i < splitJson.length; i++) {
            splitJson[i] = "{\"attributes" + splitJson[i];
        }
        final int lastIndex = splitJson.length - 1;
        final String metadataToDrop = "],\"jsonapi\":{\"version\":\"1.0\"}}";
        if (splitJson[lastIndex].contains(metadataToDrop)) {
            splitJson[lastIndex] = splitJson[lastIndex].substring(0,
                    splitJson[lastIndex].indexOf(metadataToDrop));
        }

        for (int i = 1; i < splitJson.length - 1; i++) {
            splitJson[i] = splitJson[i].substring(0, splitJson[i].lastIndexOf(",{"));
        }
        return splitJson;
    }
}
