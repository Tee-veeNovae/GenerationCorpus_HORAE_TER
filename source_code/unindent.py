
input_file_path = "corpus.txt"
print(input_file_path)       #debug

output_file_path = "corpus_pro.txt"
print(output_file_path)      #debug

i = 0

with open(input_file_path, 'r', encoding='utf-8') as input_f :
    with open(output_file_path, 'w', encoding='utf-8') as output_f :
        line = input_f.read()
        print(len(line));
            
        while i < len(line) :

            if line[i] == "\n" :
                output_f.write("")
                #print("NewLine")

            elif line[i] == "\t" :
                output_f.write("")
                #print("Tab")

            elif line[i] == " " :
                output_f.write(" ")
                #print ("espace")
                i += 1
                while line[i] == " " and i < len(line) :
                    output_f.write("")
                    i += 1
                i -= 1 
            else :
                output_f.write(line[i])

            i += 1