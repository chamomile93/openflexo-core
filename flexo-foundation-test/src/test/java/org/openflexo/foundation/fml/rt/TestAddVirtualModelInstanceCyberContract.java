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

package org.openflexo.foundation.fml.rt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelLibrary;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This unit test is intented to {@link AddVirtualModelInstanceCyberContract}
 *
 *
 */
@RunWith(OrderedRunner.class)
public class TestAddVirtualModelInstanceCyberContract extends OpenflexoProjectAtRunTimeTestCase {

    private static VirtualModel virtualModelCyberContract;
    private static VirtualModel containedVM;
    private static CreationScheme creationScheme;

    @Test
    @TestOrder(1)
    public void testLoadVirtualModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

        //TODO is this here that the test-resources are added ?
        instanciateTestServiceManager();
        VirtualModelLibrary vpLib = serviceManager.getVirtualModelLibrary();
        assertNotNull(vpLib);

        virtualModelCyberContract = vpLib.getVirtualModel("http://openflexo.org/test/TestResourceCenter/TestAddVirtualModelInstanceCyberContract.fml");

        assertNotNull(virtualModelCyberContract); // TODO idf why this fails, probably because the logic does not exist
        // assertNotNull(containedVM = virtualModelCyberContract.getVirtualModelNamed("MyVM"));

        CompilationUnitResource virtualModelResource = virtualModelCyberContract.getResource();
        // CompilationUnitResource containedVMResource = containedVM.getResource();

        // assertTrue(virtualModelResource.getDependencies().contains(containedVMResource));
        assertVirtualModelIsValid(virtualModelCyberContract);
        // assertVirtualModelIsValid(containedVM);

        System.out.println("virtualModel: " + virtualModelCyberContract.getCompilationUnit().getFMLPrettyPrint());
        assertEquals(1, virtualModelCyberContract.getCompilationUnit().getElementImports().size());
    }

    @Test
    @TestOrder(2)
    public void testPrettyPrint() {
        // System.out.println("containedVM: " + containedVM.getCompilationUnit().getFMLPrettyPrint());

        assertNotNull(creationScheme = virtualModelCyberContract.getCreationSchemes().get(0));//TODO idf why this fails
        // assertEquals("vm = new MyVM::createInstance(\"foo\") with (name=\"myVMInstance\");",
        // 		creationScheme.getControlGraph().getFMLPrettyPrint());

    }

}
