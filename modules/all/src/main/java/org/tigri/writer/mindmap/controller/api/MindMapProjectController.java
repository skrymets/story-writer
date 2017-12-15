package org.tigri.writer.mindmap.controller.api;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import org.tigri.writer.mindmap.project.api.ProjectListener;
import org.tigri.writer.mindmap.project.api.Projects;

public interface MindMapProjectController {

    MindMapProject newProject();

    Optional<MindMapProject> openProject(File file);

    void saveProject(MindMapProject project);

    void saveProject(MindMapProject project, File file);

    void closeCurrentProject();

    void closeAllProjects();

    void closeProjects(Set<MindMapProject> projects);

    Projects getProjects();

    Optional<MindMapProject> getCurrentProject();

    void addProjectListener(ProjectListener listener);

    void removeProjectListener(ProjectListener listener);

}
