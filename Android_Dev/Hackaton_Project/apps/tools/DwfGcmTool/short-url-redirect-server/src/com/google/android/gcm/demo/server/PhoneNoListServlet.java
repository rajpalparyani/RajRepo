package com.google.android.gcm.demo.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PhoneNoListServlet extends BaseServlet
{

    static final String ATTRIBUTE_STATUS = "status";

    /**
     * Displays the existing messages and offer the option to send a new one.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.print("<html><body>");
        out.print("<head>");
        out.print("  <title>Phone list</title>");
        out.print("  <link rel='icon' href='favicon.png'/>");
        out.print("</head>");
        String status = (String) req.getAttribute(ATTRIBUTE_STATUS);
        if (status != null)
        {
            out.print(status);
        }

        List<String> phoneNoList = Datastore.getPhoneNos();

        int total = phoneNoList.size();
        if (total == 0)
        {
            out.print("<h2>No phone No. registered!</h2>");
        }
        else
        {
            for (int i = 0; i < total; i++)
            {
                out.print("<h2>" + phoneNoList.get(i) + " device(s) registered!</h2>");
            }
        }
        out.print("</body></html>");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        doGet(req, resp);
    }

}
