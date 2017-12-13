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
package org.tigri.writer.sandbox.placeholder;

import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;

/**
 *
 * @author skrymets
 */
public interface SystemAction {

    /**
     * Actually perform the action.
     * Specified in {@link java.awt.event.ActionListener#actionPerformed}.
     *
     *
     * @param ev the event triggering the action
     */
    void actionPerformed(ActionEvent ev);

    /**
     * Get a help context for the action.
     *
     * @return the help context for this action
     */
    HelpCtx getHelpCtx();

    /**
     * Get a human presentable name of the action.
     * This may be presented as an item in a menu.
     *
     * @return the name of the action
     */
    String getName();

    /**
     * Test whether the action is currently enabled.
     *
     * @return <code>true</code> if so
     */
    boolean isEnabled();

    /**
     * Set whether the action should be enabled.
     *
     * @param value <code>true</code> to enable it
     */
    void setEnabled(boolean value);

}
