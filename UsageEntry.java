// UsageEntry.java				Author: AMH
// Describes a single data point about the utility usage of a county
// operated building including the date of the data collection and
// the cost and usage overall and for each of electricity, natural gas,
// steam, water, and waste water.

public class UsageEntry {
	private int month;				// month of data collection as value 1-12
	private int year;				// year of data collection as four digit year
	private double totalEnergyUse;	// total energy use across 3 energy utilities
	private double electricUse;		// electricity use
	private double naturalGasUse;	// natural gas use
	private double steamUse;		// steam use
	private double waterUse;		// water use
	private double wastewaterUse;	// waste water use
	private double totalEnergyCost;	// total cost of energy across 3 energy utilties
	private double electricCost;	// cost of electricity use
	private double naturalGasCost;	// cost of natural gas use
	private double steamCost;		// cost of steam use
	private double waterCost;		// cost of water use
	private double wastewaterCost;	// cost of waste water use
	private double totalUtilityCost;	// cost of utility use across 5 utilities
	
	// Initialize a new UsageEntry object, being provided values for each
	// fact about the object, assuming any needed error checking has taken
	// place at the calling context
	public UsageEntry(int m, int y, double teu, double eu, double ngu,
			double su, double wu, double wwu, double tec, double ec, double ngc,
			double sc, double wc, double wwc, double tuc) {
		month = m;
		year = y;
		totalEnergyUse = teu;
		electricUse = eu;
		naturalGasUse = ngu;
		steamUse = su;
		waterUse = wu;
		wastewaterUse = wwu;
		totalEnergyCost = tec;
		electricCost = ec;
		naturalGasCost = ngc;
		steamCost = sc;
		waterCost = wc;
		wastewaterCost = wwc;
		totalUtilityCost = tuc;
	}
	
	// returns true if the indicated month matches the month of this data entry
	public boolean isMonth(int checkMonth) {
		return month==checkMonth;
	}
	
	// returns true if the indicated year matches the year of this data entry
	public boolean isYear(int checkYear) {
		return year==checkYear;
	}

	// returns the recorded cost of electricity usage
	public double getElectricCost() {
		return electricCost;
	}
	
	// returns the recorded cost of natural gas usage
	public double getNatGasCost() {
		return naturalGasCost;
	}
	
	// returns the recorded cost of steam usage
	public double getSteamCost() {
		return steamCost;
	}
	
	// returns the recorded cost of water usage
	public double getWaterCost() {
		return waterCost;
	}
	
	// returns the recorded cost of waste water usage
	public double getWastewaterCost() {
		return wastewaterCost;
	}
	
	// returns the total cost across all five utilities
	public double getTotalCost() {
		return totalUtilityCost;
	}
	
	// returns the recorded amount of electricity usage
	public double getElectricUse() {
		return electricUse;
	}
	
	// returns the recorded amount of natural gas usage
	public double getNatGasUse() {
		return naturalGasUse;
	}
	
	// returns the recorded amount of steam usage
	public double getSteamUse() {
		return steamUse;
	}
	
	// returns the recorded amount of water usage
	public double getWaterUse() {
		return waterUse;
	}
	
	// returns the recorded amount of waste water usage
	public double getWastewaterUse() {
		return wastewaterUse;
	}	
}