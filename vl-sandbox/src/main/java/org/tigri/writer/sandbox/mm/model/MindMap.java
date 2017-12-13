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
package org.tigri.writer.sandbox.mm.model;

import org.medal.graph.Edge;
import org.medal.graph.EdgeFactory;
import org.medal.graph.IDProvider;
import org.medal.graph.NodeFactory;
import org.medal.graph.id.NumberIDProvider;
import org.medal.graph.impl.AbstractGraph;

public class MindMap extends AbstractGraph<Long, String, LinkData, MindMapNode, MindMapLink> {

    protected final NumberIDProvider nidp = new NumberIDProvider();

    private MindMapNode root;

    public MindMap() {
    }

    public void setRoot(MindMapNode root) {
        this.root = root;
    }
    
    public MindMapNode getRoot() {
        return root;
    }

    @Override
    protected final NodeFactory<Long, String, LinkData, MindMapNode, MindMapLink> getNodeFactory() {
        return () -> new MindMapNode(MindMap.this);
    }

    @Override
    protected final EdgeFactory<Long, String, LinkData, MindMapNode, MindMapLink> getEdgeFactory() {
        return (MindMapNode left, MindMapNode right, Edge.Link direction) -> new MindMapLink(MindMap.this, left, right, direction);
    }

    @Override
    protected final IDProvider<Long> getIdProvider() {
        return nidp;
    }

}
