package utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    //TODO:javadoc
    public static Date parse(String date)
    {
        df.setLenient(false);
        try {
            Date d = df.parse(date);
            return d;
        } catch (ParseException e) {
            return null;
        }

    }
    //TODO:javadoc
    public static String format(Date date)
    {
        df.setLenient(false);
        return df.format(date);
    }
    /**
     * Validates the entered date.
     * @param date the entered date.
     * @return true if the date is correct, else - false.
     */
    public static boolean isCorrect(Date date)
    {
        long delta = date.getTime() - Calendar.getInstance().getTimeInMillis();
        return delta > 0;
    }
}

