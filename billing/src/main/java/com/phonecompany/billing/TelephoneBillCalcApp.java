package com.phonecompany.billing;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TelephoneBillCalcApp implements TelephoneBillCalculator {
    private static final BigDecimal STANDARD_RATE = BigDecimal.ONE;
    private static final BigDecimal REDUCED_RATE = new BigDecimal("0.50");
    private static final BigDecimal ADDITIONAL_REDUCED_RATE = new BigDecimal("0.20");

    @Override
    public BigDecimal calculate(String phoneLog) {

        //vytvori list jednotlivych calls
        List<String[]> calls = new ArrayList<>();
        String[] loglines = phoneLog.split("\n");
        for (String line : loglines) {
            calls.add(line.split(","));
        }


        //vypocita pocet hovorov na dane cisla
        Map<String, Integer> callCounts = new HashMap<>();
        for (String[] call : calls) {
            String phoneNumber = call[0];
            if (callCounts.containsKey(phoneNumber)) {
                callCounts.put(phoneNumber, callCounts.get(phoneNumber) + 1);
            } else {
                callCounts.put(phoneNumber, 1);
            }
        }

        //najde najviac volane cislo
        String mostFrequentNumber = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : callCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequentNumber = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        //zbehne vsetky cally po jednom a bude vypocitavam cenu
        for (String[] call : calls) {
            String startTimeString = call[1];
            String endTimeString = call[2];
            Date startTime;
            Date endTime;
            try {
                startTime = dateFormat.parse(startTimeString);
                endTime = dateFormat.parse(endTimeString);
            } catch (ParseException e) {
                throw new RuntimeException("Wrong date format in phone log: " + e.getMessage());
            }

            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTime(startTime);

            //nezapocita naviac volane cislo
            if (call[0].equals(mostFrequentNumber)) {
                continue;
            }

            //pocitanie costu jednotlivych minut
            while (calendarTime.getTime().before(endTime)) {
                int hour = calendarTime.get(Calendar.HOUR_OF_DAY);
                BigDecimal rate = (hour >= 8 && hour < 16) ? STANDARD_RATE : REDUCED_RATE;

                if (calendarTime.getTime().before(addMinutes(startTime))) {
                    totalCost = totalCost.add(rate);
                } else {
                    totalCost = totalCost.add(ADDITIONAL_REDUCED_RATE);
                }

                calendarTime.add(Calendar.MINUTE, 1);
            }
        }

        return totalCost;
    }

    private Date addMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 5);
        return calendar.getTime();
    }
}