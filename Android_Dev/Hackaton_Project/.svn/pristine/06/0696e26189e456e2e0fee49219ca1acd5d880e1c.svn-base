package com.telenav.carconnect.provider.applink;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.telenav.app.CommManager;
import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.fordapplink.RouteRequest;
import com.telenav.carconnect.fordapplink.RouteResponse;
import com.telenav.carconnect.mislog.CarConnectMisLogFactory;
import com.telenav.carconnect.mislog.CommonCarConnectMisLog;
import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.carconnect.provider.ProtocolConvertor;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;

public class AppLinkEncodeDestinationProvider extends AbstractProvider implements SoapSender.Receiver
{
	
    private final SoapSender sender = new SoapSender(this);
	public void handle(String eventType, Object eventData) 
	{
		if(eventType.equals(CarConnectEvent.ENCODE_DESTINATION_REQUEST))
		{

            RouteRequest routeRequest=(RouteRequest)eventData;
            String requestXml;
            Host host;
            requestXml=createApiServerReqeustXml(routeRequest);

            ServiceLocator hostProvider =  (ServiceLocator)CommManager.getInstance().getComm().getHostProvider();
            String url = hostProvider.getDomainUrl(CommManager.FORD_APPLINK_API_SERVER_URL_DOMAIN_ALIAS);
            host = hostProvider.createHost(url);
            
            sender.sendRequest(host, requestXml,routeRequest.getRequestID());
            
            
            AppLinkRouteProvider.addToRecent(ProtocolConvertor.reitriveAddres(routeRequest.getDest()));
            
		}
	}

    @Override
    public void register()
    {
        getEventBus().subscribe(CarConnectEvent.ENCODE_DESTINATION_REQUEST, this);
        
    }

    @Override
    public void unregister()
    {
        getEventBus().unsubscribe(CarConnectEvent.ENCODE_DESTINATION_REQUEST, this);
        
    }
    
    @Override
    public void receive(Document doc,long requestID)
    {
        if(doc==null)
        {
            RouteResponse response=new RouteResponse();
            response.setId(requestID);
            response.setStatusCode(RouteResponse.NETWORK_ERROR);
            getEventBus().broadcast(CarConnectEvent.ENCODE_DESTINATION_RESPONSE, response);
            return;
        }
        
        Vector data = null;
        
        // Logging
        String str = null;
        if (doc != null)
        {
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                StreamResult result = new StreamResult(new StringWriter());
                DOMSource source = new DOMSource(doc);
                transformer.transform(source, result);
                str =  result.getWriter().toString();
              } catch(TransformerException ex) {
                ex.printStackTrace();
              }
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: ApplinkEncodeDestination receive response - " + str);
        
        if (doc != null)
        {
                data = convertResponse(doc);
        }
        RouteResponse response=new RouteResponse();
        response.setId(requestID);
        response.setPayload(data);
        if(data==null)
        {
            response.setStatusCode(RouteResponse.ENCODE_DESTINATION_ERROR);
        }else{
            response.setStatusCode(RouteResponse.SUCCESS);
            //log the request
            CommonCarConnectMisLog misLog = CarConnectMisLogFactory.getInstance().createCarConnectNavMisLog();
            misLog.setSearchId(requestID + "");
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                        { misLog });
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect Mislog------:653(TYPE_HEAD_UNIT_CONNECTED_DRIVE_TO) is loggered!");
        }
        getEventBus().broadcast(CarConnectEvent.ENCODE_DESTINATION_RESPONSE, response);
        
    }
    
    private Vector convertResponse(Document doc)
    {
        Node node = doc.getElementsByTagName("code").item(0);
        String str = node == null ? null : node.getTextContent();
        if (!"OK".equals(str)) return null;
        
        node = doc.getElementsByTagName("destination").item(0);
        str = node == null ? null : node.getTextContent();
        if (str == null) return null;
        
        Vector<String> data = new Vector<String>();
        //data.add("AAMIAAA=");
        data.add(str);
        return data;
    }
    
    private String createApiServerReqeustXml(RouteRequest request)
    {
        if(request==null) return null;
        
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String encodedLocation=request.getEncodedCurrentLocation();
        int size=encodedLocation.length();
        PointOfInterest poi = request.getDest();
        String destLabel=poi.getName();
        String destFirstLine=poi.getAddress().getFormattedAddress();
        String phone = poi.getPhone();
        double destLat=poi.getLocation().getLatitude();
        double destLon=poi.getLocation().getLongitude();
                
        MandatoryProfile profile = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode();
        String phoneNumber = (profile != null) ? profile.getUserInfo().phoneNumber : "";
        String platform = (profile != null) ? profile.getClientInfo().platform : "android";
        String version = (profile != null) ? profile.getClientInfo().version : "7.x";
        String device = (profile != null) ? profile.getClientInfo().device : "";
        String buildNumber = (profile != null) ? profile.getClientInfo().buildNumber : "";
        String requestID = device + "_" + phoneNumber + "_" + String.valueOf(System.currentTimeMillis());
        
        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "soap:Envelope");
            serializer.attribute("", "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
            serializer.startTag("", "soap:Header");
            serializer.endTag("", "soap:Header");
            serializer.startTag("", "soap:Body");
            serializer.startTag("", "ns6:EncryptDestinationRequest");
            serializer.attribute("","xmlns:ns6","http://telenav.com/tnapi/services/partner/ford/v10/");
            serializer.startTag("", "phoneNumber");
            serializer.text(phoneNumber);
            serializer.endTag("", "phoneNumber");
            serializer.startTag("", "platform");
            serializer.text(platform);
            serializer.endTag("", "platform");
            serializer.startTag("", "version");
            serializer.text(version);
            serializer.endTag("", "version");
            serializer.startTag("", "device");
            serializer.text(device);
            serializer.endTag("", "device");
            serializer.startTag("", "buildNumber");
            serializer.text(buildNumber);
            serializer.endTag("", "buildNumber");
            serializer.startTag("", "requestID");
            serializer.text(requestID);
            serializer.endTag("", "requestID");
            serializer.startTag("", "origin");
            serializer.attribute("","size",String.valueOf(size));
            serializer.text(encodedLocation);
            serializer.endTag("", "origin");
            serializer.startTag("", "destLabel");
            serializer.text(destLabel);
            serializer.endTag("", "destLabel");
            serializer.startTag("", "destType");
            //TODO - clarify what is needed to put in correct value.
            serializer.text("Unknown");
            serializer.endTag("", "destType");
            serializer.startTag("", "destFirstLine");
            serializer.text(destFirstLine);
            serializer.endTag("", "destFirstLine");
            serializer.startTag("", "destPhone");
            serializer.text(phone);
            serializer.endTag("", "destPhone");
            serializer.startTag("", "destLat");
            serializer.text(String.valueOf(destLat));
            serializer.endTag("", "destLat");
            serializer.startTag("", "destLon");
            serializer.text(String.valueOf(destLon));
            serializer.endTag("", "destLon");
            serializer.endTag("","ns6:EncryptDestinationRequest");
            serializer.endTag("", "soap:Body");
            serializer.endTag("", "soap:Envelope");
            serializer.endDocument();
            return writer.toString();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
