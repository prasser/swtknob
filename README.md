SWTKnob
====

This project implements a Knob widget for the Standard Widget Toolkit (SWT).

A knob provides functionalities similar to a SWT Slider or a SWT Scale while occupying much less
screen space.

The knob is rotated by clicking and dragging the mouse up or down. While the knob's selection
is changed the mouse is hidden. It reappears when then mouse button is released. This is similar
to the behavior often implemented in audio editing software.

SWTKnob currently supports a scale for every primitive Java data type:
* KnobScale.Double(0d, 1d);
* KnobScale.Float(0f, 1f);
* KnobScale.Long(0l, 100l);
* KnobScale.Integer(0, 100);
* KnobScale.Character(0, 100);

When instantiated with a scale for Integers, Longs or Chars and when the range between
the defined minimum and maximum is small enough, the knob will display ticks reflecting the
scale and the indicator will snap to these ticks. A knob's value can be changed and retrieved 
via getters and setters, and a SelectionListener can be attached. A knob's sensitivity can be 
adjusted by calling Knob.setSensitivity(). The default value is 200.

Example
------

In the following example, a knob is created with an integer scale for values in the
range [20,60]. When the knob's selection is changed, the current value is displayed
in a label.

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
A binary version (JAR file) is available for download [here](https://rawgithub.com/prasser/swtknob/master/jars/swtknob-0.3.0.jar). 

Documentation
------
Javadoc documentation can be found [here](https://rawgithub.com/prasser/swtknob/master/doc/index.html). 