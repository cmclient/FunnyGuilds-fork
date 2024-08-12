package net.dzikoysk.funnyguilds.feature.placeholders;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class TimePlaceholdersService extends StaticPlaceholdersService<OffsetDateTime, OffsetDateTimePlaceholders> {

    public static OffsetDateTimePlaceholders createTimePlaceholders() {
        return new OffsetDateTimePlaceholders()
                .timeProperty("hour", OffsetDateTime::getHour)
                .timeProperty("minute", OffsetDateTime::getMinute)
                .timeProperty("second", OffsetDateTime::getSecond)
                .timeProperty("day_of_week", (time, locale) -> time.getDayOfWeek().getDisplayName(TextStyle.FULL, locale))
                .timeProperty("day_of_month", OffsetDateTime::getDayOfMonth)
                .timeProperty("month", (time, locale) -> time.getMonth().getDisplayName(TextStyle.FULL, locale))
                .timeProperty("month_number", OffsetDateTime::getMonthValue)
                .timeProperty("year", OffsetDateTime::getYear);
    }

}
