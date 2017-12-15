package org.tigri.writer.mindmap.controller;

import java.util.Set;
import org.tigri.writer.mindmap.model.MindMapLink;
import org.tigri.writer.mindmap.model.MindMapNode;

/**
 *
 * @author skrymets
 */
public interface MindMapListener {

    /**
     * Notify that a set of nodes (all of the same parent) have changed their parent node
     * to a new one.
     *
     * @param nodes     a set of nodes which parent has been changed
     * @param newParent new parent node
     * @param oldParent a node which was the parent
     */
    void onParentChanged(Set<MindMapNode> nodes, MindMapNode newParent, MindMapNode oldParent);

    void onLinksDeleted(Set<MindMapLink> links);

    void onLinksCreated(Set<MindMapLink> links);

    /**
     * Notify that a set of nodes was created in a map
     *
     * @param nodes new nodes
     */
    void onNodesCreated(Set<MindMapNode> nodes);

    /**
     * Notify that a set of nodes was deleted from a map
     *
     * @param nodes
     */
    void onNodesDeleted(Set<MindMapNode> nodes);

}
