package rockets.model;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.*;

public class Rocket extends Entity {
    private String name;

    private String family;

    private String country;


    private String massToLEO;

    private String massToGTO;

    private String massToOther;

    private Launch launch;

    private LaunchServiceProvider manufacturer;

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param country
     * @param manufacturer
     */
    public Rocket(String name, String country, LaunchServiceProvider manufacturer) {
        notNull(name,"Name cannot be null");
        notNull(country,"Country cannot be null");
        notNull(manufacturer,"Manufacturer cannot be null");

        this.name = name;
        this.country = country;
        this.manufacturer = manufacturer;

    }


    public String getName() {
        return name;
    }

    public String getFamily(){ return family;}

    public String getCountry() {
        return country;
    }

    public LaunchServiceProvider getManufacturer() {
        return manufacturer;
    }

    public String getMassToLEO() {
        return massToLEO;
    }

    public String getMassToGTO() {
        return massToGTO;
    }

    public String getMassToOther() {
        return massToOther;
    }

    public void setMassToLEO(String massToLEO) {
        notNull(massToLEO,"massToLEO cannot be null");
        this.massToLEO = massToLEO;
    }

    public void setFamily(String family){
        notNull(family,"Family cannot be null");
        this.family = family;
    }

    public void setMassToGTO(String massToGTO) {
        notNull(massToGTO,"Mass cannot be null");
        this.massToGTO = massToGTO;
    }

    public void setMassToOther(String massToOther) {
        notNull(massToOther,"Mass cannot be null");
        this.massToOther = massToOther;
    }

    public Launch getLaunch() {
        return launch;
    }
    public void setLaunch(Launch launch) {
        this.launch = launch;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return Objects.equals(name, rocket.name) &&
                Objects.equals(country, rocket.country) &&
                Objects.equals(manufacturer, rocket.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, manufacturer);
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", massToLEO='" + massToLEO + '\'' +
                ", massToGTO='" + massToGTO + '\'' +
                ", massToOther='" + massToOther + '\'' +
                '}';
    }
}
