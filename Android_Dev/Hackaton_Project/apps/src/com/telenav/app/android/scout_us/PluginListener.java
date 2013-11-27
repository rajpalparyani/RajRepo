/**
 * 
 */
package com.telenav.app.android.scout_us;



import java.net.URLDecoder;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.text.TextUtils;
import android.view.Window;

import com.telenav.logger.Logger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.maitai.impl.MaiTaiUtil;
import com.telenav.sdk.plugin.PluginAddress;


/**
 * @author qli(qli@telenav.cn)
 *
 */
public class PluginListener extends Activity
{
    protected Vector address;
    protected String item;
    
    
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        catchPlugin();
      
    }
    
    protected void catchPlugin()
    {
        address = new Vector();
        String postalAddress = null;
        
        String country = null;
        if (getApiLevel() < 5)
        {
            String[] methodProjection = new String[]{Contacts.ContactMethods.DATA};
            Cursor cur = getContentResolver().query(
                    getIntent().getData(), methodProjection, null,
                    null, null);
            postalAddress = "";
            if (cur != null) {
                cur.moveToFirst();
                int colum = cur.getColumnIndex(Contacts.ContactMethods.DATA);
                postalAddress = cur.getString(colum);
            }
            
        } else {

            Vector addresses = readAddresses(this, getIntent()
                    .getData());
            int addrSize = addresses.size();
            if (addrSize > 0) {
                String[] addressArray = ((String[]) (addresses.elementAt(0)));

                postalAddress = (addressArray[1] == null||(addressArray[1] != null && addressArray[1].length() == 0) ? "" : addressArray[1]); // street                                                                         
                postalAddress += ((addressArray[2] == null || (addressArray[2] != null && addressArray[2].length() == 0)) ? "" : ", " + addressArray[2]); // city
                postalAddress += ((addressArray[3] == null || (addressArray[3] != null && addressArray[3].length() == 0))? "" : ", " + addressArray[3]); // state
                postalAddress += ((addressArray[4] == null || (addressArray[4] != null && addressArray[4].length() == 0))? "" :  " " + addressArray[4]); // zip
                postalAddress += ((addressArray[5] == null || (addressArray[5] != null && addressArray[5].length() == 0))? "" : " " + addressArray[5]); // country
                country = addressArray[5];

            }else
            {
                postalAddress = "";
            }

        }
        //For Enzo device, the string would be like:
        //http://maps.google.com/maps?q=1130%20kifer%20Rd%2C%20sunnyvale%2Cca
//      postalAddress = null;
        if (postalAddress == null || postalAddress.length() == 0)
        {
            String requestUri = getIntent().getData().toString();
            //from browser->map the data is like this
//            requestUri = "http://maps.google.com/maps?f=d&saddr=37.373919+-121.999303&daddr=1576+Halford+Ave+Santa+Clara+CA+95051";
//            requestUri = "http://maps.google.com/maps?f=d&saddr=1130+kifer+rd+sunnyvale+CA&daddr=1576+Halford+Ave+Santa+Clara+CA+95051";
          
            //from google navigator->navigate the data is like this:
            //google.navigation:ll=37.337973,-121.985278&q=3131+Homestead+Rd%2C+Santa+Clara%2C+CA+95051&token=FXW7OQIdAqe6-Ck3SjN7esqPgDFqXdpFV6TbEw&opt=4%3A0,5%3A0
            
            //from browser -> google "starbucks" -> select one -> get directions
            //http://maps.google.com/maps?daddr=3461+W+3rd+St,+Los+Angeles,+CA+90020-1604+@34.069065,-118.290761
            MaiTaiUtil util = MaiTaiUtil.getInstance();
            Vector list = util.parseRequestUri(requestUri);
            
            String action = util.getValue(list, "action");
            String function = util.getValue(list, "f");
            if ("maps.google.com/maps/api/staticmap".equals(util.getValue(list, "action")) && (function == null || "d".equals(function)))
            {
                Vector marketersAddress = util.getValues(list, "markers");
                if(marketersAddress.size() > 0) 
                {
                    //TODO Current code just support one location point, that's mean just handle the first address, in this case I will create a new task onto JIRA for enhance.
                    for(int i = 0; i < marketersAddress.size(); i ++)
                    {
                        PluginAddress plugAddr = getPluginAddress(util.parseLocation((String)marketersAddress.get(i)), null, false);
                        address.add(plugAddr);
                    }
                    startTelenav(IMaiTai.MENU_ITEM_MAP_VALUE);
                    return;
                } 
            }
            else if(requestUri.indexOf("stagetln.me") != -1)
            {
                Intent intent = new Intent();
                
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory("com.telenav.intent.category.PLUGIN_LAUNCH");
                intent.setAction(TeleNav.PLUGIN_LAUNCH_DWF_LIST);
                intent.putExtra(IMaiTai.KEY_DWF_TINY_URL, requestUri);
                
                startActivity(intent);
                this.finish();
                System.exit(0);
                return;
            }
            else if(requestUri.indexOf("/dwf/go") != -1)
            {
                Intent intent = new Intent();
                
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory("com.telenav.intent.category.PLUGIN_LAUNCH");
                intent.setAction(TeleNav.PLUGIN_LAUNCH_DWF_LIST);

                for(int i = 0; i < list.size(); i++)
                {
                    String[] pair = (String[])list.get(i);
                    if("session_id".equals(pair[0]))
                    {
                        intent.putExtra(IMaiTai.KEY_DWF_SESSION_ID, pair[1]);
                    }
                    else if("user_key".equals(pair[0]))
                    {
                        intent.putExtra(IMaiTai.KEY_DWF_USER_KEY, pair[1]);
                    }
                    else if("formated_dt".equals(pair[0]))
                    {
                        intent.putExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT, pair[1]);
                    }
                }
                
                startActivity(intent);
                this.finish();
                System.exit(0);
                return;
            }
            else if (util.getValue(list, "action").contains("maps.google.com/maps") && ( function == null || "d".equals(function)))
            {
                String sourceAddress = util.getValue(list, "saddr");
                String destAddress = util.getValue(list, "daddr");
                
                if(sourceAddress != null && (sourceAddress.indexOf("@", 0) != -1))
                    sourceAddress = sourceAddress.substring(sourceAddress.indexOf("@", 0));
                if(destAddress != null && (destAddress.indexOf("@",0) != -1))
                    destAddress = destAddress.substring(destAddress.indexOf("@",0));
                
                if ("d".equals(function))
                {
                    if (sourceAddress == null)
                    {
                        address.add(getPluginAddress(destAddress, null, false));
                        startTelenav(IMaiTai.MENU_ITEM_DRIVE_TO);
                    }
                    else
                    {
                        PluginAddress source = getPluginAddress(parseLatLon(sourceAddress), null, false);
                        address.add(source);
                        
                        PluginAddress dest = getPluginAddress(parseLatLon(destAddress), null, false);
                        address.add(dest);
                        startTelenav(IMaiTai.MENU_ITEM_DIRECTIONS);
                    }
                    return;
                }
                else if (function == null && sourceAddress != null  && destAddress != null)
                {
                    if (sourceAddress.trim().length() == 0 )
                    {
                        address.add(getPluginAddress(destAddress, null, false));
                        startTelenav(IMaiTai.MENU_ITEM_DRIVE_TO);
                    }
                    else
                    {
                        // handle this case below, function is empty.
                        // http://maps.google.com/maps?&saddr=1055 E Evelyn Ave,Sunnyvale, CA 94086&daddr=201 S Mary Ave+Sunnyvale+CA+94086
                        PluginAddress source = getPluginAddress(parseLatLon(sourceAddress), null, false);
                        PluginAddress dest = getPluginAddress(parseLatLon(destAddress), null, false);
                        address.add(source);
                        address.add(dest);
                        startTelenav(IMaiTai.MENU_ITEM_DIRECTIONS);
                    }
                    return;
                }
                
                if (destAddress == null || destAddress.length() == 0)
                {
                    destAddress = util.getValue(list, "q");
                }
                if (destAddress == null || destAddress.length() == 0)
                {
                    destAddress = util.getValue(list, "ll");
                }
                // the source address if contains lat/lon such as "3737611,-12199797",
                // server will return parse error, so ignore directions
                postalAddress = destAddress;
                PluginAddress pluginAddress = getPluginAddress(postalAddress, null, false);
                address.add(pluginAddress);
                startTelenav(IMaiTai.MENU_ITEM_MAP_VALUE);
                return;
            } 
            else if ("maps.google.com/maps".equals(util.getValue(list, "action")) &&function != null && "browserDirections".equals(function))
            {
                String daddr = parseLatLon(util.getValue(list, "daddr"));
                PluginAddress pluginAddr = getPluginAddress(parseLatLon(daddr), null, false);
                address.add(pluginAddr);
                startTelenav(IMaiTai.MENU_ITEM_DRIVE_TO);
                return;
            } 
            else if ("google.navigation".equals(util.getValue(list, "scheme")))
            {
                String daddr = util.getValue(list, "q");
                if (daddr == null || daddr.length() == 0)
                {
                    daddr = util.getValue(list, "destAddr");
                }
                
                    //this is to handle such case:
                    //google.navigation:ll=37.353046,-121.997201&title=Starbucks&q=3605+El+Camino+Real&token=FVb2OQIdb3i6-CH2cGb2AfG9kQ
                    //It contains street but no city/state but lat/lon.
                    //We need combine them and let backend handle the situation.
                String ll = util.getValue(list, "ll");
                if (ll != null && ll.length() != 0)
                {
                    ll = convertLatLon(ll);
                    if(daddr != null && daddr.length() > 0)
                    {
                        daddr = daddr + "@" + ll;
                    }
                    else
                    {
                        daddr = "@" + ll;
                    }
                }

                if (daddr != null)
                {
                    postalAddress = daddr; //daddr;
                    PluginAddress pluginAddr = getPluginAddress(postalAddress, null, true);
                    address.add(pluginAddr);
                    startTelenav(IMaiTai.MENU_ITEM_DRIVE_TO);
                }
                
                return;
            }
            else if ("telenav".equals(util.getValue(list, "scheme"))
                    || "scout".equals(util.getValue(list, "scheme")))
            {
                if (action.equals("navTo"))
                {
                    String sourceAddress = util.getValue(list, "addr");
                    if (sourceAddress != null)
                    {
                        postalAddress = sourceAddress;
                        PluginAddress pluginAddr = getPluginAddress(postalAddress, null, false);
                        address.add(pluginAddr);
                        startTelenav(IMaiTai.MENU_ITEM_DRIVE_TO);
                        return;
                    }
                }
                else if (action.equals("map"))
                {
                    String sourceAddress = util.getValue(list, "addr");
                    if (sourceAddress != null)
                    {
                        postalAddress = sourceAddress;
                        PluginAddress pluginAddr = getPluginAddress(postalAddress, null, false);
                        address.add(pluginAddr);
                        startTelenav(IMaiTai.MENU_ITEM_MAP_VALUE);
                        return;
                    }
                }
                else if (action.equals("directions"))
                {
                    String sourceAddress = util.getValue(list, "addr1");
                    if (sourceAddress == null)
                    {
                        postalAddress = util.getValue(list, "addr2");
                        PluginAddress pluginAddr = getPluginAddress(postalAddress, null, false);
                        address.add(pluginAddr);
                        startTelenav(IMaiTai.MENU_ITEM_DRIVE_TO);
                    }
                    else
                    {
                        PluginAddress source = getPluginAddress(sourceAddress, null, false);
                        PluginAddress addr2 = getPluginAddress(util.getValue(list, "addr2"), null, false);
                        address.add(source);
                        address.add(addr2);
                        startTelenav(IMaiTai.MENU_ITEM_DIRECTIONS);
                    }
                    return;
                }
                else if (action.equals("search"))
                {
                    String sourceAddress = util.getValue(list, "addr");
                    if (sourceAddress != null)
                    {
                        postalAddress = sourceAddress;
                        PluginAddress pluginAddr = getPluginAddress(postalAddress, null, false);
                        address.add(pluginAddr);
                        item = util.getValue(list, "term");
                        startTelenav(IMaiTai.MENU_ITEM_BIZ_VALUE);
                        return;
                    }
                }
            }
            else if ("geo".equals(util.getValue(list, "scheme")))
            {
                int queryOffset = 2;
                int queryIndex = requestUri.indexOf("q=");
                if (queryIndex == -1)
                {
                    queryIndex = requestUri.indexOf("q =");
                    queryOffset = 3;
                }

                String query;
                if (queryIndex == -1)
                {
                    int index = requestUri.indexOf("geo:");
                    int index2 = requestUri.indexOf("?");
                    if (index2 == -1)
                        query = requestUri.substring(index + 4);
                    else
                        query = requestUri.substring(index + 4, index2);
                }
                else
                {
                    query = requestUri.substring(queryIndex + queryOffset);
                    int index2 = query.indexOf("&");
                    if (index2 != -1)
                    {
                        query = query.substring(0, index2);
                    }
                    try
                    {
                        query = URLDecoder.decode(query, "UTF-8");
                    }
                    catch (Exception e)
                    {
                    }
                }
                postalAddress = parseLatLon(query);
                PluginAddress pluginAddr = getPluginAddress(postalAddress, null, false);
                address.add(pluginAddr);

                startTelenav("");
                return;
            }
            
            postalAddress = util.getValue(list, "q");
        }
        
        
        if (TextUtils.isEmpty(postalAddress))
        {
            String queryString; // = activity.getIntent().getData().getQuery();
            try
            {
                queryString = getIntent().getData().getQuery();
                queryString = URLDecoder.decode(queryString, "UTF-8");
            }
            catch (Exception e)
            {
                queryString = null;
            }
            
            if(queryString != null) {
            
            StringTokenizer stringTokenizer = new StringTokenizer(queryString, "&");
            while (stringTokenizer.hasMoreTokens())
            {
                String queryItem = stringTokenizer.nextToken();
                String[] queryItems = queryItem.split("=");
                if (queryItems.length == 2 && "q".equalsIgnoreCase(queryItems[0]))
                {
                    postalAddress = queryItems[1];
                    break;
                }
            }
            }
            
            PluginAddress pluginAddr = getPluginAddress(postalAddress, null, false);
            address.add(pluginAddr);
            startTelenav(IMaiTai.MENU_ITEM_MAP_VALUE);
            return;
            
        }
        
        if( postalAddress != null )
        {
            PluginAddress pluginAddr = getPluginAddress(postalAddress, country, false);
            address.add(pluginAddr);
            startTelenav("");
        }
    }
    
    private Object[] parseGeoAddr(String addr)
    {
        if (addr == null) return new String[] {null};
        
        //add the support about the format like "1955+W.+El+Camino+Rd,++Mountain+View,+CA+94040+@37.394079,-122.098601"
        int index = addr.indexOf('@');
        if (index != -1)
            addr = addr.substring(0, index);
        
        index = addr.indexOf(',');
        if (index == -1)
            index = addr.indexOf('+');      
        if (index == -1)
            index = addr.indexOf(' ');      
        if (index == -1) return new String[] {addr};
        
        String strLat = addr.substring(0, index);
        String strLon = addr.substring(index + 1);
        
        try
        {
            double lat = Location.convert(strLat);
            double lon = Location.convert(strLon);
        
            return new Double[] {lat, lon};
        }
        catch(Throwable t)
        {
            return new String[] {addr};
        }
    }
    
    private String toPluginAddress(Object[] addr)
    {
        if (addr[0] instanceof String)
        {
            return (String)addr[0];
        }
        else
        {
            return "" + addr[0] + "," + addr[1];
        }
    }
    
    
    protected int getApiLevel()
    {
        String strApiLevel = Build.VERSION.SDK;
        int apiLevel = 1;
        try
        {
            apiLevel = Integer.parseInt(strApiLevel);
        }
        catch (Exception e)
        {
        }
        finally
        {
            return apiLevel;
        }
    }
    
    protected Vector readAddresses(Context context, Uri data )
    {
        Vector addresses = new Vector();

        Cursor addrCursor = context.getContentResolver().query(
                data,
                new String[] {StructuredPostal.TYPE, StructuredPostal.STREET,
                        StructuredPostal.CITY, StructuredPostal.REGION,
                        StructuredPostal.POSTCODE, StructuredPostal.COUNTRY},
                null,
                null, null);
        while (addrCursor != null && addrCursor.moveToNext()) {
            
            String type = addrCursor.getString(0);
            String street = addrCursor.getString(1);
            String city = addrCursor.getString(2);
            String state = addrCursor.getString(3);
            String zip = addrCursor.getString(4);
            String country = addrCursor.getString(5);

            String[] addr = new String[] { type, street, city, state, zip,
                    country };
            addresses.addElement(addr);
        }
        if(addrCursor != null )
        {
            addrCursor.close();
        }

        return addresses;
    }
    
    protected String parseLatLon(String address)
    {
        if (address == null)
        {
            return "Unknown";
        }
        String[] string = address.split(",");
        if(string.length != 2)
        {
            string = address.split(" ");
        }
        
        if (string.length != 2)
        {
            return address;
        }
        else
        {
            try
            {
                return Integer.parseInt(string[0].trim()) + "," + Integer.parseInt(string[1].trim());
            }
            catch(NumberFormatException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            try 
            {
                int lat = (int)(Location.convert(string[0].trim()) * 100000);
                int lon = (int)(Location.convert(string[1].trim()) * 100000);
                return lat + "," + lon;
            }
            catch (IllegalArgumentException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            return address;
        }
    }
    
    protected String convertLatLon(String latLon)
    {
        if(latLon == null)
        {
            return null;
        }
        String string[] = latLon.split(",");
        if(string.length != 2)
        {
            return null;
        }
        
        try
        {
            int lat = (int) (Location.convert(string[0].trim()) * 100000);
            int lon = (int) (Location.convert(string[1].trim()) * 100000);
            
            return lat + "," + lon;
        }
        catch (IllegalArgumentException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        return null;
    }
    
    protected void startTelenav(String flow)
    {
        Intent intent = new Intent();
        intent.setAction(TeleNav.ACTION_PLUGIN);
        intent.addCategory(TeleNav.CATEGORY_PLUGIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        HashMap data = getPluginData();
        data.put(IMaiTai.KEY_SELECTED_MENU_ITEM, flow);
        intent.putExtra(TeleNav.PLUGIN_REQUEST, data);
        
        startActivity(intent);
        this.finish();
        System.exit(0);
    }
    
    private HashMap getPluginData()
    {
        HashMap data = new HashMap();
        
        data.put(IMaiTai.KEY_ONE_BOX_ADDRESS, address);
        
        if (item != null && item.length() > 0)
            data.put(IMaiTai.KEY_SEARCH_ITEM, item);
        
        return data;
    }
    
    public PluginAddress getPluginAddress(String addressLine, String country, boolean useOneBox)
    {
        PluginAddress pluginAddress = new PluginAddress();
        pluginAddress.setAddressLine(addressLine);
        pluginAddress.setCountry(country);
        pluginAddress.setUseOneBox(useOneBox);
        
        return pluginAddress;
    }
}
