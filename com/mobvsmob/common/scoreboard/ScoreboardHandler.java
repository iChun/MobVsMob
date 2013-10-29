package com.mobvsmob.common.scoreboard;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.Iterator;

public class ScoreboardHandler {

	public final int dimensionID;
	private final Scoreboard scoreboard;

	public ScoreboardHandler(int dimID) {
		this.dimensionID = dimID;
		this.scoreboard = MinecraftServer.getServer().worldServerForDimension(dimID).getScoreboard();
	}

	/**
	 * Adds a player to a team
	 * @param player The player to be added to the team
	 * @param teamName Name of the team
	 */
	public void addToTeam(EntityPlayer player, String teamName) {
		ScorePlayerTeam scoreplayerteam = this.scoreboard.func_96508_e(teamName);
		if (scoreplayerteam != null) this.scoreboard.addPlayerToTeam(player.username, scoreplayerteam);
		else throw new ScoreboardException("The team " + teamName + " doesn't exist!");
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
		if (this.scoreboard.func_96508_e(teamName) != null) throw new ScoreboardException("The team " + teamName + " already exists!");
		if ((teamName.length() < 17) && (teamName.length() != 0)) {
			if (teamDisplayName == null) this.scoreboard.createTeam(teamName);
			else {
				if ((teamDisplayName.length() > 32) || (teamDisplayName.length() == 0)) throw new ScoreboardException("The team " + teamName + " display name is the incorrect length!");
				else this.scoreboard.createTeam(teamName).setTeamName(teamDisplayName);
			}
		}
		else throw new ScoreboardException("The team " + teamName + " name is the incorrect length!");
	}

	/**
	 * Removes a team and all the members from that team
	 * @param teamName Name of the team
	 */
	public void removeTeam(String teamName) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		this.scoreboard.func_96511_d(scoreplayerteam);
	}

	public void removePlayerFromTeam(EntityPlayer player, String teamName) {
		ScorePlayerTeam team = this.scoreboard.func_96508_e(teamName);
		this.removePlayerFromTeam(player, team);
	}

	/**
	 * Removes a player from the team if they are in it
	 * @param player The player to be removed
	 * @param team The team the player should be removed from
	 */
	public void removePlayerFromTeam(EntityPlayer player, ScorePlayerTeam team) {
		this.scoreboard.removePlayerFromTeam(player.username, team);
	}

	/**
	 * Empties a team of all members but doesn't remove the team
	 * @param teamName Name of the team
	 */
	public void emptyTeam(String teamName) {
		ScorePlayerTeam scoreplayerteam = this.getTeam(teamName);
		if (!scoreplayerteam.getMembershipCollection().isEmpty()) {
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
		Collection teamCollection = scoreboard.func_96525_g();
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
		if (collection.size() <= 0) return null;
		else return collection;
	}
}
