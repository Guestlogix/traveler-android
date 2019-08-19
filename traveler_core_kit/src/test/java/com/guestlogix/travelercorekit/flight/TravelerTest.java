package com.guestlogix.travelercorekit.flight;

import android.content.Context;
import com.guestlogix.travelercorekit.common.MockContext;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.utilities.MockTaskManager;
import com.guestlogix.travelercorekit.utilities.TaskManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TravelerTest {
    private static final String API_KEY = "757";

    private Context mockContext = new MockContext();
    private Traveler travelerUnderTest;
    private TaskManager mockTaskManager = new MockTaskManager();
    private TaskManager mockOrderSerialTaskManager = new MockTaskManager();

    @Before
    public void setUp() {
        travelerUnderTest = Traveler.initializeForTesting(API_KEY, mockContext, mockTaskManager, mockOrderSerialTaskManager);
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}