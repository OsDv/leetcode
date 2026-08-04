
// https://leetcode.com/problems/3sum-closest/description/
import java.lang.classfile.instruction.ReturnInstruction;
import java.util.Arrays;

public class ThreeSumClosest {
  public int threeSumClosest(int[] nums, int target) {
    if (nums.length < 3)
      return target;
    Arrays.sort(nums);
    int oldI = nums[0] + 1;
    int current = nums[0] + nums[1] + nums[2];
    for (int i = 0; i < nums.length - 2; i++) {
      if (nums[i] == oldI)
        continue;
      for (int j = i + 1; j < nums.length - 1; j++) {
        int lowK = j + 1;
        int highK = nums.length - 1;
        int sum = nums[i] + nums[j];
        while (lowK <= highK) {
          int k = (highK + lowK) / 2;
          int temp = nums[k] + sum;
          if (temp == target)
            return target;
          if (temp > target) {
            highK = k - 1;
          } else {
            lowK = k + 1;
          }
          if (Math.abs(temp - target) < Math.abs(current - target)) {
            current = temp;
          }
        }
      }
      oldI = nums[i];
    }
    return current;
  }
}
