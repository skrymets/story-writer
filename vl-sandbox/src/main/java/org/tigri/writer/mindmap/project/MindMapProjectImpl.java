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
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.tigri.writer.mindmap.project.api.MindMapProject;
import org.tigri.writer.mindmap.project.api.ProjectInformation;

public class MindMapProjectImpl implements MindMapProject, Serializable {

    private static final long serialVersionUID = -7910074731156195355L;

    private final InstanceContent instanceContent = new InstanceContent();

    private final AbstractLookup lookup = new AbstractLookup(instanceContent);

    public MindMapProjectImpl() {

        ProjectInformation projectInformation = new ProjectInformationImpl();
        instanceContent.add(projectInformation);

    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public final void add(Object inst) {
        instanceContent.add(inst);
    }

    public final void remove(Object inst) {
        instanceContent.remove(inst);
    }

}
