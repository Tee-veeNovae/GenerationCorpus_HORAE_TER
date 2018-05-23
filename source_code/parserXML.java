import javax.xml.parsers.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.lang.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.util.regex.*;
import java.util.Scanner;

class parserXML {

	/********
	Variables
	********/

	//Permet de lire et explorer un fichier XML (fichier en latin)
	private DocumentBuilderFactory factoryRead; 
	private DocumentBuilder builderRead;
	private Document documentRead;
	
	//Permet d'écrire un nouveau fichier XML (fichier en français)
	private DocumentBuilderFactory factoryWrite;
	private DocumentBuilder builderWrite;
	private Document documentWrite;
	private TransformerFactory transformerFactory;
    private Transformer transformer;
    private DOMSource source;
    private StreamResult sortie;

    //Permet d'effectuer des recherches d'expression régulière
    private Pattern pattern;
    private Matcher matcher;

    //Fichier qui permet d'obtenir le corpus tiré la création du XML en français 
    private FileWriter fw;

    //Objet qui permet de récupérer des textes en français
    private RecupTexte_itf txtRecup;

    /****************************************************************************
    Constructeur, prend le nom du fichier XML à explorer et le nom du fichier XML
    ****************************************************************************/
	public parserXML(String fichierXML, String fichierXMLSortie){
		try{

			//Ouverture pour la lecture du XML à explorer
			factoryRead = DocumentBuilderFactory.newInstance();
			builderRead = factoryRead.newDocumentBuilder();
			documentRead = builderRead.parse(new File(fichierXML));

			//Ouverture pour l'écriture du nouveau XML
			factoryWrite = DocumentBuilderFactory.newInstance();
			builderWrite = factoryWrite.newDocumentBuilder();
			documentWrite = builderWrite.newDocument();
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			source = new DOMSource(documentWrite);
			sortie = new StreamResult(new File(fichierXMLSortie));

			//Initialisation du nouveau fichier XML
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		    //RegExpr qui contient au minimum 2 caractères blanc
		    //Permet de déterminer si un TEXT_NODE contient du texte ou juste des espaces blanc
		    pattern = Pattern.compile("\\s\\s+");

		    txtRecup = new RecupTexte_impl();
		
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch (TransformerConfigurationException e) {
	    e.printStackTrace();
		}

		//Affiche les informations du fichier exploré
		System.out.println("Exploration du fichier XML : " + fichierXML);
		System.out.println("Version XML : " + documentRead.getXmlVersion());
		System.out.println("Encodage : " + documentRead.getXmlEncoding());

	}

	/*********************************************************
	Méthodes qui permet de lancer l'exploration du fichier XML
	*********************************************************/
	public void lancerExploration(String fichierTXTSortie){
		try{
			Element racine = documentRead.getDocumentElement(); //Réupération de l'élément racine
			Node temp = documentWrite.importNode((Node)racine, false); //Création d'une copie de l'élément racine
			documentWrite.appendChild(temp); //Ecriture de la racine dans le nouveau XML
			System.out.println(temp.getNodeName()); 
			fw = new FileWriter(new File(fichierTXTSortie)); //Création du fichier contenant le corpus 
			
			explorationXML((Node)racine, (Node)documentWrite.getDocumentElement()); //Lancement d'une fonction récursive d'exploration
			 
			fw.close();
		
			transformer.transform(source, sortie);
		}catch (TransformerException e) {
	    	e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/*******************************************
	Méthode récursive qui explore le fichier XML
	*******************************************/
	private void explorationXML(Node baliseRead, Node baliseWrite){
		Node temp; //Noeud temporaire qui récupère le noeud actuellement exploré
		NodeList baliseFilles = baliseRead.getChildNodes(); //Noeud fils du noeud exploré
		NodeList temp2; 
		int nbBaliseFilles = baliseFilles.getLength(); //Nombre de noeuds fils

		for (int i = 0; i < nbBaliseFilles; ++i) { //Pour chaque noeud fils
			
			temp = documentWrite.importNode(baliseFilles.item(i), false); //Récupération du noeud fils n°i sans ses noeuds fils
					
			//Si le noeud est de type texte
			if(temp.getNodeType() == Node.TEXT_NODE) {
				
				
				matcher = pattern.matcher(temp.getTextContent()); //On compare la RegExpr avec le contenu du noeud
				//Si le noeud contient du texte
				if(!matcher.matches()){
					
					//temp.setTextContent(String.valueOf(temp.getTextContent().length()) + " STARGATE");
					System.out.println("//////////////////////////////////////////////////////////////////");
					System.out.println(temp.getTextContent());
					System.out.println("//////////////////////////////////////////////////////////////////");
				

					//Selon le type de noeud parent
					switch(baliseRead.getNodeName()) {

						case "hi":

							temp.setTextContent(getHiTrad(temp.getTextContent())); //Récupère une pseudo traduction des types de texte en latin
						
							break;

						case "s":

							if(baliseRead.hasAttributes()){ //Test sur les attributs
								NamedNodeMap attributes = baliseRead.getAttributes(); // récupère liste des attributs
								for(int j = 0; j < attributes.getLength(); ++j){
									if(attributes.item(j).getNodeName() == "corresp"){ // si on a un attribut corresp
										String val = attributes.item(j).getNodeValue(); // récupère valeur de l'attribut

										//System.out.println(attributes.item(j).getNodeName()+" : "+attributes.item(j).getNodeValue());
										temp.setTextContent(getTextCorresp(val));
									}
								}
							}else {
								//Récupère un texte de la longueur de texte en latin
								temp.setTextContent(txtRecup.getVersetParLongueur(temp.getTextContent().length(), 50).getContenu()); 
							}

							break;

						default :
							//Récupère un texte de la longueur de texte en latin
							temp.setTextContent(txtRecup.getVersetParLongueur(temp.getTextContent().length(), 50).getContenu());

							break;
					}

					try{
						fw.write(temp.getTextContent()); //On écrit dans le corpus le texte
					}catch (IOException e){
						e.printStackTrace();
					}
				}
				
			}
			
			//On ajoute le noeud dans le nouveau XML			
			baliseWrite.appendChild(temp);

			temp2 = baliseWrite.getChildNodes();
			explorationXML(baliseFilles.item(i), temp2.item(i)); //On continue l'exploration
		}
	}


	/**************************************************************************************************
	Méthode qui récupère les données interressantes d'un attribut corresp et recherche le texte associé 
	**************************************************************************************************/
	public String getTextCorresp(String attributesValue){
		String typeText = "";
		String chapitre = "";
		String verset = "";

		String texte;
		boolean afterVirgule = false; // Si on a passé une virgule
		for (int i = 0; i < attributesValue.length(); ++i){ // On parcourt le string 
			if(Character.isDigit(attributesValue.charAt(i))){ // Si on a un chiffre
				if(afterVirgule){ // situé après la virgule
					verset += attributesValue.charAt(i); 
				}else{
					chapitre += attributesValue.charAt(i);
				}
			}else{
				if(Character.isLetter(attributesValue.charAt(i))){ // Si on a une lettre
					typeText += attributesValue.charAt(i);
				}else {
					if(attributesValue.charAt(i) == ','){ //Si on arrive à la virgule
						afterVirgule = true;
					}
				}
			}
			
		}

		//System.out.println(typeText + " " + chapitre + " " + verset);

		texte = analyseCorresp(typeText, chapitre, verset);

		return (texte);


	}


	/***********************************************************************************
	Méthode qui récupère du texte en fonction du type de texte, du chapitre et du verset
	***********************************************************************************/
	private String analyseCorresp(String typeText, String chapitre, String verset){

		String nomFichier;
		Scanner scan;
		int num = 0;
		String result = "STARGATE";

		switch(typeText){

			case "Jn" :

				result = txtRecup.getVersetBible("jean", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();

				break;

			case "Lc" :
			
				result = txtRecup.getVersetBible("luc", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();

				break;

			case "Mt" :

				result = txtRecup.getVersetBible("matthieu", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();
			
				break;

			case "Mc" :

				result = txtRecup.getVersetBible("marc", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();
			
				break;

			/*case "Obs" :
			
				result = txtRecup.getVersetBible("psaumes", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();

				break;*/

			case "Ps" :

				result = txtRecup.getVersetBible("psaumes", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();

				break;

			/*case "Sir" :

				result = txtRecup.getVersetBible("psaumes", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();
			
				break;*/

			case "Dan" :

				result = txtRecup.getVersetBible("daniel", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();
			
				break;

			case "Cant" :

				result = txtRecup.getVersetBible("cantiques", Integer.parseInt(chapitre), Integer.parseInt(verset)).getContenu();
			
				break;

			default :

				result = txtRecup.getVersetParLongueur(100,50).getContenu();

				break;
		}

		return result;
	}

	/***********************************************************************
	Méthode de traduction des types de textes inscrit dans un livre d'heures
	***********************************************************************/
	public String getHiTrad(String text){
		return (" "+text+" ");
	}

}

