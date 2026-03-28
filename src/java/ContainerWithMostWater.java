/*
 * Solution for: Container With Most Water [MEDIUM]
 * https://leetcode.com/problems/container-with-most-water/
 */
public class ContainerWithMostWater {
  public static void main(String args[]) {
    ContainerWithMostWater o = new ContainerWithMostWater();
    System.out.print(o.maxArea(new int[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 }));
  }

  public int maxArea(int[] height) {
    int max = 0;
    int leftIndex = 0;
    int rightIndex = height.length - 1;
    while (leftIndex != rightIndex) {
      int currentContainerSize = Math.min(height[leftIndex], height[rightIndex]) * (Math.abs(rightIndex - leftIndex));
      if (currentContainerSize > max)
        max = currentContainerSize;
      if (height[rightIndex] >= height[leftIndex])
        leftIndex++;
      else
        rightIndex--;
    }
    return max;
  }
}
