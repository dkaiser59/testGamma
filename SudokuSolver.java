package de.dkaiser.sudoku.parts;

public class SudokuSolver {

	private boolean[][][] possibleValue = new boolean[9][9][9];
	
	private int[][] finalValue = new int[9][9];

	public boolean[][][] getPossibleValue() {
		return possibleValue;
	}

	public int[][] solve(int[][] initalValues) {

		long startTime = System.nanoTime();
		setAllTrue();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int value = initalValues[i][j];
				if (0 < value && value < 10) {
					addValue(i, j, value);
				}
			}
		}

		printFinalValues();

		int numberOfTries = 0;
		int diff = 1;
		while (diff > 0) {
			int old = getPossibleEntries();
			numberOfTries++;
			tryFields();
			tryRows();
			tryColumns();
			diff = old - getPossibleEntries();
		}

		System.out.println("Number of Tries: " + numberOfTries);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double durationSec = duration * (10e-9);
		System.out.println("Duration: " + durationSec);

		return finalValue;
	}

	private void tryColumns() {
		for (int j = 0; j < 9; j++) {
			for (int k = 0; k < 9; k++) {
				int nbPos = 0;
				for (int i = 0; i < 9; i++) {
					if (possibleValue[i][j][k]) {
						nbPos++;
					}
				}
				if (nbPos == 1) {
					for (int i = 0; i < 9; i++) {
						if (possibleValue[i][j][k]) {
							addValue(i, j, k + 1);
						}
					}
				}

			}
		}
	}

	private void tryRows() {
		for (int i = 0; i < 9; i++) {
			for (int k = 0; k < 9; k++) {
				int nbPos = 0;
				for (int j = 0; j < 9; j++) {
					if (possibleValue[i][j][k]) {
						nbPos++;
					}
				}
				if (nbPos == 1) {
					for (int j = 0; j < 9; j++) {
						if (possibleValue[i][j][k]) {
							addValue(i, j, k + 1);
						}
					}
				}

			}
		}
	}

	private void tryFields() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int nbPos = 0;
				for (int k = 0; k < 9; k++) {
					if (possibleValue[i][j][k]) {
						nbPos++;
					}
				}
				if (nbPos == 1) {
					for (int k = 0; k < 9; k++) {
						if (possibleValue[i][j][k]) {
							addValue(i, j, k + 1);
						}
					}
				}

			}
		}
	}

	public int getPossibleEntries() {
		int number = 0;
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				for (int v = 0; v < 9; v++) {
					if (possibleValue[r][c][v]) {
						number++;
					}
				}
			}
		}
		return number;
	}

	private void printFinalValues() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (finalValue[i][j] != 0) {
					System.out.print(finalValue[i][j] + " ");
				} else {
					System.out.print("  ");
				}
				if ((j + 1) % 3 == 0 && j < 8) {
					System.out.print("| ");
				}
			}
			System.out.println();
			if ((i + 1) % 3 == 0 && i < 8) {
				System.out.println("----------------------");
			}
		}
		System.out.println("\n");
	}

	private void printPossibleValues() {
		for (int v = 0; v < 9; v++) {
			for (int r = 0; r < 9; r++) {
				for (int c = 0; c < 9; c++) {
					if (possibleValue[r][c][v]) {
						System.out.print(v + 1 + " ");
					} else {
						System.out.print(". ");
					}
					if ((c + 1) % 3 == 0 && c < 8) {
						System.out.print("| ");
					}
				}
				System.out.println();
				if ((r + 1) % 3 == 0 && r < 8) {
					System.out.println("----------------------");
				}
			}
			System.out.println("\n");
		}
	}

	private void setAllTrue() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				for (int v = 0; v < 9; v++) {
					possibleValue[r][c][v] = true;
				}
			}
		}
	}

	private void addValue(int r, int c, int v) {
		// set row to false
		for (int i = 0; i < 9; i++) {
			possibleValue[r][i][v - 1] = false;
		}
		// set column to false
		for (int j = 0; j < 9; j++) {
			possibleValue[j][c][v - 1] = false;
		}
		// set square to false
		setSquare(r, c, v);
		// set other values in same field to false
		for (int k = 0; k < 9; k++) {
			possibleValue[r][c][k] = false;
		}
		// set actual value to true
		possibleValue[r][c][v - 1] = true;
		// set finalValue
		finalValue[r][c] = v;
	}

	private void setSquare(int r, int c, int v) {
		int squareNumber = getSquareNumber(r, c);
		switch (squareNumber) {
		case 1:
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 2:
			for (int i = 0; i < 3; i++) {
				for (int j = 3; j < 6; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 3:
			for (int i = 0; i < 3; i++) {
				for (int j = 6; j < 9; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 4:
			for (int i = 3; i < 6; i++) {
				for (int j = 0; j < 3; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 5:
			for (int i = 3; i < 6; i++) {
				for (int j = 3; j < 6; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 6:
			for (int i = 3; i < 6; i++) {
				for (int j = 6; j < 9; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 7:
			for (int i = 6; i < 9; i++) {
				for (int j = 0; j < 3; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 8:
			for (int i = 6; i < 9; i++) {
				for (int j = 3; j < 6; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		case 9:
			for (int i = 6; i < 9; i++) {
				for (int j = 6; j < 9; j++) {
					possibleValue[i][j][v - 1] = false;
				}
			}
			break;

		default:
			break;
		}

	}

	private int getSquareNumber(int r, int c) {
		if (r < 3) {
			if (c < 3) {
				return 1;
			} else if (c < 6) {
				return 2;
			} else if (c < 9) {
				return 3;
			}
		} else if (r < 6) {
			if (c < 3) {
				return 4;
			} else if (c < 6) {
				return 5;
			} else if (c < 9) {
				return 6;
			}
		} else if (r < 9) {
			if (c < 3) {
				return 7;
			} else if (c < 6) {
				return 8;
			} else if (c < 9) {
				return 9;
			}
		}
		return -1;
	}

}
