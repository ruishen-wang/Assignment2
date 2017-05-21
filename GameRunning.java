package application;

// work by Tao Lei s3552269  ass01
// updated by Hongyi Yan s3521449 ass02

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameRunning extends Game {
	GameResultJDBCDao gameJDBCDao = new GameResultJDBCDao();
	GameResultFileDao gameFileDao = new GameResultFileDao();
	ParticipantJDBCDao participantJDBCDao = new ParticipantJDBCDao();
	ParticipantFileDao participantFileDao = new ParticipantFileDao();

	@Override
	public List<Game> generateScore(List<Participant> participantList) throws NumberFormatException {
		List<Game> gameRunningList = new ArrayList();
		GenerateTimeStamp generateTimeStamp = new GenerateTimeStamp();
		String timeString = generateTimeStamp.getNowDate();
		for (int j = 0; j < participantList.size(); j++) {
			Game gameRunning = new GameRunning();
			try {
				gameRunning.setScore(((Sprinter) participantList.get(j)).compete(gameRunning));
			} catch (ClassCastException e) {
				gameRunning.setScore(new Athlete().compete(gameRunning));
			}
			gameRunning.setGameID(this.gentrateGameID());
			gameRunning.setAthlethID(participantList.get(j).getID());
			gameRunning.setTimeStamp(timeString);
			gameRunningList.add(gameRunning);
		}
		return gameRunningList;
	}

	public String gentrateGameID() throws NumberFormatException {
		int maxNumber = -1;
		List<Game> gameIDList;
		try {
			gameIDList = gameJDBCDao.selectGameGameID();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			gameIDList = gameFileDao.readFileByLines();
			e.printStackTrace();
		}
		if (gameIDList == null || gameIDList.size() == 0) {
			return "R0" + 1;
		}
		for (int i = 0; i < gameIDList.size(); i++) {
			String[] gameIDSplit = gameIDList.get(i).getGameID().split("0");
			if (gameIDSplit[0].toString().equals("R")) {
				if (maxNumber < Integer.parseInt(gameIDSplit[1])) {
					maxNumber = Integer.parseInt(gameIDSplit[1]);
				}
			}
		}
		maxNumber += 1;
		if (maxNumber == -1 || maxNumber == 0) {
			return "R0" + 1;
		}
		return "R0" + maxNumber;

	}

	@Override
	public String generatePoint(List<Participant> participantList) {
		// select offical
		Participant offical = new Official();
		offical.setType("Official");
		String resultGameID = null;
		List<Game> gameRunningList = null;
		List<Game> gameRunningWritingFileList = new ArrayList();
		int firstPlace = 0;
		int secondPlace = 0;
		int thirdPlace = 0;
		int indexOffical = -1;
		List<Participant> officalList = null;

		String officalID = null;
		try {
			for (int i = 0; i < participantList.size(); i++) {
				if (participantList.get(i).getType().toString().equals("Official")) {
					indexOffical = i;
					officalID = participantList.get(i).getID();
				}
			}
			participantList.remove(indexOffical);
			gameRunningList = generateScore(participantList);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int k = 0; k < 3; k++) {
			double minTime = 1000000;
			int minIndex = -1;
			for (int j = 0; j < gameRunningList.size(); j++) {
				gameRunningList.get(j).setOfficalID(officalID);
				if (gameRunningList.get(j).getResult() == 0) {
					if (gameRunningList.get(j).getScore() < minTime) {
						minTime = gameRunningList.get(j).getScore();
						minIndex = j;
					}
				}
			}
			if (k == 0) {
				gameRunningList.get(minIndex).setResult(5);
				gameRunningWritingFileList.add(gameRunningList.get(minIndex));
				firstPlace = minIndex;
				resultGameID = gameRunningList.get(minIndex).getAthlethID();
			}
			if (k == 1) {
				secondPlace = minIndex;
				gameRunningWritingFileList.add(gameRunningList.get(minIndex));
				gameRunningList.get(minIndex).setResult(2);
			}
			if (k == 2) {
				thirdPlace = minIndex;
				gameRunningWritingFileList.add(gameRunningList.get(minIndex));
				gameRunningList.get(minIndex).setResult(1);
			}

		}
		// TODO Auto-generated catch block
		for (int i = 0; i < gameRunningList.size(); i++) {
			if (i == firstPlace || i == secondPlace || i == thirdPlace) {
				continue;
			}
			gameRunningWritingFileList.add(gameRunningList.get(i));
		}
		try {
			gameJDBCDao.insertGameresults(gameRunningWritingFileList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultGameID;
	}
}