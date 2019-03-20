package stepDefinition;

import context.CRContextXML;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import inboundProcessing.ReadInboundFile;
import utilities.PropertiesFileReader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.text.ParseException;


public class DataBaseValidationStepDef {


    private final ReadInboundFile readInboundFile;

    private PropertiesFileReader obj = new PropertiesFileReader();  // Initialize the PropertiesFileReader class with object

    /*********************************************************************************************************************************/

    public DataBaseValidationStepDef(  ReadInboundFile readInboundFile) {

        this.readInboundFile = readInboundFile;

    }

    /*********************************************************************************************************************************/

    @Given("^I have source (.*) XMLs in Inbound$")
    public void SourceToQueue(String interfaceID) throws Throwable {



        System.out.println("Messages has been posted to Queue");
    }

    /*********************************************************************************************************************************/

    @Then("^the (.*) customer data should load into the CDF Master table$")
    public void wcsDataToCdfMasterTable(String interfaceID) throws Throwable {


        System.out.println(interfaceID);

        Properties properties = obj.getProperty(); // initialize properties class
        String xmlPath = properties.getProperty("XmlPath_" + interfaceID);                // XMLs file location



        List<CRContextXML> contextListXML = readInboundFile.getWcsDataFromXml(xmlPath);        // Read the XMLs and get the FirstName, LastName etc from it.

        //convert List to map for processing data. If we have duplicate values on WcsExternalCustomerId,
        //extract the details based on highest CreationDateTime from XML tags
        Map<String, CRContextXML> mapCRContextXML = createMapforXMLData(contextListXML);
        //convert DB data List to map for processing data.


        for (CRContextXML wcsDataXML : mapCRContextXML.values()) {

            System.out.println("DataBaseValidationStepDef.WcsDataToCdfMasterTable(): " + wcsDataXML.getWcsExternalCustomerId() + " " +
                    wcsDataXML.getWcsDOB() + " " + wcsDataXML.getWcsCreationDateTime() + " " + wcsDataXML.getWcsLastName() + " "
                    + wcsDataXML.getWcsPreferenceTypeMail() + " " + " " + wcsDataXML.getWcsCreationDateTime() + " --- "
            );


        }

    }


    /*********************************************************************************************************************************/

    private static Map<String, CRContextXML> createMapforXMLData(List<CRContextXML> contextListXML) {

        return contextListXML.stream()
                .collect(Collectors.toMap
                        (x -> x.getWcsExternalCustomerId(), x -> x, (x1, x2) -> {
                            try {
                                if (convertToDate(x1.getWcsCreationDateTime()).compareTo(convertToDate(x2.getWcsCreationDateTime())) < 0)
                                    x1 = x2;    //If Date1 is less than date2, use set2 data.
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return x1;
                        }));

    }

    private static Date convertToDate(String dateValue) throws ParseException {
        return new SimpleDateFormat("dd-MMM-yy HH.mm.ss.SSSSSSSSS").parse(dateValue);
    }
}