package com.phonecompany.billing;

import java.math.BigDecimal;

public class App 
{
    public static void main( String[] args )
    {
        TelephoneBillCalcApp app = new TelephoneBillCalcApp();
        BigDecimal cost = app.calculate("420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57\n420776562353,18-01-2020 08:59:20,18-01-2020 09:10:00\n420776562353,19-01-2020 08:59:20,19-01-2020 09:10:00");
        System.out.println(cost);
    }
}
