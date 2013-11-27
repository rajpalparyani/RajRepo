package com.telenav.navservice;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.telenav.navservice.location.CellRecorderTest;
import com.telenav.navservice.location.GpsRecorderImplTest;
import com.telenav.navservice.location.GpsRecorderTest;
import com.telenav.navservice.location.LocationBufferTest;
import com.telenav.navservice.location.LocationSenderTest;
import com.telenav.navservice.location.StationaryMonitorTest;
import com.telenav.navservice.model.AppTest;
import com.telenav.navservice.network.TnNetworkTest;
import com.telenav.navservice.policy.PoliciesTest;
import com.telenav.navservice.policy.PolicyRequesterTest;
import com.telenav.navservice.policy.PolicyTest;

public class NavServiceTestSuite
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.telenav.navservice");
        //$JUnit-BEGIN$
        suite.addTest(new JUnit4TestAdapter(ControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(ServiceLocatorTest.class));
        
        suite.addTest(new JUnit4TestAdapter(CellRecorderTest.class));
        suite.addTest(new JUnit4TestAdapter(GpsRecorderImplTest.class));
        suite.addTest(new JUnit4TestAdapter(GpsRecorderTest.class));
        suite.addTest(new JUnit4TestAdapter(LocationBufferTest.class));
        suite.addTest(new JUnit4TestAdapter(LocationSenderTest.class));
        suite.addTest(new JUnit4TestAdapter(StationaryMonitorTest.class));

        suite.addTest(new JUnit4TestAdapter(AppTest.class));
        
        suite.addTest(new JUnit4TestAdapter(TnNetworkTest.class));

        suite.addTest(new JUnit4TestAdapter(PoliciesTest.class));
        suite.addTest(new JUnit4TestAdapter(PolicyRequesterTest.class));
        suite.addTest(new JUnit4TestAdapter(PolicyTest.class));
        //$JUnit-END$
        return suite;
    }

}
