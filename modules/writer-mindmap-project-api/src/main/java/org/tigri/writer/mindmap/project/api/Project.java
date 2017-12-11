/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigri.writer.mindmap.project.api;

import org.openide.util.Lookup;

/**
 *
 * @author skrymets
 */
public interface Project extends Lookup.Provider {

    @Override
    public Lookup getLookup();

}
