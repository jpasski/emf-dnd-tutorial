package org.example.book.dnd;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

public class ExampleEditingDomainViewerDropAdapter extends EditingDomainViewerDropAdapter {


	/**
	 * @param domain
	 * @param viewer
	 */
	public ExampleEditingDomainViewerDropAdapter(EditingDomain domain, Viewer viewer) {
		super(domain, viewer);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragEnter(DropTargetEvent event) {
		// If the event can support copy, but is trying to move, change to copy.
		// If the drop source disallows DROP_MOVE and the drag source prefers DROP_MOVE
		// the event detail will then be DROP_NONE, which disallows dropping :-/
		// Below inspired by org.eclipse.emf.edit.command.DragAndDropCommand.prepareDropInsert()
		if (event.detail != DND.DROP_COPY && event.detail != DND.DROP_LINK && (event.operations & DND.DROP_COPY) != 0) {
			event.detail = DND.DROP_COPY;
		}
		super.dragEnter(event);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#extractDragSource(java.lang.Object)
	 */
	@Override
	protected Collection<?> extractDragSource(Object object) {
		if (object instanceof String) {
			return Arrays.asList((String)object);
		}
		return super.extractDragSource(object);
	}

	
}
