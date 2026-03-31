// UtilityUsageEMS		Author: AMH + Eli Summerville
// 
// A graphics application that visualizes energy and water usage of Allegheny
// County operated facilities, both overall and as separated based on the
// building group, year, or month.
//
// Data Sources:
// https://data.wprdc.org/dataset/allegheny-county-energy-and-water-use

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.util.Scanner;
import java.io.*;
import javafx.scene.input.MouseEvent;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class UtilityUsageEMS extends Application {
	/* Lines of code to define the filename to read from; only one should be
	uncommented at a time. */
	// private final String filename = "UtilityUse-Small.txt";
	// private final String filename = "UtilityUse-Synthetic.txt";
	// private final String filename = "UtilityUse-Medium1.txt";
	// private final String filename = "UtilityUse-Medium2.txt";
	 private final String filename = "UtilityUse-Full.txt";
	
	// collection of usage data stored by building group
	private BuildingGroupEMS[] usageData;	
	
	// Circle defining scope of left-hand pie chart
	private Circle totalPie;
	// Arc pieces corresponding to the five utilities making up the left-hand
	// pie chart representing the complete set of data
	private Arc electricWedge, natGasWedge, steamWedge, waterWedge,
				wastewaterWedge;

	// Circle defining scope of right-hand pie chart
	private Circle subsetPie;
	// Arc pieces corresponding to the five utilities making up the right-hand
	// pie chart representing a subset of the compelte data
	private Arc subElectricWedge, subNatGasWedge, subSteamWedge, subWaterWedge,
				subWastewaterWedge;
	// label for cost represented across entire right-hand pie chart for
	// the selected subset of data
	private Label subCostLabel;
	
	// label for presenting utility usage upon mouse click within one of
	// the pie charts
	private Label detailsLabel;
	
    public void start(Stage primaryStage) {
		final int PLOTSIZE = 600;	// height and width of graphics window

		int numRows = 0;
		int numBuildings = 0;
		int minYear = 1000000;
		int maxYear = 0;
		String[] BuildingNames = new String[100];
		int[] BuildingCount = new int[100];
		boolean buildingFound = false;
		// try-catch block for first pass through data file
		try {
			
			Scanner filescan = new Scanner(new File(filename));	
			filescan.nextLine();// Skips the first line of titles 
			filescan.useDelimiter("\\t|\\n");
			
			
			while (filescan.hasNext())
			{
				String id = filescan.next();
				String buildingGroup = filescan.next();
				String location = filescan.next();
				String date = filescan.next();
				String energyUse = filescan.next();
				String electricUse = filescan.next();
				String natGasUse = filescan.next();
				String steamUse = filescan.next();
				String waterUse = filescan.next();
				String wasteWaterUse = filescan.next();
				String totalEnergyCost = filescan.next();
				String electricCost = filescan.next();
				String natGasCost = filescan.next();
				String steamCost = filescan.next();
				String waterCost = filescan.next();
				String wasteWaterCost = filescan.next();
				String totalUtilityCost = filescan.next();
				String totalTonsCo2 = filescan.next();
				String electricTonsCo2 = filescan.next();
				String natGasTonsCo2 = filescan.next();
				String steamTonsCo2 = filescan.next();
				numRows++;
				
				// Takes a substring of the last 4 letters in the string and 
				// parses them as a number while comparing thier running value 
				// to low and high values respectively
				String year = date.substring(date.length()-4);
				minYear = Math.min(Integer.parseInt(year), minYear);
				maxYear = Math.max(Integer.parseInt(year), maxYear);
				
				buildingFound = false;
				int buildingIndex = -1;
				// Array of type string to store the unique strings
				// Array of type int to store the amount of times each building 
				// group is referenced
				for(int i=0; i<numBuildings; i++)
				{
					if(BuildingNames[i] != null && BuildingNames[i].equals(buildingGroup))
					{
						buildingFound = true;
						buildingIndex = i;
					}					
				}
				if (!buildingFound)
				{
					BuildingNames[numBuildings] = buildingGroup;
					buildingIndex = numBuildings; 
					numBuildings++;							
				}
				// Incrementor for entries per building
				BuildingCount[buildingIndex]++;
			}

		} catch (IOException e) {System.out.println(e);};

		// output statements for debugging with variables removed from all but first
		System.out.println("Number of records: " + numRows);
		System.out.println("Min year: " + minYear);
		System.out.println("Max year: " + maxYear);
		System.out.println("Number of buildings: " + numBuildings);
		System.out.println("Building list:");
		// TODO: instantiate and initialize BuildingGroupXXX object
		usageData = new BuildingGroupEMS[numBuildings];
		for(int i = 0; i < numBuildings; i++)
		{
			usageData[i] = new BuildingGroupEMS(
			BuildingNames[i], 
			new UsageEntry[BuildingCount[i]]);	
		}
		// try-catch block for second pass through data file
		try {
			Scanner filescan = new Scanner(new File(filename));	
			filescan.nextLine();			
			filescan.useDelimiter("\\t|\\n");
			
			while(filescan.hasNext())
			{
				// for loop to deal with a bounds issue: scans out the first row 
				// Scan out anything before the date
				String id = filescan.next(); 
				String buildingGroup = filescan.next();
				String location = filescan.next();
				String dateUnparsed = filescan.next();
// https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/lang/StringBuffer.html#indexOf(java.lang.String,int)

				int monthSlash = dateUnparsed.indexOf("/");
				int yearSlash = dateUnparsed.lastIndexOf("/");

				int m = Integer.parseInt(dateUnparsed.substring(0, monthSlash));
				int y = Integer.parseInt(dateUnparsed.substring(yearSlash + 1));
				
				double teu = Double.parseDouble(filescan.next());
				double eu = Double.parseDouble(filescan.next());
				double ngu = Double.parseDouble(filescan.next());
				double su = Double.parseDouble(filescan.next());
				double wu = Double.parseDouble(filescan.next());
				double wwu = Double.parseDouble(filescan.next());
				double tec = Double.parseDouble(filescan.next());
				double ec = Double.parseDouble(filescan.next());
				double ngc = Double.parseDouble(filescan.next());
				double sc = Double.parseDouble(filescan.next());
				double wc = Double.parseDouble(filescan.next());
				double wwc = Double.parseDouble(filescan.next());
				double tuc = Double.parseDouble(filescan.next());
				filescan.next();
				filescan.next();
				filescan.next();
				filescan.next();

				int buildingIndex = -1;
				for(int i=0; i<numBuildings; i++)
				{
					if(BuildingNames[i] != null && BuildingNames[i].equals(buildingGroup))
					{
						buildingIndex = i;
					}
				}
				if(buildingIndex != -1)
				{
					usageData[buildingIndex].addEntry(
						m, y, teu, eu, ngu, su, wu, wwu, tec, 
						ec, ngc, sc, wc, wwc, tuc);
				}				
			}

		} catch (IOException e) {System.out.println(e);};
		
		for(int i=0; i<numBuildings; i++)
		{
			System.out.println("\t"+BuildingNames[i]+" ("+BuildingCount[i]+")");
		}
		
		// initialize placements and colors of left-hand pie chart components
		totalPie = new Circle(150, 300, 120);
		electricWedge = new Arc(150, 300, 120, 120, 0, 0);
		electricWedge.setFill(Color.RED);
		electricWedge.setType(ArcType.ROUND);
		natGasWedge = new Arc(150, 300, 120, 120, 0, 0);
		natGasWedge.setFill(Color.YELLOW);
		natGasWedge.setType(ArcType.ROUND);
		steamWedge = new Arc(150, 300, 120, 120, 0, 0);
		steamWedge.setFill(Color.GREEN);
		steamWedge.setType(ArcType.ROUND);
		waterWedge = new Arc(150, 300, 120, 120, 0, 0);
		waterWedge.setFill(Color.BLUE);
		waterWedge.setType(ArcType.ROUND);
		wastewaterWedge = new Arc(150, 300, 120, 120, 0, 0);
		wastewaterWedge.setFill(Color.PURPLE);
		wastewaterWedge.setType(ArcType.ROUND);
		Label totalCostLabel = new Label("Total Utility Cost");
		totalCostLabel.setTranslateX(50);
		totalCostLabel.setTranslateY(450);
		// call helper method to configure left-hand pie chart based on file data
		drawFullPie(totalCostLabel);
		
		
		
		
		
		// initialize placements and colors of right-hand pie chart components
		subsetPie = new Circle(450, 300, 120);
		subElectricWedge = new Arc(450, 300, 120, 120, 0, 0);
		subElectricWedge.setFill(Color.RED);
		subElectricWedge.setType(ArcType.ROUND);
		subNatGasWedge = new Arc(450, 300, 120, 120, 0, 0);
		subNatGasWedge.setFill(Color.YELLOW);
		subNatGasWedge.setType(ArcType.ROUND);
		subSteamWedge = new Arc(450, 300, 120, 120, 0, 0);
		subSteamWedge.setFill(Color.GREEN);
		subSteamWedge.setType(ArcType.ROUND);
		subWaterWedge = new Arc(450, 300, 120, 120, 0, 0);
		subWaterWedge.setFill(Color.BLUE);
		subWaterWedge.setType(ArcType.ROUND);
		subWastewaterWedge = new Arc(450, 300, 120, 120, 0, 0);
		subWastewaterWedge.setFill(Color.PURPLE);
		subWastewaterWedge.setType(ArcType.ROUND);
		subCostLabel = new Label("Total Utility Cost");
		subCostLabel.setTranslateX(350);
		subCostLabel.setTranslateY(450);
		
		// set up drop down menu for selecting the building group
		// TODO: add MenuItem objects to menu
		MenuButton buildingMenu = new MenuButton("Building Group");
		buildingMenu.setTranslateX(10);
		buildingMenu.setTranslateY(10);
		// Recreating a for loop similar to the one I used earlier when referencing
		// the years that can be selected and the premade month drop down
		for(int i=0; i<numBuildings; i++)
		{
			MenuItem newItem = new MenuItem(BuildingNames[i]);
			buildingMenu.getItems().add(newItem);
			newItem.setOnAction(this::buildingAction);
		}
		
		
		
		// set up drop down menu for selecting the month and attach MenuItem objects
		MenuButton monthMenu = new MenuButton("Month");
		monthMenu.setTranslateX(200);
		monthMenu.setTranslateY(10);
		String[] monthList = {"1", "2", "3", "4", "5", "6" , "7", "8", "9",
								"10", "11", "12"};
		for (int i=0; i<12; i++) {
			MenuItem newItem = new MenuItem(monthList[i]);
			monthMenu.getItems().add(newItem);
			newItem.setOnAction(this::monthAction);
		}

		// set up drop down menu for selecting the year
		// TODO: add MenuItem objects to menu
		MenuButton yearMenu = new MenuButton("Year");
		yearMenu.setTranslateX(350);
		yearMenu.setTranslateY(10);
		int yearSize = (maxYear-minYear)+1;
		// create and fill a yearList collection to store all of the years starting 
		// from minYear and incrementing to the maximum year in the array
		int[] yearList = new int[yearSize];
		for (int i=0; i<yearSize; i++)
		{
			yearList[i] = minYear + i;
			MenuItem newItem = new MenuItem(String.valueOf(yearList[i]));
			yearMenu.getItems().add(newItem);
			newItem.setOnAction(this::yearAction);
		}

		// set up Button object for resetting right-hand pie chart
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(this::resetAction);
		resetButton.setTranslateX(500);
		resetButton.setTranslateY(10);

		// set up Label for displaying usage statistics when pie chart is clicked
		detailsLabel = new Label("");
		detailsLabel.setTranslateX(200);
		detailsLabel.setTranslateY(100);
		
		// set up the components of the key for reading the pie charts
		Rectangle red = new Rectangle(50,550,10,10);
		red.setFill(Color.RED);
		Label redLabel = new Label("Electric");
		redLabel.setTranslateX(70);
		redLabel.setTranslateY(545);
		Rectangle yellow = new Rectangle(150,550,10,10);
		yellow.setFill(Color.YELLOW);
		Label yellowLabel = new Label("Natural Gas");
		yellowLabel.setTranslateX(170);
		yellowLabel.setTranslateY(545);
		Rectangle green = new Rectangle(250,550,10,10);
		green.setFill(Color.GREEN);
		Label greenLabel = new Label("Steam");
		greenLabel.setTranslateX(270);
		greenLabel.setTranslateY(545);
		Rectangle blue = new Rectangle(350,550,10,10);
		blue.setFill(Color.BLUE);
		Label blueLabel = new Label("Water");
		blueLabel.setTranslateX(370);
		blueLabel.setTranslateY(545);
		Rectangle purple = new Rectangle(450,550,10,10);
		purple.setFill(Color.PURPLE);
		Label purpleLabel = new Label("Waste water");
		purpleLabel.setTranslateX(470);
		purpleLabel.setTranslateY(545);
		Group key = new Group(red, redLabel, yellow, yellowLabel, green, greenLabel, blue, blueLabel, purple, purpleLabel);

		// create root Group containing all compoents of the application
		Group root = new Group(totalPie, electricWedge, natGasWedge, steamWedge, waterWedge, wastewaterWedge, totalCostLabel, subsetPie, subElectricWedge, subNatGasWedge, subSteamWedge, subWaterWedge, subWastewaterWedge, subCostLabel, buildingMenu, monthMenu, yearMenu, resetButton, detailsLabel, key);

		// set up the Scene and primaryStage to finish configuring the application
        Scene scene = new Scene(root, PLOTSIZE, PLOTSIZE, Color.WHITE);
		// add the ability to respond to mouse clicks
		scene.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        primaryStage.setTitle("Utility Usage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	// given usageData and the Arc objects making up the left-hand pie chart
	// available as instance data, determine and set the start angles and
	// angle lengths for each Arc object and the total amount spent for the
	// pie chart's label
	private void drawFullPie(Label totalCostLabel) {
		// Instance data to store the sums of each utility from the for loop
		double electricSum = 0;
		double natGasSum = 0;
		double steamSum = 0;
		double waterSum = 0;
		double wasteWaterSum = 0;
		double total = 0;
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		
		// Loops through all of the buildings getting the sums of each
		// utility usage 
		for(int i=0; i<usageData.length; i++)
		{
			double[] sumsCollection = usageData[i].getSums();
			
			electricSum += sumsCollection[0];
			natGasSum += sumsCollection[1];
			steamSum += sumsCollection[2];
			waterSum += sumsCollection[3];
			wasteWaterSum += sumsCollection[4];
			total += sumsCollection[5];
		
		}
		// Math for each angle on the left pie chart
		
		double electricAngle = (electricSum/total)*360;
		double natGasAngle = (natGasSum/total)*360;
		double steamAngle = (steamSum/total)*360;
		double waterAngle = (waterSum/total)*360;
		double wasteWaterAngle = (wasteWaterSum/total)*360;
		
		// Adding the data for each left side wedge starting from electric and 
		// rotating counterclockwise
		double runningTotal = 0;
		electricWedge.setStartAngle(runningTotal);
		electricWedge.setLength(electricAngle);
		runningTotal += electricAngle;
		natGasWedge.setStartAngle(runningTotal);
		natGasWedge.setLength(natGasAngle);
		runningTotal += natGasAngle;
		steamWedge.setStartAngle(runningTotal);
		steamWedge.setLength(steamAngle);
		runningTotal += steamAngle;
		waterWedge.setStartAngle(runningTotal);
		waterWedge.setLength(waterAngle);
		runningTotal += waterAngle;
		wastewaterWedge.setStartAngle(runningTotal);
		wastewaterWedge.setLength(wasteWaterAngle);
		runningTotal += wasteWaterAngle;
		
		totalCostLabel.setText("Total Utility Cost "+fmt.format(total));
		
	}
	
	// given the sums of the costs for each of the utilities and the total across
	// them for a subset of the data, set the start angles and angle lengths for
	// each Arc object in the right-hand pie chart
	private void drawSubPie(double electric, double natGas, double steam,
							double water, double wastewater, double total) {
								

			
		// Same math as the left pie chart 
		double electricAngle = (electric/total)*360;
		double natGasAngle = (natGas/total)*360;
		double steamAngle = (steam/total)*360;
		double waterAngle = (water/total)*360;
		double wasteWaterAngle = (wastewater/total)*360;
								
		//Instantiate another running total to keep track of the arc positions 
		double runningTotal = 0;
		
		//https://openjfx.io/javadoc/24/javafx.graphics/javafx/scene/shape/Arc.html
		subElectricWedge.setStartAngle(runningTotal);
		subElectricWedge.setLength(electricAngle);
		runningTotal += electricAngle;
		subNatGasWedge.setStartAngle(runningTotal);
		subNatGasWedge.setLength(natGasAngle);
		runningTotal += natGasAngle;
		subSteamWedge.setStartAngle(runningTotal);
		subSteamWedge.setLength(steamAngle);
		runningTotal += steamAngle;
		subWaterWedge.setStartAngle(runningTotal);
		subWaterWedge.setLength(waterAngle);
		runningTotal += waterAngle;
		subWastewaterWedge.setStartAngle(runningTotal);
		subWastewaterWedge.setLength(wasteWaterAngle);
		runningTotal += wasteWaterAngle;
								

	}

	// event handler for the building group drop down menu
	// determines the amount spent on each utility and overall for the selected
	// building group and then calls the drawSubPie method to update the
	// right-hand pie chart
	public void buildingAction(ActionEvent event) {
		String selection = ((MenuItem)(event.getSource())).getText();
		System.out.println("Building group selected: " + selection);
		boolean indexFound = false;
		int buildingIndex = 0;
		detailsLabel.setText("");

		
		for (int i=0; i<usageData.length; i++)
		{
			// If an index of usageData has an id that equals the event handler 
			// selection, if it is equal then the sentinel is evaluated to true  
			// and buildingIndex is set to the index of the array 
			if (usageData[i].getId().equals(selection))
			{
				indexFound = true;
				buildingIndex = i;		
			}
		}
		
		if (indexFound)
		{
			// Calls drawSubPie with the parameters of each index in the 
			// array of sums
			double[] sums = usageData[buildingIndex].getSums();
			drawSubPie(sums[0], sums[1], sums[2], sums[3], sums[4], sums[5]);
			NumberFormat fmt = NumberFormat.getCurrencyInstance();
			subCostLabel.setText("Total Utility Cost, \nBuilding "+selection+";\n"+
				fmt.format(sums[5]));
		}
		


	}

	// event handler for the month drop down menu
	// determines the amounbt spent on each utility and overall for the selected
	// month and then calls the drawSubPie method to update the right-hand
	// pie chart
	public void monthAction(ActionEvent event) {
		String selection = ((MenuItem)(event.getSource())).getText();
		System.out.println("Month selected: " + selection);
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		int monthPicked = Integer.parseInt(selection);
		detailsLabel.setText("");

		double electricSum = 0;
		double natGasSum = 0;
		double steamSum = 0;
		double waterSum = 0;
		double wasteWaterSum = 0;
		double total = 0;
		
		for(int i=0; i<usageData.length; i++)
		{
			double[] sumsEachMonth = usageData[i].getSumsEachMonth(monthPicked);
			electricSum += sumsEachMonth[0];
			natGasSum += sumsEachMonth[1];
			steamSum += sumsEachMonth[2];
			waterSum += sumsEachMonth[3];
			wasteWaterSum += sumsEachMonth[4];
			total += sumsEachMonth[5];
		}
		drawSubPie(electricSum, natGasSum, steamSum, waterSum, wasteWaterSum,
					total);
		subCostLabel.setText("Total Utility Cost, \nMonth "+monthPicked+";\n"+
								fmt.format(total));
	}

	// event handler for the year drop down menu
	// determines the amounbt spent on each utility and overall for the selected
	// year and then calls the drawSubPie method to update the right-hand pie chart
	public void yearAction(ActionEvent event) {
		String selection = ((MenuItem)(event.getSource())).getText();
		System.out.println("Year selected: " + selection);
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		int yearPicked = Integer.parseInt(selection);
		detailsLabel.setText("");

		double electricSum = 0;
		double natGasSum = 0;
		double steamSum = 0;
		double waterSum = 0;
		double wasteWaterSum = 0;
		double total = 0;
		
		for(int i=0; i<usageData.length; i++)
		{
			double[] sumsEachYear = usageData[i].getSumsEachYear(yearPicked);
			electricSum += sumsEachYear[0];
			natGasSum += sumsEachYear[1];
			steamSum += sumsEachYear[2];
			waterSum += sumsEachYear[3];
			wasteWaterSum += sumsEachYear[4];
			total += sumsEachYear[5];
		}
		drawSubPie(electricSum, natGasSum, steamSum, waterSum, wasteWaterSum,
					total);
		subCostLabel.setText("Total Utility Cost, \nYear "+yearPicked+";\n"+
								fmt.format(total));

	}

	// event handler for reset button to set right hand pie chart back to
	// all Arc start and length angles being 0
	public void resetAction(ActionEvent event) {
		subElectricWedge.setStartAngle(0);
		subElectricWedge.setLength(0);
		subNatGasWedge.setStartAngle(0);
		subNatGasWedge.setLength(0);
		subSteamWedge.setStartAngle(0);
		subSteamWedge.setLength(0);
		subWaterWedge.setStartAngle(0);
		subWaterWedge.setLength(0);
		subWastewaterWedge.setStartAngle(0);
		subWastewaterWedge.setLength(0);
		detailsLabel.setText("");
		subCostLabel.setText("Total Utility Cost");
	}
	
	// event handler to respond to mouse clicks
	// determines which, if any, Arc within a pie chart has been clicked and
	// then displays the utility usage underlying the data currently being
	// represented by that Arc
	private void mousePressed(MouseEvent event) {
		double pressX = event.getX();
		double pressY = event.getY();
		System.out.println(pressX + " " + pressY);
		String[] utilityNames = {"electricity", "natural gas", "steam", "water", 
		"waste water"};
		DecimalFormat fmt = new DecimalFormat("000000.#");

		// .contains method found in node class used in the circle class
		// https://openjfx.io/javadoc/24/javafx.graphics/javafx/scene/Node.html#contains(double,double)

		// Left pie chart
		double electricLeft = 0;
		double natGasLeft = 0;
		double steamLeft = 0;
		double waterLeft = 0;
		double wasteWaterLeft = 0;
		double[] sumsCollectionLeft = new double[usageData.length];
		// Loops through all of the buildings getting the sums of each
		// utility usage 
		for(int i=0; i<usageData.length; i++)
		{
			sumsCollectionLeft = usageData[i].getSums();
			
			electricLeft += sumsCollectionLeft[0];
			natGasLeft += sumsCollectionLeft[1];
			steamLeft += sumsCollectionLeft[2];
			waterLeft += sumsCollectionLeft[3];
			wasteWaterLeft += sumsCollectionLeft[4];
		}
		double[] totalsLeft = {electricLeft, natGasLeft, steamLeft, waterLeft, 
								wasteWaterLeft};
		Arc[] leftWedges = {electricWedge, natGasWedge, steamWedge, waterWedge,
							wastewaterWedge};
		
		if(totalPie.contains(pressX,pressY))
		{
			for(int i=0; i<leftWedges.length; i++)
			{
				if(leftWedges[i].contains(pressX,pressY))
				{
					detailsLabel.setText("Total overall "+utilityNames[i]+
					" usage: "+ String.valueOf(fmt.format(totalsLeft[i])));
				}
			}
		}
		
	Arc[] rightWedges = {subElectricWedge, subNatGasWedge, subSteamWedge, 
						subWaterWedge, subWastewaterWedge};
						
	String currentLabel = subCostLabel.getText();
	double[] totalsRight = new double[5];

	for(int i=0; i<usageData.length; i++)
	{
		// if statments to check the contents of the current label and if one
		// is true then it is searched further for specificity 
		if(currentLabel.contains("Building"))
		{
		int start = currentLabel.indexOf("Building ") + 9;
        int end = currentLabel.indexOf(";", start);
        String buildingName = currentLabel.substring(start, end);
		
        if(usageData[i].getId().equals(buildingName))
			
			if(usageData[i].getId().equals(buildingName))
			{
				double[] sums = usageData[i].getSums();
				totalsRight[0] = sums[0]; 
				totalsRight[1] = sums[1]; 
				totalsRight[2] = sums[2]; 
				totalsRight[3] = sums[3]; 
				totalsRight[4] = sums[4];
			}
		}
		
		// if statement to check for months and calculate the total if true 
		if(currentLabel.contains("Month"))
		{
			int start = currentLabel.indexOf("Month ") + 6;
			int end = currentLabel.indexOf(";", start);
			int month = Integer.parseInt(currentLabel.substring(start, end));
			
			double[] sumsEachMonth = usageData[i].getSumsEachMonth(month);
			
			totalsRight[0] += sumsEachMonth[0];
			totalsRight[1] += sumsEachMonth[1];
			totalsRight[2] += sumsEachMonth[2];
			totalsRight[3] += sumsEachMonth[3];
			totalsRight[4] += sumsEachMonth[4];
		}
		// if stateent to calculate the total for the year if true 
		if(currentLabel.contains("Year"))
		{
			int start = currentLabel.indexOf("Year ") + 5;
			int end = currentLabel.indexOf(";", start);
			int yearPicked = Integer.parseInt(currentLabel.substring(start, end));
			
			double[] sumsEachYear = usageData[i].getSumsEachYear(yearPicked);
			
			totalsRight[0] += sumsEachYear[0];
			totalsRight[1] += sumsEachYear[1];
			totalsRight[2] += sumsEachYear[2];
			totalsRight[3] += sumsEachYear[3];
			totalsRight[4] += sumsEachYear[4];
		}
	}
	// final print statement for the detailsLabel which has to check again 
	// through if statemnts how it is going to be formatted when printed
	if(subsetPie.contains(pressX,pressY))
	{
		for(int i=0; i<rightWedges.length; i++)
		{
			if(rightWedges[i].contains(pressX,pressY))
			{
				if(currentLabel.contains("Building"))
				{
					detailsLabel.setText("Total building "+utilityNames[i]+
					" usage: "+ String.valueOf(fmt.format(totalsRight[i])));
				}
				if(currentLabel.contains("Month"))
				{
					detailsLabel.setText("Total month "+utilityNames[i]+
					" usage: "+ String.valueOf(fmt.format(totalsRight[i])));
				}
				if(currentLabel.contains("Year"))
				{
					detailsLabel.setText("Total year "+utilityNames[i]+
					" usage: "+ String.valueOf(fmt.format(totalsRight[i])));
				}
			}
		}
	}
	else
	{
		subElectricWedge.setStartAngle(0);
		subElectricWedge.setLength(0);
		subNatGasWedge.setStartAngle(0);
		subNatGasWedge.setLength(0);
		subSteamWedge.setStartAngle(0);
		subSteamWedge.setLength(0);
		subWaterWedge.setStartAngle(0);
		subWaterWedge.setLength(0);
		subWastewaterWedge.setStartAngle(0);
		subWastewaterWedge.setLength(0);
		detailsLabel.setText("");
		subCostLabel.setText("Total Utility Cost");
	}


		
	}

    public static void main(String[] args)
    {
        launch(args);
    }
}
