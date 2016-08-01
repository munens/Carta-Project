package carta.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.google.gwt.user.client.ui.Label;

public class SortableLabelGroup {
	public int currentlySortedLabelIndex = -1;
	int numberOfSortedLabels = 0;

	List<SortableLabel> sls = new ArrayList<SortableLabel>();

	public SortableLabel createLabel(String title, int defaultPosition) {
		++numberOfSortedLabels;
		SortableLabel sl = new SortableLabel(title, defaultPosition, -1+numberOfSortedLabels, this);
		sls.add(sl);

		return sl;
	}

	public void reset() {
		if (currentlySortedLabelIndex >= 0) {
			SortableLabel sl = sls.get(currentlySortedLabelIndex);
			
			if (sl.isUsed()) {
				sl.removeStyleName(styleAscend);
				sl.removeStyleName(styleDescend);
			}
								
		}
			
		currentlySortedLabelIndex = -1;
	}
	
	public String styleAscend = "sortableLabelAscending";
	public String styleDescend = "sortableLabelDescending";
}


class SortableLabel extends Label {

	SortableLabelGroup slf;

	public int defaultPosition;
	int currentPosition;
	int labelIndex;

	static public int UNPRESSED = 0;
	static public int SORT_ASCEND = 1;
	static public int SORT_DESCEND = - 1;

	public SortableLabel(String title, int defaultPosition, int labelIndex, SortableLabelGroup slf) {
		super(title);
		this.defaultPosition = defaultPosition;
		this.labelIndex = labelIndex;
		this.slf = slf;
	}

	public int toggle() {
		if (!isUsed()) {
			slf.reset();
			slf.currentlySortedLabelIndex = labelIndex;
			currentPosition = defaultPosition;
		}
		else {
			currentPosition *= -1;
		}

		if (currentPosition == SORT_ASCEND) {
			this.setStyleName(slf.styleAscend);
		}
		else {
			this.setStyleName(slf.styleDescend);
		}
		
		return currentPosition;
	}
	
	public boolean isUsed () {
		return labelIndex == slf.currentlySortedLabelIndex;
	}
	
	
	
	public <T> List<T> getNextSortedList(List<T> list, Comparator<T> cptr) {

		if (!isUsed()) {
			if (toggle() == SortableLabel.SORT_ASCEND) {
				Collections.sort(list, cptr);			
			}
			else {
				Comparator<T> cptr_reverse = Collections.reverseOrder(cptr);
				Collections.sort(list, cptr_reverse);
			}
		}
		else {
			Collections.reverse(list);
		}
		
		return list;
	}
}
