## Système de fichiers de la base de données de textes:

**autres_txt** contient les fichiers texte des prières "Obsecro Te", "Pater Nostre" et "Gloria Patri", ainsi que des antiennes.

**bible-en-ligne** contient les fichiers texte de la Bible récupérés du site [bible-en-ligne.net](https://www.bible-en-ligne.net/)

**bible_notfrombibleenligne** contient les fichiers texte des quelques livres qui n'étaient pas présent sur bible-en-ligne.net, et ont été récupérés sur [Mission-Web](https://missionweb.free.fr/bible.php). Ils ont été copiés et traités à la main, ces fichiers sont donc directement utilisables

**bible_txt** contient les fichiers texte finaux de la Bible utilisés dans le programme, traités avec le script Python textextracting.py

## Instructions de reproduction de la récupération et du traitement du texte de la Bible sur bible-en-ligne.net

### Récupération des fichiers de base :

1)	Récupérer l'URL d'un chapitre sur bible-en-ligne.net
	ex : `http://www.bible-en-ligne.net/bible,01O-1,genese.php`

2)	Coller l'URL dans l'API web de Boilerpipe `https://boilerpipe-web.appspot.com/`
	utiliser les paramètres : `Extractor : ArticleExtractor`, `Output mode : HTML (extract fragment)`

3)	Enregistrer le contenu de la page obtenue sous forme de fichier texte (option "Fichier texte" lors de l'enregistrement sur Mozilla Firefox)

### Classement et traitement :

1)	Le fichier obtenu précédemment doit être rangé dans bible-en-ligne etdans un dossier nommé selon le livre Biblique duquel il provient, et nommé `n.txt`, avec n le numéro du chapitre. Par exemple, le 4e chapitre du livre "Amos" est stocké dans `bible-en-ligne/amos/4.txt`.

> Note : Nous utilisons une nomenclature des noms des livres basées sur les URLs de bible-en-ligne, tout en minuscules et sans accents, et avec les quelques erreurs qui semblent avoir été commises sur ce site ("agee", "esaie").

2)	Créer le dossier bible\_txt et copier le contenu de bible-en-ligne dans celui-ci. Nous avons choisi d'adopter cette méthode du fait que la fonction d'ouverture de fichiers en écriture de Python que nous avons utilisé ne créé pas les répertoires et fichiers s'ils n'existent pas, mais renvoie une erreur. Le contenu de bible_txt doit refleter celui de bible-en-ligne, mais sera traité par le script.

3)	Placer le fichier textextracting.py au même niveau que bible_txt et bible-en-ligne et l'exécuter

4)	Si tout s'est passé correctement, les fichiers de bible_txt seront prêt à l'emploi par notre programme.

> Note : Ne pas oublier d'ajouter au répertoire bible\_txt le contenu de bible_notfrombibleenligne pour compléter la Bible.

### Création de l'index :

1)	Placer le fichier creationindexlongueurs.py au même niveau que bible_txt et l'exécuter

2)	Le fichier index_longueurs.txt utilisable par notre programme sera créé.

## Référence : Nomenclature des livres de la Bible

Utile pour la création des répertoires ainsi que l'appel de la méthode "getVersetBible" de RecupTexte. Voici les mots-clés acceptés :

1-chroniques

1-corinthiens

1-jean

1-machabees

1-pierre

1-rois

1-samuel

1-thessaloniciens

1-timothee

2-chroniques

2-corinthiens

2-jean

2-machabees

2-pierre

2-rois

2-samuel

2-thessaloniciens

2-timothee

3-jean

abdias

actes

agee

amos

apocalypse

baruch

cantiques

colossiens

daniel

deuteronome

ephesiens

esaie

esdras

esther

exode

ezechiel

galates

genese

habakuk

hebreux

jacques

jean

jeremie

job

joel

jonas

josue

jude

judith

juges

lamentations

l-ecclesiaste

les-proverbes

levitique

luc

malachie

marc

matthieu

michee

nahum

nehemie

nombres

osee

philemon

philipiens

psaumes

romains

ruth

sagesse

siracide

sophonie

tite

tobie

zacharie

