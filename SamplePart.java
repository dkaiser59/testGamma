package de.dkaiser.sudoku.parts;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SamplePart {

	private ArrayList<Text> textInputs = new ArrayList<Text>();

	@Inject
	private MDirtyable dirty;

	private Color black;
	private Color blue;
	private Color green;

	private Composite parent;

	@PostConstruct
	public void createComposite(Composite parent) {
		Shell shell = parent.getShell();
		shell.setMinimumSize(650, 768);
		this.parent = parent;
		parent.setLayout(new GridLayout(11, false));

		Font displayFont = new Font(parent.getDisplay(), "Arial", 60, SWT.BOLD);
		black = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		blue = parent.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		green = parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);

		for (int j = 1; j <= 11; j++) {
			if (j == 4 || j == 8) {
				new Label(parent, SWT.None);
			} else {
				new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
			}
		}

		for (int i = 1; i <= 11; i++) {
			// horizontal separators
			if (i == 4 || i == 8) {
				for (int j = 1; j <= 11; j++) {
					if (j == 4 || j == 8) {
						new Label(parent, SWT.None);
					} else {
						new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
					}
				}
			} else {
				for (int j = 1; j <= 9; j++) {
					// vertical separators
					if (j == 4 || j == 7) {
						new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);
					}
					Text txtInput = new Text(parent, SWT.CENTER);
					txtInput.setFont(displayFont);

					txtInput.setMessage(" ");
					txtInput.addModifyListener(new ModifyListener() {
						@Override
						public void modifyText(ModifyEvent e) {
							dirty.setDirty(true);
						}
					});
					txtInput.setLayoutData(new GridData(GridData.CENTER));

					textInputs.add(txtInput);
				}
			}
		}
		textInputs.get(0).setFocus();
	}

	@Persist
	public void save() {
		getDataAndSolve();
		dirty.setDirty(false);
	}

	public void getDataAndSolve() {
		Font hintFont = new Font(parent.getDisplay(), "Monaco", 17, SWT.BOLD);
		Font displayFont = new Font(parent.getDisplay(), "Arial", 60, SWT.BOLD);

		int[][] inputValues = new int[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Text text = textInputs.get(9 * i + j);
				String input = text.getText();
				if (input.length() != 1) {
					inputValues[i][j] = 0;
				} else {
					inputValues[i][j] = Integer.parseInt(input);
				}
			}
		}

		SudokuSolver sudokuSolver = new SudokuSolver();
		int[][] solvedValues = sudokuSolver.solve(inputValues);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int solution = solvedValues[i][j];
				Text text = textInputs.get(9 * i + j);
				if (inputValues[i][j] != 0) {
					text.setForeground(black);
					text.setFont(displayFont);
					text.setText(Integer.toString(inputValues[i][j]));
				} else if (0 < solution && solution < 10) {
					text.setForeground(green);
					text.setFont(displayFont);
					text.setText(Integer.toString(solution));
				}
			}
		}

		if (sudokuSolver.getPossibleEntries() > 81) {
			boolean[][][] possibleValue = sudokuSolver.getPossibleValue();

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					int nbPos = 0;
					for (int k = 0; k < 9; k++) {
						if (possibleValue[i][j][k]) {
							nbPos++;
						}
					}
					String possibleValues = "";
					if (nbPos > 1) {
						for (int k = 0; k < 9; k++) {
							if (possibleValue[i][j][k]) {
								possibleValues = possibleValues + (k + 1);
							} else {
								possibleValues = possibleValues + " ";
							}
							if ((k + 1) % 3 != 0) {
								possibleValues = possibleValues + " ";
							}
							if ((k + 1) % 3 == 0 && k < 8) {
								possibleValues = possibleValues + "\n";
							}
						}
						Text text = textInputs.get(9 * i + j);
						text.setForeground(blue);
						text.setFont(hintFont);
						text.setText(possibleValues);
					}
				}
			}
		}

	}

	public void reset() {
		Font displayFont = new Font(parent.getDisplay(), "Arial", 60, SWT.BOLD);

		for (Text text : textInputs) {
			text.setFont(displayFont);
			text.setForeground(black);
			text.setText("");
		}
		textInputs.get(0).setFocus();
	}

}