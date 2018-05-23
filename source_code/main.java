import java.lang.*;
import java.util.*;

class DomXML {
	public static void main(String[] args) {
		
		parserXML parser = new parserXML("TOC_Arsenal1194_Indente.xml", "newDocXML.xml");

		parser.lancerExploration("corpus.txt");

	}
}