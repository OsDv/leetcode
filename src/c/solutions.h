#ifndef SOLUTIONS_H
#define SOLUTIONS_H
#define SOL_MIN(X, Y) (((X) < (Y)) ? (X) : (Y))
#define SOL_MAX(a, b) (((a) > (b)) ? (a) : (b))
#define SOL_ABS(x) (x < 0) ? -x : x

int maxArea(int *height, int heightSize);
char *intToRoman(int num);
int **threeSum(int *nums, int numsSize, int *returnSize,
               int **returnColumnSizes);
char** letterCombinations(char* digits, int* returnSize);
#endif
