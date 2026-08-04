#include "solutions.h"
#include <pthread.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
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
/*
 * Solution for: Integer to Roman [MEDIUM]
 * https://leetcode.com/problems/integer-to-roman/
 */

#define MAX_ROMAN_WIDTH 128
#define MAX_DECIMAL_DEGITS 10
const char __attribute__((nonstring))ROMAN_MAP[4][2] = {"IV", "XL", "CD", "M\0"};
int digitToRoman(int digit, int weight, char *dst) {
  if (digit > 9 || digit < 0)
    return -1;
  if (digit == 9) {
    dst[0] = ROMAN_MAP[weight][0];
    dst[1] = ROMAN_MAP[weight + 1][0];
    return 2;
  } else {
    const char *map = ROMAN_MAP[weight];
    if (digit == 4) {
      dst[0] = map[0];
      dst[1] = map[1];
      return 2;
    } else {
      int i = 0;

      if (digit >= 5) {
        digit -= 5;
        dst[i++] = map[1];
      }

      for (int j = 0; j < digit; j++)
        dst[i++] = map[0];
      return i;
    }
  }
}
char *intToRoman(int num) {
  char *roman = malloc(sizeof(char) * MAX_ROMAN_WIDTH);
  int digits[MAX_DECIMAL_DEGITS];
  int weight = 1;
  int tmp = num;
  int digitsCount = 0;
  do {
    digits[digitsCount++] = tmp % 10;
    weight *= 10;
  } while ((tmp = num / weight) != 0);
  char *cursor = roman;
  for (int i = digitsCount - 1; i >= 0; i--) {
    int n = digitToRoman(digits[i], i, cursor);
    cursor += n;
  }
  *cursor = 0;
  return roman;
}

// 3SUM problem

