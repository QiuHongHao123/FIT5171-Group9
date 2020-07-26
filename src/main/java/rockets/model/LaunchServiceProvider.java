package rockets.model;

import com.google.common.collect.Sets;

import static org.apache.commons.lang3.Validate.notBlank;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

public class LaunchServiceProvider extends Entity {
    private String name;

    private int yearFounded;

    private String country;

    private String headquarters;

    private Set<Rocket> rockets;

    public LaunchServiceProvider(String name, int yearFounded, String country) {
        Calendar date = Calendar.getInstance();
        int currentyear = date.get(Calendar.YEAR);
    	 if(name == null){
             throw new NullPointerException("name should not be null");
         }
         if(yearFounded < 1000 || yearFounded > currentyear){
             throw new IllegalArgumentException("The founded year is invalid");
         }
         if(country == null){
             throw new NullPointerException("country should not be null");
         }

    	
        this.name = name;
        this.yearFounded = yearFounded;
        this.country = country;

        
        rockets = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public int getYearFounded() {
        return yearFounded;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setHeadquarters(String headquarters) {
    	notBlank(headquarters, "headquarters cannot be null or empty");
        this.headquarters = headquarters;
    }

    public void setRockets(Set<Rocket> rockets) {
    	if(rockets == null) {
    		throw new NullPointerException("Rockets can't be null");
    	}
    	if(rockets.size()==0) {
    		throw new IllegalArgumentException("The rockets set should not be empty");
    	}
        this.rockets = rockets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider that = (LaunchServiceProvider) o;
        return yearFounded == that.yearFounded &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, yearFounded, country);
    }
}
