import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * This class represents a factory that will produce TypeAdapters to parse Stops.
 */
public class StopAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Stop.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        return (TypeAdapter<T>) new StopAdapter();
    }

    /**
     * This class represents an adapter that will parse stops in JSON to the Stop class.
     */
    private static class StopAdapter extends TypeAdapter<Stop> {

        @Override
        public void write(JsonWriter out, Stop value) throws IOException {
            // No-op, we only care about deserialization
        }

        @Override
        public Stop read(JsonReader in) throws IOException {
            final Stop stop = new Stop();
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "id":
                        stop.setId(in.nextString());
                        break;
                    case "attributes":
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "address":
                                    try {
                                        stop.setAddress(in.nextString());
                                    } catch (IllegalStateException e) {
                                        in.skipValue();
                                    }
                                    break;
                                case "name":
                                    stop.setName(in.nextString());
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
            return stop;
        }
    }
}
