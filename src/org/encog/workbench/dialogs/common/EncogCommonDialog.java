/*
 * Encog Workbench v1.x
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.encog.workbench.dialogs.common;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.encog.workbench.EncogWorkBench;

/**
 * Common dialog box for use in the workbench.  Provides an ok and cancel
 * button.  Also provides methods for collecting and validating values
 * from the dialog box.
 * @author jheaton
 */
abstract public class EncogCommonDialog extends JDialog implements
		ActionListener {

	/**
	 * The OK button.
	 */
	private JButton ctrlOK;
	
	/**
	 * The cancel button.
	 */
	private JButton ctrlCancel;
	
	/**
	 * The pannel that holds the body of this dialog.
	 */
	private final JPanel bodyPanel;
	
	/**
	 * The panel that holds the OK and cancel button.
	 */
	private final JPanel buttonPanel;
	
	/**
	 * True if the user clicked OK and this dialog should
	 * be processed.
	 */
	private boolean shouldProcess;

	/**
	 * Construct the common dialog box.
	 * @param owner The owner of this dialog box.
	 */
	public EncogCommonDialog(final Frame owner) {
		super(owner, true);

		final Container content = getContentPane();
		content.setLayout(new BorderLayout());

		this.bodyPanel = new JPanel();
		this.buttonPanel = new JPanel();

		this.buttonPanel.add(this.ctrlOK = new JButton("OK"));
		this.buttonPanel.add(this.ctrlCancel = new JButton("Cancel"));

		this.ctrlCancel.addActionListener(this);
		this.ctrlOK.addActionListener(this);

		content.add(this.bodyPanel, BorderLayout.CENTER);
		content.add(this.buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Process action events.
	 * @param e The action event.
	 */
	public void actionPerformed(final ActionEvent e) {

		if (e.getSource() == this.ctrlOK) {
			if (collect()) {
				dispose();
				this.shouldProcess = true;
			}
		} else if (e.getSource() == this.ctrlCancel) {
			dispose();
			this.shouldProcess = false;
		}
	}

	/**
	 * Collect the values from all of the fields.
	 * @return True if no validation errors occured.
	 */
	public boolean collect() {
		try {
			collectFields();
			return true;
		} catch (final ValidationException e) {
			EncogWorkBench.displayError("Validation Error", e.getMessage());
			return false;
		}
	}

	/**
	 * Implmented by child classes to collect data from their fields.
	 * @throws ValidationException A validation error occured.
	 */
	abstract public void collectFields() throws ValidationException;

	/**
	 * @return the bodyPanel
	 */
	public JPanel getBodyPanel() {
		return this.bodyPanel;
	}

	/**
	 * @return the buttonPanel
	 */
	public JPanel getButtonPanel() {
		return this.buttonPanel;
	}

	/**
	 * Called to display the dialog box and wait for the user
	 * to click OK or Cancel.
	 * @return True if the dialog box should be processed.
	 */
	public boolean process() {
		setVisible(true);
		return this.shouldProcess;
	}

	/**
	 * Implemented by subclasses to set the fields of the dialog box.
	 */
	abstract public void setFields();

	/**
	 * Validate and collect the value from a numeric field.
	 * @param name The name of the field.
	 * @param field The field.
	 * @return The numeric value collected from the field.
	 * @throws ValidationException Thrown if a validation error occurs.
	 */
	public double validateFieldNumeric(final String name, final JTextField field)
			throws ValidationException {
		try {
			final double d = Double.parseDouble(field.getText());
			return d;
		} catch (final NumberFormatException e) {
			throw new ValidationException("Must enter a valid number for: "
					+ name);
		}
	}

	/**
	 * Validate and collect the value from a numeric field.
	 * @param name The name of the field.
	 * @param field The field.
	 * @param low The low value for the field.
	 * @return The numeric value collected from the field.
	 * @throws ValidationException Thrown if a validation error occurs.
	 */
	public double validateFieldNumeric(final String name,
			final JTextField field, final double low, final double high)
			throws ValidationException {
		final double d = validateFieldNumeric(name, field);
		if (d < low) {
			throw new ValidationException("Must enter a value above " + low
					+ " for: " + name);
		}
		if (d > high) {
			throw new ValidationException("Must enter a value below " + low
					+ " for: " + name);
		}

		return d;
	}

	/**
	 * Validate and collect the value from a string field.
	 * @param name The name of the field.
	 * @param field The field.
	 * @param required Is this field required?
	 * @return The value collected.
	 * @throws ValidationException A validation error occured.
	 */
	public String validateFieldString(final String name, final JComboBox field,
			final boolean required) throws ValidationException {
		String result = (String) field.getSelectedItem();
		if (result == null && required) {
			throw new ValidationException("The " + name + " field is required.");
		}

		if (result != null) {
			result = result.trim();
		}

		return result;
	}

	/**
	 * Validate and collect the value from a string field.
	 * @param name The name of the field.
	 * @param field The field.
	 * @param required Is this field required.
	 * @return The value collected.
	 * @throws ValidationException A validation error occured.
	 */
	public String validateFieldString(final String name,
			final JTextField field, final boolean required)
			throws ValidationException {
		final String result = field.getText().trim();
		if (result.length() < 1 && required) {
			throw new ValidationException("The " + name + " field is required.");
		}
		return result;
	}

}