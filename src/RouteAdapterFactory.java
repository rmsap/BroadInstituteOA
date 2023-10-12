import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a factory that will produce TypeAdapters to parse Routes.
 */
public class RouteAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Route.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        return (TypeAdapter<T>) new RouteAdapter();
    }

    /**
     * This class represents an adapter that will parse routes in JSON to the Route class.
     */
    private static class RouteAdapter extends TypeAdapter<Route> {
        @Override
        public void write(JsonWriter out, Route value) throws IOException {
            // No-op, we only care about deserialization
        }

        @Override
        public Route read(JsonReader in) throws IOException {
            final Route route = new Route();
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "id":
                        route.setId(in.nextString());
                        break;
                    case "type":
                        route.setTypeString(in.nextString());
                        break;
                    case "attributes":
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "description":
                                    route.setDescription(in.nextString());
                                    break;
                                case "direction_destinations":
                                    in.beginArray();
                                    final List<String> direction_destinations = new ArrayList<>();
                                    while (in.hasNext()) {
                                        direction_destinations.add(in.nextString());
                                    }
                                    route.setDirectionDestinations(direction_destinations.toArray(new String[direction_destinations.size()]));
                                    in.endArray();
                                    break;
                                case "direction_names":
                                    in.beginArray();
                                    final List<String> direction_names = new ArrayList<>();
                                    while (in.hasNext()) {
                                        direction_names.add(in.nextString());
                                    }
                                    route.setDirectionNames(direction_names.toArray(new String[direction_names.size()]));
                                    in.endArray();
                                    break;
                                case "fare_class":
                                    route.setFareClass(in.nextString());
                                    break;
                                case "long_name":
                                    route.setLongName(in.nextString());
                                    break;
                                case "short_name":
                                    route.setShortName(in.nextString());
                                    break;
                                case "type":
                                    route.setTypeInt(in.nextInt());
                                    break;
                                default:
                                    in.skipValue();
                                    break;
                            }
                        }
                        in.endObject();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return route;
        }
    }
}
