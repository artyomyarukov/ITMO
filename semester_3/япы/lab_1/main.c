#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void encrypt(char *word, int shift, int alphabet_size);

void decrypt(char *word, int shift, int alphabet_size);


char process_letter(char curChar, int alphabet_size, int shift, char base) {

    return (curChar - base + shift) % alphabet_size + base;



}

void encrypt(char *word, int shift, int alphabet_size) {
    shift = (shift % alphabet_size + alphabet_size);
    for (int i = 0; word[i] != '\0'; ++i) {
        char curChar = word[i];
        if (curChar >= 'a' && curChar <= 'a' + alphabet_size) {
             word[i] = process_letter(curChar,alphabet_size,shift,'a');
         }



         if (curChar >= 'A' && curChar <= 'A' + alphabet_size) {
             word[i] = process_letter(curChar,alphabet_size,shift,'A');
         }

    }

}

void decrypt(char *word, int shift, int alphabet_size) {
    encrypt(word, -shift, alphabet_size);
}


int main(int argc, char *argv[]) {
    if (argc != 3) {
        return 2;
    }
    char *word = argv[1];
    int shift = atoi(argv[2]);
    encrypt(word, shift, 26);
    printf("%s\n", word);
    decrypt(word,shift,26);
    printf("%s", word);


}

