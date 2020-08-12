package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.Launch.LaunchOutcome;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.util.ArrayList;
//import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {
    	logger.info("find most " + k + " launched rockets");
 
    	Map<Rocket,Integer> rocketLaunched=eachRocketLaunchednum();
    	Comparator<Map.Entry<Rocket,Integer>> rocketLaunchNumComparator = (a, b) -> a.getValue().compareTo(b.getValue());
    	List<Map.Entry<Rocket,Integer>> list = new ArrayList<Map.Entry<Rocket,Integer>>(rocketLaunched.entrySet());
    	Collections.sort(list, rocketLaunchNumComparator);
    	List<Rocket> result=new ArrayList<Rocket>();
    	list.stream().sorted(rocketLaunchNumComparator).limit(k).forEach(l->{
    		result.add(l.getKey());
    	});
        return result;
    }
    //Return a map, which list each rocket launched amount 
    public Map<Rocket,Integer> eachRocketLaunchednum(){
    	Collection<Launch> lauchedcollection=dao.loadAll(Launch.class);
    	Map<Rocket,Integer> rocketLaunched=new HashMap<Rocket,Integer>();
    	lauchedcollection.forEach(launch->{
    		if(rocketLaunched.containsKey(launch.getLaunchVehicle())) {
    			rocketLaunched.put(launch.getLaunchVehicle(),rocketLaunched.get(launch.getLaunchVehicle())+1);
    		}
    		else {rocketLaunched.put(launch.getLaunchVehicle(),1);}
    	});
    	return rocketLaunched;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
    	logger.info("find most " + k + " reliable launch service providers");
        Map<LaunchServiceProvider,Float> successRate= eachLauchServiceLaunchedSuccessrate();
        
        Comparator<Map.Entry<LaunchServiceProvider,Float>> rocketLaunchNumComparator = (a, b) -> a.getValue().compareTo(b.getValue());
    	List<Map.Entry<LaunchServiceProvider,Float>> list = new ArrayList<Map.Entry<LaunchServiceProvider,Float>>(successRate.entrySet());
    	Collections.sort(list, rocketLaunchNumComparator);
    	List<LaunchServiceProvider> result=new ArrayList<LaunchServiceProvider>();
    	list.stream().sorted(rocketLaunchNumComparator).limit(k).forEach(l->{
    		result.add(l.getKey());
    	});
		return result;
        
    }
    //return the success rate of each provider  
    public Map<LaunchServiceProvider,Float> eachLauchServiceLaunchedSuccessrate(){
    	Collection<Launch> launches = dao.loadAll(Launch.class);
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
        return successRate;
    	
    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */
    public String dominantCountry(String orbit) { return null;}

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
        return Collections.emptyList();
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) {
        return Collections.emptyList();
    }
}
