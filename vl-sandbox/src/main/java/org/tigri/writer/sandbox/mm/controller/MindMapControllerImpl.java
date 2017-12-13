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

import static java.util.Arrays.asList;
import java.util.Collection;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.stream.Collectors;
import org.medal.graph.Split;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.lookup.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigri.writer.mindmap.project.ProjectsImpl;
import org.tigri.writer.mindmap.project.api.Projects;
import org.tigri.writer.sandbox.mm.controller.api.MindMapController;
import org.tigri.writer.sandbox.mm.exceptions.BrokenMindMapException;
import org.tigri.writer.sandbox.mm.exceptions.OperationNotAllowedException;
import org.tigri.writer.sandbox.mm.model.LinkData;
import org.tigri.writer.sandbox.mm.model.MindMap;
import org.tigri.writer.sandbox.mm.model.MindMapLink;
import org.tigri.writer.sandbox.mm.model.MindMapNode;
import org.tigri.writer.sandbox.mm.view.MindMapScene;

@ServiceProvider(service = MindMapController.class)
public class MindMapControllerImpl implements MindMapController {

    private static final Logger LOG = LoggerFactory.getLogger(MindMapControllerImpl.class);

    // private MindMap mindMap;
    // private MindMapScene mindMapScene;
    // private Set<MindMapNode> selectedNodes = new HashSet<>();
    // private MindMapNode activeNode;
    //    initRootNode(rootData);

    private Projects projects = new ProjectsImpl();

    public MindMapControllerImpl() {
    }
    
    protected final void initRootNode(String rootNodeData) {
        MindMapNode root = getMindMap().getRoot();
        if (root == null) {
            root = getMindMap().createNode(rootNodeData);
            __notifyNodesCreated(singleton(root));
            getMindMap().setRoot(root);
        }
    }

    @Override
    public MindMap getMindMap() {
        return mindMap;
    }

    @Override
    public MindMapNode getRootNode() {
        return getMindMap().getRoot();
    }

    @Override
    public boolean isRootNode(MindMapNode node) {
        requireNonNull(node);
        return (node == getRootNode());
    }

    @Override
    public boolean isActive(MindMapNode node) {
        requireNonNull(node);
        if (null == node) {
            return false;
        }
        return getActiveNode() == node;
    }

    @Override
    public MindMapNode getActiveNode() {
        return activeNode;
    }

    @Override
    public MindMapNode setActiveNode(MindMapNode node) {
        MindMapNode previouslyActive = activeNode;
        activeNode = node;
        return previouslyActive;
    }

    @Override
    public Set<MindMapNode> getSelectedNodes() {
        return unmodifiableSet(selectedNodes);
    }

    @Override
    public boolean isSelected(MindMapNode node) {
        requireNonNull(node);
        return selectedNodes.contains(node);
    }

    @Override
    public void selectNodes(Collection<MindMapNode> nodes) {
        requireNonNull(nodes);
        selectedNodes.addAll(nodes);
    }

    @Override
    public void unSelectNodes(Collection<MindMapNode> nodes) {
        requireNonNull(nodes);
        selectedNodes.removeAll(nodes);
    }

    @Override
    public void selectAllNodes() {
        selectedNodes.addAll(getMindMap().getNodes());
    }

    @Override
    public void unSelectAllNodes() {
        selectedNodes.clear();
    }

    @Override
    public MindMapNode insertNewParentNode(MindMapNode node, String data) {
        requireNonNull(node);
        if (node == getRootNode()) {
            //TODO: 1. Consider exception
            //TODO: 2. Consider SHIFT the root node to become a child
            //TODO: 3. Consider do nothing, probably log only
            return null;
        }

        MindMapLink linkToParent = getLinkToParentNode(node);

        MindMapNode oldParentNode = linkToParent.getLeft();
        MindMapNode newParentNode = getMindMap().createNode(data);

        Split<Long, String, LinkData, MindMapNode, MindMapLink> split = linkToParent
                .insertMiddleNode(newParentNode);

        // restore link information
        split.getLeftEdge().setData(split.getEdgePayload());
        split.getRightEdge().setData(split.getEdgePayload());

        __notifyLinksDeleted(singleton(linkToParent));
        __notifyLinksCreated(new HashSet<>(asList(split.getLeftEdge(), split.getRightEdge())));
        __notifyParentChanged(node, newParentNode, oldParentNode);

        return newParentNode;
    }

    protected MindMapLink getLinkToParentNode(MindMapNode node) throws BrokenMindMapException {

        Collection<MindMapLink> incomingEdges = node.getIncomingEdges();
        if (incomingEdges.size() > 1) {
            // Only one directed link from a parent is allowed
            throw new BrokenMindMapException("It seems a node has more than one parent.");
        }

        MindMapLink parentChildLink = incomingEdges.iterator().next();
        return parentChildLink;
    }

