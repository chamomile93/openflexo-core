/**
 *
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.foundation.fml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.DefaultFlexoEditor;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.md.ListMetaData;
import org.openflexo.foundation.fml.md.MultiValuedMetaData;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceImpl;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceImpl.PersistencyStrategy;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * This unit test is intended to test ViewPoint loading
 *
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestFMLAnnotations extends OpenflexoTestCase {

    static FlexoEditor editor;

    /**
     * Instanciate compound test resource center
     */
    @Test
    @TestOrder(1)
    public void testinstanciateTestServiceManager() {
        instanciateTestServiceManager();

        System.out.println("ServiceManager= " + serviceManager);
        assertNotNull(serviceManager);

        editor = new DefaultFlexoEditor(null, serviceManager);
        assertNotNull(editor);

    }

    /**
     * Test the loading
     *
     * @throws FlexoException
     * @throws ResourceLoadingCancelledException
     * @throws FileNotFoundException
     */
    @Test
    @TestOrder(2)
    public void testLoadViewPoint() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

        VirtualModelLibrary vpLib = serviceManager.getVirtualModelLibrary();
        assertNotNull(vpLib);
		/*for (CompilationUnitResource compilationUnitResource : vpLib.getCompilationUnitResources()) {
			System.out.println("> " + compilationUnitResource.getURI());
		}*/

        CompilationUnitResource cuResource = vpLib
                .getCompilationUnitResource("http://openflexo.org/test/flexo-test-resources/TestResourceCenter/TestAnnotations.fml");
        ((CompilationUnitResourceImpl) cuResource).setPersistencyStrategy(PersistencyStrategy.FML);

        assertNotNull(cuResource);

        VirtualModel virtualModel = cuResource.getCompilationUnit().getVirtualModel();
        assertNotNull(virtualModel);

        System.out.println("FML=\n" + virtualModel.getFMLPrettyPrint());

        assertTrue(virtualModel.hasMetaData("BasicAnnotation"));
        assertTrue(virtualModel.getBasicMetaData("BasicAnnotation") != null);

        assertTrue(virtualModel.hasMetaData("SingleAnnotation"));
        assertEquals("value", virtualModel.getSingleMetaData("SingleAnnotation", String.class));

        assertTrue(virtualModel.hasMetaData("ComplexAnnotation"));
        MultiValuedMetaData md = virtualModel.getMultiValuedMetaData("ComplexAnnotation");
        assertEquals("aValue", md.getValue("key1", String.class));
        assertEquals("anOtherValue", md.getValue("key2", String.class));

        assertTrue(virtualModel.hasMetaData("SomeAnnotation"));
        assertEquals("value2", virtualModel.getSingleMetaData("SomeAnnotation", String.class));

        assertTrue(virtualModel.hasMetaData("ListAnnotation"));
        ListMetaData listMD = virtualModel.getListMetaData("ListAnnotation");
        assertEquals(6, listMD.getMetaDataList().size());

        System.out.println("Normalized FML=\n" + virtualModel.getNormalizedFML());
    }

}
