<%
    String ptn = request.getParameter("ptn") ;
    int length = 6;
    

    	long ptnL = 0;
	if( ptn==null || ptn.length() != 10 )
	{
		out.println("Wrong or no PTN, please include the query \"ptn\" with a 10 digit value in the URL");
		return;
	}
    	try {
        	ptnL = Long.parseLong(ptn);
	} catch (java.lang.NumberFormatException e)
	{
		out.println("Wrong or no PTN, please include the query \"ptn\" with a 10 digit value in the URL");
		return;
	}

        // a prime number, not too small.
        long mainDisturbance = 19843L;

        // second prime number
        long secondDisturbance = 751L;
        final long tenThousand = 10000L;
        long middleResult = ((ptnL / (tenThousand * tenThousand)) * mainDisturbance)
                + ((ptnL / tenThousand) * secondDisturbance) + ((ptnL % tenThousand) * mainDisturbance)
                + (secondDisturbance * mainDisturbance);

        int refValue = 1;
        for (int i = 0; i < length; i++)
            refValue = refValue * 10;
        while (middleResult >= refValue)
        {
            middleResult = (middleResult % refValue) + (middleResult / refValue);
        }

        String resultPin = middleResult + "";

        if (resultPin.length() < length)
        {
            resultPin = resultPin + ptn.substring(4, ((4 + length) - resultPin.length()));
        }

        out.println( "Activation pin for " + ptn + " is " + resultPin );
    
    
    %>
    
    
