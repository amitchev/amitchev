/********************/
/* Austyn Mitchev   */
/* 29th Nov 2021    */
/* CS-241 Section 06*/
/* Lab 8 (Desteg)   */
/********************/

#include <stdio.h>


/* My desteg implementation is a fairly straightforward
 * reversal of steg.c. I used fseek (to navigate to the
 * beginning of the file) and fread(to read the file).
 * And as with steg.c I used a while loop with fread 
 * to read through and decrypt the bytes. I OR'd each
 * of the bytes (with the lowest two masked)  in the bytes 
 * array (shifting each by 6, 4, 2, and non respectively)
 * to accomplish this. After this a NULL/0 check is run, 
 * and then character is printed.
*/
int main(int argc, char **argv)
{
  char bytes[4];
  int output;
  FILE* in = fopen(argv[1], "rb");
  fseek(in, 54, SEEK_SET);
  while(fread(bytes, sizeof(char), 4, in))
  {
    output = (bytes[0] & 3) << 6 | (bytes[1] & 3) << 4 |
	     (bytes[2] & 3) << 2 | (bytes[3] & 3);
    if(!output) break;
    putchar(output);
  }
  return 0;
}
