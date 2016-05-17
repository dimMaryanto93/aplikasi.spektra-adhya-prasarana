package app.configs;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class CurrencyNumberFormatter {

	public String getCurrencyFormate(Number number) {
		return NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(number);
	}

}
