// BuildingGroupEMS				Author: Eli Summerville
// 
// Object class that represents a single building group of utility usage data.


//---------------------------
// BuildingGroupEMS.java Instance Data
//---------------------------
public class BuildingGroupEMS
{
	// The name of the building group
	private String id;
	// The count of how many data items have been stored 
	private int items;
	// An array of type UsageEntry which stores all of the usage entry information
	// in each index of the array.
	private UsageEntry[] collection;
	
	
//-----------------------------------------------------------------
//  Constructor: Sets the initial face value of the variables
//-----------------------------------------------------------------
	
	public BuildingGroupEMS(String idName, UsageEntry[] itemsCollection)
	{
		id = idName;
		items = 0;
		collection = itemsCollection;
	}
	
//--------------------------
// BulidingGroupEMS Methods
//--------------------------

	// Creates a new UsageEntry object by adding the provided values to a collection
	public void addEntry(int m, int y,
        double teu, double eu, double ngu,
        double su, double wu, double wwu,
        double tec, double ec, double ngc,
        double sc, double wc, double wwc,
        double tuc)
		{
		collection[items] = new UsageEntry( m, y, teu, eu, ngu, su, wu, wwu, 
		tec, ec, ngc, sc, wc, wwc, tuc);
			items++;
		}

	// Returns a collection of sums of utility usage numbers
	public double[] getSums()
	{
		double[] sumCollection = new double[6];;
		
		for(int i=0; i<items; i++)
		{
			sumCollection[0] += collection[i].getElectricCost();
			sumCollection[1] += collection[i].getNatGasCost();
			sumCollection[2] += collection[i].getSteamCost();
			sumCollection[3] += collection[i].getWaterCost();
			sumCollection[4] += collection[i].getWastewaterCost();
			sumCollection[5] += collection[i].getTotalCost();
		}
		return sumCollection;
	}
	// Getter method that returns an array of all of the utility costs for the 
	// chosen month
	public double[] getSumsEachMonth(int monthPicked)
	{
		double[] sumsEachMonth = new double [6];
		
		for(int i=0; i<items; i++)
		{
			if(collection[i].isMonth(monthPicked))
			{
				sumsEachMonth[0] += collection[i].getElectricCost();
				sumsEachMonth[1] += collection[i].getNatGasCost();
				sumsEachMonth[2] += collection[i].getSteamCost();
				sumsEachMonth[3] += collection[i].getWaterCost();
				sumsEachMonth[4] += collection[i].getWastewaterCost();
				sumsEachMonth[5] += collection[i].getTotalCost();	
			}
			
		}	
		return sumsEachMonth;
	}
	
	// Getter method that returns an array of all of the utility costs for the 
	// chosen year
	public double[] getSumsEachYear(int yearPicked)
	{
		double[] sumsEachyear = new double [6];
		
		for(int i=0; i<items; i++)
		{
			if(collection[i].isYear(yearPicked))
			{
				sumsEachyear[0] += collection[i].getElectricCost();
				sumsEachyear[1] += collection[i].getNatGasCost();
				sumsEachyear[2] += collection[i].getSteamCost();
				sumsEachyear[3] += collection[i].getWaterCost();
				sumsEachyear[4] += collection[i].getWastewaterCost();
				sumsEachyear[5] += collection[i].getTotalCost();	
			}
			
		}	
		return sumsEachyear;
	}
	
	
	
	// Getter method which returns the id from the BuildingGroup class
	public String getId()
	{
		return id;
	}
	
	// Getter method which returns the number of items in the BuildingGroup class
	public int getItems()
	{
		return items;
	}
	
	// Test method which returns the entirety of the collection array
	/*
	public UsageEntry[] getCollection()
	{
		for (int i=0; i<collection.length; i++)
		{
			return System.out.println("Index "+i+": "+ collection[i]); 	
		}
	}
	*/
}