package carta.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.user.client.ui.DialogBox;


//http://stackoverflow.com/questions/10659467/gwt-disable-dragging-in-dialogbox
public class PopUp extends DialogBox {

	protected void beginDragging(MouseDownEvent e){
		e.preventDefault();
	}


}