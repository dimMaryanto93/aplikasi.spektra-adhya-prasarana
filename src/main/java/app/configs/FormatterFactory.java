package app.configs;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class FormatterFactory {

	public String getCurrencyFormate(Number number) {
		return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(number);
	}

	public String getNumberIntegerOnlyFormate(Number number) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
		nf.setGroupingUsed(true);
		nf.setParseIntegerOnly(true);
		return nf.format(number);
	}

}
