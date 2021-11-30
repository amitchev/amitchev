/********************/
/* Austyn Mitchev   */
/* 29th Nov 2021    */
/* CS-241 Section 06*/
/* Lab 8 (Steg)     */
/********************/
#include <stdio.h>

/* I contained my steg.c implementation within main(),
 * it takes 'int argc' and 'char **argv' as function
 * arguments. I pulled some of my initial set-up (variable
 * declarations and such) from unredBMP.c we were provided
 * by Professor Chenoweth. From there, I utilized the file
 * IO functions fread, fwrite, fseek, and ftell to traverse,
 * read, write, and measure the given files. 
 * As for the main algorithm, I used 2 while loops to iterate
 * through the file adjusting the bits as required. 
*/
int main(int argc, char **argv)
{
  char c;
  unsigned char header[54];
  char bytes[4];
  int fileSize;
  FILE* in = fopen(argv[1], "rb");
  FILE* out = fopen(argv[2], "wb");

  fread(header, 54, 1, in);
  fwrite(header, sizeof(header), 1, out);
  fseek(in, 54, SEEK_END);

  /*Used as a decrement for an extra check against EOF*/
  fileSize = ftell(in);

  fseek(in, 54, SEEK_SET);

  /*Main algorithm: iterates over the file while fread
   *has a valid return value. First a char is read in 
   *using getchar. Then to accomplish the actual
   *steganography, I opted for a hard coded approach, 
   *performing an OR operation between two values. The
   *read in byte with all but the lowest two bits masked,
   *and the c value right shifted(6, 4, 2, and none to
   *ensure the file is iterated over correctly) with the lowest
   *two bits masked. The result is then written into the
   *output file, then a final EOF check is run and the
   *fileSize variable is decremented.*/
  while(fread(bytes, sizeof(char), 4, in))
  {
    c = getchar();
    if(fileSize < 3) break;
    bytes[0] = (bytes[0] & ~3) | ((c >> 6) & 3);
    bytes[1] = (bytes[1] & ~3) | ((c >> 4) & 3);
    bytes[2] = (bytes[2] & ~3) | ((c >> 2) & 3);
    bytes[3] = (bytes[3] & ~3) | (c & 3);
    fwrite(bytes, sizeof(char), 4, out);
    if(c == EOF) break;
    fileSize -= 4;
  }

  /*Final while loop I had to use as it won't traverse
   *the whole file otherwise.*/
  while(fread(bytes, sizeof(char), 1, in)) fwrite(bytes, 1, sizeof(char), out);
  return 0;
}
