package com.telenav.data.serializable.txnode;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.data.serializable.IAddressSerializable;
import com.telenav.data.serializable.IExtraInfoSerializable;
import com.telenav.data.serializable.ILocationSerializable;
import com.telenav.data.serializable.IMandatorySerializable;
import com.telenav.data.serializable.IMisLogSerializable;
import com.telenav.data.serializable.IPoiSerializable;
import com.telenav.data.serializable.IPreferenceSerializable;
import com.telenav.data.serializable.IPrimitiveSerializable;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *@author gbwang
 *@date 2011-6-28
 */

public class TxNodeSerializableManagerTest extends TestCase
{
	TxNodeSerializableManager txNodeSerializableManager;
	
	protected void setUp() throws Exception
	{
		txNodeSerializableManager = new TxNodeSerializableManager();
		super.setUp();
	}
	
	public void testGetAddressSerializable()
	{
		IAddressSerializable addressSerializable = txNodeSerializableManager.getAddressSerializable();
		Assert.assertNotNull(addressSerializable);
	}
	
	public void testGetPoiSerializable()
	{
		IPoiSerializable poiSerializable = txNodeSerializableManager.getPoiSerializable();
		Assert.assertNotNull(poiSerializable);
	}
	
	public void testGetPrimitiveSerializable()
	{
		IPrimitiveSerializable primitiveSerializable = txNodeSerializableManager.getPrimitiveSerializable();
		Assert.assertNotNull(primitiveSerializable);
	}
	
	public void testGetLocationSerializable()
	{
		ILocationSerializable locationSerializable = txNodeSerializableManager.getLocationSerializable();
		Assert.assertNotNull(locationSerializable);
	}
	
	public void testGetPreferenceSerializable()
	{
		IPreferenceSerializable preferenceSerializable = txNodeSerializableManager.getPreferenceSerializable();
		Assert.assertNotNull(preferenceSerializable);
	}
	
	public void testGetMisLogSerializable()
	{
		IMisLogSerializable misLogSerializable = txNodeSerializableManager.getMisLogSerializable();
		Assert.assertNotNull(misLogSerializable);
	}
	
	public void testGetMandatorySerializable()
	{
		IMandatorySerializable mandatorySerializable = txNodeSerializableManager.getMandatorySerializable();
		Assert.assertNotNull(mandatorySerializable);
	}
	
	public void testGetExtraInfoSerializable()
	{
		IExtraInfoSerializable extraInfoSerializable = txNodeSerializableManager.getExtraInfoSerializable();
		Assert.assertNotNull(extraInfoSerializable);
	}	
}
