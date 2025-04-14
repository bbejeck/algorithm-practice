package dev.bbejeck.arrays;

/**
 * User: Bill Bejeck
 * Date: 4/13/25
 * Time: 8:43 PM
 */
public class ContainerWithMostWater {

    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = Integer.MIN_VALUE;
        while (left < right) {
            maxArea = Math.max(maxArea, area(left, right, height));
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    int area(int left, int right, int[] h) {
        int w = Math.min(h[left], h[right]);
        int l = right - left;
        return l * w;
    }
}
