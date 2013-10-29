package com.mobvsmob.common.scoreboard;

//TODO Make this class useful
public class ScoreboardException extends RuntimeException {

	private Object[] errorObjects;

	public ScoreboardException(String exception, Object ... args) {
		super(exception);
		this.errorObjects = args;
	}

	public Object[] getErrorOjbects() {
		return this.errorObjects;
	}
}
