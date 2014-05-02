package main.info.tiefenauer.songster.view;

import org.eclipse.swt.widgets.Composite;

public class TableViewer extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TableViewer(Composite parent, int style) {
		super(parent, style);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
