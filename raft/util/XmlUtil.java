package raft.util;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Xml handling utils.
 * 
 *
 */
public class XmlUtil {

	/**
	 * Create a map based on a xml file and a xpath expression and an attribute.
	 * Treat attribute's value as map's key, treat xpath expression represented node's text content as map's value.
	 * 
	 * @param xmlFile handled xml file
	 * @param xpathExp xpath express, such as "//var"
	 * @param arrtibute attribute, such as "name"
	 * @return created Map object
	 * @throws Exception
	 */
	public static Map<String, String> readXmlToMap(String xmlFile, String xpathExp, String arrtibute) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
	    
	    DocumentBuilder db = factory.newDocumentBuilder();
	    Document xmlDoc = null;
	    try {
	    	xmlDoc = db.parse(new File(xmlFile));
	    } catch(SAXException e) {
	    	if( "Premature end of file.".equals(e.getMessage()) )
	    		System.out.println("Maybe your local data file has no content.");
	    	//e.printStackTrace();
	    	return new HashMap<String, String>();
	    }
	    XPathFactory xpathFactory=XPathFactory.newInstance();
        XPath xpath=xpathFactory.newXPath();
        NodeList varList = (NodeList) xpath.evaluate(xpathExp, xmlDoc, XPathConstants.NODESET);
        Map<String, String> dataMap = new HashMap<String, String>();
        for(int i=0; i<varList.getLength(); i++) 
        	dataMap.put(varList.item(i).getAttributes().getNamedItem(arrtibute).getNodeValue(), varList.item(i).getTextContent());
        
	    return dataMap;
	}
    
}
