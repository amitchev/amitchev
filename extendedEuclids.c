#include <stdio.h>
#include <stdlib.h>

int extendedEuclids(int a, int b, int *s, int *t);

int main(int argc, char *argv[])
{
  int a, b, s, t;
  if(argc == 3)
  {
    a = abs(atoi(argv[1]));
    b = abs(atoi(argv[2]));
    printf("gcd(%d,%d) is %d, ", a, b, extendedEuclids(a,b,&s,&t));
    printf("s is %d and t is %d, ", s, t);
    printf("such that gcd(%d,%d)=(%d)(%d)+(%d)(%d)\n", a, b, s, a, t, b);
  }
  else
  {
    printf("Incorrect number of arguments\n");
    return 0;
  }
  return 1;
}

int extendedEuclids(int a, int b, int *s, int *t)
{
  int newS, newT, gcd;
  if(a == 0)
  {
    *s = 0;
    *t = 1;
    return b;
  }
  gcd = extendedEuclids(b%a, a, &newS, &newT);
  *s = newT-(b/a)*newS;
  *t =  newS;
  return gcd;
}
