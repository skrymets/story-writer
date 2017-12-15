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
package org.tigri.writer.mindmap.controller;

import java.io.File;
import java.util.Collections;
import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.tigri.writer.mindmap.controller.api.MindMapController;
import org.tigri.writer.mindmap.controller.api.MindMapProjectController;
import org.tigri.writer.mindmap.model.MindMap;
import org.tigri.writer.mindmap.project.MindMapProjectImpl;
import org.tigri.writer.mindmap.project.ProjectsImpl;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import org.tigri.writer.mindmap.project.api.ProjectInformation;
import org.tigri.writer.mindmap.project.api.ProjectListener;
import org.tigri.writer.mindmap.project.api.Projects;

@ServiceProvider(service = MindMapProjectController.class)
public class MindMapProjectControllerImpl implements MindMapProjectController {

    private final Set<ProjectListener> projectListeners = Collections.synchronizedSet(new HashSet());

    private final Projects projects = new ProjectsImpl();

    @Override
    public MindMapProject newProject() {
        MindMapProject project = new MindMapProjectImpl();
        
        MindMapController mmc = Lookup.getDefault().lookup(MindMapController.class);
        MindMap mindMap = mmc.createMindMap(NbBundle.getMessage(MindMap.class, "CTL_RootNodeDefaultTitle"));
        project.add(mindMap);
        
        projects.addProject(project);

        for (ProjectListener listener : projectListeners) {
            listener.onCreateProject(project);
        }

        return project;
    }

    @Override
    public Optional<MindMapProject> openProject(File file) {
        return Optional.empty();
    }

    @Override
    public void saveProject(MindMapProject project) {
        requireNonNull(project);

        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        File projectFile;
        if (null == (projectFile = pi.getFile())) {
            throw new UnsupportedOperationException();
        } else {
            saveProject(project, projectFile);
        }
    }

    @Override
    public void saveProject(MindMapProject project, File file) {
        requireNonNull(project);
        requireNonNull(file);

    }
    protected static final String MAP_EXTENSION = ".map";

    @Override
    public void closeCurrentProject() {
        closeProjects(singleton(projects.getCurrentProject().orElse(null)));
    }

    @Override
    public void closeProjects(Set<MindMapProject> projects) {
        requireNonNull(projects);
        projects.stream()
                .filter(Objects::nonNull)
                .forEach((MindMapProject project) -> {
                    closeProject(project);
                });

    }

    @Override
    public void closeAllProjects() {
        closeProjects(new HashSet<>(projects.getProjects()));
    }

    @Override
    public Projects getProjects() {
        return projects;
    }

    @Override
    public Optional<MindMapProject> getCurrentProject() {
        return projects.getCurrentProject();
    }

    private void openProject(MindMapProject project) {
        // closeCurrentProject();
    }

    private synchronized void closeProject(MindMapProject project) {
        projects.removeProject(project);

        if (projects.getCurrentProject().isPresent()) {
            if (projects.getCurrentProject().get() == project) {
                projects.setCurrentProject(null);
            }
        }

        for (ProjectListener listener : projectListeners) {
            listener.onCloseProject(project);
        }
    }

    @Override
    public void addProjectListener(ProjectListener listener) {
        if (listener == null) {
            return;
        }
        projectListeners.add(listener);
    }

    @Override
    public void removeProjectListener(ProjectListener listener) {
        if (listener == null) {
            return;
        }
        projectListeners.remove(listener);
    }

}
