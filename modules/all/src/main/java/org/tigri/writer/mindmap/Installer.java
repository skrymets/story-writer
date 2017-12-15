package org.tigri.writer.mindmap;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.tigri.writer.mindmap.controller.api.MindMapProjectController;
import org.tigri.writer.mindmap.editor.ui.MindMapEditorTopComponent;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import org.tigri.writer.mindmap.project.api.ProjectListener;

/**
 *
 * @author skrymets
 */
public class Installer extends ModuleInstall {

    private static final long serialVersionUID = 4821800441375764857L;

    @Override
    public void restored() {
        MindMapProjectController projectController = Lookup.getDefault().lookup(MindMapProjectController.class);
        projectController.addProjectListener(new ProjectListener() {

            protected MindMapEditorTopComponent findMindMapEditor(MindMapProject project) {
                return TopComponent.getRegistry().getOpened().stream()
                        .filter(tc -> tc instanceof MindMapEditorTopComponent)
                        .map(tc -> (MindMapEditorTopComponent) tc)
                        .filter(tc -> tc.getProject().isPresent() && tc.getProject().get() == project)
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public void onCreateProject(MindMapProject project) {
                WindowManager.getDefault().invokeWhenUIReady(() -> {
                    MindMapEditorTopComponent editor = findMindMapEditor(project);
                    if (editor == null) {
                        // re-new
                        editor = new MindMapEditorTopComponent(project);
                        editor.open();
                    }
                    editor.requestActive();
                });
            }

            @Override
            public void onSelectProject(MindMapProject project) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onUnSelectProject(MindMapProject project) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onCloseProject(MindMapProject project) {
                MindMapEditorTopComponent editor = findMindMapEditor(project);
                if (editor != null) {
                    editor.close();
                }
            }
        });
    }

}
