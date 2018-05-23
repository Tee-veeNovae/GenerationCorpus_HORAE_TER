# permet de convertir des fichiers txt bruts tirés d'une page de bible-en-ligne.net
# passé dans l'API de Boilerpipe en fichiers TXT formattés.
# Comme une page de bible-en-ligne représente le contenu d'un chapitre, chaque ligne des fichiers txt correspond à un verset
# par exemple, la ligne 1 correspond au premier verset, etc

# Cette version permet de gérer tous les fichiers d'un dossier à la fois, selon notre nomenclature (fichiers nommés par numéro de chapitre)

# PREREQUIS : placer ce fichier au même niveau que les dossiers "bible-en-ligne" et "bible_txt"

import os


current_working_directory = os.path.dirname(os.path.abspath(__file__)).replace("\\", "/")

for nom_dossier in next(os.walk(current_working_directory + "/bible-en-ligne/"))[1] :
    nb_fichiers = len([name for name in os.listdir(current_working_directory + "/bible-en-ligne/" + nom_dossier)])
    for i in range(1, nb_fichiers+1) :
        
        input_file_path = current_working_directory+"/bible-en-ligne/"+nom_dossier+"/"+str(i)+".txt"
        output_file_path = current_working_directory+"/bible_txt/"+nom_dossier+"/"+str(i)+".txt"

        # ouvre fichiers en lecture/écriture
        premiere_ligne = True
        skip_first_return = False
        stock_str = ""
        i = 0
        skip_first_two_chars = 0 # compteur qui parmet d'éviter le problème où toutes les lignes commencaient par 2 espaces
        located_in_header = True # permet de savoir si le curseur se trouve dans l'en-tête (liens vers autres psaumes) ou non
        with open(input_file_path, 'r', encoding='utf-8') as input_f :
            with open(output_file_path, 'w', encoding='utf-8') as output_f :
                # on sépare le fichier en lignes afin de traîter des chaînes, plus pratiques
                for line in input_f:
                    if not premiere_ligne or line[0] != "*" : # afin d'éviter la première ligne "chapitre" de certains fichiers
                    # else :
                        # conserve les lignes vides pour retour à la ligne
                        if line[0] == "\n" :
                            skip_first_two_chars = 0
                            if not skip_first_return : # permet d'éviter un premier retour à la ligne dûs à la ligne vide entre l'en-tête et les versets
                                output_f.write("\n")
                            else :
                                skip_first_return = False
                        # si une ligne commence par un "<" (lien), on ne la copie pas
                        elif line[0] != "<" or not located_in_header :
                # note : les lignes vides ont toutes une longueur de 1
                            # on scan chaque caractère de la ligne
                            while i < len(line) :
                                # print("CHAR : " + str(i) + "  " + line[i])   # debug
                                # si on tombe sur un chiffre
                                if line[i].isdigit() :
                                    located_in_header = False
                                    # on continue de suivre les chiffres à la suite,
                                    # jusqu'à tomber sur un autre type de caractère
                                    while line[i].isdigit() :
                                        # on stocke le chiffre dans stock_str (après l'avoir vidé)
                                        stock_str = stock_str+line[i]
                                        i += 1
                                    # lorsqu'on tombe sur un caractère non-décimal,
                                    # Si celui-ci est un ":", on écrit dans le fichier la première partie du numéro
                                    # stockée dans stock_str et on continue l'analyse pour récupérer et écrire la seconde
                                    if line[i] == ":" :
                                        #output_f.write(stock_str + "$") # LEGACY : cette instruction servait lorsque l'on souhaitait stocker les versets dans des fichiers CSV (séparés par $)
                                        stock_str = ""
                                        i += 1
                                        while line[i].isdigit() :
                                            stock_str = stock_str+line[i]
                                            i += 1
                                        #output_f.write(stock_str + "$") # LEGACY : cette instruction servait lorsque l'on souhaitait stocker les versets dans des fichiers CSV (séparés par $)
                                        stock_str = ""
                                    # Si le caractère non-décimal n'est pas un ":", on écrit le chiffre stocké et on continue
                                    else :
                                        # output_f.write(stock_str + line[i])
                                        stock_str = ""
                                        i += 1
                                # supression des liens dans les versets
                                elif line[i] == "<" :
                                    while line[i] != ">" :
                                        i += 1
                                    i += 1
                                # on ignore les retours à la ligne, mais on les change en espaces
                                elif line[i] == "\n" :
                                    if skip_first_two_chars >= 2 :
                                        output_f.write(" ")
                                    else :
                                        skip_first_two_chars += 1
                                    i += 1
                                # Si on n'est pas tombé sur un chiffre, on écrit dans le fichier
                                else :
                                    if skip_first_two_chars >= 2 or line[i] != " " :
                                        output_f.write(line[i])
                                    else :
                                        skip_first_two_chars += 1
                                    i += 1
                            i=0
                    elif premiere_ligne :
                        premiere_ligne = False
                        if line[0] == "*" :
                            skip_first_return = True

