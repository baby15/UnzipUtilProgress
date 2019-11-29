package httc.test.com.getphone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */

public class DateUtils {


    /**
     *
     * @param timeMills YYYYMMDD
     * @return  yyyy-mm-dd类型
     */
    public static String getDate(String timeMills) {//参数为YYYYMMDD类型
        SimpleDateFormat formatOld = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");
        Date parse;
        String date = timeMills;
        try {
             parse = formatOld.parse(timeMills);
            date = formatNew.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    public static String getExpireDays(String timeMills) {//参数为YYYYMMDD类型,计算剩余时间
        SimpleDateFormat formatOld = new SimpleDateFormat("yyyyMMdd");
        String day = "0";
        try {
           long time_difference =  formatOld.parse(timeMills).getTime()- System.currentTimeMillis();
           if (time_difference >= 0) {
               long daysTime = time_difference / (1000 * 60 * 60 * 24);
               day = Long.toString(daysTime + 1);
           }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public static String getStringDate(long timeMills) {//参数为YYYYMMDD类型
        SimpleDateFormat formatOld = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");

      String  date = formatNew.format(new Date(timeMills));


        return date;
    }
}