    @Override
    public MindMapNode createNewChildNode(MindMapNode node, String label) {
        requireNonNull(node);

        MindMapNode newChild = getMindMap().createNode(label);
        MindMapLink childLink = node.connectNodeFromRight(newChild);
        childLink.setData(new LinkData(LinkData.LinkType.DETAIL));

        __notifyNodesCreated(singleton(newChild));
        __notifyLinksCreated(singleton(childLink));

        return newChild;
    }

    @Override
    public MindMapNode createSiblingTopNode(MindMapNode node, String data) {
        requireNonNull(node);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MindMapNode createSiblingBottomNode(MindMapNode node, String data) {
        requireNonNull(node);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canReAttach(MindMapNode node, MindMapNode targetNode) {
        return (node != null && targetNode != null
                && node != targetNode
                && node == getRootNode() && targetNode == getRootNode());
    }

    @Override
    public void reAttachNode(MindMapNode node, MindMapNode targetNode) {
        if (!canReAttach(node, targetNode)) {
            return;
        }

        MindMapLink linkToOldParent = getLinkToParentNode(node);
        getMindMap().breakEdge(linkToOldParent);

        MindMapLink linkToNewParent = targetNode.connectNodeFromRight(node);
        linkToNewParent.setData(new LinkData(LinkData.LinkType.DETAIL));

        __notifyLinksDeleted(singleton(linkToOldParent));
        __notifyLinksCreated(singleton(linkToNewParent));
        __notifyParentChanged(node, linkToOldParent.getLeft(), targetNode);

    }

    @Override
    public void deleteNode(MindMapNode node) {
        requireNonNull(node);
        deleteNodes(singleton(node));
    }

    @Override
    public void deleteNodes(Set<MindMapNode> nodes) {
        requireNonNull(nodes);
        if (nodes.isEmpty()) {
            return;
        }

        Set<MindMapLink> deletedLinks = new HashSet<>();

        __notifyNodesDeleted(nodes);
        __notifyLinksDeleted(deletedLinks);

    }

    @Override
    public MindMapLink createReference(MindMapNode source, MindMapNode target) {
        requireNonNull(source);
        requireNonNull(target);

        MindMapLink reference = source.connectNodeFromRight(target);
        reference.setData(new LinkData(LinkData.LinkType.REFERENCE));
        __notifyLinksCreated(singleton(reference));
        return reference;
    }

    @Override
    public void removeReferences(Set<MindMapLink> references) {
        requireNonNull(references);

        long parentChildLinkCount = references.stream()
                .filter((MindMapLink link) -> {
                    return link.getData().getType() == LinkData.LinkType.DETAIL;
                })
                .count();

        if (parentChildLinkCount > 0) {
            throw new OperationNotAllowedException("Parent-child relation is the special reference type. It can not be managed in this way.");
        }

        for (MindMapLink link : references) {
            getMindMap().breakEdge(link);
        }

        __notifyLinksDeleted(references);
    }

    @Override
    public Set<MindMapLink> removeAllReferences(MindMapNode node) {
        requireNonNull(node);

        Set<MindMapLink> allReferences = node.getEdges().stream()
                .filter((MindMapLink link) -> {
                    return link.getData().getType() != LinkData.LinkType.DETAIL;
                })
                .collect(Collectors.toSet());

        removeReferences(allReferences);
        return allReferences;
    }

    private void __notifyParentChanged(MindMapNode node, MindMapNode newParentNode, MindMapNode oldParentNode) {
    }

    private void __notifyLinksDeleted(Set<MindMapLink> links) {
        for (MindMapLink link : links) {
            mindMapScene.removeEdge(link);
        }
    }

    private void __notifyLinksCreated(Set<MindMapLink> links) {

        Set<Widget> nodeWidgets = links.stream()
                .map((MindMapLink link) -> {
                    Widget edge = mindMapScene.addEdge(link);
                    mindMapScene.setEdgeSource(link, link.getLeft());
                    mindMapScene.setEdgeTarget(link, link.getRight());
                    return edge;
                })
                .collect(Collectors.toSet());
    }

    private void __notifyNodesCreated(Set<MindMapNode> nodes) {

        Set<Widget> nodeWidgets = nodes.stream()
                .map((MindMapNode node) -> {
                    return mindMapScene.addNode(node);
                })
                .collect(Collectors.toSet());

    }

    private void __notifyNodesDeleted(Set<MindMapNode> nodes) {
        for (MindMapNode node : nodes) {
            mindMapScene.removeNode(node);
        }
    }

}
