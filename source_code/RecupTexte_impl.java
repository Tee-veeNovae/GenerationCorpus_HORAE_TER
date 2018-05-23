import java.io.*;
import java.lang.String;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Random;

class RecupTexte_impl implements RecupTexte_itf
{

	// adresses relatives du répertoire racine contenant les fichiers de textes sources
	static final String adresseRelativeRepTextesSources = "textes_sources";

	private ArrayList<Integer> 		listeLongueurs; 		//liste des longueurs des versets
	private ArrayList<Integer>		listeVersets;			//liste des numéros des versets dans un chapitre
	private ArrayList<String>		listeAdressesFichiers;	//liste des adresses des fichiers contenant les versets pour un chapitre






	// constructeur
	public RecupTexte_impl() {
		listeLongueurs 			= new ArrayList<Integer>();
		listeVersets 			= new ArrayList<Integer>();
		listeAdressesFichiers 	= new ArrayList<String>();
	}










	public Texte_type getVersetBible(String livre, int chapitre, int verset) {
		String 	filename 	= adresseRelativeRepTextesSources+"/bible_txt/"+livre+"/"+chapitre+".txt";
		int 	line 		= 1;
		boolean lineReached = false;	// permet de sortir de la boucle une fois le verset trouvé
		String 	foundString = "ERREUR : Verset demandé non disponible";	// valeur d'erreur par défaut

		try {
			FileReader 		filereader 		= new FileReader(filename);
			BufferedReader 	buffer 			= new BufferedReader(filereader);
			String 			currentLine;

			while ((currentLine = buffer.readLine()) != null && !lineReached) {
				if (line != verset) {
					++line;
				}
				else {
					lineReached = true;
					foundString = currentLine;
				}
			}

			buffer.close();
			
			return new Texte_type(lineReached ? "verset" : "erreur", livre, chapitre, verset, foundString);
		} catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			return new Texte_type("erreur", "ERREUR LECTURE FICHIER");
		}
	}












	private void chargerIndexLongueurs() {
		// clear des listes
		listeLongueurs.clear();
		listeVersets.clear();
		listeAdressesFichiers.clear();

		try {
			FileReader 		filereader 		= new FileReader(adresseRelativeRepTextesSources + "/index_longueurs.txt");
			BufferedReader 	buffer 			= new BufferedReader(filereader);
			String 			currentLine;
			int 			indiceTypeInfo 	= 0;	//permet d'alterner toutes les 3 lignes entre les différents types d'informations pour entrer les bonnes valeurs dans les listes correspondantes

			while ((currentLine = buffer.readLine()) != null) {
				if (indiceTypeInfo == 0) {	// longueur d'un verset
					listeLongueurs.add(Integer.parseInt(currentLine));
					indiceTypeInfo++;
				}
				else if (indiceTypeInfo == 1) {	// numéro du verset
					listeVersets.add(Integer.parseInt(currentLine));
					indiceTypeInfo++;
				}
				else {	// adresse du fichier contenant le verset
					listeAdressesFichiers.add(currentLine);
					indiceTypeInfo = 0;
				}
			}

			buffer.close();
			

			// vérification que les 3 listes sont bien de tailles égales, sinon il y a problème
			if (listeAdressesFichiers.size() == listeVersets.size() && listeVersets.size() == listeLongueurs.size()) {
				System.out.println("index des longueurs chargé");
			}
			else {
				System.out.println("erreur de chargement de l'index des longueurs : tailles des listes inégales");
			}


		} catch (IOException e) {
			System.out.println("Erreur lecture fichier index");
		}

	}













	public Texte_type getVersetParLongueur(int longueur, int ecartAcceptable) {

		// chargement de l'index des longueurs si ça n'est pas déjà fait
		if (listeLongueurs.isEmpty()) {
			chargerIndexLongueurs();
		}

		ArrayList<Integer> valeursExactes 	= new ArrayList<Integer>(); //indices des listes d'index où le verset est de taille exacte
		ArrayList<Integer> assezProche 		= new ArrayList<Integer>();	//indices des listes d'index où le verset est de taille acceptée selon la flexibilité donnée

		// parcours de l'index et trouve les versets de longueur égales ou proches
		for (int i=0; i < listeLongueurs.size(); i += 3) {
			if (listeLongueurs.get(i) == longueur) {
				valeursExactes.add(i);
			}
			else if (listeLongueurs.get(i)-ecartAcceptable <= longueur 
						&& listeLongueurs.get(i)+ecartAcceptable >= longueur) {
				assezProche.add(i);
			}
		}

		// choix au hasard d'un verset parmi ceux trouvés
		Random 	RNG 				= new Random();
		int 	valeurRandom 		= 0;
		int 	indiceVersetChoisi 	= -1;	// valeur -1 d'erreur par défaut

		if (!valeursExactes.isEmpty()) {
			valeurRandom 		= RNG.nextInt(valeursExactes.size());
			indiceVersetChoisi 	= valeursExactes.get(valeurRandom);
		}
		else if (!assezProche.isEmpty()) {
			valeurRandom 		= RNG.nextInt(assezProche.size());
			indiceVersetChoisi 	= assezProche.get(valeurRandom);
		}

		// récupération et retour du verset
		Texte_type 	versetChoisi;
		String		livreBible;
		int			numChapitre;

		if (indiceVersetChoisi == -1) {
			versetChoisi = new Texte_type("erreur", "ERREUR : VERSET DE TAILLE "+longueur+" NON TROUVÉ, MÊME MALGRÉ UNE LAXITÉ DE "+ecartAcceptable);
		}
		else {
			livreBible 	= listeAdressesFichiers.get(indiceVersetChoisi).split("/")[0];
			numChapitre = Integer.parseInt(listeAdressesFichiers.get(indiceVersetChoisi).split("/")[1].split("\\.")[0]);

			versetChoisi = getVersetBible(livreBible, numChapitre, listeVersets.get(indiceVersetChoisi));
		}

		return versetChoisi;

	}







	public Texte_type getVersetParLongueur(int longueur) {
		return getVersetParLongueur(longueur, 0);
	}









	public Texte_type getObsecroTe() {
		//récupère l'intéralité de la prière Obsecro Te dans le fichier et la retourne

		try {
			FileReader 		filereader 		= new FileReader(adresseRelativeRepTextesSources + "/autres_txt/obsecro_te.txt");
			BufferedReader 	buffer 			= new BufferedReader(filereader);
			String 			currentLine;
			String 			contenu 		= "";

			while ((currentLine = buffer.readLine()) != null) {
				contenu = contenu + currentLine + "\n";
			}

			buffer.close();
			
			return new Texte_type("obsecro_te", contenu);
		} catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			return new Texte_type("erreur", "ERREUR LECTURE FICHIER");
		}
	}









	public Texte_type getPaterNostre() {
		//récupère l'intéralité de la prière Pater Nostre dans le fichier et la retourne

		try {
			FileReader 		filereader 		= new FileReader(adresseRelativeRepTextesSources + "/autres_txt/pater_nostre.txt");
			BufferedReader 	buffer 			= new BufferedReader(filereader);
			String 			currentLine;
			String 			contenu 		= "";

			while ((currentLine = buffer.readLine()) != null) {
				contenu = contenu + currentLine + "\n";
			}

			buffer.close();
			
			return new Texte_type("pater_nostre", contenu);
		} catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			return new Texte_type("erreur", "ERREUR LECTURE FICHIER");
		}
	}









	public Texte_type getGloriaPatri() {
		//récupère l'intéralité de la prière Gloria Patri dans le fichier et la retourne

		try {
			FileReader 		filereader 		= new FileReader(adresseRelativeRepTextesSources + "/autres_txt/gloria_patri.txt");
			BufferedReader 	buffer 			= new BufferedReader(filereader);
			String 			currentLine;
			String 			contenu 		= "";		

			while ((currentLine = buffer.readLine()) != null) {
				contenu = contenu + currentLine + "\n";
			}

			buffer.close();
			
			return new Texte_type("gloria_patri", contenu);
		} catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			return new Texte_type("erreur", "ERREUR LECTURE FICHIER");
		}
	}







	public Texte_type getAntienne() {
		//récupère toutes les antiennes dans le fichier et en retourne une au hasard

		try {
			FileReader 			filereader 		= new FileReader(adresseRelativeRepTextesSources + "/autres_txt/antiennes.txt");
			BufferedReader 		buffer 			= new BufferedReader(filereader);
			String 				currentLine;
			ArrayList<String> 	listeAntiennes 	= new ArrayList<String>();

			while ((currentLine = buffer.readLine()) != null) {
				listeAntiennes.add(currentLine);
			}

			buffer.close();

			// valeur aléatoire
			Random 	RNG 			= new Random();
			int 	valeurRandom 	= RNG.nextInt(listeAntiennes.size());

			return new Texte_type("antienne", listeAntiennes.get(valeurRandom));
		} catch (IOException e) {
			System.out.println("Erreur lecture fichier");
			return new Texte_type("erreur", "ERREUR LECTURE FICHIER");
		}
	}





}
