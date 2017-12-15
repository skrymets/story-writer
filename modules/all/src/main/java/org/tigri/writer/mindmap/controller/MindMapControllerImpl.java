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

import static java.util.Arrays.asList;
import java.util.Collections;
import static java.util.Collections.singleton;
import java.util.HashSet;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.medal.graph.Split;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigri.writer.mindmap.controller.api.MindMapController;
import org.tigri.writer.mindmap.exceptions.BrokenMindMapException;
import org.tigri.writer.mindmap.exceptions.OperationNotAllowedException;
import org.tigri.writer.mindmap.model.LinkData;
import org.tigri.writer.mindmap.model.MindMap;
import org.tigri.writer.mindmap.model.MindMapLink;
import org.tigri.writer.mindmap.model.MindMapNode;

@ServiceProvider(service = MindMapController.class)
public class MindMapControllerImpl implements MindMapController {

    private static final Logger LOG = LoggerFactory.getLogger(MindMapControllerImpl.class);
    private final Set<MindMapListener> mindMapListeners;

    public MindMapControllerImpl() {
        this.mindMapListeners = Collections.synchronizedSet(new HashSet<MindMapListener>());
    }

    @Override
    public MindMap createMindMap(String message) {

        String safeMsg = (message == null || message.isEmpty())
                ? NbBundle.getMessage(MindMap.class, "CTL_RootNodeDefaultTitle")
                : message;

        return MindMap.Factory.newInstance(safeMsg);
    }

//    @Override
//    public synchronized Optional<MindMap> getMindMap() {
//        //TODO: Consider to extlicitly set an active mind map into the controller, while switching editor.
//
//        MindMapProjectController mmpc = Lookup.getDefault().lookup(MindMapProjectController.class);
//        Optional<MindMapProject> currentProject = mmpc.getCurrentProject();
//        if (!currentProject.isPresent()) {
//            return Optional.empty();
//        }
//
//        MindMapProject project = currentProject.get();
//        MindMap mindMap;
//        mindMap = project.getLookup().lookup(MindMap.class);
//        if (mindMap == null) {
//            mindMap = MindMap.Factory.newInstance(); // Automatically creates the root node
//            project.add(mindMap);
//        }
//
//        return Optional.of(mindMap);
//    }
    @Override
    public boolean isRootNode(MindMapNode node) {
        requireNonNull(node);
        return ((MindMap) node.getGraph()).getRoot() == node;
    }

    @Override
    public Optional<MindMapNode> insertNewParentNode(MindMapNode node, String data) {
        requireNonNull(node);

        Optional<MindMapLink> optionalLinkToParent = getLinkToParentNode(node);
        if (!optionalLinkToParent.isPresent()) {
            return Optional.empty();
        }

        MindMapLink linkToParent = optionalLinkToParent.get();

        MindMapNode oldParentNode = linkToParent.getLeft();
        MindMapNode newParentNode = node.getGraph().createNode(data);

        Split<Long, String, LinkData, MindMapNode, MindMapLink> split = linkToParent
                .insertMiddleNode(newParentNode);

        // restore link information
        split.getLeftEdge().setData(split.getEdgePayload());
        split.getRightEdge().setData(split.getEdgePayload());

        mindMapListeners.stream().forEach(l -> {
            l.onLinksDeleted(singleton(linkToParent));
            l.onLinksCreated(new HashSet<>(asList(split.getLeftEdge(), split.getRightEdge())));
            l.onParentChanged(singleton(node), newParentNode, oldParentNode);
        });

        return Optional.of(newParentNode);
    }

    /**
     * Any tree-node, except the root node, <strong>must</strong> have only one
     * directed-detail link to itself from a parent. This method finds that link (or not,
     * if the root node is passed).
     *
     * @param node a node which link to parent should be found
     *
     * @return a link (in an <code>optional</code> wrapper) to a parent node, or empty
     *         <code>Optional</code> if the root node is passed as the parameter.
     *
     * @throws BrokenMindMapException in the case if the rule of "single link from the
     *                                parent" is broken for some reason.
     */
    protected Optional<MindMapLink> getLinkToParentNode(MindMapNode node) throws BrokenMindMapException {
        if (isRootNode(node)) {
            // It seems this is a root node.
            // Root node MUST not have any income detaile links.
            return Optional.empty();
        }

        Set<MindMapLink> incomeDetailLinks = node.getIncomingEdges()
                .stream()
                .filter(e -> e.getData().getType() == LinkData.LinkType.DETAIL)
                .collect(Collectors.toSet());

        if (incomeDetailLinks.isEmpty()) {
            throw new BrokenMindMapException(String.format("A node %s has no parent.", node.getId()));
        }

        if (incomeDetailLinks.size() > 1) {
            // Only one directed link from a parent is allowed
            throw new BrokenMindMapException(String.format("A node %s has more than one parent.", node.getId()));
        }

        MindMapLink parentChildLink = incomeDetailLinks.iterator().next();
        return Optional.of(parentChildLink);
    }

