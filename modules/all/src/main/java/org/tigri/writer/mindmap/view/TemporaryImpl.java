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
package org.tigri.writer.mindmap.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author skrymets
 */
@Deprecated
public class TemporaryImpl {

    @Deprecated
    public static Font getDefaultWidgetFont() {
        Font defaultFont = new Font("Verdana", Font.BOLD, 16);
        return defaultFont;
    }

    @Deprecated
    public static void applyStyles(LabelWidget widget) {
        widget.setOpaque(true);
        widget.setFont(TemporaryImpl.getDefaultWidgetFont());
        widget.setBorder(BorderFactory.createLineBorder(1));
        widget.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
        widget.setMinimumSize(new Dimension(0, 100));

    }

}
