import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CalendarParseMain {

	public enum CalendarParserCommand { displayday, quit };
	public enum CalendarFormat { duke, google };
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, ParseException {

		final JFileChooser fc = new JFileChooser("Select a calendar file to parse");
		fc.setCurrentDirectory(new File("."));
		int returnVal = fc.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			System.out.println("User cancelled file open!");
			System.exit(0);
		}
		
		CalendarFormat fileFormat = (CalendarFormat) JOptionPane.showInputDialog(
		                    null,
		                    "What data format is the file?",
		                    "File Format",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    CalendarFormat.values(),
		                    "duke");
		if(fileFormat == null) {
			System.out.println("User cancelled format dialog!");
			System.exit(0);
		}
		
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Ready for commands! Available commands: ");
		System.out.println("quit");
		System.out.println("displayday YYYY MM DD");
		System.out.print("> ");
		while(input.hasNextLine()) {
			String command = input.next();
			CalendarParserCommand c;
			try {
				 c = CalendarParserCommand.valueOf(command);
			} catch (IllegalArgumentException e) {
				System.out.println("Unknown command: " + command);
				continue;
			}
			switch(c) {
			case displayday:
				String year = input.next();
				String month = input.next();
				String day = input.next();

				File fXmlFile = fc.getSelectedFile();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setNamespaceAware(false); // not ideal, but if you leave this on it's annoying

				XPathFactory factory = XPathFactory.newInstance();
				XPath xpathFactory = factory.newXPath();

				switch(fileFormat) {
				case duke:
				{	
					
					String xpath = String.format("//event[start/fourdigityear='%s' and start/twodigitmonth='%s' and start/twodigitday='%s']/summary/text()",year, month, day);
					XPathExpression expr = xpathFactory.compile(xpath);

					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();

					Object result = expr.evaluate(doc, XPathConstants.NODESET);
					NodeList nodes = (NodeList) result;

					for (int i = 0; i < nodes.getLength(); i++) {
						System.out.println(nodes.item(i).getNodeValue()); 
					}
				}
					break;
				case google:
				{
					String xpath = "//entry";
					XPathExpression entryExpr = xpathFactory.compile(xpath);
					XPathExpression titleExpr = xpathFactory.compile("title");
					XPathExpression summaryExpr = xpathFactory.compile("summary");
					SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date targetDate = inputDateFormat.parse(year+"-"+month+"-"+day);
					SimpleDateFormat googleDateFormat = new SimpleDateFormat("'When: 'EEE MMM d, y");
					
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();

					Object result = entryExpr.evaluate(doc, XPathConstants.NODESET);
					NodeList nodes = (NodeList) result;
					
					for (int i = 0; i < nodes.getLength(); i++) {
						Node title = (Node) titleExpr.evaluate(nodes.item(i), XPathConstants.NODE);
						Node summary = (Node) summaryExpr.evaluate(nodes.item(i), XPathConstants.NODE);
						
						
						try {
							
							Date eventDate = googleDateFormat.parse(summary.getTextContent());
							if(eventDate.equals(targetDate)) {
								System.out.println(title.getTextContent()); 								
							}
									
							
						} catch (ParseException e) {
							//if we can't parse the date, ignore the event
						}
						 
						
					}
				}	
					break;
				}
				break;
			case quit:
				System.out.print("Exiting normally.");
				System.exit(0);
				break;
			}
			System.out.print("> ");
		}
	}

}
