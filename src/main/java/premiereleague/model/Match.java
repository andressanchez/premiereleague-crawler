package premiereleague.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Match
 * @author Andrés Sánchez
 */

@Entity("premiereleague")
public class Match
{
    @Id
    private UUID id;

    // Always present
    private Long timestamp;
    private String homeTeam;
    private String awayTeam;
    private Integer homeScore;
    private Integer awayScore;

    // Goals
    @Embedded
    private List<Goal> homeGoals;
    @Embedded
    private List<Goal> awayGoals;

    // Statistics
    @Property("homeStatistics")
    private HashMap<String, Integer> homeStatistics = new HashMap<>();
    @Property("awayStatistics")
    private HashMap<String, Integer> awayStatistics = new HashMap<>();
    @Property("totalStatistics")
    private HashMap<String, Integer> totalStatistics = new HashMap<>();

    /**
     * Create a new match
     * @param id UUID
     * @param timestamp Timestamp in milliseconds
     * @param homeTeam Name of the home team
     * @param awayTeam Name of the away team
     * @param homeScore Number of goals scored by the home team
     * @param awayScore Number of goals scored by the away team
     * @see Match(Long, String, String, Integer, Integer)
     */
    public Match(UUID id, Long timestamp, String homeTeam, String awayTeam, Integer homeScore, Integer awayScore) {
        this.id = id;
        this.timestamp = timestamp;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore =  awayScore;
        this.homeGoals = new LinkedList<>();
        this.awayGoals = new LinkedList<>();
    }

    /**
     * Create a new match
     * @param timestamp Timestamp in milliseconds
     * @param homeTeam Name of the home team
     * @param awayTeam Name of the away team
     * @param homeScore Number of goals scored by the home team
     * @param awayScore Number of goals scored by the away team
     */
    public Match(Long timestamp, String homeTeam, String awayTeam, Integer homeScore, Integer awayScore) {
        this(UUID.randomUUID(), timestamp, homeTeam, awayTeam, homeScore, awayScore);
    }

    /**
     * Add a new goal
     * @param player Name of the player who scored this goal
     * @param team Player's team
     * @param minute Minute in which the goal was scored
     */
    public void addHomeGoal(String player, String team, Integer minute) {
        homeGoals.add(new Goal(player, team, minute));
    }

    /**
     * Add a new goal
     * @param player Name of the player who scored this goal
     * @param team Player's team
     * @param minute Minute in which the goal was scored
     */
    public void addAwayGoal(String player, String team, Integer minute) {
        awayGoals.add(new Goal(player, team, minute));
    }

    /**
     * Add a new home statistic
     * @param name Name
     * @param value Correspondent value
     */
    public void addHomeStatistic(String name, Integer value) {
        homeStatistics.put(name, value);
    }

    /**
     * Add a new away statistic
     * @param name Name
     * @param value Correspondent value
     */
    public void addAwayStatistic(String name, Integer value) {
        awayStatistics.put(name, value);
    }

    /**
     * Add a new total statistic
     * @param name Name
     * @param value Correspondent value
     */
    public void addTotalStatistic(String name, Integer value) {
        totalStatistics.put(name, value);
    }
}
