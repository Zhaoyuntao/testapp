package com.zhaoyuntao.myjava;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 24/11/2021
 * description:
 */
abstract class MyFileDropListener implements DropTargetListener {

    public MyFileDropListener(Component component) {
        new DropTarget(component, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dropTargetDropEvent) {
        try {
            Transferable tr = dropTargetDropEvent.getTransferable();

            if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                System.out.println("file cp");
                List list = (List) (dropTargetDropEvent.getTransferable()
                        .getTransferData(DataFlavor.javaFileListFlavor));
                Iterator iterator = list.iterator();
                while (iterator.hasNext()) {
                    File f = (File) iterator.next();
                    onSelectFile(f);
                }
                dropTargetDropEvent.dropComplete(true);
            } else {
                dropTargetDropEvent.rejectDrop();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        }
    }

    abstract void onSelectFile(File file);
}
