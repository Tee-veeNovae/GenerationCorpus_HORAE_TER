

interface RecupTexte_itf {

	// requête d'un verset biblique particulier en donnant le nom de son livre (respecter la nomenclature !), son numéro chapitre, et son numéro dans le chapitre
	public Texte_type getVersetBible(String livre, int chapitre, int verset);
	
	// requête d'un verset aléatoire d'une longueur (en caractères) donnée, avec un écart de longueur  acceptable donnée
	public Texte_type getVersetParLongueur(int longueur, int ecartAcceptable);

	// requête d'un verset aléatoire d'une longueur exacte (en caractère) donnée, sans laxité
	public Texte_type getVersetParLongueur(int longueur);

	// requête de la prière Obsecro Te, en intégralité
	public Texte_type getObsecroTe();

	// requête de la prière Pater Nostre, en intégralité
	public Texte_type getPaterNostre();

	// requête de la prière Gloria Patri, en intégralité
	public Texte_type getGloriaPatri();

	// requête d'une antienne aléatoire parmi celles disponibles
	public Texte_type getAntienne();

}
