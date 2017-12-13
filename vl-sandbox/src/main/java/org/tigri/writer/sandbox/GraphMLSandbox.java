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

import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.graphdrawing.graphml.Data;
import org.graphdrawing.graphml.Default;
import org.graphdrawing.graphml.Edge;
import org.graphdrawing.graphml.Graph;
import org.graphdrawing.graphml.GraphML;
import org.graphdrawing.graphml.Key;
import org.graphdrawing.graphml.KeyFor;
import org.graphdrawing.graphml.Keys;
import org.graphdrawing.graphml.Node;
import org.graphdrawing.graphml.ObjectFactory;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutSupport;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigri.writer.sandbox.mm.controller.api.MindMapController;
import org.tigri.writer.sandbox.mm.controller.MindMapControllerImpl;
import org.tigri.writer.sandbox.mm.model.MindMap;
import org.tigri.writer.sandbox.mm.model.MindMapLink;
import org.tigri.writer.sandbox.mm.model.MindMapNode;
import org.tigri.writer.sandbox.mm.view.MindMapScene;

/**
 *
 * @author skrymets
 */
public class GraphMLSandbox {

    private static final Logger LOG = LoggerFactory.getLogger(GraphMLSandbox.class);

    private static void buildSampleGraph() {
        /*
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:ns2="http://www.w3.org/1999/xlink">
            <key id="default-color" for="all" attr.name="color" attr.type="string">
                <default>yellow</default>
            </key>
            <graph id="G">
                <node id="N1">
                    <data key="default-color"/>
                </node>
                <node id="N2">
                    <data key="default-color"/>
                </node>
                <edge id="E1" source="N1" target="N2"/>
            </graph>
        </graphml>
         */

        /**
         * See for details:
         * http://graphml.graphdrawing.org/specification/schema_element.xsd.htm
         */
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Default colorDefaults = new Default();
        colorDefaults.setContent("yellow");

        Key key1 = new Key();
        key1.setFor(KeyFor.ALL);
        key1.setId("default-color");
        key1.setDefault(colorDefaults);
        key1.setAttrName("color");
        key1.setAttrType(Keys.STRING);

        final Data commonColorData = new Data();
        commonColorData.setKey(key1.getId());

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Graph graph = new Graph();
        graph.setId("G");

        Node node1 = new Node();
        // node1.setGraph(graph); // throws SAXException2: A cycle is detected in the object graph. This will cause infinitely deep XML
        node1.setId("N1");
        node1.getDataOrPort().add(commonColorData);

        Node node2 = new Node();
        // node2.setGraph(graph); // throws SAXException2: A cycle is detected in the object graph. This will cause infinitely deep XML
        node2.setId("N2");
        node2.getDataOrPort().add(commonColorData);

        Edge edge = new Edge();
        edge.setId("E1");
        edge.setSource(node1.getId());
        edge.setTarget(node2.getId());

        graph.getDataOrNodeOrEdge().add(node1);
        graph.getDataOrNodeOrEdge().add(node2);
        graph.getDataOrNodeOrEdge().add(edge);

        GraphML gml = new GraphML();
        gml.getGraphOrData().add(graph);
        gml.getKey().add(key1);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ObjectFactory of = new ObjectFactory();
        JAXBElement<GraphML> xelm = of.createGraphml(gml);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        try {
            JAXBContext jaxbc = JAXBContext.newInstance(GraphML.class);
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            marshaller.marshal(xelm, System.out);

        } catch (JAXBException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        EditorSupport.go(buildScene());
    }

    protected static Scene buildScene() {

        final int HALF_INT = (Integer.MAX_VALUE / 2);
        
        MindMapScene scene = new MindMapScene();
        scene.setMaximumBounds (new Rectangle (-HALF_INT, -HALF_INT, HALF_INT, HALF_INT));
        //scene.setPreferredBounds(new Rectangle (-1000, -1000, 1000, 1000));
        scene.getActions ().addAction (ActionFactory.createZoomAction());
        scene.getActions ().addAction (ActionFactory.createPanAction ());
        
        
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        MindMap mindMap = new MindMap();
        MindMapController mmc = new MindMapControllerImpl();

        MindMapNode root = mmc.getRootNode();
        MindMapNode node1 = mmc.createNewChildNode(root, "This is just a proof-of-concept");
        MindMapNode node2 = mmc.createNewChildNode(root, "node2w");
        
        scene.revalidate();

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // GraphLayout<MindMapNode, MindMapLink> layout = GraphLayoutFactory.createTreeGraphLayout(0, 0, 10, 30, false);
        GraphLayout<MindMapNode, MindMapLink> layout = new MindMapLayout(scene);
        //TreeGraphLayout(0, 0, 10, 30, false);
        GraphLayoutSupport.setTreeGraphLayoutRootNode(layout, mmc.getRootNode());

        SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene, layout);
        sceneLayout.invokeLayoutImmediately();

        return scene;
    }

}
