package carta.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

public class NumberOfArtworksField extends TextBox {

	Integer requestedNumberOfArtworks;

	public NumberOfArtworksField(int amount) {
		this.requestedNumberOfArtworks = amount;
		this.setText(requestedNumberOfArtworks.toString());

		this.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String input = event.getValue();

				if (isInteger(input))
					requestedNumberOfArtworks = Integer.parseInt(input);
				else {
					requestedNumberOfArtworks = Integer.MAX_VALUE;
					setText("All");
				}
			}
		});	
	}
	
	public Integer getNumberOfArtworks() {
		return requestedNumberOfArtworks;
	}

	static boolean isInteger(String str)  
	{  
		try {  
			Integer i = Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
}
