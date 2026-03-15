package io.github.amsatrio.spring_crud_demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TimeZoneUtil {
        public Date getCurrentDateByHours(String hours) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = simpleDateFormat.format(date);
                dateString += " " + hours;
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                        date = simpleDateFormat.parse(dateString);
                } catch (Exception exception) {
                        log.error("TimeZoneUtil > error", exception);

                }
                return date;
        }
}
