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
package org.tigri.writer.sandbox.mm.controller;

import java.io.File;
import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;
import org.tigri.writer.mindmap.project.MindMapProjectImpl;
import org.tigri.writer.mindmap.project.ProjectsImpl;
import org.tigri.writer.mindmap.project.api.Projects;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.tigri.writer.mindmap.project.api.ProjectInformation;
import org.tigri.writer.sandbox.mm.controller.api.MindMapProjectController;
import static java.util.Objects.requireNonNull;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNull;

@ServiceProvider(service = MindMapProjectController.class)
public class MindMapProjectControllerImpl implements MindMapProjectController {

    private final Projects projects = new ProjectsImpl();

    @Override
    public MindMapProject newProject() {
        MindMapProject project = new MindMapProjectImpl();
        openProject(project);
        return project;
    }

    @Override
    public Optional<MindMapProject> openProject(File file) {
        return Optional.of(null);
    }

    @Override
    public void saveProject(MindMapProject project) {
        requireNonNull(project);

        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        File projectFile;
        if (null == (projectFile = pi.getFile())) {
            saveAsProject(project);
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

    private void saveAsProject(MindMapProject project) {
        requireNonNull(project);

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(MAP_EXTENSION);
            }

            @Override
            public String getDescription() {
                return NbBundle.getMessage(MindMapProjectControllerImpl.class, "SaveAsProject_filter_description")
                        + " [" + MAP_EXTENSION + "]";
            }
        };

        //File chooser
        final JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(filter);
        int returnFile = chooser.showSaveDialog(null);

        if (returnFile == JFileChooser.APPROVE_OPTION) {
            File saveAsFile = chooser.getSelectedFile();
            saveAsFile = FileUtil.normalizeFile(saveAsFile);
            saveProject(project, saveAsFile);
        }
    }

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
        projects.addProject(project);
    }

    private void closeProject(MindMapProject project) {
        projects.removeProject(project);

        if (projects.getCurrentProject().orElse(null) == project) {
            projects.setCurrentProject(null);
        }
    }

}
