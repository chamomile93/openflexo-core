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
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.pamela.validation.ValidationReport;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This unit test is intended to test ViewPoint loading
 *
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestTypeVersions extends OpenflexoTestCase {

    private static VirtualModel vm;

    /**
     * Instanciate service manager
     *
     * @throws IOException
     */
    @Test
    @TestOrder(1)
    public void testinstanciateTestServiceManager() throws IOException {
        instanciateTestServiceManager();
        assertNotNull(serviceManager);

    }

    @Test
    @TestOrder(2)
    public void testTypeVersions() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

        VirtualModelLibrary vpLib = serviceManager.getVirtualModelLibrary();
        vm = vpLib.getVirtualModel("http://openflexo.org/test/TestResourceCenter/TestTypeVersions.fml");
        assertNotNull(vm);

        System.out.println("TestTypeVersions : Normalized: " + vm.getNormalizedFML());
        System.out.println("TestTypeVersions : FML: " + vm.getFMLPrettyPrint());

        ValidationReport report = validate(vm.getCompilationUnit());
        assertEquals(0, report.getAllErrors().size());

        assertNotNull(vm.getDeclaredProperty("foo1"));
        FlexoProperty<?> foo1 = vm.getDeclaredProperty("foo1");
        System.out.println("foo1=" + foo1);

    }

}
