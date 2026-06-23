/**
 *
 * Copyright (c) 2014, Openflexo
 * <p>
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
 * <p>
 * <p>
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either
 * version 1.1 of the License, or any later version ), which is available at
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * <p>
 * You can redistribute it and/or modify under the terms of either of these licenses
 * <p>
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 * <p>
 * Additional permission under GNU GPL version 3 section 7
 * <p>
 * If you modify this Program, or any covered work, by linking or
 * combining it with software containing parts covered by the terms
 * of EPL 1.0, the licensors of this Program grant you additional permission
 * to convey the resulting work. *
 * <p>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * <p>
 * See http://www.openflexo.org/license.html for details.
 * <p>
 * <p>
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 *
 */

package org.openflexo.foundation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import java.io.File;

import static org.junit.Assert.*;

/**
 * This unit test is intended to test project creation and reloading facilities<br>
 * We should retrieve project metadata after project creation and saving
 *
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestReloadProject extends OpenflexoProjectAtRunTimeTestCase {

    private static FlexoEditor editor;
    private static FlexoProject<File> project;

    /**
     * Create an empty project
     *
     * @throws SaveResourceException
     */
    @Test
    @TestOrder(1)
    public void testCreateProject() throws SaveResourceException {
        editor = createStandaloneProject("TestProject");
        project = (FlexoProject<File>) editor.getProject();
        System.out.println("Created project " + project.getProjectDirectory());
        assertTrue(project.getProjectDirectory().exists());
        assertTrue(project.getResource().getIODelegate().exists());
        project.setProjectDescription("This is a test project");
        project.save();

        assertTrue(project.isStandAlone());
        assertEquals(project.getRootFolder().getSerializationArtefact(), project.getProjectDirectory());
        assertEquals(project.getBaseArtefact(), project.getProjectDirectory());
        assertSame(project.getProjectResource().getDelegateResourceCenter(), project.getResourceCenter());
        assertTrue(project.getDelegateResourceCenter() instanceof DirectoryResourceCenter);
        assertEquals(project.getProjectDirectory(), ((DirectoryResourceCenter) project.getDelegateResourceCenter()).getRootDirectory());

    }

    /**
     * Reload the project, tests that uri, name and description are persistent
     */
    @Test
    @TestOrder(2)
    public void testReloadProject() {

        FlexoProject<File> oldProject = project;
        String oldURI = oldProject.getProjectURI();
        System.out.println("Old URI: " + oldURI);
        System.out.println("Old project dir: " + project.getProjectDirectory());
        // instanciateTestServiceManager();
        editor = reloadProject(project);
        project = (FlexoProject<File>) editor.getProject();
        String newURI = project.getProjectURI();
        System.out.println("New URI: " + newURI);
        assertNotNull(editor);
        assertNotNull(project);
        assertNotSame(oldProject, project);
        assertEquals(newURI, oldURI);
        assertEquals("This is a test project", project.getProjectDescription());

        assertTrue(project.isStandAlone());
        assertEquals(project.getRootFolder().getSerializationArtefact(), project.getProjectDirectory());
        assertEquals(project.getBaseArtefact(), project.getProjectDirectory());
        assertSame(project.getProjectResource().getDelegateResourceCenter(), project.getResourceCenter());
        assertTrue(project.getDelegateResourceCenter() instanceof DirectoryResourceCenter);
        assertEquals(project.getProjectDirectory(), ((DirectoryResourceCenter) project.getDelegateResourceCenter()).getRootDirectory());

    }

}
