package com.google.android.gcm.demo.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterPhoneNoServlet extends BaseServlet
{

    private static final String PARAMETER_REG_ID = "regId";

    private static final String PARAMETER_PHONE_NO = "phoneNo";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException
    {
        String regId = getParameter(req, PARAMETER_REG_ID);
        String phoneNo = getParameter(req, PARAMETER_PHONE_NO);

        logger.severe(" Register phoneNo : " + phoneNo + " , regId : " + regId);

        Datastore.unregisterPhoneNo(phoneNo);
        Datastore.registerPhoneNo(phoneNo, regId);
        setSuccess(resp);
    }

}
