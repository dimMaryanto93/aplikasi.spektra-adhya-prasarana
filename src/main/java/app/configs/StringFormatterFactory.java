package app.configs;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

}
