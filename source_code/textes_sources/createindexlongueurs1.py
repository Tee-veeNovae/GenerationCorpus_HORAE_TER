# création d'un fichier ou les longueurs en caractères de chaque verset de la Bible
# sont indexées pour référence rapide

# Les longueurs sont stockés sur 3 lignes, avec la longueur du verset, le numéro du verset, et notre fichier correspondant
# NOTE : cet algorithme/façon d'indexer est largement optimisable, nous sommes allé ici au plus simple à parcourir

import os
from glob import glob

try : 
    file = open('index_longueurs.txt', 'r')
except IOError :
    file = open('index_longueurs.txt', 'w')
file.close()

current_working_directory = os.path.dirname(os.path.abspath(__file__)).replace("\\", "/")
bible_txt_path = current_working_directory + "/bible_txt/"
i = 1

with open('index_longueurs.txt', 'w', encoding='utf-8') as output_f :
    for nom_fichier in [y for x in os.walk(bible_txt_path) for y in glob(os.path.join(x[0], '*.txt'))] :
        # print(nom_fichier)       #debug
        if os.path.isfile(nom_fichier) :
            with open(nom_fichier, 'r', encoding='utf-8') as input_f :
                i = 1
                for line in input_f:
                    output_f.write(str(len(line)) + "\n")   # longueur du verset
                    output_f.write(str(i) + "\n")   # numéro du verset
                    output_f.write(nom_fichier[len(bible_txt_path):].replace("\\", "/") + "\n") #adresse relative du fichier du chapitre du verset
                    i += 1

