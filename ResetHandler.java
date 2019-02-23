
package de.dkaiser.sudoku.handlers;

import java.util.Collection;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import de.dkaiser.sudoku.parts.SamplePart;

public class ResetHandler {
	@Execute
	public void execute(EPartService partService) {
		Collection<MPart> parts = partService.getParts();
		for (MPart part : parts) {
			if (part.getObject() instanceof SamplePart) {
				((SamplePart) part.getObject()).reset();
			}
		}
	}

}