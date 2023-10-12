import java.util.Objects;

/**
 * This class represents a Stop as fetched from the MBTA API.
 */
public class Stop {
    private String id;
    private String name;
    private String address;

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(id, stop.id) && Objects.equals(name, stop.name) && Objects.equals(address, stop.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }
}
