package com.guestlogix.travelercorekit.flight;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import com.guestlogix.travelercorekit.AuthenticatedUrlRequest;
import com.guestlogix.travelercorekit.flight.helpers.FlightSearchCallbackSpy;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.models.Traveler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({URL.class, Settings.Secure.class})
public class TravelerTest {
    private static final String API_KEY = "757";
    private static final String ANDROID_ID = "android_id";
    private static final String TOKEN = "token";
    private static final String PACKAGE_NAME = "com.guestlogix.travelercorekit";
    private Traveler travelerUnderTest;
    private Token token = new Token(TOKEN);

    @Mock private static Context mockContext;
    @Mock private AuthenticatedUrlRequest mockAuthenticatedUrlRequest;
    @Mock private Session mockSession;
    @Mock private Resources mockResources;
    @Mock private Configuration mockConfiguration;
    @Mock private ContentResolver mockContentResolver;
    private URL mockUrl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        travelerUnderTest = Traveler.initializeForTesting(API_KEY, mockContext, mockSession);
        PowerMockito.mockStatic(URL.class);
        mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.mockStatic(Settings.Secure.class);
        PowerMockito.when(Settings.Secure.getString(mockContentResolver, Settings.Secure.ANDROID_ID))
                .thenReturn(ANDROID_ID);

        Mockito.when(mockSession.getApiKey()).thenReturn(API_KEY);
        Mockito.when(mockSession.getContext()).thenReturn(mockContext);
        Mockito.when(mockSession.getToken()).thenReturn(token);
        Mockito.when(mockContext.getPackageName()).thenReturn(PACKAGE_NAME);
        Mockito.when(mockContext.getResources()).thenReturn(mockResources);
        Mockito.when(mockContext.getContentResolver()).thenReturn(mockContentResolver);
        Mockito.when(mockResources.getConfiguration()).thenReturn(mockConfiguration);
        mockConfiguration.locale = Locale.ENGLISH;
    }

    @Test
    public void test() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        HttpURLConnection huc = Mockito.mock(HttpURLConnection.class);
        PowerMockito.when(url.openConnection()).thenReturn(huc);

        //assertTrue(url.openConnection() instanceof HttpURLConnection);
    }

    @Test
    public void flightSearchTest() throws Exception {
        final String urlPath = "https://traveler.rc.guestlogix.io/v1/flight?flight-number=NUM-33R&departure-date=2019-08-20T14%3A37%3A47";
        //given
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FlightQuery flightQuery = new FlightQuery("NUM-33R", calendar.getTime());
        FlightSearchCallbackSpy flightSearchCallbackSpy = new FlightSearchCallbackSpy();

        PowerMockito.whenNew(URL.class).withArguments(ArgumentMatchers.anyString()).thenReturn(mockUrl);

        //when
        Traveler.flightSearch(flightQuery, flightSearchCallbackSpy);
        //TODO: simulate task completion

        //then

        Assert.assertEquals(0, flightSearchCallbackSpy.getOnFlightSearchError_invocationCount());
        Assert.assertEquals(0, flightSearchCallbackSpy.getOnFlightSearchSuccess_invocationCount());
    }
}