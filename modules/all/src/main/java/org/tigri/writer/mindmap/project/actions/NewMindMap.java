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
package org.tigri.writer.mindmap.project.actions;

import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.tigri.writer.mindmap.controller.api.MindMapProjectUIController;
import org.tigri.writer.mindmap.editor.actions.*;

public class NewMindMap extends MindMapAction {

    private static final long serialVersionUID = 6177261515529340231L;

    @Override
    public void actionPerformed(ActionEvent ev) {
        Lookup.getDefault().lookup(MindMapProjectUIController.class).newProject();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewMindMap.class, "CTL_NewMindMap");
    }

    @Override
    public boolean isEnabled() {
        return Lookup.getDefault().lookup(MindMapProjectUIController.class).canNewProject();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

}
