package rockets.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LaunchServiceProviderTest {

	@DisplayName("should threw exception when set name to null")
    @Test
    public void shouldThrewExceptionWhenSetNameToNull(){
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new LaunchServiceProvider(null, 2020, "CN"));
        assertEquals("name should not be null", exception.getMessage());
    }
	@DisplayName("should threw exception when set country to null")
    @Test
    public void shouldThrewExceptionWhenSetCountryToNull(){
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new LaunchServiceProvider("Qiu", 2020, null));
        assertEquals("country should not be null", exception.getMessage());
    }
	@DisplayName("should threw exception when set year invalid")
    @Test
    public void shouldThrewExceptionWhenSetYearInvalid(){
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new LaunchServiceProvider("Qiu", 2222, "CN"));
        assertEquals("The founded year is invalid", exception.getMessage());
    }
	
	@DisplayName("should threw exception when set country to null")
    @Test
    public void shouldThrewExceptionWhenSetHeadquartersToNull(){
		LaunchServiceProvider target=new LaunchServiceProvider("qiu",2000,"CN");
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setHeadquarters(null));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }
	@DisplayName("should throw exception when pass a empty Headquarters address to setHeadquarters function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetHeadquartersToEmpty(String headquarters) {
		LaunchServiceProvider target=new LaunchServiceProvider("qiu",2000,"CN");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setHeadquarters(headquarters));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }
	@DisplayName("should threw exception when set rockets to null")
    @Test
    public void shouldThrewExceptionWhenSetRocketsToNull(){
		LaunchServiceProvider target=new LaunchServiceProvider("qiu",2000,"CN");
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setRockets(null));
        assertEquals("Rockets can't be null", exception.getMessage());
    }
	@DisplayName("should threw exception when set rockets to null")
    @Test
    public void shouldThrewExceptionWhenSetRocketsToEmpty(){
		Set test = new HashSet();
		LaunchServiceProvider target=new LaunchServiceProvider("qiu",2000,"CN");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setRockets(test));
        assertEquals("The rockets set should not be empty", exception.getMessage());
    }

    @DisplayName("should return true when two launch service providers have same name, year and country")
    @Test
    public void shouldReturnTrueWhenLaunchServiceProvidersHaveSameNameYearFoundedCountry(){
        LaunchServiceProvider test1 = new LaunchServiceProvider("qiu", 2000, "CN");
        LaunchServiceProvider test2 = new LaunchServiceProvider("qiu", 2000, "CN");
        assertTrue(test1.equals(test2));
    }

    @DisplayName("should return false when two launch service providers have different name")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProvidersHaveDifferentName(){
        LaunchServiceProvider test1 = new LaunchServiceProvider("qiu", 2000, "CN");
        LaunchServiceProvider test2 = new LaunchServiceProvider("si", 2000, "CN");
        assertFalse(test1.equals(test2));
    }

    @DisplayName("should return false when two launch service providers have different year ")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProvidersHaveDifferentYearFounded(){
        LaunchServiceProvider test1 = new LaunchServiceProvider("qiu", 2000, "CN");
        LaunchServiceProvider test2 = new LaunchServiceProvider("qiu", 2020, "CN");
        assertFalse(test1.equals(test2));
    }

    @DisplayName("should return false when two launch service providers have different country")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProvidersHaveDifferentCountry(){
        LaunchServiceProvider test1 = new LaunchServiceProvider("qiu", 2000, "CN");
        LaunchServiceProvider test2 = new LaunchServiceProvider("qiu", 2000, "JAP");
        assertFalse(test1.equals(test2));
    }
}
