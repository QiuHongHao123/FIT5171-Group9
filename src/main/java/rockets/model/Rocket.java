package rockets.model;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Rocket extends Entity {
    private String name;

    private String country;

    private String manufacturer;

    private String massToLEO;

    private String massToGTO;

    private String massToOther;

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param country
     * @param manufacturer
     */
    public Rocket(String name, String country, String manufacturer) {
        notNull(name,"param of construct function cannot be null");
        notNull(country,"param of construct function cannot be null");
        notNull(manufacturer,"param of construct function cannot be null");

        this.name = name;
        this.country = country;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getManufacturer() {
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
        notBlank( massToLEO, "massToLEO cannot be null or empty");
        this.massToLEO = massToLEO;
    }

    public void setMassToGTO(String massToGTO) {
        notBlank( massToGTO, "massToGTO cannot be null or empty");
        this.massToGTO = massToGTO;
    }

    public void setMassToOther(String massToOther) {
        notBlank( massToOther, "massToOther cannot be null or empty");
        this.massToOther = massToOther;
    }
    public boolean gtoAndLeoStandard(){
        if(this.massToGTO==null||this.massToLEO==null) return false;
        else if((!this.massToGTO.matches("^\\d+$"))||(!this.massToLEO.matches("^\\d+$"))){
            return false;
        }
        int numToGTO=Integer.parseInt(this.massToGTO);
        int numToLEO=Integer.parseInt(this.massToLEO);
        if(numToGTO<=0||numToLEO<=0) return false;
        else if(numToGTO>=(0.5*numToLEO*0.7)&&numToGTO<=(0.5*numToLEO*1.3)){
            return true;
        }
        else{
            return false;
        }
    }
    public String belongToFamily(){
        String[] family={"LM","Delta","OmegA","Saturn","Ariane","Angara","Soyuz","H","Falcon"};
        String name=this.getName();
        String belong="";
        for(String s:family){
            if(name.matches("^"+s+".*")){
                belong=s;
            }
        }
        return belong;
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
