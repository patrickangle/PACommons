/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.swing.filedialog;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * A modern API for choosing files and handling errors for Swing. This is built
 * on the AWT file choosers, but may eventually be based on JNA to provide more
 * modern choosers.
 *
 * @author patrickangle
 */
public class FileChooser {

    private String title = null;
    private File defaultFileOrDirectory = null;
    private String placeholderFileName = "Untitled";
    private FileFilter fileFilter = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getDefaultFileOrDirectory() {
        return defaultFileOrDirectory;
    }

    public void setDefaultFileOrDirectory(File defaultFileOrDirectory) {
        this.defaultFileOrDirectory = defaultFileOrDirectory;
    }

    public String getPlaceholderFileName() {
        return placeholderFileName;
    }

    public void setPlaceholderFileName(String placeholderFileName) {
        this.placeholderFileName = placeholderFileName;
    }

    public FileFilter getFileFilter() {
        return fileFilter;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    /**
     * Show a dialog for selecting a single file to open with the given parent,
     * or null to orphan the dialog.
     *
     * @param parent
     * @return
     */
    public File showOpenFileDialog(Window parent) {
        FileDialog dialog = dialogForParent(parent);
        dialog.setMode(FileDialog.LOAD);
        dialog.setMultipleMode(false);
        decorateDialog(dialog);

        dialog.setVisible(true);

        File[] files = dialog.getFiles();
        if (files.length > 0) {
            return files[0];
        } else {
            return null;
        }
    }

    /**
     * Show a dialog for selecting a single or group of files to open with the
     * given parent, or null to orphan the dialog.
     *
     * @param parent
     * @return
     */
    public List<File> showOpenMultipleFilesDialog(Window parent) {
        FileDialog dialog = dialogForParent(parent);
        dialog.setMode(FileDialog.LOAD);
        dialog.setMultipleMode(true);
        decorateDialog(dialog);

        dialog.setVisible(true);

        File[] files = dialog.getFiles();
        return List.of(files);
    }

    /**
     * Show a dialog for defining a file to save with the given parent, or null
     * to orphan the dialog.
     *
     * @param parent
     * @return
     */
    public File showSaveFileDialog(Window parent) {
        FileDialog dialog = dialogForParent(parent);
        dialog.setMode(FileDialog.SAVE);
        dialog.setMultipleMode(false);
        decorateDialog(dialog);

        dialog.setVisible(true);
        // TODO: Enforce file extensions/provide extension based filtering in addition to current file filter mechanism.
        File[] files = dialog.getFiles();
        if (files.length > 0) {
            return files[0];
        } else {
            return null;
        }
    }

    protected FileDialog dialogForParent(Window parent) {
        if (parent instanceof Dialog) {
            return new FileDialog((Dialog) parent);
        } else if (parent instanceof Frame) {
            return new FileDialog((Frame) parent);
        } else if (parent == null) {
            return new FileDialog((Dialog) null);
        } else {
            throw new IllegalArgumentException("Parent must be instance of Dialog or Frame. `" + parent.getClass().getSimpleName() + "` is not permitted.");
        }
    }

    protected void decorateDialog(FileDialog dialog) {
        if (title != null) {
            dialog.setTitle(title);
        }

        if (defaultFileOrDirectory != null) {
            if (defaultFileOrDirectory.isFile()) {
                dialog.setFile(defaultFileOrDirectory.getPath());
            } else {
                dialog.setDirectory(defaultFileOrDirectory.getPath());
            }
        } else {
            dialog.setDirectory(System.getProperty("user.home"));
            dialog.setFile(placeholderFileName);
        }

        if (fileFilter != null) {
            dialog.setFilenameFilter((dir, name) -> {
                return fileFilter.accept(new File(dir, name));
            });
        }
    }
}