int intComparator(const void *first, const void *second) {
  int firstInt = *(const int *)first;
  int secondInt = *(const int *)second;
  return firstInt - secondInt;
}
#define NUM_THREADS 2
pthread_mutex_t lock;
struct triplet_list {
  int *triplet;
  struct triplet_list *next;
};
struct thread_struct {
  int min, max;
  int id;
  int *nums;
  struct triplet_list **head;
  int *tripleCount;
};
void *threeSumWorker(void *arg) {
  struct thread_struct *th_arg = (struct thread_struct *)arg;
  struct triplet_list *list = NULL, *p = NULL, *q = NULL;
  int tripleCount = 0;
  int *nums = th_arg->nums;
  int min = th_arg->min;
  int max = th_arg->max;
  int oldI = nums[min] + 1;
  int id = th_arg->id;
  for (int i = min; i <= max - 2; i++) {
    if (oldI == nums[i] || i % NUM_THREADS != id) {
      oldI = nums[i];
      continue;
    }
    if (nums[i] > 0)
      break;
    int oldJ = nums[i + 1] + 1;
    for (int j = i + 1; j <= max - 1; j++) {
      if (oldJ == nums[j])
        continue;
      int sum = nums[i] + nums[j];
      if (sum + nums[j + 1] > 0)
        break;
      int highK = max;
      int lowK = j + 1;
      while (highK >= lowK) {
        int k = (highK + lowK) / 2;
        if (sum + nums[k] == 0) {
          (q) = (struct triplet_list *)malloc(sizeof(struct triplet_list));
          (q)->triplet = (int *)malloc(3 * sizeof(int));
          (q)->triplet[0] = nums[i];
          (q)->triplet[1] = nums[j];
          (q)->triplet[2] = nums[k];
          printf("new tripel: %d,%d,%d\n", i, j, k);
          q->next = NULL;
          if (list == NULL) {
            list = q;
            p = list;
          } else {
            p->next = q;
            p = q;
          }
          tripleCount++;
          break;
        }
        if (sum + nums[k] < 0) {
          lowK = k + 1;
        } else {
          highK = k - 1;
        }
      }

      oldJ = nums[j];
    }
    oldI = nums[i];
  }
  if (p == NULL) {
    return NULL;
  }
  pthread_mutex_lock(&lock);
  p->next = *(th_arg->head);
  *(th_arg->head) = list;
  (*th_arg->tripleCount) += tripleCount;
  pthread_mutex_unlock(&lock);
  printf("Thread %d, finish!\n", th_arg->id);
  return NULL;
}
int **threeSum(int *nums, int numsSize, int *returnSize,
               int **returnColumnSizes) {
  qsort(nums, numsSize, sizeof(int), intComparator);
  int min = 0;
  while (min < numsSize &&
         nums[min] + nums[numsSize - 2] + nums[numsSize - 1] < 0)
    min++;
  int max = numsSize - 1;
  while (nums[max] + nums[1] + nums[0] > 0 && max > 0)
    max--;
  if (max - min < 2) {
    (*returnSize) = 0;
    (*returnColumnSizes) = NULL;
    return NULL;
  }
  int tripleCount = 0;
  struct triplet_list *list = NULL, *q = NULL;
  struct thread_struct threads_args[NUM_THREADS];
  pthread_t threads[NUM_THREADS];
  for (int i = 0; i < NUM_THREADS; i++) {
    threads_args[i].id = i;
    threads_args[i].min = min;
    threads_args[i].max = max;
    threads_args[i].head = &list;
    threads_args[i].nums = nums;
    threads_args[i].tripleCount = &tripleCount;

    pthread_create(&threads[i], NULL, threeSumWorker, &threads_args[i]);
  }
  for (int i = 0; i < NUM_THREADS; i++)
    pthread_join(threads[i], NULL);
  /*for (int i = min; i < max - 2; i++) {
    if (oldI == nums[i])
      continue;
    if (nums[i] > 0)
      break;
    int oldJ = nums[i + 1] + 1;
    for (int j = i + 1; j < max - 1; j++) {
      if (oldJ == nums[j])
        continue;
      int sum = nums[i] + nums[j];
      if (sum + nums[j + 1] > 0)
        break;
      int oldK = nums[j + 1] + 1;
      for (int k = j + 1; k < max; k++) {
        if (oldK == nums[k] || nums[j] < 0)
          continue;
        if (sum + nums[k] == 0) {
          (q) = (struct triplet_list *)malloc(sizeof(struct triplet_list));
          (q)->triplet = (int *)malloc(3 * sizeof(int));
          (q)->triplet[0] = nums[i];
          (q)->triplet[1] = nums[j];
          (q)->triplet[2] = nums[k];
          q->next = NULL;
          if (list == NULL) {
            list = q;
            p = list;
          } else {
            p->next = q;
            p = q;
          }
          tripleCount++;
        }
        oldK = nums[k];
      }
      oldJ = nums[j];
    }
    oldI = nums[i];
  }
  */
  int **result = (int **)malloc(sizeof(int *) * tripleCount);
  (*returnColumnSizes) = malloc(sizeof(int) * tripleCount);
  (*returnSize) = tripleCount;
  q = list;
  for (int i = 0; i < tripleCount; i++) {
    if (q == NULL)
      break;
    result[i] = q->triplet;
    (*returnColumnSizes)[i] = 3;
    list = q;
    q = q->next;
    free(list);
  }
  return result;
}
/* https://leetcode.com/problems/letter-combinations-of-a-phone-number/ */

char **letterCombinations(char *digits, int *returnSize) {
  if (!digits || !(*digits))
    return (void *)((*returnSize) = 0);
  const int ButtonsNumber = 8;
  const int MaxCharactersPerButton = 4;
  struct Button {
    const char *characters;
    int charactersLength;
  } buttons[ButtonsNumber];
  buttons[0] = (struct Button){"abc", 3};
  buttons[1] = (struct Button){"def", 3};
  buttons[2] = (struct Button){"ghi", 3};
  buttons[3] = (struct Button){"jkl", 3};
  buttons[4] = (struct Button){"mno", 3};
  buttons[5] = (struct Button){"pqrs", 4};
  buttons[6] = (struct Button){"tuv", 3};
  buttons[7] = (struct Button){"wxyz", 4};
  (*returnSize) = 1;
  int digitsLength = 0;
  while (digits[digitsLength])
    (*returnSize) *= buttons[digits[digitsLength++] - '1' - 1].charactersLength;
  //  printf("DEBUG: %d", (*returnSize));
  char **result = (char **)calloc(*returnSize, sizeof(char *));
  for (int i=0;i<*returnSize;i++) result[i] = calloc(digitsLength+1,sizeof(char));
  int swapEach= 1;
  for (int i = 0; i < digitsLength; i++) {
    int charIndex = 0;
    for (int j = 0; j < *returnSize; j++) {
      if (j%swapEach==0) charIndex=(charIndex+1) % buttons[digits[i] - '1' - 1].charactersLength;
      result[j][i] =
          buttons[digits[i] - '1' - 1]
              .characters[charIndex];
    }
    swapEach*=buttons[digits[i] - '1' - 1].charactersLength;
  }
  return result;
}
