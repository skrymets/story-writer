package org.tigri.writer.mindmap.project.api;

public interface ProjectListener {

    /**
     * Notify a project has been created.
     *
     * @param project the newly created project
     */
    public void onCreateProject(MindMapProject project);

    /**
     * Notify a project has become selected
     *
     * @param project the project that is now selected (current)
     */
    public void onSelectProject(MindMapProject project);

    /**
     * Notify a project has become unselected
     *
     * @param project the project that has become unselected
     */
    public void onUnSelectProject(MindMapProject project);

    /**
     * Notify a project has become closed.
     *
     * @param project the project that has become closed
     */
    public void onCloseProject(MindMapProject project);

}
