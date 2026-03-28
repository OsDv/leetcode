#include "solutions.h"

/*
 * Solution for: Container With Most Water [MEDIUM]
 * https://leetcode.com/problems/container-with-most-water/
 */
int maxArea(int *height, int heightSize) {
  int rightIndex = heightSize - 1;
  int leftIndex = 0;
  int max = 0;
  while (rightIndex != leftIndex) {
    int currentContainerSize = SOL_MIN(height[leftIndex], height[rightIndex]) *
                               (SOL_ABS(rightIndex - leftIndex));
    if (currentContainerSize > max)
      max = currentContainerSize;
    if (height[rightIndex] >= height[leftIndex])
      leftIndex++;
    else
      rightIndex--;
  }
  return max;
}
