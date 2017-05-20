package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// work by Tao Lei s3552269  ass01
//work by Hongyi Yan s3521449ass02
// the explain of gameCycling gameSwimming and gameRunning are all in gameSwimming 
//because these three are similar
public class GameCycling extends Game {

	GameResultJDBCDao gameJDBCDao = new GameResultJDBCDao();
	GameResultFileDao gameFileDao = new GameResultFileDao();
	ParticipantJDBCDao participantJDBCDao = new ParticipantJDBCDao();
	ParticipantFileDao participantFileDao = new ParticipantFileDao();

	@Override
	public List<Game> generateScore(List<Participant> participantList) throws NumberFormatException, SQLException {
		List<Game> gameCyclingList = new ArrayList();
		GenerateTimeStamp generateTimeStam = new GenerateTimeStamp();
		String timeString = generateTimeStam.getNowDate();
		for (int j = 0; j < participantList.size(); j++) {
			Game gameGysling = new GameCycling();
			try {
				gameGysling.setScore(((Cyclist) participantList.get(j)).compete(gameGysling));
			} catch (ClassCastException e) {
				gameGysling.setScore(new Athlete().compete(gameGysling));
			}
			gameGysling.setGameID(this.gentrateGameID());
			gameGysling.setAthlethID(participantList.get(j).getID());
			gameGysling.setTimeStamp(timeString);
			gameCyclingList.add(gameGysling);
		}
		return gameCyclingList;
	}

	public String gentrateGameID() throws NumberFormatException {
		int maxNumber = -1;
		List<Game> gameIDList = null;
		try {
			gameIDList = gameJDBCDao.selectGameGameID();
		} catch (SQLException e) {
			gameIDList = gameFileDao.readFileByLines();
			e.printStackTrace();
		}
		if (gameIDList == null || gameIDList.size() == 0) {
			return "C0" + 1;
		}
		for (int i = 0; i < gameIDList.size(); i++) {
			String[] gameIDSplit = gameIDList.get(i).getGameID().split("0");
			if (gameIDSplit[0].toString().equals("C")) {
				if (maxNumber < Integer.parseInt(gameIDSplit[1])) {
					maxNumber = Integer.parseInt(gameIDSplit[1]);
				}
			}
		}
		maxNumber += 1;
		if (maxNumber == -1 || maxNumber == 0) {
			return "C0" + 1;
		}
		return "C0" + maxNumber;
		// TODO Auto-generated method stub

	}

	@Override
	public String generatePoint(List<Participant> participantList) {
		// select offical
		List<Game> gameCyclingList = null;
		Participant offical = new Official();
		String resultGameID = null;
		offical.setType("Official");
		int indexOffical = -1;
		String OfficalID = null;

		List<Game> gameCyclingWritingFileList = new ArrayList();
		int firstPlace = 0;
		int secondPlace = 0;
		int thirdPlace = 0;

		for (int i = 0; i < participantList.size(); i++) {
			if (participantList.get(i).getType().toString().equals("Official")) {
				indexOffical = i;
				OfficalID = participantList.get(i).getID();
			}
		}
		participantList.remove(indexOffical);
		try {
			gameCyclingList = generateScore(participantList);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < 3; i++) {
			double minTime = 1000000;
			int minIndex = -1;
			for (int j = 0; j < gameCyclingList.size(); j++) {
				gameCyclingList.get(j).setOfficalID(OfficalID);
				if (gameCyclingList.get(j).getResult() == 0) {
					if (gameCyclingList.get(j).getScore() < minTime) {
						minTime = gameCyclingList.get(j).getScore();
						minIndex = j;
					}
				}
			}
			if (i == 0) {
				gameCyclingList.get(minIndex).setResult(5);
				firstPlace = minIndex;
				gameCyclingWritingFileList.add(gameCyclingList.get(minIndex));
				resultGameID = gameCyclingList.get(minIndex).getAthlethID();
			}
			if (i == 1) {
				secondPlace = minIndex;
				gameCyclingWritingFileList.add(gameCyclingList.get(minIndex));
				gameCyclingList.get(minIndex).setResult(2);
			}
			if (i == 2) {
				thirdPlace = minIndex;
				gameCyclingWritingFileList.add(gameCyclingList.get(minIndex));
				gameCyclingList.get(minIndex).setResult(1);
			}

		}

		for (int i = 0; i < gameCyclingList.size(); i++) {
			if (i == firstPlace || i == secondPlace || i == thirdPlace) {
				continue;
			}
			gameCyclingWritingFileList.add(gameCyclingList.get(i));
		}

		try {
			gameJDBCDao.insertGameresults(gameCyclingWritingFileList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultGameID;
	}
}