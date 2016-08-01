package carta.client;

import com.google.gwt.user.client.ui.TextArea;

public class Utility {
	
	static void appendLineToTextArea(TextArea ta, String line) {
		ta.setText(ta.getText() + line + "\n");
	}
}
