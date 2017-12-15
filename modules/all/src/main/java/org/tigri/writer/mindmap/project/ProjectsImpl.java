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
package org.tigri.writer.mindmap.project;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import org.tigri.writer.mindmap.project.api.Projects;

public class ProjectsImpl implements Projects, Serializable {

    private static final long serialVersionUID = -1177709743471691265L;

    private final Set<MindMapProject> projects;

    private MindMapProject currentProject;

    public ProjectsImpl() {
        this.projects = new HashSet<>();
    }

    @Override
    public Optional<MindMapProject> getCurrentProject() {
        return Optional.ofNullable(this.currentProject);
    }

    @Override
    public synchronized void setCurrentProject(MindMapProject project) {
        if (project == null || projects.contains(project)) {
            this.currentProject = project;
        }
    }

    @Override
    public Set<MindMapProject> getProjects() {
        return Collections.unmodifiableSet(projects);
    }

    @Override
    public synchronized void addProject(MindMapProject project) {
        if (project == null || projects.contains(project)) {
            return;
        }

        projects.add(project);
    }

    @Override
    public synchronized void removeProject(MindMapProject project) {
        if (project != null && projects.contains(project)) {
            projects.remove(project);
        }
    }

}
