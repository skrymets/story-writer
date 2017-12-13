/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigri.writer.mindmap.project.impl;

import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author skrymets
 */
public class LoadMindMapTask implements Runnable {

    protected static final String MIND_MAP = "MindMap";

    private final File file;

    public LoadMindMapTask(File file) {
        this.file = file;
    }

    @Override
    public void run() {

        try (ZipFile zip = new ZipFile(file)) {

            ZipEntry mindMapEntry = zip.getEntry(MIND_MAP);
            if (mindMapEntry == null) {
                //TODO: LOG
                
                return;
            }

        } catch (Exception e) {
            //TODO: LOG
        }

    }

}
