package rockets.mining;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.Launch.LaunchOutcome;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();

        lsps = Arrays.asList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe "),
                new LaunchServiceProvider("ULA", 2002, "USA"),
                new LaunchServiceProvider("ESA", 2002, "Europe "),
                new LaunchServiceProvider("CALT", 2002, "China ")
        );

        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};
        // outcome of each launch
        LaunchOutcome[] outcome = new LaunchOutcome[]{
                LaunchOutcome.SUCCESSFUL, LaunchOutcome.SUCCESSFUL, LaunchOutcome.SUCCESSFUL, LaunchOutcome.SUCCESSFUL, LaunchOutcome.FAILED,
                LaunchOutcome.FAILED, LaunchOutcome.FAILED, LaunchOutcome.FAILED, LaunchOutcome.SUCCESSFUL, LaunchOutcome.SUCCESSFUL
        };

        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchServiceProvider(rockets.get(rocketIndex[i]).getManufacturer());
            l.setLaunchSite("VAFB");
            l.setLaunchOutcome(outcome[i]);
            l.setOrbit("LEO");
            l.setPrice(new BigDecimal(38000000.056));
            spy(l);
            return l;
        }).collect(Collectors.toList());
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }


    @DisplayName("should return most top most LaunchedRockets")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostLaunchedRockets(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> alllaunches = new ArrayList<>(launches);
        Map<Rocket, List<Launch>> rockets = alllaunches.stream().
                collect(Collectors.groupingBy(Launch::getLaunchVehicle));
        Map<Rocket, Integer> numOfRockets = new HashMap<Rocket, Integer>();
        rockets.forEach((key, value) -> {
            numOfRockets.put(key, value.size());
        });

        List<Map.Entry<Rocket, Integer>> numOfRocketsList = new ArrayList<Map.Entry<Rocket, Integer>>(numOfRockets.entrySet());
        List<Rocket> mostLaunchedRockets = numOfRocketsList.stream().
                sorted(new Comparator<Map.Entry<Rocket, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Rocket, Integer> o1, Map.Entry<Rocket, Integer> o2) {
                        return o1.getValue()-o2.getValue();
                    }
                }).map(Map.Entry::getKey).limit(k).collect(Collectors.toList());

        List<Rocket> launchedRockets = miner.mostLaunchedRockets(k);
        assertEquals(k, launchedRockets.size());
        assertEquals(mostLaunchedRockets.subList(0, k), launchedRockets);
    }

    @DisplayName("should return most reliable launch service providers")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnMostReliableLaunchServiceProviders(int k){
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        List<LaunchServiceProvider> loadedLaunchServiceProviders = miner.mostReliableLaunchServiceProviders(k);

        Map<LaunchServiceProvider,Integer> successLaunch=new HashMap<LaunchServiceProvider,Integer>();
        Map<LaunchServiceProvider,Integer> totalLaunch=new HashMap<LaunchServiceProvider,Integer>();
        launches.forEach(l->{
            if(!successLaunch.containsKey(l.getLaunchServiceProvider())){
                successLaunch.put(l.getLaunchServiceProvider(),0);
                totalLaunch.put(l.getLaunchServiceProvider(),0);
            }
            totalLaunch.put(l.getLaunchServiceProvider(),totalLaunch.get(l.getLaunchServiceProvider())+1);
            if(l.getLaunchOutcome()==LaunchOutcome.SUCCESSFUL) {
                successLaunch.put(l.getLaunchServiceProvider(),successLaunch.get(l.getLaunchServiceProvider())+1);
            }
        });
        Map<LaunchServiceProvider,Float> successRate=new HashMap<LaunchServiceProvider,Float>();
        for(Entry<LaunchServiceProvider, Integer> entry: totalLaunch.entrySet()) {
            successRate.put(entry.getKey(),new Float(successLaunch.get(entry.getKey())/entry.getValue()));
        }
        Comparator<Map.Entry<LaunchServiceProvider,Float>> rocketLaunchNumComparator = (a, b) -> a.getValue().compareTo(b.getValue());
        List<Map.Entry<LaunchServiceProvider,Float>> list = new ArrayList<Map.Entry<LaunchServiceProvider,Float>>(successRate.entrySet());
        Collections.sort(list, rocketLaunchNumComparator);
        List<LaunchServiceProvider> result=new ArrayList<LaunchServiceProvider>();
        list.stream().sorted(rocketLaunchNumComparator).limit(k).forEach(l->{
            result.add(l.getKey());
        });
        assertEquals(k, loadedLaunchServiceProviders.size());
        assertEquals(result, loadedLaunchServiceProviders);
    }
    @DisplayName("Returns the dominant country who has the most launched rockets in an orbit.")
    @ParameterizedTest
    @ValueSource(strings = {"USA", "Europe "})
    public void shouldReturnDominantCountry(String orbit){
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        HashMap<String,Integer> cur = new HashMap<>();
        launches.forEach(l->{
            String temp = l.getLaunchServiceProvider().getCountry();
            if(orbit.equals(l.getOrbit()))
                cur.put(temp,cur.getOrDefault(temp,0)+1);
        });
        String ans = "";
        int ansCount = 0;
        for(String county:cur.keySet()){
            if(cur.get(county) > ansCount){
                ansCount = cur.get(county);
                ans = county;
            }
        }
        String testResult = miner.dominantCountry(orbit);
        assertEquals(testResult,ans);

    }
    @DisplayName("should return top expensive launches")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostExpensiveLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -Double.valueOf(a.getPrice().doubleValue()).compareTo(Double.valueOf(b.getPrice().doubleValue())));
        List<Launch> loadedLaunches = miner.mostExpensiveLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }
    @DisplayName("should return top highest sales revenue in a year")
    @ParameterizedTest
    @MethodSource("salesGenerator")
    public void shouldReturnTopHighestSalesRevenueInAyear(int k,int year) {
        when(dao.loadAll(LaunchServiceProvider.class)).thenReturn(lsps);
        List<LaunchServiceProvider> sortedLsps = new ArrayList<>();
        for(LaunchServiceProvider temp:lsps){
            if(temp.getYearFounded()==year){
                sortedLsps.add(temp);
            }
        }
        sortedLsps.sort((a, b) -> -Integer.valueOf(a.getRockets().size()).compareTo(Integer.valueOf(b.getRockets().size())));
        List<LaunchServiceProvider> loadedLaunches = miner.highestRevenueLaunchServiceProviders(k,year);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLsps.subList(0, k), loadedLaunches);
    }
    static Stream<Arguments> salesGenerator(){
        return Stream.of(Arguments.of(1,2002), Arguments.of(2,2002), Arguments.of(3,2002));
    }

}