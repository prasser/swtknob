SWTKnob
====

This project implements a Knob widget for the Standard Widget Toolkit (SWT).

A knob provides functionalities similar to a SWT Slider or a SWT Scale while occupying much less
screen space.

Introduction
------

A knob is rotated by clicking and dragging the mouse up or down. While a knob's selection
is changed the mouse is hidden. It re-appears when then mouse button is released. This is similar
to the behavior often implemented in audio editing software.

SWTKnob currently supports ranges for all primitive Java data types:
* KnobRange.Double(0d, 1d);
* KnobRange.Float(0f, 1f);
* KnobRange.Long(0l, 100l);
* KnobRange.Integer(0, 100);
* KnobRange.Character(0, 100);

When instantiated with a range for Longs, Integers or Chars and when the range between
the defined minimum and maximum is small enough, a knob will display ticks reflecting the
range and the indicator will snap to these ticks. A knob's value can be changed and retrieved 
via getters and setters, and a SelectionListener can be attached. A knob's sensitivity can be 
adjusted by calling Knob.setSensitivity(). The default value is 200.

Accessibility
------
SWTKnob supports keyboard navigation (e.g. via shell.setTabList(...)). Moreover, a knob can also be controlled with the
keyboard. When focused, the keys '0', '1', '2', ..., '9' set a knob's value to 0%, 10%, 20%, ..., 90% of the current range. 
The keys '+' and '-' increase and decrease the value by 10%. Up and down keys increase and decrease the value according to 
the defined sensitivity. When a knob is double-clicked, a dialog is shown that allows to enter a value with they keyboard. 

Customization
------
SWTKnob supports customizable color profiles and profiles for its input dialog. For example, a knob that is rendered differently,
when it is focused can be defined as follows: 

```Java
KnobColorProfile defaultProfile = KnobColorProfile.createFocusedSystemProfile(display);
KnobColorProfile focusedProfile = KnobColorProfile.createFocusedBlueRedProfile(display);
knob.setDefaultColorProfile(defaultProfile);
knob.setFocusedColorProfile(focusedProfile);
```

A profile for the input dialog can be used to display messages specific to the semantics of a knob's value, or for
internationalization, i.e., displaying messages in different languages. For example, a German language profile
can be created as follows:

```Java
KnobDialogProfile profile = new KnobDialogProfile();
profile.setMessage("Bitte geben Sie eine [type] im Bereich [range] ein:");
profile.setInteger("Ganzzahl");
profile.setDecimal("Gleitkommazahl");
profile.setTitle("Eingabemaske");
```

The string "[type]" will be replaced by the type of range (i.e., integer or decimal) and the string "[range]" 
will be replaced by a representation of the current range.

Example
------

In the following example, a knob is created for integers in the range [20,60]. 
When the knob's selection is changed, the current value is displayed in a label.

```Java
// Create Knob
final Knob<Integer> knob = new Knob<Integer>(shell, SWT.NULL, 
                                             new KnobScale.Integer(20,60));
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
A binary version (JAR file) is available for download [here](https://rawgithub.com/prasser/swtknob/master/jars/swtknob-1.0.0.jar). 

Documentation
------
Javadoc documentation can be found [here](https://rawgithub.com/prasser/swtknob/master/doc/index.html). 