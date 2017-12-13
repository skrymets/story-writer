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

import org.medal.graph.Edge.Link;
import org.medal.graph.Graph;
import org.medal.graph.impl.AbstractEdge;

public class MindMapLink extends AbstractEdge<Long, String, LinkData, MindMapNode, MindMapLink> {

    private final LinkData DEFAULT_INFO = new LinkData(LinkData.LinkType.DETAIL);

    MindMapLink(Graph graph, MindMapNode left, MindMapNode right, Link link) {
        super(graph, left, right, link);
    }

    @Override
    public LinkData getData() {
        final LinkData existingData = super.getData();
        return (existingData == null)
                ? DEFAULT_INFO
                : existingData;
    }

}
