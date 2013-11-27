/**
 * 
 */
package com.telenav.ui.citizen.android;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.ui.citizen.CitizenGallery;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.frogui.widget.android.AndroidFrogUiBinder;

/**
 * @author jbtian
 *
 */
public class AndroidCitizenUiBinder extends AndroidFrogUiBinder
{
	/**
     * Bind native UI for Frog UI
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if(component instanceof CitizenWebComponent)
        {
            return new AndroidCitizenWebView(context, (CitizenWebComponent)component);
        }
		else if (component instanceof CitizenGallery)
        {
            return new AndroidCitizenGallery(context, (CitizenGallery)component);
        }
        else
        {
            return super.bindNativeUiComponent(component);
        }
    }
}

    

