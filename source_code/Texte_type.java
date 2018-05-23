import java.lang.String;

class Texte_type
{
	private String type_;
	private String livreBible_;
	private int chapitreBible_;
	private int versetBible_;
	private String contenu_;

	public Texte_type(String type, String livre, int chapitre, int verset, String contenu) {
		type_ = type;
		livreBible_ = livre;
		chapitreBible_ = chapitre;
		versetBible_ = verset;
		contenu_ = contenu;
	}

	public Texte_type(String type, String contenu) {
		type_ = type;
		livreBible_ = "";
		chapitreBible_ = 0;
		versetBible_ = 0;
		contenu_ = contenu;
	}

	public String getType() { return type_; }
	public String getLivre() { return livreBible_; }
	public int getChapitre() { return chapitreBible_; }
	public int getVerset() { return versetBible_; }
	public String getContenu() { return contenu_; }

}
