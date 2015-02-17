package sg.com.temasys.skylink.sdk.rtc;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests related to GMTService
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GMTServiceTest {

    private static final String TAG = SkylinkConnectionTest.class.getName();

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void testCurrentTime() throws InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GMTService GMTService = new GMTService(new GMTServiceListener() {
            @Override
            public void onCurrentTimeFetched(Date date) {
                assertNotNull("Fetched current time", date);
                Log.d(TAG, "Current GMT time" + date.toString());
                countDownLatch.countDown();
            }

            @Override
            public void onCurrentTimeFetchedFailed() {
                assertTrue("Failed to fetch time", false);
                countDownLatch.countDown();
            }
        });

        GMTService.execute();
        countDownLatch.await();
    }
}
