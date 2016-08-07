package app.configs;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class StringFormatterFactory {

    public String getCurrencyFormate(Number number) {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(number);
    }

    public String getNumberIntegerOnlyFormate(Number number) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setGroupingUsed(true);
        nf.setParseIntegerOnly(true);
        return nf.format(number);
    }

    public String getDateTimeFormatterWithDayAndDateMonthYear(LocalDate date) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("E, dd MMMM yyyy");
        return formater.format(date);
    }

    public String getDateIndonesianFormatter(LocalDate date) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.getDefault());
        return formater.format(date);
    }

    public String getDateIndonesionFormatterOnlyYearAndMonth(LocalDate date) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MMMM").withLocale(Locale.getDefault());
        return formater.format(date);
    }

    public String getLongDateBetween(LocalDate awal, LocalDate akhir) {
        Long jmlYear = ChronoUnit.YEARS.between(awal, akhir);
        Long jmlBulan = ChronoUnit.MONTHS.between(awal, akhir);
        Long bulanDalamTahun = jmlBulan - (jmlYear * 12);

        StringBuilder sb = new StringBuilder();
        sb.append(jmlYear).append(" tahun ").append(bulanDalamTahun).append(" bulan ");
        return sb.toString();
    }

    public String getDateIndonesiaFormater(LocalDateTime date) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd MMMM yyyy 'pukul' hh:mm a")
                .withLocale(Locale.getDefault());
        return formater.format(date);
    }

}
