package dev.bbejeck.intervals_heap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeetingRoomsIITest {

    private MeetingRoomsII meetingRooms;

    @BeforeEach
    void setUp() {
        meetingRooms = new MeetingRoomsII();
    }

    @Test
    @DisplayName("Test LeetCode example 1: [[0,30],[5,10],[15,20]] should return 2")
    void testLeetCodeExample1() {
        int[][] intervals = {{0, 30}, {5, 10}, {15, 20}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should need 2 meeting rooms for overlapping meetings");
    }

    @Test
    @DisplayName("Test LeetCode example 2: [[7,10],[2,4]] should return 1")
    void testLeetCodeExample2() {
        int[][] intervals = {{7, 10}, {2, 4}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(1, result, "Should need only 1 meeting room for non-overlapping meetings");
    }

    @Test
    @DisplayName("Test empty intervals array")
    void testEmptyIntervals() {
        int[][] intervals = {};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(0, result, "Should return 0 for empty intervals array");
    }

    @Test
    @DisplayName("Test single meeting")
    void testSingleMeeting() {
        int[][] intervals = {{1, 5}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(1, result, "Should need 1 room for single meeting");
    }

    @Test
    @DisplayName("Test consecutive meetings - no overlap")
    void testConsecutiveMeetings() {
        int[][] intervals = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(1, result, "Should need only 1 room for consecutive non-overlapping meetings");
    }

    @Test
    @DisplayName("Test meetings ending when others start")
    void testMeetingsEndingWhenOthersStart() {
        int[][] intervals = {{0, 30}, {30, 40}, {40, 50}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(1, result, "Should need only 1 room when meetings end exactly when others start");
    }

    @Test
    @DisplayName("Test all meetings overlapping")
    void testAllMeetingsOverlapping() {
        int[][] intervals = {{0, 10}, {5, 15}, {10, 20}, {15, 25}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should handle overlapping pattern correctly");
    }

    @Test
    @DisplayName("Test maximum simultaneous meetings")
    void testMaximumSimultaneousMeetings() {
        int[][] intervals = {{0, 5}, {1, 6}, {2, 7}, {3, 8}, {4, 9}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(5, result, "Should need 5 rooms when all meetings overlap");
    }

    @Test
    @DisplayName("Test meetings with same start time")
    void testMeetingsWithSameStartTime() {
        int[][] intervals = {{0, 3}, {0, 5}, {0, 7}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(3, result, "Should need separate rooms for meetings starting at same time");
    }

    @Test
    @DisplayName("Test meetings with same end time")
    void testMeetingsWithSameEndTime() {
        int[][] intervals = {{1, 10}, {5, 10}, {8, 10}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(3, result, "Should need separate rooms for overlapping meetings ending at same time");
    }

    @Test
    @DisplayName("Test complex overlapping pattern")
    void testComplexOverlappingPattern() {
        int[][] intervals = {{9, 10}, {4, 9}, {4, 17}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should handle complex overlapping pattern correctly");
    }

    @Test
    @DisplayName("Test unsorted input intervals")
    void testUnsortedIntervals() {
        int[][] intervals = {{15, 20}, {0, 30}, {5, 10}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should handle unsorted input correctly");
    }

    @Test
    @DisplayName("Test large time values")
    void testLargeTimeValues() {
        int[][] intervals = {{999995, 1000000}, {999990, 999999}, {999985, 999995}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should handle large time values correctly");
    }

    @Test
    @DisplayName("Test zero-duration meetings")
    void testZeroDurationMeetings() {
        int[][] intervals = {{5, 5}, {10, 10}, {15, 15}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(1, result, "Should handle zero-duration meetings (though technically invalid per constraints)");
    }

    @Test
    @DisplayName("Test peak usage scenario")
    void testPeakUsageScenario() {
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should correctly calculate peak room usage");
    }

    @Test
    @DisplayName("Test many short meetings during long meeting")
    void testShortMeetingsDuringLongMeeting() {
        int[][] intervals = {{0, 100}, {10, 15}, {20, 25}, {30, 35}, {40, 45}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should need 2 rooms when short meetings occur during long meeting");
    }

    @Test
    @DisplayName("Test identical meeting times")
    void testIdenticalMeetingTimes() {
        int[][] intervals = {{5, 10}, {5, 10}, {5, 10}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(3, result, "Should need separate rooms for identical meetings");
    }

    @Test
    @DisplayName("Test alternating pattern")
    void testAlternatingPattern() {
        int[][] intervals = {{1, 3}, {2, 4}, {3, 5}, {4, 6}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should handle alternating overlap pattern correctly");
    }

    @Test
    @DisplayName("Test single room sufficient for multiple meetings")
    void testSingleRoomSufficient() {
        int[][] intervals = {{1, 2}, {3, 4}, {5, 6}, {7, 8}, {9, 10}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(1, result, "Should need only 1 room for well-spaced meetings");
    }

    @Test
    @DisplayName("Test room reuse scenario")
    void testRoomReuseScenario() {
        int[][] intervals = {{0, 5}, {10, 15}, {20, 25}, {1, 3}, {11, 13}, {21, 23}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should efficiently reuse rooms when meetings end");
    }

    @Test
    @DisplayName("Test boundary conditions with start equals previous end")
    void testBoundaryConditions() {
        int[][] intervals = {{0, 10}, {10, 20}, {5, 15}};
        int result = meetingRooms.minMeetingRooms(intervals);

        assertEquals(2, result, "Should handle boundary conditions where start equals previous end");
    }
}