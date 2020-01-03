/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.ihm.filesystem;

/**
 *
 * @author Hyperion
 */
import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileSystemTreeRenderer extends DefaultTreeCellRenderer
{
    private FileSystemView fsv;

public FileSystemTreeRenderer(FileSystemView fsv)
{
    this.fsv = fsv;
}

public FileSystemTreeRenderer()
{
    this(FileSystemView.getFileSystemView());
}

public Component getTreeCellRendererComponent(JTree tree,
Object value, boolean sel, boolean expanded, boolean leaf,
int row, boolean hasFocus)
{
    if (!(value instanceof File))
    {
        return super.getTreeCellRendererComponent(tree, value,
        sel, expanded, leaf, row, hasFocus);
    }

    super.getTreeCellRendererComponent(
    tree, value, sel, expanded, leaf, row, hasFocus);

    setText(fsv.getSystemDisplayName((File) value));
    setIcon(fsv.getSystemIcon((File) value));

    return this;
}
}
