/******************************************************************************************************************
* File:Plumber.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*   1.1 February 2017 - System A implementation.
*
* Description:
* System A implementation.
*
* Parameters: 		None
*
* Internal Methods:	None
*
******************************************************************************************************************/
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.
import java.text.DecimalFormat;		// This class is used to format and write time in a string format.

public class Plumber
{
   public static void main( String argv[])
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/

		SourceFileReader fileReaderSource = new SourceFileReader("../DataSets/FlightData.dat");
	    BytesToMeasurementsTransformer bytesToMeasurements = new BytesToMeasurementsTransformer();
	   	SimpleDateFormat timeStampFormatter = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");
	   	DecimalFormat temperatureFormatter = new DecimalFormat("000.00000");
	   	DecimalFormat altitudeFormatter = new DecimalFormat("00000.00000");

		SinkMeasurementPrinter sink = new SinkMeasurementPrinter(
				"OutputA.dat",
				"Time:\t\t\t" + "Temperature (C):\t" + "Altitude (m):\t" + "\n",
				(m) -> {
			return timeStampFormatter.format(m.timestamp) + "\t" +
					temperatureFormatter.format(m.temperature) + "\t" +
					altitudeFormatter.format(m.altitude) + "\n";
		});

		TransformMeasurementFilter transformTemperatureAndConvertAltitude = new TransformMeasurementFilter((m) -> {
			m.temperature = (m.temperature - 32) * 5/9; // F -> C
			m.altitude = m.altitude * 0.3048; // Feet to meters.
			return m;
		});

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (sink) which
		* we connect to feetToMeters the fahrenheitToCelsius filter. Then we connect fahrenheitToCelsius to the
		* source filter (fileReaderSource).
		****************************************************************************/

	   	sink.Connect(transformTemperatureAndConvertAltitude);
	   	transformTemperatureAndConvertAltitude.Connect(bytesToMeasurements);
	   	bytesToMeasurements.Connect(fileReaderSource);

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/

	   	fileReaderSource.start();
	   	bytesToMeasurements.start();
	    sink.start();
	   	transformTemperatureAndConvertAltitude.start();
   }
}