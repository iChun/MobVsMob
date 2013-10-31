package com.mobvsmob.common.scoreboard;

import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.HashMap;

public class ScoreboardHandler {

	private final Scoreboard scoreboard;

	public static final HashMap<Integer, ScoreboardHandler> scoreboardHandlers = new HashMap<Integer, ScoreboardHandler>();

	public ScoreboardHandler(int dimID) {
		if (scoreboardHandlers.containsKey(dimID)) throw new IllegalArgumentException("A scoreboard handler has already been registered for dimension" + dimID);
		else {
			this.scoreboard = MinecraftServer.getServer().worldServerForDimension(dimID).getScoreboard();
			scoreboardHandlers.put(dimID, this);
		}
	}

	/**
	 * Adds a player to a team
	 * @param player The player to be added to the team
	 * @param teamName Name of the team
	 */
	public void addToTeam(String player, String teamName) {
		ScorePlayerTeam scoreplayerteam = this.scoreboard.func_96508_e(teamName);
		if (scoreplayerteam == null) throw new ScoreboardException("scoreboard.team.notFound", teamName);
		else this.scoreboard.addPlayerToTeam(player, scoreplayerteam);
	}

	/**
	 * Gets the ScorePlayerTeam
	 * @param teamName Name of the team
	 * @return The ScorePlayerTeam, returns null if team doesn't exist
	 */
	public ScorePlayerTeam getTeam(String teamName) {
		return this.scoreboard.func_96508_e(teamName);
	}

	public void createTeam(String teamName) {
		this.createTeam(teamName, null);
	}

	/**
	 * Creates a team.
	 * @param teamName Name of the team
	 * @param teamDisplayName Display name of the team
	 */
	public void createTeam(String teamName, String teamDisplayName) {
		if (this.scoreboard.func_96508_e(teamName) != null) throw new ScoreboardException("scoreboard.team.alreadyExists", teamName);
		if (teamName.length() > 16) throw new ScoreboardException("scoreboard.team.name.tooLong", teamName);
		if (teamName.length() != 0) throw new ScoreboardException("scoreboard.team.name.tooShort");
		if (teamDisplayName == null) this.scoreboard.createTeam(teamName);
		else {
			if (teamDisplayName.length() > 32) throw new ScoreboardException("scoreboard.team.displayName.tooLong", teamName);
			if (teamDisplayName.length() == 0) throw new ScoreboardException("scoreboard.team.displayName.tooShort");
			else this.scoreboard.createTeam(teamName).setTeamName(teamDisplayName);
		}
	}

