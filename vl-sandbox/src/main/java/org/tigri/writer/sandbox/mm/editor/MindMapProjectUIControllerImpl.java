/*
 * Copyright 2017 skrymets.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tigri.writer.sandbox.mm.editor;

import java.io.File;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import org.tigri.writer.sandbox.mm.controller.api.MindMapProjectController;
import org.tigri.writer.sandbox.mm.editor.api.MindMapProjectUIController;

@ServiceProvider(service = MindMapProjectUIController.class)
public class MindMapProjectUIControllerImpl implements MindMapProjectUIController {

    MindMapProjectController projectController;

    private boolean canNewProject = true;
    private boolean canOpenProject = true;
    private boolean canSaveProject = true;
    private boolean canCloseProject = true;
    private boolean canSaveAsProject = true;

    public MindMapProjectUIControllerImpl() {
        projectController = Lookup.getDefault().lookup(MindMapProjectController.class);
    }

    @Override
    public MindMapProject getCurrentProject() {
        return projectController.getProjects().getCurrentProject().orElse(null);
    }

    @Override
    public boolean canNewProject() {
        return canNewProject;
    }

    @Override
    public MindMapProject newProject() {
        if (closeCurrentProject()) {
            MindMapProject newProject = projectController.newProject();
            return newProject;
        }
        return null;
    }

    @Override
    public boolean canOpenProject() {
        return canOpenProject;
    }

    @Override
    public void openProject(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canSaveProject() {
        return canSaveProject;
    }

    @Override
    public void saveProject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canSaveAsProject() {
        return canSaveAsProject;
    }

    @Override
    public void saveAsProject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canCloseProject() {
        return canCloseProject;
    }

    @Override
    public void closeProject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean closeCurrentProject() {
        if (projectController.getCurrentProject().isPresent()) {
            //TODO: Save changes if any
        }

        return true;
    }

}
