#!/bin/bash

# utilitaire qui crée n fichiers textes nommés n.txt (de 1 à n inclus)
# commande : ./createtxtfiles.sh n
for i in `seq 1 $1`
do
   touch $i.txt
done
