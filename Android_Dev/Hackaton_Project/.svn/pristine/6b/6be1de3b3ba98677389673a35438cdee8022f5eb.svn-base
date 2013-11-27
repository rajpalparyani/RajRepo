package com.google.android.gcm.demo.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONTokener;

@SuppressWarnings("serial")
public class SendUrlsServlet extends BaseServlet
{

    /**
     * Processes the request to add a new message.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException
    {

        String dataToSend = getParameter(req, "friends");
        logger.severe(" DataToSend : " + dataToSend);

        JSONTokener jsonTokener = new JSONTokener(dataToSend);
        JSONArray jsonArray = null;
        try
        {
            jsonArray = (JSONArray) jsonTokener.nextValue();
            logger.severe(" DataToSend : " + dataToSend + " , " + jsonArray.length());
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String status = null;
        ;
        if (jsonArray == null || jsonArray.length() == 0)
        {
            status = "No URL to send!";
        }
        else
        {
            Queue queue = QueueFactory.getQueue("gcm");
            // NOTE: check below is for demonstration purposes; a real application
            // could always send a multicast, even for just one recipient
            // send a single message using plain post

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = null;
                try
                {
                    obj = jsonArray.getJSONObject(i);

                    if (obj != null)
                    {
                        String phoneNo = obj.getString("ptn");
                        String url = obj.getString("url");
                        String msg = obj.getString("msg");
                        String[] regIds = Datastore.fndDeviceRegIdByPhoneNo(phoneNo);
                        logger.severe("phoneNo : " + phoneNo + " , msg : " + msg
                                + " , url : " + url + " , hasRegId size ? " + (regIds == null ? 0 : regIds.length));

                        if (url != null && regIds != null && regIds.length > 0)
                        {
                            Datastore.saveMsg(phoneNo, msg);
                        
                            for (int m = 0 ; m < regIds.length ; m ++)
                            {
                                TaskOptions taskOptions = withUrl("/send");
                                taskOptions
                                        .param(SendMessageServlet.PARAMETER_DEVICE, regIds[m]);
                                taskOptions.param(SendMessageServlet.PARAMETER_URL, url);
                                taskOptions.param(SendMessageServlet.PARAMETER_MSG, msg);
    
                                queue.add(taskOptions);
                                status = "Single message queued for phone " + phoneNo;
                                logger.severe("status : " + status);
                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    status = "Single message me exception for index " + i;
                    e.printStackTrace();
                }

            }
        }
        setSuccess(resp);
    }
}
