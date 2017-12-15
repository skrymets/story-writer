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

import java.io.Serializable;

/**
 *
 * @author skrymets
 */
public class LinkData implements Serializable {

    private static final long serialVersionUID = 8414189590107405970L;

    public static enum LinkType {
        /**
         * Means parent-child relation
         */
        DETAIL,
        /**
         * Means arbitrary reference for any other node in the map
         */
        REFERENCE;
    }

    private final LinkType type;

    public LinkData(LinkType type) {
        this.type = type;
    }

    public LinkType getType() {
        return type;
    }

}
