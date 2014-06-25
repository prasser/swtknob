SWTKnob
====

This project provides a Knob widget for the Standard Widget Toolkit (SWT).

Example
------

```Java
// Create Knob
final Knob<Integer> knob = new Knob<Integer>(shell, SWT.NULL, new KnobScale.Integer(20,60));
knob.setLayoutData(new GridData(GridData.FILL_BOTH));

// Create Label
final Label label = new Label(shell, SWT.NULL);
label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

// Attach
knob.addSelectionListener(new SelectionAdapter() {
	public void widgetSelected(SelectionEvent arg0) {
		label.setText("Value: "+String.valueOf(knob.getValue()));
	}
});    
``` 

Screenshot
------
[![Screenshot-1](https://raw.github.com/prasser/swtknob/master/img/screenshot.png)](https://raw.github.com/prasser/swtknob/master/img/screenshot.png)

Download
------
Download [here](https://rawgithub.com/prasser/swtknob/master/jars/swtknob-0.0.1.jar). 

Documentation
------
Documentation can be found [here](https://rawgithub.com/prasser/swtknob/master/doc/index.html). 