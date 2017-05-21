package application;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

// work by Tao Lei s3552269  ass01 
//  updated by Hongyi Yan s3521449 ass02

class Game {
	GameResultJDBCDao gameJDBCDao = new GameResultJDBCDao();
	GameResultFileDao gameFileDao = new GameResultFileDao();
	ParticipantJDBCDao participantJDBCDao = new ParticipantJDBCDao();
	ParticipantFileDao participantFileDao = new ParticipantFileDao();
	private String gameID;
	private String officalID;
	private String officalName;
	private String timeStamp;
	private String athlethID;

	public Game() {
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getOfficalID() {
		return officalID;
	}
	
	public String getOfficalName() {
		return officalName;
	}

	public void setOfficalID(String officalID) {
		this.officalID = officalID;
	}
	
	public void setOfficalName(String officalName) {
		this.officalName = officalName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAthlethID() {
		return athlethID;
	}

	public void setAthlethID(String athlethID) {
		this.athlethID = athlethID;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	private int result;
	private double score;

	

	public List<Game> generateScore(List<Participant> participant) throws NumberFormatException, SQLException {
		return null;
	};

	public String generatePoint(List<Participant> participant) {
		return null;
	};

	public List<Game> displayScoreAndResult() {
		List<Game> gameList = null;
		try {
			gameList = gameJDBCDao.selectGameContidion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			gameList = gameFileDao.readFileByLines();
			e.printStackTrace();
		}
		return gameList;
	}
}