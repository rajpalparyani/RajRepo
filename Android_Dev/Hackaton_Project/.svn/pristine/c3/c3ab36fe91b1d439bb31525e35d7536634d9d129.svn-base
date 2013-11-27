package com.telenav.navservice;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

public class MockLocationManager extends TnLocationManager
{
    private static MockLocationManager instance = new MockLocationManager();
    
    private TestLocationProvider p;
    
    public static interface TestLocationProvider
    {
        public TnLocationProvider getProviderDelegate(String provider);
    }
    
    private MockLocationManager()
    {
        TnLocationManager.init(this);
    }
    
    public static MockLocationManager getInstance()
    {
        return instance;
    }
    
    public void setTestLocationProvider(TestLocationProvider p)
    {
        this.p = p;
    }
    
    @Override
    protected TnLocationProvider getProvider(TnCriteria criteria)
    {
        return null;
    }

    @Override
    protected TnLocationProvider getProviderDelegate(String provider)
    {
        return p.getProviderDelegate(provider);
    }

    @Override
    public boolean isGpsProviderAvailable(String provider)
    {
        return true;
    }
  
}
