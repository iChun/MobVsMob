package com.mobvsmob.common.scoreboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.ArrayList;
import java.util.List;

public class TeamHelper {

	/**
	 * This will evenly split the players provided across the teams. It will not clear teams so can be used to add new players to existing teams.
	 * It does NOT randomise the players provided, it will find the first legible team to place them in. This will try to balance out the new players
	 * so team sizes are as even as possible. It will NOT check if a player is currently already in a team
	 * @param dimID The dimension ID
	 * @param teams A list of teams player should be split over
	 * @param players A list of players that need teams
	 */
	public static void splitPlayersEvenly(int dimID, List<String> teams, int maxTeamSize, EntityPlayer ... players) {
		ScoreboardHandler scoreboardHandler = ScoreboardHandler.scoreboardHandlers.get(dimID);
		//TODO Should this create the handler if one doesn't exist?
		if (scoreboardHandler == null) throw new ScoreboardException("A scoreboard handler does not exist for the dimension " + dimID);
		//Remove teams who are already full or over capacity
		for (String teamName:teams) {
			if (scoreboardHandler.getTeamMembers(teamName).size() >= maxTeamSize) teams.remove(teamName);
		}
		for (EntityPlayer player:players) {
		 	//Get the smallest team
			String smallestTeam = teams.get(0);
			for (String teamName:teams) {
				if ((scoreboardHandler.getTeamMembers(teamName).size() < maxTeamSize) && (scoreboardHandler.getTeamMembers(teamName).size() < scoreboardHandler.getTeamMembers(smallestTeam).size())) smallestTeam = teamName;
			}
			//If we reach max size on the smallest team, throw an exception as we don't want to overfill teams
			if (scoreboardHandler.getTeamMembers(smallestTeam).size() > maxTeamSize) {
				throw new ScoreboardException("Reached max size for all teams when trying to add players to a team!");
			}
			scoreboardHandler.addToTeam(player.username, smallestTeam);
		}
	}

	/**
	 * Returns a List of all the teams the player is currently in
	 * @param dimID The dimension for the scoreboard
	 * @param player The players name
	 * @return A List of teams
	 */
	public static List<String> getPlayersTeams(int dimID, String player) {
		ScoreboardHandler scoreboardHandler = ScoreboardHandler.scoreboardHandlers.get(dimID);
		//TODO Should this create the handler if one doesn't exist?
		if (scoreboardHandler == null) throw new ScoreboardException("A scoreboard handler does not exist for the dimension " + dimID);
		List<String> teamList = new ArrayList<String>();
		for (Object obj:scoreboardHandler.getTeamList()) {
			ScorePlayerTeam team = (ScorePlayerTeam) obj;
			for (Object teamMember:team.getMembershipCollection()) {
				if (teamMember.equals(teamMember)) teamList.add(team.func_96661_b());
			}
		}
		return teamList;
	}
}