	/**
	 * Removes a team and all the members from that team
	 * @param teamName Name of the team
	 */
	public void removeTeam(String teamName) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		this.scoreboard.func_96511_d(scoreplayerteam);
	}

	/**
	 * Removes a player from the team if they are in it
	 * @param player The players name to be removed
	 * @param team The team the player should be removed from
	 */
	public void removePlayerFromTeam(String player, ScorePlayerTeam team) {
		this.scoreboard.removePlayerFromTeam(player, team);
	}

	public void removePlayerFromTeam(String player, String teamName) {
		ScorePlayerTeam team = this.scoreboard.func_96508_e(teamName);
		if (team == null) throw new ScoreboardException("scoreboard.team.notFound", teamName);
		else this.removePlayerFromTeam(player, team);
	}

	/**
	 * Removes the player from all teams
	 * @param player The players name to be removed
	 */
	public void removePlayerFromAllTeams(String player) {
		this.scoreboard.removePlayerFromTeams(player);
	}

	/**
	 * Empties a team of all members but doesn't remove the team
	 * @param teamName Name of the team
	 */
	public void emptyTeam(String teamName) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		if (scoreplayerteam.getMembershipCollection().isEmpty()) throw new ScoreboardException("scoreboard.team.empty", teamName);
		else {
			for (Object playerObject : scoreplayerteam.getMembershipCollection()) {
				String player = (String) playerObject;
				this.scoreboard.removePlayerFromTeam(player, scoreplayerteam);
			}
		}
	}

	/**
	 * Gets the a Collection of teams that exist
	 * @return A Collection of teams
	 */
	public Collection getTeamList() {
		Collection teamCollection = this.scoreboard.func_96525_g();
		if (teamCollection.size() > 0) return teamCollection;
		else return null;
	}

	/**
	 * Returns a Collection of the members of the team
	 * @param teamName Name of the team
	 * @return The Collection of names
	 */
	public Collection getTeamMembers(String teamName) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		Collection collection = scoreplayerteam.getMembershipCollection();
		if (collection.size() <= 0) throw new ScoreboardException("scoreboard.team.empty", teamName);
		else return collection;
	}

	/**
	 * Sets the teams colour prefix
	 * @param teamName Name of the team
	 * @param teamColour The team colour
	 */
	public void setTeamColorPrefix(String teamName, String teamColour) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		if (scoreplayerteam == null) throw new ScoreboardException("scoreboard.team.notFound", teamName);
		EnumChatFormatting enumchatformatting = EnumChatFormatting.func_96300_b(teamColour);
		scoreplayerteam.setNamePrefix(enumchatformatting.toString());
	}

	/**
	 * Sets the teams colour suffix
	 * @param teamName Name of the team
	 * @param teamColour The team colour
	 */
	public void setTeamColorSuffix(String teamName, String teamColour) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		if (scoreplayerteam == null) throw new ScoreboardException("scoreboard.team.notFound", teamName);
		EnumChatFormatting enumchatformatting = EnumChatFormatting.func_96300_b(teamColour);
		scoreplayerteam.setNameSuffix(enumchatformatting.toString());
	}

	/**
	 * Sets if the team can inflict friendly damage
	 * @param teamName Name of the team
	 * @param friendlyFire True to enable, false to disable
	 */
	public void setTeamFriendlyFire(String teamName, boolean friendlyFire) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		if (scoreplayerteam == null) throw new ScoreboardException("scoreboard.team.notFound", teamName);
		scoreplayerteam.setAllowFriendlyFire(friendlyFire);
	}

	/**
	 * Sets if the team can see invisible teammates
	 * @param teamName Name of the team
	 * @param friendlyInvisibles True to enable, false to disable
	 */
	public void setTeamSeeInvisibleFriendlies(String teamName, boolean friendlyInvisibles) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		if (scoreplayerteam == null) throw new ScoreboardException("scoreboard.team.notFound", teamName);
		scoreplayerteam.setSeeFriendlyInvisiblesEnabled(friendlyInvisibles);
	}

	/**
	 * Returns the ScoreObjective by that name
	 * @param objectiveName The name of the objective
	 * @param par2 If this is true, will only return the ScoreObjective if it's not read-only
	 * @return The ScoreObjective
	 */
	public ScoreObjective getScoreObjective(String objectiveName, boolean par2) {
		ScoreObjective scoreobjective = this.scoreboard.getObjective(objectiveName);
		if (scoreobjective == null) throw new ScoreboardException("scoreboard.objective.notFound", objectiveName);
		else if (par2 && scoreobjective.getCriteria().isReadOnly()) throw new ScoreboardException("scoreboard.objective.readOnly", objectiveName);
		else return scoreobjective;
	}

	/**
	 * Adds an objective based on the ScoreObjectiveCriteria
	 * @param objectiveName Name of the objective
	 * @param scoreObjectiveCriteria The ScoreObjectiveCriteria for the objective
	 * @param displayName Display name of the objective
	 */
	public void addObjective(String objectiveName, ScoreObjectiveCriteria scoreObjectiveCriteria, String displayName) {
		if (this.scoreboard.getObjective(objectiveName) != null) throw new ScoreboardException("scoreboard.objective.notFound", objectiveName);
		else if (objectiveName.length() > 16) throw new ScoreboardException("scoreboard.objective.name.tooLong", objectiveName);
		if (objectiveName.length() == 0) throw new ScoreboardException("scoreboard.objective.name.tooShort");
		if (displayName == null) scoreboard.func_96535_a(objectiveName, scoreObjectiveCriteria);
		else {
			if (displayName.length() > 32) throw new ScoreboardException("scoreboard.objective.displayName.tooLong", objectiveName);
			if (displayName.length() == 0) throw new ScoreboardException("scoreboard.objective.displayName.tooShort");
			this.scoreboard.func_96535_a(objectiveName, scoreObjectiveCriteria).setDisplayName(displayName);
		}
	}

	public void addObjective(String objectiveName, String scoreObjectiveCriteriaName, String displayName) {
		ScoreObjectiveCriteria scoreObjectiveCriteria = (ScoreObjectiveCriteria) ScoreObjectiveCriteria.field_96643_a.get(scoreObjectiveCriteriaName);
		if (scoreObjectiveCriteriaName == null) throw new ScoreboardException("scoreboard.objective.criteria.notFound", scoreObjectiveCriteriaName);
		else this.addObjective(objectiveName, scoreObjectiveCriteria, displayName);
	}

	public void addObjective(String objectiveName, String scoreObjectiveCriteriaName) {
		this.addObjective(objectiveName, scoreObjectiveCriteriaName, null);
	}

	public void addObjective(String objectiveName, ScoreObjectiveCriteria scoreObjectiveCriteria) {
		this.addObjective(objectiveName, scoreObjectiveCriteria, null);
	}

	/**
	 * Removes the ScoreObjective
	 * @param scoreObjective The ScoreObjective to remove
	 */
	public void removeObjective(ScoreObjective scoreObjective) {
		this.scoreboard.func_96519_k(scoreObjective);
	}

	public void removeObjective(String objectiveName) {
		ScoreObjective scoreObjective = this.getScoreObjective(objectiveName, false);
		if (scoreObjective == null) throw new ScoreboardException("scoreboard.objective.notFound", objectiveName);
		else this.removeObjective(scoreObjective);
	}

	/**
	 * Sets the players score for the ScoreObjective
	 * @param player The players name
	 * @param scoreObjectiveName The ScoreObjective name
	 * @param playerScore The new score
	 */
	public void setPlayerScore(String player, String scoreObjectiveName, int playerScore) {
		ScoreObjective scoreObjective = this.getScoreObjective(scoreObjectiveName, true);
		if (scoreObjective == null) throw new ScoreboardException("scoreboard.objective.notFound", scoreObjectiveName);
		Score score = this.scoreboard.func_96529_a(player, scoreObjective);
		if (score == null) throw new ScoreboardException("scoreboard.score.notFound", score);
		score.func_96647_c(playerScore);
	}

	/**
	 * Increases the players score for the ScoreObjective
	 * @param player The players name
	 * @param scoreObjectiveName The ScoreObjective name
	 * @param playerScore The increase in score
	 */
	public void increasePlayerScore(String player, String scoreObjectiveName, int playerScore) {
		ScoreObjective scoreObjective = this.getScoreObjective(scoreObjectiveName, true);
		if (scoreObjective == null) throw new ScoreboardException("scoreboard.objective.notFound", scoreObjectiveName);
		Score score = this.scoreboard.func_96529_a(player, scoreObjective);
		if (score == null) throw new ScoreboardException("scoreboard.score.notFound", score);
		score.func_96649_a(playerScore);
	}

	/**
	 * Decreases the players score for the ScoreObjective
	 * @param player The players name
	 * @param scoreObjectiveName The ScoreObjective name
	 * @param playerScore The cecrease in score
	 */
	public void decreasePlayerScore(String player, String scoreObjectiveName, int playerScore) {
		ScoreObjective scoreObjective = this.getScoreObjective(scoreObjectiveName, true);
		if (scoreObjective == null) throw new ScoreboardException("scoreboard.objective.notFound", scoreObjectiveName);
		Score score = this.scoreboard.func_96529_a(player, scoreObjective);
		if (score == null) throw new ScoreboardException("scoreboard.score.notFound", score);
		score.func_96646_b(playerScore);
	}

	/**
	 * Resets all the scores for the player
	 * @param player The players name
	 */
	public void resetPlayerScore(String player) {
		scoreboard.func_96515_c(player);
	}

	/**
	 * Returns a Collection of all the objectives
	 * @return A Collection of objectives
	 */
	public Collection getObjectivesList() {
		Collection collection = this.scoreboard.getScoreObjectives();
		if (collection.size() <= 0) throw new ScoreboardException("scoreboard.objective.noneExist");
		else return collection;
	}
}
