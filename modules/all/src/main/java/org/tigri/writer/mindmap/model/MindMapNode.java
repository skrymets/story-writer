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
package org.tigri.writer.mindmap.model;

import java.util.Set;
import java.util.stream.Collectors;
import org.medal.graph.Edge;
import org.medal.graph.Graph;
import org.medal.graph.impl.AbstractNode;

public class MindMapNode extends AbstractNode<Long, String, LinkData, MindMapNode, MindMapLink> {

    MindMapNode(Graph graph) {
        super(graph);
    }

    public Set<MindMapNode> getChildren() {
        return getEdgesToChildren().stream()
                .map((link) -> {
                    return link.getRight();
                })
                .filter((node) -> {
                    return node != MindMapNode.this;
                })
                .collect(Collectors.toSet());
    }

    public Set<MindMapLink> getEdgesToChildren() {
        return getEdges().stream()
                .filter((link) -> {
                    return link.getData().getType() == LinkData.LinkType.DETAIL;
                })
                .collect(Collectors.toSet());
    }

    public Set<MindMapLink> getIncomeReferences() {
        return getReferences(true);
    }

    public Set<MindMapLink> getOutgoingReferences() {
        return getReferences(false);
    }

    public MindMapLink getParentEdge() {
        return getEdges()
                .stream()
                .filter((link) -> {
                    return link.getData().getType() == LinkData.LinkType.DETAIL
                            && link.getRight() == this;
                }).findFirst().orElse(null);

    }

    private Set<MindMapLink> getReferences(boolean income) {
        return getEdges().stream()
                .filter((link) -> {
                    boolean accept
                            = link.getDirected() == Edge.Link.DIRECTED
                            && link.getData().getType() == LinkData.LinkType.REFERENCE
                            && ((income && link.getRight() == this)
                            || (!income && link.getLeft() == this));
                    return accept;
                })
                .collect(Collectors.toSet());
    }

}
