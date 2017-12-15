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
package org.tigri.writer.mindmap.project;

import java.io.File;
import org.tigri.writer.mindmap.project.api.ProjectInformation;

/**
 *
 * @author skrymets
 */
public class ProjectInformationImpl implements ProjectInformation {

    private String name;

    private File file;

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

}
