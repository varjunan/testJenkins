package inboundProcessing;

import context.CRContextXML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReadInboundFile {

    public List<CRContextXML> getWcsDataFromXml(String sourceXMLFolder)
            throws ParserConfigurationException, SAXException, IOException, ParseException, XPathExpressionException
    {
        List<CRContextXML> crContextList = new ArrayList<>();

        File folder = new File(sourceXMLFolder);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            String filename = listOfFiles[i].getName();
            if (filename.endsWith(".xml") || filename.endsWith(".XML"))
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true); // never forget this!
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(folder + "/" + filename);
                String nameSpace = doc.getDocumentElement().getNamespaceURI();  // get Namespace of the given xml
                XPathFactory xpathfactory = XPathFactory.newInstance();
                XPath xpath = xpathfactory.newXPath();
                xpath.setNamespaceContext( setNamespaceContext(nameSpace)) ;    // set Namespace to xpath
                crContextList.add(getWCSData(doc, xpath));
            }
        }

		/*				Print list data	to console
				for (CRContextXML crLst : crContextList) {
					System.out.println("ReadInboundFile.getWcsDataFromXml() , " + crLst.getWcsCreationDateTime () + " , " + crLst.getWcsSourceSystem() + " , " + crLst.getWcsExternalCustomerId() + " , " + crLst.getWcsTitle() + " , " + crLst.getWcsFirstName() + " , " + crLst.getWcsLastName() + " , " + crLst.getWcsDOB() + " , " + crLst.getWcsEmail() + " , " + crLst.getWcsAddressFirstName() + " , " + crLst.getWcsAddressLastName() + " , " + crLst.getWcsAddressTitle() + " , " + crLst.getWcsAddressAddressLine1() + " , " + crLst.getWcsAddressAddressLine2() + " , " + crLst.getWcsAddressAddressLine3() + " , " + crLst.getWcsAddressAddressLine4() + " , " + crLst.getWcsAddressCity() + " , " + crLst.getWcsAddressCountry() + " , " + crLst.getWcsAddressCountryCode() + " , " + crLst.getWcsAddressPostalCode() + " , " + crLst.getWcsUserType() + " , " + crLst.getWcsEmailType() + " , " + crLst.getWcsPreferenceTypeMail() + " , " + crLst.getWcsPreferenceTypePhone() + " , " + crLst.getWcsPreferenceMail() + " , " + crLst.getWcsPreferencePhone() + " , " + crLst.getWcsPhone() + " , " + crLst.getWcsPhoneType() + " , " + crLst.getWcsAddressPhone() + " , " + crLst.getWcsAddressPhoneType() +
				" , " + crLst.getWcsPhoneType() + "_" + crLst.getWcsPhone() +
				" , " + crLst.getWcsAddressPhoneType() + "_" + crLst.getWcsAddressPhone() +
				" , " + crLst.getWcsPreferenceTypeMail() + "_" + crLst.getWcsPreferenceMail() +
				" , " + crLst.getWcsPreferenceTypePhone() + "_" + crLst.getWcsPreferencePhone()
				);
			}
		 */
        return crContextList;
    }


    private static CRContextXML getWCSData(Document doc, XPath xpath) throws ParseException, XPathExpressionException {

        CRContextXML contexttemp = new CRContextXML();
        contexttemp.setWcsCreationDateTime(dateMilliSeconds(extractData(doc, xpath,  "/service:CustomerInfo/Header/CreationDateTime/text()")));
        contexttemp.setWcsSourceSystem(extractData(doc, xpath,  "/service:CustomerInfo/Header/SourceSystem/text()"));
        contexttemp.setWcsExternalCustomerId(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/ExternalCustomerId/text()"));
        contexttemp.setWcsPhone(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Telephone/text()"));
        contexttemp.setWcsTitle(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Title/text()"));
        contexttemp.setWcsFirstName(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/FirstName/text()"));
        contexttemp.setWcsLastName(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/LastName/text()"));
        contexttemp.setWcsDOB(dateDdMMMyy(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Dob/text()")));
        contexttemp.setWcsEmail(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Email/text()"));
        contexttemp.setWcsAddressFirstName(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/FirstName/text()"));
        contexttemp.setWcsAddressLastName(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/LastName/text()"));
        contexttemp.setWcsAddressTitle(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/Title/text()"));
        contexttemp.setWcsAddressAddressLine1(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/AddressLine1/text()"));
        contexttemp.setWcsAddressAddressLine2(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/AddressLine2/text()"));
        contexttemp.setWcsAddressAddressLine3(Objects.toString(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/AddressLine3/text()"), ""));
        contexttemp.setWcsAddressCity(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/City/text()"));
        contexttemp.setWcsAddressCountry(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/Country/text()"));
        contexttemp.setWcsAddressCountryCode(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/CountryCode/text()"));
        contexttemp.setWcsAddressPostalCode(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/PostalCode/text()"));
        contexttemp.setWcsAddressPhone(extractData(doc, xpath,  "/service:CustomerInfo/CustomerDetails/Address/Telephone/text()"));
        contexttemp.setWcsPreferenceMail(extractData(doc, xpath,  "/service:CustomerInfo/CsrContactPrefs/Mail/text()"));
        contexttemp.setWcsPreferencePhone(extractData(doc, xpath,  "/service:CustomerInfo/CsrContactPrefs/Phone/text()"));
        contexttemp.setWcsUserType(extractData(doc, xpath,  "/service:CustomerInfo/service:UserData/service:UserDataField[@name='userType']"));
        contexttemp.setWcsEmailType("Registered");
        contexttemp.setWcsAddressType("Registered");

        contexttemp.setWcsPhoneType("Registered");
        contexttemp.setWcsAddressPhoneType("Residential");

        contexttemp.setWcsPreferenceTypeMail("MailOnly");
        contexttemp.setWcsPreferenceTypePhone("PhoneOnly");



        // if WCS xml type is guest, then assign values accordingly as below.
        if (contexttemp.getWcsUserType() != null && contexttemp.getWcsUserType().equalsIgnoreCase("G") ) {
            contexttemp.setWcsFirstName(contexttemp.getWcsAddressFirstName());
            contexttemp.setWcsLastName(contexttemp.getWcsAddressLastName());
            contexttemp.setWcsTitle(contexttemp.getWcsAddressTitle());
            contexttemp.setWcsFirstName(contexttemp.getWcsAddressFirstName());
            contexttemp.setWcsFirstName(contexttemp.getWcsAddressFirstName());
            contexttemp.setWcsEmail(extractData(doc, xpath, "/service:CustomerInfo/service:UserData/service:UserDataField[@name='guestEmail']"));
            contexttemp.setWcsEmailType("Guest");
            contexttemp.setWcsPhoneType("Residential");
            contexttemp.setWcsAddressPhoneType("Residential");
            contexttemp.setWcsPhone(contexttemp.getWcsAddressPhone());
        }

        return contexttemp;
    }

    private static String extractData(Document doc, XPath xpath, String xPathExpression)
            throws XPathExpressionException {

        XPathExpression expr = xpath.compile(xPathExpression);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        String returnData = null;

        if (nodes.getLength() > 0)
            returnData = nodes.item(0).getTextContent();
        return returnData;


    }



    public static String dateMilliSeconds(String dateString) throws ParseException {
        Date parsedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
        String formatedDate = new SimpleDateFormat("dd-MMM-yy HH.mm.ss.SSSSSSSSS").format(parsedDate).toUpperCase();
        return formatedDate;
    }

    public static String dateDdMMMyy(String dateString) throws ParseException {
        String formatedDate = null;
        if (dateString != null ) {
            Date parsedDate = new SimpleDateFormat("MM-dd-yyyy").parse(dateString);
            formatedDate = new SimpleDateFormat("dd-MMM-yy").format(parsedDate).toUpperCase();
        }
        return formatedDate;
    }

    public static NamespaceContext setNamespaceContext (final String nameSpace) {
        NamespaceContext namespaceContext = new NamespaceContext() {	// set namespace
            @Override
            public Iterator<?> getPrefixes(String arg0) {
                return null;
            }
            @Override
            public String getPrefix(String arg0) {
                return null;
            }
            @Override
            public String getNamespaceURI(String arg0) {
                return nameSpace;
            }
        };

        return namespaceContext;

    }
}
