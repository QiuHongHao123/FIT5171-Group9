package rockets.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RocketUnitTest {
    private Rocket target;
  
    @BeforeEach
    public void setUp() {
        target=new Rocket("LM-9","China",new LaunchServiceProvider("CALT", 2000, "AES"));
    }
    @DisplayName("should throw exception when pass null param to construct function")
    @ParameterizedTest
    @MethodSource("rocketGenerator")
    public void shouldThrowExceptionWhenSetParamToNull(String name,String country,LaunchServiceProvider manufacturer){
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target=new Rocket(name, country,manufacturer ));
        assertEquals("param of construct function cannot be null", exception.getMessage());
    }
    static Stream<Arguments> rocketGenerator(){
        return Stream.of(Arguments.of(null, "USA",new LaunchServiceProvider("CALT", 2000, "AES")), Arguments.of("Zenit",null, new LaunchServiceProvider("CALT", 2000, "AES")), Arguments.of("OmegA", "USA",null));
    }

    @DisplayName("should throw exception when pass null to setMassToLEO function")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToLEO(null));
        assertEquals("massToLEO cannot be null or empty", exception.getMessage());
    }
    @DisplayName("should throw exception when pass an empty massToLEO address to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToLEOToEmpty(String massToLEO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToLEO(massToLEO));
        assertEquals("massToLEO cannot be null or empty", exception.getMessage());
    }
    @DisplayName("should throw exception when pass null to setMassToGTO function")
    @Test
    public void shouldThrowExceptionWhenSetMassToGTOToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToGTO(null));
        assertEquals("massToGTO cannot be null or empty", exception.getMessage());
    }
    @DisplayName("should throw exception when pass an empty massToGTO address to setMassToGTO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToGTOToEmpty(String massToGTO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToGTO(massToGTO));
        assertEquals("massToGTO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setMassToOther function")
    @Test
    public void shouldThrowExceptionWhenSetMassToOtherToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToOther(null));
        assertEquals("massToOther cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty massToOther address to setMassToOther function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToOtherToEmpty(String massToOther) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToOther(massToOther));
        assertEquals("massToOther cannot be null or empty", exception.getMessage());
    }
    @DisplayName("should return true when two rockets have the same infomation")
    @Test
    public void shouldReturnTrueWhenRocketsHaveSameINfo() {
        String name = "LM-9";
        String country="China";
        LaunchServiceProvider manufacturer=new LaunchServiceProvider("CALT", 2000, "AES");
        Rocket anotherRocket = new Rocket(name,country,manufacturer);
        assertTrue(target.equals(anotherRocket));
    }
    @DisplayName("should return true when two rockets have different infomation")
    @Test
    public void shouldReturnTrueWhenRocketsHaveDifferentINfo() {
        String name = "Saturn V";
        String country="USA";
        String manufacturer="NASA";
        Rocket anotherRocket = new Rocket(name,country,new LaunchServiceProvider("CALT", 2000, "China"));
        assertFalse(target.equals(anotherRocket));
    }
    @DisplayName("should return true when The relationship between GTO and LEO meets the standard ")
    @ParameterizedTest
    @MethodSource("LEOGTOGeneratorOne")
    public void shouldReturnTrueWhenMeetTheStandard(String leo,String gto){
        target.setMassToLEO(leo);
        target.setMassToGTO(gto);
        assertTrue(target.gtoAndLeoStandard());
    }
    static Stream<Arguments> LEOGTOGeneratorOne(){
        return Stream.of(Arguments.of("16500", "6500"), Arguments.of("34900","16300"), Arguments.of("140000", "66000"));
    }
    @DisplayName("should return false when The relationship between GTO and LEO doesn't meet the standard GTO=(1/2)*LEO*(1+/-30%)")
    @ParameterizedTest
    @MethodSource("LEOGTOGeneratorTwo")
    public void shouldReturnFalseWhenNotMeetTheStandard(String leo,String gto){
        target.setMassToLEO(leo);
        target.setMassToGTO(gto);
        assertFalse(target.gtoAndLeoStandard());
    }
    static Stream<Arguments> LEOGTOGeneratorTwo(){
        return Stream.of(Arguments.of("16500", "none"), Arguments.of("34900","24430"), Arguments.of("140000", "-46000"));
    }
    @DisplayName("should return the correct name of the family of the rocket")
    @ParameterizedTest
    @MethodSource("nameAndFamilyGenerator")
    public void shouldReturnFamilyOfRocket(String name,String family){
        target=new Rocket(name,"USA",new LaunchServiceProvider("CALT", 2000, "China"));
        assertEquals(family,target.belongToFamily());
    }
    static Stream<Arguments> nameAndFamilyGenerator(){
        return Stream.of(Arguments.of("Delta V", "Delta"), Arguments.of("Falcon 9","Falcon"), Arguments.of("Ariane 5[8]", "Ariane"));
    }
    @AfterAll
    public static void tearDown(){
        System.out.println("Tests finish.");
    }
}