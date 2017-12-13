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
package org.tigri.writer.sandbox.mm.controller.api;

import java.util.Collection;
import java.util.Set;
import org.tigri.writer.sandbox.mm.model.MindMap;
import org.tigri.writer.sandbox.mm.model.MindMapLink;
import org.tigri.writer.sandbox.mm.model.MindMapNode;

/**
 * @author skrymets
 */
public interface MindMapController {

    MindMap getMindMap();

    MindMapNode getRootNode();

    boolean isRootNode(MindMapNode node);

    boolean isActive(MindMapNode node);

    MindMapNode getActiveNode();

    MindMapNode setActiveNode(MindMapNode node);

    Set<MindMapNode> getSelectedNodes();

    boolean isSelected(MindMapNode node);

    void selectNodes(Collection<MindMapNode> nodes);

    void unSelectNodes(Collection<MindMapNode> nodes);

    void selectAllNodes();

    void unSelectAllNodes();

    MindMapNode insertNewParentNode(MindMapNode node, String data);

    MindMapNode createNewChildNode(MindMapNode parentNode, String data);

    MindMapNode createSiblingTopNode(MindMapNode node, String data);

    MindMapNode createSiblingBottomNode(MindMapNode node, String data);

    boolean canReAttach(MindMapNode node, MindMapNode targetNode);

    void reAttachNode(MindMapNode node, MindMapNode targetNode);

    void deleteNode(MindMapNode node);

    void deleteNodes(Set<MindMapNode> nodes);

    MindMapLink createReference(MindMapNode source, MindMapNode target);

    void removeReferences(Set<MindMapLink> links);

    Set<MindMapLink> removeAllReferences(MindMapNode node);

}
