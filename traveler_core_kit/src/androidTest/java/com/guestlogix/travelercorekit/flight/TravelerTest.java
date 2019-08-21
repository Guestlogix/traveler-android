package com.guestlogix.travelercorekit.flight;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import com.guestlogix.travelercorekit.models.Traveler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TravelerTest{

    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;
    private Context instrumentationContext;

    @Before
    public void setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();

    }

    @Test
    public void initTraveller() {
        Traveler.initialize("testtesttesttesttest", instrumentationContext);
        // Verify that the received data is correct.
        Assert.assertEquals(1, 4);
    }
}
