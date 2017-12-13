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
package org.tigri.writer.sandbox.mm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Objects;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.tigri.writer.sandbox.mm.model.MindMapLink;
import org.tigri.writer.sandbox.mm.model.MindMapNode;

/**
 *
 * @author skrymets
 */
public class MindMapScene extends GraphScene<MindMapNode, MindMapLink> {

    private final LayerWidget mainLayer;
    private final LayerWidget connectionLayer;

    private final WidgetAction rectangularSelectAction;
    
    private final WidgetAction moveAction;
    private final WidgetAction selectAction;
    // = ActionFactory.createSelectAction(new MindMapNodeSelectProvider());

    public MindMapScene() {
        mainLayer = new LayerWidget(this);
        connectionLayer = new LayerWidget(this);

        addChild(mainLayer);
        addChild(connectionLayer);
        
        this.rectangularSelectAction = ActionFactory.createRectangularSelectAction(this, mainLayer);

        this.moveAction = ActionFactory.createMoveAction();
        this.selectAction = createSelectAction();
        
        getActions().addAction(selectAction);
        getActions().addAction(rectangularSelectAction);
        getActions().addAction(moveAction);


    }

    @Override
    protected Widget attachNodeWidget(MindMapNode node) {
        Objects.requireNonNull(node, "It's impossible to add an undefined node to the scene.");

        LabelWidget widget = new LabelWidget(this) {
            @Override
            protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
                setBackground(getScene().getLookFeel().getBackground(state));
                setForeground(getScene().getLookFeel().getForeground(state));
            }
        };

        TemporaryImpl.applyStyles(widget);
        
        widget.setLabel(node.getData());
        
        widget.getActions().addAction(selectAction);
        widget.getActions().addAction(moveAction);

        mainLayer.addChild(widget);

        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(MindMapLink edge) {
        Objects.requireNonNull(edge, "It's impossible to add an undefined edge to the scene.");
        ConnectionWidget widget = new ConnectionWidget(this);
        connectionLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void attachEdgeSourceAnchor(MindMapLink edge, MindMapNode oldSourceNode, MindMapNode sourceNode) {
        final ConnectionWidget cw = (ConnectionWidget) findWidget(edge);
        cw.setSourceAnchor(AnchorFactory.createRectangularAnchor(findWidget(sourceNode)));
    }

    @Override
    protected void attachEdgeTargetAnchor(MindMapLink edge, MindMapNode oldTargetNode, MindMapNode targetNode) {
        final ConnectionWidget cw = (ConnectionWidget) findWidget(edge);
        cw.setTargetAnchor(AnchorFactory.createRectangularAnchor(findWidget(targetNode)));
    }

    private static class MindMapNodeHoverProvider implements TwoStateHoverProvider {

        @Override
        public void unsetHovering(Widget widget) {
            widget.setBackground(Color.WHITE);
            widget.setForeground(Color.BLACK);
        }

        @Override
        public void setHovering(Widget widget) {
            widget.setBackground(Color.CYAN);
            widget.setForeground(Color.YELLOW);
        }

    }

}
