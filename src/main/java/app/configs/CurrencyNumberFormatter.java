package app.configs;

import java.text.NumberFormat;

import org.springframework.stereotype.Component;

@Component
public class CurrencyNumberFormatter {

	public String getCurrencyFormate(Number number){
		return NumberFormat.getCurrencyInstance().format(number);
	}
	
}
