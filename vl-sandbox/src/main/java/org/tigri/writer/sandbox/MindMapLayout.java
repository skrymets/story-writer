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
package org.tigri.writer.sandbox;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.UniversalGraph;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Widget;
import org.tigri.writer.sandbox.mm.model.MindMapLink;
import org.tigri.writer.sandbox.mm.model.MindMapNode;

/**
 *
 * @author skrymets
 */
public class MindMapLayout extends GraphLayout<MindMapNode, MindMapLink> {

    public static final int HORISONTAL_GAP = 20;
    public static final int VERTICAL_GAP = 5;

    private final ObjectScene scene;

    public MindMapLayout(ObjectScene scene) {
        this.scene = scene;
    }

    @Override
    protected void performGraphLayout(UniversalGraph<MindMapNode, MindMapLink> graph) {

        Map<MindMapNode, Rectangle> calculatedBoundaries = new HashMap<MindMapNode, Rectangle>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Rectangle get(Object node) {
                if (node == null) {
                    throw new NullPointerException();
                }
                if (!(node instanceof MindMapNode)) {
                    throw new IllegalArgumentException();
                }

                synchronized (node) {
                    Rectangle rectangle = super.get(node);
                    if (rectangle == null) {
                        rectangle = new Rectangle();
                        put((MindMapNode) node, rectangle);
                    }
                    return rectangle;
                }
            }
        };

        MindMapNode rootNode = graph.getNodes()
                .stream()
                .filter((node) -> {
                    return node.getParentEdge() == null;
                }).findFirst().get();

        calculateSpace(rootNode, 0, calculatedBoundaries);
        
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        int originY = -(calculatedBoundaries.get(rootNode).height / 2);
        
        for (MindMapNode node : rootNode.getChildren()) {
            Rectangle rectangle = calculatedBoundaries.get(node);
            Widget nodeWidget = scene.findWidget(node);
            nodeWidget.setPreferredLocation(new Point(rectangle.x, rectangle.y));
        }

    }

    protected int calculateSpace(MindMapNode node, int totalHorisontalOffset, Map<MindMapNode, Rectangle> calculatedBoundaries) {

        Widget nodeWidget = scene.findWidget(node);
        nodeWidget.getLayout().layout(nodeWidget);


        Rectangle selfPreferredBounds = nodeWidget.getPreferredBounds();

        int currentHorisontalOffset
                = totalHorisontalOffset
                + selfPreferredBounds.width
                + HORISONTAL_GAP;

        int calculatedChildrenHeight = 0;
        Set<MindMapNode> children = node.getChildren();
        for (MindMapNode childNode : children) {
            calculatedChildrenHeight += VERTICAL_GAP;
            calculatedChildrenHeight += calculateSpace(childNode, currentHorisontalOffset, calculatedBoundaries);
        }

        int selfHeight = Math.max(
                selfPreferredBounds.height,
                calculatedChildrenHeight
        );

        // Save the calculated width and height for the current node
        Rectangle rectangle = calculatedBoundaries.get(node);
        rectangle.x = totalHorisontalOffset;
        rectangle.setSize(
                selfPreferredBounds.width + HORISONTAL_GAP,
                selfHeight
        );

        // Return the calculated height of all children (if any)
        return selfHeight;
    }

    @Override
    protected void performNodesLayout(UniversalGraph<MindMapNode, MindMapLink> graph, Collection<MindMapNode> nodes) {
        throw new UnsupportedOperationException("There is no ability to perform a partial layout yet.");
    }

}
