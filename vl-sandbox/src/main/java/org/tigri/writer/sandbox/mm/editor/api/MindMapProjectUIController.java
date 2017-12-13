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
package org.tigri.writer.sandbox.mm.editor.api;

import java.io.File;
import org.tigri.writer.mindmap.project.api.MindMapProject;

/**
 *
 * @author skrymets
 */
public interface MindMapProjectUIController {

    public MindMapProject getCurrentProject();

    public boolean canNewProject();

    public MindMapProject newProject();

    public boolean canOpenProject();

    public void openProject(File file);

    public boolean canSaveProject();

    public void saveProject();

    public boolean canSaveAsProject();

    public void saveAsProject();

    public boolean canCloseProject();

    public void closeProject();

}