    @Override
    public MindMapNode createNewChildNode(MindMapNode node, String label) {
        requireNonNull(node);

        MindMap graph = (MindMap) node.getGraph();
        MindMapNode newChild = graph.createNode(label);
        MindMapLink childLink = node.connectNodeFromRight(newChild);
        childLink.setData(new LinkData(LinkData.LinkType.DETAIL));

        mindMapListeners.stream().forEach(l -> {
            l.onNodesCreated(singleton(newChild));
            l.onLinksCreated(singleton(childLink));
        });

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
        return (node != null && targetNode != null)
                && (node != targetNode)
                && (node.getGraph() == targetNode.getGraph())
                && (node != ((MindMap) node.getGraph()).getRoot());
    }

    @Override
    public void reAttachNode(MindMapNode node, MindMapNode targetNode) {
        if (!canReAttach(node, targetNode)) {
            return;
        }

        MindMap graph = (MindMap) node.getGraph();
        MindMapLink linkToOldParent = getLinkToParentNode(node).get();
        graph.breakEdge(linkToOldParent);

        MindMapLink linkToNewParent = targetNode.connectNodeFromRight(node);
        linkToNewParent.setData(new LinkData(LinkData.LinkType.DETAIL));

        mindMapListeners.stream().forEach(l -> {
            l.onLinksDeleted(singleton(linkToOldParent));
            l.onLinksCreated(singleton(linkToNewParent));
            l.onParentChanged(singleton(node), linkToOldParent.getLeft(), targetNode);
        });

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

        mindMapListeners.stream().forEach(l -> {
            l.onNodesDeleted(nodes);
            l.onLinksDeleted(deletedLinks);
        });

    }

    @Override
    public MindMapLink createReference(MindMapNode source, MindMapNode target) {
        requireNonNull(source);
        requireNonNull(target);

        MindMapLink reference = source.connectNodeFromRight(target);
        reference.setData(new LinkData(LinkData.LinkType.REFERENCE));

        mindMapListeners.stream().forEach(l -> {
            l.onLinksCreated(singleton(reference));
        });

        return reference;
    }

    @Override
    public void removeReferences(Set<MindMapLink> references) {
        requireNonNull(references);

        long parentChildLinkCount = references.stream()
                .filter(link -> {
                    return link.getData().getType() == LinkData.LinkType.DETAIL;
                })
                .count();

        if (parentChildLinkCount > 0) {
            throw new OperationNotAllowedException("Parent-child relation is the special reference type. It can not be managed in this way.");
        }

        for (MindMapLink link : references) {
            link.getGraph().breakEdge(link);
        }

        mindMapListeners.stream().forEach(l -> {
            l.onLinksDeleted(references);
        });
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

    @Override
    public Optional<MindMapNode> mergeNodes(Set<MindMapNode> nodes) {
        requireNonNull(nodes);

        if (!ofTheSameGraph(nodes) || !haveSameParent(nodes)) {
            // Can not merge nodes from different graphs
            // Must reside on the same level of the same perent node
            return Optional.empty();
        }

        String mergedData = nodes.stream().map(n -> n.getData()).collect(Collectors.joining(" "));
        deleteNodes(nodes);

        // Create a new node
        // Attach it to the known parent
        // Merge payload for all (old)existing detail-links
        // Reassign all in-/out-references
        // Delete old nodes
        // Delete old parent detail-links
        throw new UnsupportedOperationException();
    }

    protected static boolean ofTheSameGraph(Set<MindMapNode> nodes) {
        return nodes.stream().map(n -> n.getGraph()).count() == 1;
    }

    private boolean haveSameParent(Set<MindMapNode> nodes) {

        MindMapNode parent = null;
        for (MindMapNode node : nodes) {
            if (isRootNode(node)) {
                return (nodes.size() == 1);
            }

            // Parent is always at the left
            MindMapNode currentNodeParent = getLinkToParentNode(node).get().getLeft();
            if (parent == null) {
                parent = currentNodeParent;
            } else if (parent != currentNodeParent) {
                // Another one parent node. Stop the check.
                return false;
            }

        }

        return true;
    }

    @Override
    public void addMindMapListener(MindMapListener listener) {
        if (listener == null) {
            return;
        }
        mindMapListeners.add(listener);
    }

    @Override
    public void removeMindMapListener(MindMapListener listener) {
        if (listener == null) {
            return;
        }
        mindMapListeners.remove(listener);
    }

}
