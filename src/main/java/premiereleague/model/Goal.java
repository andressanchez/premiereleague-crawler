package premiereleague.model;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Goal
 * @author Andrés Sánchez
 */

@Embedded
public class Goal
{
    private String player;
    private String team;
    private Integer minute;

    /**
     * Create a new goal
     * @param player Name of the player who scored this goal
     * @param team Player's team
     * @param minute Minute in which the goal was scored
     */
    public Goal(String player, String team, Integer minute) {
        this.player = player;
        this.team = team;
        this.minute = minute;
    }

    /**
     * Get the name of the player who scored this goal
     * @return The name of the player
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Get the name of the team the player plays for
     * @return The name of the team
     */
    public String getTeam() {
        return team;
    }

    /**
     * Get the minute in which the goal was scored
     * @return The minute in which the goal was scored
     */
    public Integer getMinute() {
        return minute;
    }
}
