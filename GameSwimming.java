package application;
// work by Tao Lei s3552269  ass01
//work by Hongyi Yan s3521449 ass02

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSwimming extends Game {
	GameResultJDBCDao gameJDBCDao = new GameResultJDBCDao();
	GameResultFileDao gameFileDao = new GameResultFileDao();
	ParticipantJDBCDao participantJDBCDao = new ParticipantJDBCDao();
	ParticipantFileDao participantFileDao = new ParticipantFileDao();

	public List<Game> generateScore(List<Participant> participantList) throws NumberFormatException {

		List<Game> gameSwimmingList = new ArrayList();
		GenerateTimeStamp generateTimeStamp = new GenerateTimeStamp();
		String timeString = generateTimeStamp.getNowDate();
		for (int j = 0; j < participantList.size(); j++) {
			Game gameSwimming = new GameSwimming();
			try {
				gameSwimming.setScore(((Swimmer) participantList.get(j)).compete(gameSwimming));
			} catch (ClassCastException e) {
				gameSwimming.setScore(new Athlete().compete(gameSwimming));
			}
			gameSwimming.setGameID(this.gentrateGameID());
			gameSwimming.setAthlethID(participantList.get(j).getID());
			gameSwimming.setTimeStamp(timeString);
			gameSwimmingList.add(gameSwimming);
		}
		return gameSwimmingList;
	}

	public String gentrateGameID() throws NumberFormatException {
		int maxNumber = -1;
		List<Game> gameIDList = null;
		try {
			gameIDList = gameJDBCDao.selectGameGameID();
		} catch (SQLException e) {
			gameIDList = gameFileDao.readFileByLines();
		}
		if (gameIDList == null || gameIDList.size() == 0) {
			return "S0" + 1;
		}
		for (int i = 0; i < gameIDList.size(); i++) {
			String[] gameIDSplit = gameIDList.get(i).getGameID().split("0");
			if (gameIDSplit[0].toString().equals("S")) {
				if (maxNumber < Integer.parseInt(gameIDSplit[1])) {
					maxNumber = Integer.parseInt(gameIDSplit[1]);
				}
			}
		}
		maxNumber += 1;
		if (maxNumber == -1 || maxNumber == 0) {
			return "S0" + 1;
		}
		return "S0" + maxNumber;
		// TODO Auto-generated method stub

	}

	@Override
	public String generatePoint(List<Participant> participantList) {
		// select offical
		Participant offical = new Official();
		offical.setType("Official");
		String resultGameID = null;
		List<Game> gameSwimmingList = null;
		List<Game> gameSwimmingWritingFileList = new ArrayList();
		int indexOffical = -1;
		int firstPlace = 0;
		int secondPlace = 0;
		int thirdPlace = 0;
		String officalID = null;
		for (int i = 0; i < participantList.size(); i++) {
			// System.out.println(participantList.get(i).getType().toString().equals("Official"));
			// System.out.println(participantList.get(i).getType().equals("Official"));
			if (participantList.get(i).getType().toString().equals("Official")) {

				indexOffical = i;
				officalID = participantList.get(i).getID();
			}
		}
		participantList.remove(indexOffical);
		gameSwimmingList = generateScore(participantList);

		for (int i = 0; i < 3; i++) {
			double minTime = 1000000;
			int minIndex = -1;
			for (int j = 0; j < gameSwimmingList.size(); j++) {
				gameSwimmingList.get(j).setOfficalID(officalID);
				if (gameSwimmingList.get(j).getResult() == 0) {
					if (gameSwimmingList.get(j).getScore() < minTime) {
						minTime = gameSwimmingList.get(j).getScore();
						minIndex = j;
					}
				}
			}
			if (i == 0) {
				firstPlace = minIndex;
				gameSwimmingList.get(minIndex).setResult(5);
				gameSwimmingWritingFileList.add(gameSwimmingList.get(minIndex));
				resultGameID = gameSwimmingList.get(minIndex).getAthlethID();

			}
			if (i == 1) {
				secondPlace = minIndex;
				gameSwimmingWritingFileList.add(gameSwimmingList.get(minIndex));
				gameSwimmingList.get(minIndex).setResult(2);
			}
			if (i == 2) {
				thirdPlace = minIndex;
				gameSwimmingWritingFileList.add(gameSwimmingList.get(minIndex));
				gameSwimmingList.get(minIndex).setResult(1);
			}

		}

		// TODO Auto-generated catch block
		for (int i = 0; i < gameSwimmingList.size(); i++) {
			if (i == firstPlace || i == secondPlace || i == thirdPlace) {
				continue;
			}
			gameSwimmingWritingFileList.add(gameSwimmingList.get(i));
		}

		try {
			gameJDBCDao.insertGameresults(gameSwimmingWritingFileList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultGameID;
	}
}