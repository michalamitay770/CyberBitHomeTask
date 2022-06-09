package common.helpers;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {
  //  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    static  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public final static TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");

    public static Date now() {
       return new Date();
    }

    public static Date getFormattedDate(String stringDate) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(GMT_ZONE);
        Date date=null;
        try
        {
            date=sdf.parse(stringDate);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        finally{
            return date;
        }
    }

    public static String getRoundedDate(Date date) {
        return sdf.format(DateUtils.round(date, Calendar.MINUTE));
    }

    public static boolean compareDatesWithRound(Date date1,Date date2) {
        return getRoundedDate(date1).equals(getRoundedDate(date2));
    }
}
