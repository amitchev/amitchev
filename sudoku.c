/********************/
/* Austyn Mitchev   */
/* 29th September   */
/* CS-241 Section 06*/
/********************/


#include <stdio.h>

/*Prototypes for all my helper functions*/
int checkInput(char *input, int inSize);
int solvePuzzle(int board[][9], int r, int c);
int validNum(int board[][9], int r, int c, int n);
int validPuzzle(int board[][9], int r, int c, int n);
void fillBoard(char *input, int board[][9]);
void printInput(char *input, int inSize);
void printOutput(int output[][9]);

/*
** In my main function I use 4 variables:
** 2 Arrays (1 for input, 1 for output), 2 int's
** (1 to count the input, 1 to receive input). Input
** is read into the char array in the while loop until
** it reads a new line, at which point it checks the
** input for length, valid characters and form. Then 
** it runs the solvePuzzle function and prints out an
** error message, no solution message, solved 
** board dependant on the functions' output.
*/
int main()
{
  char input[83]; int board[9][9];
  int c; int charCount = 0;
  while((c=getchar()) != EOF)
  {
    if(c!='\n')
    {
      input[charCount] = c; charCount++;
    } else {
      if(checkInput(input, charCount))
      {
        fillBoard(input, board);
        if(solvePuzzle(board, 0, 0))
        {
          printInput(input, charCount); 
          printOutput(board);
	        printf("\n");
          input[0] = '\0';
	        board[0][0] = '\0';
          charCount = 0;
        } else {
          printInput(input, charCount);
          printf("No Solution\n \n");
          input[0] = '\0';
	        board[0][0] = '\0';
          charCount = 0;
        }
      } else {
        printInput(input, charCount);
        printf("Error\n \n");
        input[0] = '\0'; 
        board[0][0] = '\0';
        charCount = 0;
      }
    }
  }
  return 0;
}

/*
** Tests the input scanned passed in the arguments
** as well as the number of elements. Checks the
** integer values of the characters to ensure they're
** valid. Then uses the fillBoard function to create
** a temporary board to test the validity of the
** number placement using the validPuzzle function.
*/
int checkInput(char *input, int inSize)
{
  int x, r, c; int tempBoard[9][9];
  if(inSize == 81)
  {
    for(x = 0; x < inSize; x++)    
    {
      if(input[x] != '.' && (input[x] < '1' || input[x] > '9'))
      {return 0;}
    }
    fillBoard(input, tempBoard);
    for(r = 0; r < 9; r++)
    {
      for(c = 0; c < 9; c++)
      {
        if(tempBoard[r][c] != 0 && 
        !validPuzzle(tempBoard, r, c, tempBoard[r][c]))
        {return 0;}
      }
    }
    return 1;
  } else return 0;
}

/*
** This is a recursive function that essentially handles
** solving a board using the validNum function. Calls
** itself until it has solved and scanned the entire board
** passed to it in the arguments. Uses the validNum
** function to check solutions for empty spaces. Will only
** return a value once the entire board has been scanned
** or solved.
*/
int solvePuzzle(int board[][9], int r, int c)
{
  int j;
  if(r < 9 && c < 9)
  {
    if(board[r][c] != 0)
    {
      if((c+1)<9){return solvePuzzle(board, r, c+1);}
      else if((r+1)<9){return solvePuzzle(board, r+1, 0);}
      else{return 1;}
    } else {
      for(j = 0; j < 9; j++)
      {
        if(validNum(board, r, c, j+1))
        {
          board[r][c] = j+1;
          if(solvePuzzle(board, r, c)){return 1;}
          else{board[r][c] = 0;}
        }
      }
    }
    return 0;
  } else {return 1;}
}

/*
** Scans the board passed in the arguments, at the
** position provided by 'r' and 'c', to see if the
** 'n' value is valid at said position. Checks the
** column, row, and box of the position, all done
** in the same for loop to increase efficiency. 
*/
int validNum(int board[][9], int r, int c, int n)
{
  int rStart = (r/3)*3;
  int cStart = (c/3)*3;
  int i;
  for(i = 0; i < 9; i++)
  {
    if(board[r][i] == n){return 0;}
    if(board[i][c] == n){return 0;}
    if(board[rStart+(i%3)][cStart+(i/3)] == n){return 0;}
  }
  return 1;
}

/*
** Sister function of the validNum function
** with additional conditions to ensure the position
** passed in the arguments is not tested, otherwise
** it will always return zero when called to test
** inputs in checkInput.
*/
int validPuzzle(int board[][9], int r, int c, int n)
{
  int rStart = (r/3)*3;
  int cStart = (c/3)*3;
  int i;
  for(i = 0; i < 9; i++)
  {
    if(i != c && board[r][i] == n){return 0;}
    if(i != r && board[i][c] == n){return 0;}
    if((rStart+(i%3) != r && cStart+(i/3) != c) && 
    board[rStart+(i%3)][cStart+(i/3)] == n)
    {return 0;}
  }
  return 1;
}

/*
** Fills the integer board passed in the arguments
** using the char array, also passed in the arguments.
** does this using nested for loops, and then by subtracting
** the integer value of '0' from the char referenced in
** 'input'.
*/
void fillBoard(char *input, int board[][9])
{
  int x, y; int z = 0;
  for(x = 0; x < 9; x++)
  {
    for(y = 0; y < 9; y++)
    {
      if(input[z] == '.')
      {board[x][y] = 0; z++;}
      else
      {
	board[x][y] = input[z]-'0';
	z++;
      }
    }
  }
}

/*
** Prints the input passed in the arguments.
*/
void printInput(char *input, int inSize)
{
  int size = inSize;
  int x;
  for(x = 0; x < size; x++)
  {printf("%c", input[x]);}
  printf("\n");
}

/*
** Prints the output passed in the arguments.
*/
void printOutput(int output[][9])
{
  int x, y;
  for(x = 0; x < 9; x++)
  {
    for(y = 0; y < 9; y++)
    {printf("%d", output[x][y]);}
  }
  printf("\n");
}
