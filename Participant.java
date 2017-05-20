package application;

import java.sql.SQLException;
import java.util.List;

// work by Tao Lei s3552269  ass01
// work by Hongyi Yan s3521449 ass02
public class Participant {

	private String ID;
	private int Age;
	private String Name;
	private String State;
	private String type;
	public ParticipantJDBCDao ppjd = null;

	// public int Number;
	public Participant() {

	}

	public Participant(String ID, int Age, String Name, String State, String type) {
		this.ID = ID;
		this.Age = Age;
		this.Name = Name;
		this.State = State;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// Constructor

	public void setID(String iD) {
		ID = iD;
	}

	public void setAge(int age) {
		Age = age;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setState(String state) {
		State = state;
	}

	public String getID() {
		return ID;
	}

	public int getAge() {
		return Age;
	}

	public String getName() {
		return Name;
	}

	public String getState() {
		return State;
	}

	public List<Participant> selectAllparticipants() throws NullPointerException, SQLException {
		ppjd = new ParticipantJDBCDao();
		return ppjd.selectAllPaticipant();
	}
}
