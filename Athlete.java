package application;

/* ass01
 * Wrote by Hongyi Yan
 * s3521449
 */

import java.util.ArrayList;

class Athlete extends Participant{
	
	// construction of Athlete
	public Athlete(String ID, int Age, String Name, String State,String type){
		
		super(ID, Age, Name, State,type);
		
	}
	
	public Athlete() {
		// TODO Auto-generated constructor stub
	}

	// construct data about different athlete in array list
	static ArrayList<Athlete> athlete = new ArrayList<Athlete>();
	
	// add the detail data information
//	public static void addAthlete(){
//		
//		athlete.add(new Swimmer("SW01", 24, "Aaron", "VIC"));
//		athlete.add(new Swimmer("SW02", 28, "Eric", "NSW"));
//		athlete.add(new Swimmer("SW03", 30, "Kelly","VIC"));
//		athlete.add(new Swimmer("SW04", 21, "Barret","VIC"));
//		athlete.add(new Sprinter("SP01", 19, "Magee","NSW"));
//		
//		athlete.add(new Sprinter("SP02", 22, "Ivan", "NSW"));
//		athlete.add(new Sprinter("SP03", 25, "Edward", "VIC"));
//		athlete.add(new Sprinter("SP04", 26, "Steven", "NSW"));
//		
//		athlete.add(new SuperAthlete("SU01", 34, "Rodney", "VIC"));
//		athlete.add(new SuperAthlete("SU02", 23, "William", "NSW"));
//		athlete.add(new SuperAthlete("SU03", 16, "Abbott", "VIC"));
//		athlete.add(new SuperAthlete("SU04", 28, "Owen", "NSW"));
//		athlete.add(new SuperAthlete("SU05", 33, "Nelson", "VIC"));
//		
//		athlete.add(new Cyclist("CY01", 21, "Berg", "VIC"));
//		athlete.add(new Cyclist("CY02", 26, "Hyman", "NSW"));
//		athlete.add(new Cyclist("CY03", 32, "Alexander", "VIC"));
//		athlete.add(new Cyclist("CY04", 23, "Kerwin", "VIC"));
//		
//		athlete.add(new SuperAthlete("SU06", 22, "Addis","NSW"));
//		athlete.add(new SuperAthlete("SU07", 25, "Zack","VIC"));
//		athlete.add(new SuperAthlete("SU08", 28, "Taylor","NSW"));
//		athlete.add(new SuperAthlete("SU09", 21, "Zachariah","VIC"));
//		athlete.add(new SuperAthlete("SU10", 30, "Parker","NSW"));
//		athlete.add(new SuperAthlete("SU11", 29, "Able","VIC"));
//	}
	
	// According the different game type, define the range of compete time
	public int compete(Game game){
		
		if(game instanceof GameSwimming){
			
			return 100 + (int)(Math.random()*101);
			
		}else if(game instanceof GameCycling){
			
			return 500 + (int)(Math.random()*301);
			
		}else {
			
			return 10 + (int)(Math.random()*11);
			
		}
	}
	
}