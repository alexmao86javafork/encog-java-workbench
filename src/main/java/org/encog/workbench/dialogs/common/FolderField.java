/*
 * Encog(tm) Workbench v3.3
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-workbench
 *
 * Copyright 2008-2014 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.workbench.dialogs.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FolderField extends PropertiesField implements ActionListener {

	private String value;
	private JButton button;

	public FolderField(String name, String label, boolean required) {
		super(name, label, required);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		((JTextField) this.getField()).setText(value);
	}

	@Override
	public int createField(JPanel panel, int x, int y, int width) {
		this.button = new JButton("Browse...");
		button.setSize(button.getPreferredSize());
		int buttonWidth = button.getWidth();
		button.setLocation(x + width - buttonWidth, y);

		JTextField field = new JTextField();
		field.setLocation(x, y);
		field.setSize(field.getPreferredSize());
		field.setSize(width - buttonWidth - 8, field.getHeight());
		this.setField(field);

		JLabel label = createLabel();
		label.setLocation(label.getX(), y);
		panel.add(label);
		panel.add(field);
		panel.add(button);

		button.addActionListener(this);

		return y + button.getHeight() + 2;
	}

	@Override
	public void collect() throws ValidationException {
		this.value = ((JTextField) this.getField()).getText();
		if (this.value.length() < 1 && this.isRequired()) {
			throw new ValidationException("The field " + this.getName()
					+ " is required.");
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.button) {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(((JTextField)this.getField()).getText()));
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			final int result = fc.showOpenDialog(this.getOwner());
			if (result == JFileChooser.APPROVE_OPTION) {
				String file = fc.getSelectedFile().getAbsolutePath();
				((JTextField) this.getField()).setText(file);
			}
		}
	}
}
