# EMF and Drag & Drop Tutorial

## Intro

* Debug launch the `Runtime Instance` launch configuration
* In the runtime instance, create a General Project and a Java Project
* In the Java project, create a sample class with a sample method that contains some amount of filler text.
* In the General Project, create two sample EMF project (File &rarr; New &rarr; Other... &rarr; Example EMF Model Creation Wizards). For both, choose "Library" as the base model.
  * Book Model (My.book)
  * Library Model (My.library)
* Position `My.book`, `My.library`, and the Java method so they're all side-by-side in some way 
* Set a breakpoint on `org.example.book.dnd.ExampleEditingDomainViewerDropAdapter.extractDragSource(Object)`
* In `My.library`, drag the `Library` model to the `My.book` viewer and its `Library` model. Note the breakpoint being hit. Continue or inspect as desired.
* In the Java project, select some of the text in the Java editor and then drag over to the `My.book` viewer and its `Library` model. Also note the breakpoint being hit.

## What's Going On?

Not much. Nothing is created, but that's OK. Having that breakpoint even hit was magical :)
The magic is happening in `org.example.book.presentation.BookEditor.createContextMenuFor(StructuredViewer)`:

```java
int dndOperations = DND.DROP_COPY | DND.DROP_LINK; 

Transfer[] transfers = new Transfer[] {
	LocalTransfer.getInstance(),
	LocalSelectionTransfer.getTransfer(),
	TextTransfer.getInstance(),
	FileTransfer.getInstance()
};
viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
viewer.addDropSupport(dndOperations, transfers, new ExampleEditingDomainViewerDropAdapter(editingDomain, viewer));
```

To see what an unmodified, generated version looks like, take a look at `org.example.library.presentation.LibraryEditor.createContextMenuFor(StructuredViewer)`:

```java
int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance(), LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance() };
viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, viewer));
```

Differences:

* The `DND.DROP_MOVE` is removed, because that's silly in some cases. For example, the Java text case would *move* the text. You probably don't want that. I didn't. I wanted to copy it. However, just removing the `DND.DROP_MOVE` isn't good enough. Refer to `org.example.book.dnd.ExampleEditingDomainViewerDropAdapter.dragEnter(DropTargetEvent)` to see why.
* An additional `Transfer` is added that supports text transfers.
* The viewer's drop support adapter refers to the example one.
* Lastly, `org.example.book.dnd.ExampleEditingDomainViewerDropAdapter.extractDragSource(Object)` adds a string into a collection, which is expected by the existing EMF DnD support.

## Next Steps

The customized `extractDragSource` should call something that adapts the drag
sources to something that makes sense for the drop target. For a copy, refer to
`org.eclipse.emf.edit.command.InitializeCopyCommand.doExecute()` to see what
it's expecting. It looks for attributes and references on the drop target that
exist in the drag source: a model to model transformation.
