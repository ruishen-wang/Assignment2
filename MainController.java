package application;

/* ass02
 * Wrote by Hongyi Yan
 * s3521449
 */

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

	@FXML
	public Label switchGame;
	@FXML
	public ChoiceBox gameoption;
	@FXML
	public Label predictGame;
	@FXML
	public Label showResult;
	@FXML
	public TextField predictGamecontext;
	@FXML
	public Button startGame;
	@FXML
	public Button displayscore;
	@FXML
	public Button displayresult;
	@FXML
	public TextArea scorecontext;
	@FXML
	public TextArea resultcontext;
	@FXML
	public Button exit;
	@FXML
	public VBox ShowAthlere;
	@FXML
	public Label showException;
	
	@FXML
	public Button test;

	public static ObservableList<String> sportTypes = FXCollections.observableArrayList();
	Game gameInformation = new Game();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		sportTypes.add("Swimming");
		sportTypes.add("Cycling");
		sportTypes.add("Running");
		gameoption.setItems(sportTypes);
		gameoption.getSelectionModel().selectFirst();
		Participant participant = new Participant();
		List<Participant> participantList = null;
		try {
			participantList = participant.selectAllparticipants();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 运动员ID，类型，姓名，年龄，代表州

		String tempateStringParticipant = null;
		List<CheckBox> tempateStringParticipantList = new ArrayList<CheckBox>();
		for (int i = 0; i < participantList.size(); i++) {
			participant = participantList.get(i);
			tempateStringParticipant = participant.getID() + ", " + participant.getType() + ", " + participant.getName()
					+ ", " + participant.getAge() + ", " + participant.getState();
			CheckBox tempateStringParticipantCheckBox = new CheckBox(tempateStringParticipant);

			tempateStringParticipantList.add(tempateStringParticipantCheckBox);
		}
		ShowAthlere.getChildren().addAll(tempateStringParticipantList);
	}

	@FXML
	public void startGame() throws InterruptedException {
		int numOffical = 0;
		Game gameDifferent = null;
		Participant participant = null;
		List<Participant> participantList = new ArrayList<Participant>();
		int participantNumber = -1;
		for (int i = 0; i < ShowAthlere.getChildren().size(); i++) {
			CheckBox tempateStringParticipantCheckBox = (CheckBox) ShowAthlere.getChildren().get(i);
			if (tempateStringParticipantCheckBox.isSelected()) {
				String[] ParticipantString = tempateStringParticipantCheckBox.getText().toString().split(", ");
				participant = new Participant();
				participant.setID(ParticipantString[0]);
				participant.setType(ParticipantString[1]);
				participant.setName(ParticipantString[2]);
				participant.setAge(Integer.parseInt(ParticipantString[3]));
				participant.setState(ParticipantString[4]);
				participantList.add(participant);
			}
		}
		participantNumber = participantList.size() - 1;
		try {
			if (participantNumber < 4) {
				showException.setText("a game, which has less than 4 athletes registered");
				throw new TooFewAthleteException("a game, which has less than 4 athletes registered");
			}
		} catch (TooFewAthleteException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			if (participantNumber > 8) {
				showException.setText("a game which already has 8 athletes registered ");
				throw new GameFullException("a game which already has 8 athletes registered ");
			}
		} catch (GameFullException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			for (int j = 0; j < participantList.size(); j++) {
				Participant participantSelected = participantList.get(j);
				if (participantSelected.getType().toString().equals("Official")) {
					numOffical += 1;
					continue;
				}
				if (participantSelected.getType().toString().equals("SuperAthlete")) {
					continue;
				}
				if (gameoption.getSelectionModel().getSelectedItem().toString() == "Swimming") {
					if (!participantSelected.getType().toString().equals("Swimmer")) {
						showException.setText("you choose the wrong type");
						throw new WrongTypeException("you choose the wrong type");
					}
				}
				if (gameoption.getSelectionModel().getSelectedItem().toString() == "Running") {
					if (!participantSelected.getType().toString().equals("Sprinter")) {
						showException.setText("you choose the wrong type");
						throw new WrongTypeException("you choose the wrong type");
					}
				}
				if (gameoption.getSelectionModel().getSelectedItem().toString() == "Cycling") {
					if (!participantSelected.getType().toString().equals("Cyclist")) {
						showException.setText("you choose the wrong type");
						throw new WrongTypeException("you choose the wrong type");
					}
				}
			}
			if (numOffical == 0) {
				showException.setText("No Referee，the game doesnot continue!");
				throw new NoRefereeException("No Referee，the game doesnot continue!");
			}
		} catch (WrongTypeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (NoRefereeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		if (gameoption.getSelectionModel().getSelectedItem().toString() == "Swimming") {
			participant = new Swimmer();
			gameDifferent = new GameSwimming();
			participant.setType("Swimmer");
		}
		if (gameoption.getSelectionModel().getSelectedItem().toString() == "Running") {
			participant = new Sprinter();
			gameDifferent = new GameRunning();
			participant.setType("Sprinter");
		}
		if (gameoption.getSelectionModel().getSelectedItem().toString().equals("Cycling")) {
			participant = new Cyclist();
			gameDifferent = new GameCycling();
			participant.setType("Cyclist");
		}
		String winnerID = gameDifferent.generatePoint(participantList);
		if (predictGamecontext.getText().toString().equals(winnerID))
			showResult.setText("Congratulations, you predict the winner!");
		else {
			showResult.setText("sorry,you predict the winner is " + predictGamecontext.getText().toString()
					+ ", but the winner is  " + winnerID);
		}

	}

	
	
	
	@FXML
	public void displayScore() {
		try {
			List<Game> gameList = gameInformation.displayScoreAndResult();
			StringBuilder sb = new StringBuilder();
			Game showGame;
			for (Iterator<Game> gameiterator = gameList.iterator(); gameiterator.hasNext();) {
				showGame = gameiterator.next();
				sb.append(showGame.getGameID() + "  " + showGame.getOfficalName() + "  " + showGame.getAthlethID() + "  "
						+ showGame.getScore() + "\n");
			}
			scorecontext.setText(sb.toString());
			sb.setLength(0);
		} catch (NullPointerException e) {
			resultcontext.setText("there is not games");
			e.printStackTrace();
		}
	}

	@FXML
	public void displayResult() {
		try {
			List<Game> gameList = gameInformation.displayScoreAndResult();
			StringBuilder sb = new StringBuilder();
			Game showGame;
			for (Iterator<Game> gameiterator = gameList.iterator(); gameiterator.hasNext();) {
				showGame = gameiterator.next();
				sb.append(showGame.getGameID() + "  " + showGame.getOfficalID() + "  " + showGame.getAthlethID() + "  "
						+ showGame.getResult() + "\n");
			}
			resultcontext.setText(sb.toString());
			sb.setLength(0);
		} catch (NullPointerException e) {
			resultcontext.setText("there is not games");
			e.printStackTrace();
		}
	}

	@FXML
	public void exitSystem() {
		System.exit(-1);
	}

}
