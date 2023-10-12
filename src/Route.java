import java.util.List;

/**
 * This class represents a Route as fetched from the MBTA API.
 */
public class Route {
    private String id;
    private String description;
    private String[] directionDestinations;
    private String[] directionNames;
    private String fareClass;
    private String longName;
    private String shortName;
    private int typeInt;
    private String typeString;
    private List<Stop> stops;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDirectionDestinations(String[] directionDestinations) {
        this.directionDestinations = directionDestinations;
    }

    public void setDirectionNames(String[] directionNames) {
        this.directionNames = directionNames;
    }

    public void setFareClass(String fareClass) {
        this.fareClass = fareClass;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(final List<Stop> stops) {
        this.stops = stops;
    }
}
