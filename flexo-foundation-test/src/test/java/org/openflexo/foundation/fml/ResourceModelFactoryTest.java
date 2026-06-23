/**
 *
 * Copyright (c) 2015, Openflexo
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
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceImpl;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.PamelaMetaModel;
import org.openflexo.pamela.exceptions.MissingImplementationException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.PamelaModelFactory;
import org.openflexo.pamela.model.ModelEntity;

import java.util.Iterator;
import java.util.logging.Logger;

import static org.junit.Assert.fail;

/**
 * Test PAMELA model for all resources
 *
 */
public class ResourceModelFactoryTest {

    private static final Logger logger = FlexoLogger.getLogger(ResourceModelFactoryTest.class.getPackage().getName());

    @Test
    public void testInstantiateVirtualModelResourceFactory() {
        try {
            System.out.println("Instanciating VirtualModelResource PamelaModelFactory");

            PamelaModelFactory factory = new PamelaModelFactory(CompilationUnitResource.class);
            factory.setImplementingClassForInterface(CompilationUnitResourceImpl.class, CompilationUnitResource.class);
            PamelaMetaModel pamelaMetaModel = factory.getModelContext();
            for (Iterator<ModelEntity> it = pamelaMetaModel.getEntities(); it.hasNext(); ) {
                // FD unused ModelEntity e =
                it.next();
            }
            factory.checkMethodImplementations();
        } catch (ModelDefinitionException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (MissingImplementationException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testInstantiateVirtualModelInstanceResourceModelFactory() {
        try {
            System.out.println("Instanciating VirtualModelInstanceResource PamelaModelFactory");

            PamelaModelFactory factory = new PamelaModelFactory(FMLRTVirtualModelInstanceResource.class);
            PamelaMetaModel pamelaMetaModel = factory.getModelContext();
            for (Iterator<ModelEntity> it = pamelaMetaModel.getEntities(); it.hasNext(); ) {
                // FD unused ModelEntity e =
                it.next();
            }
            factory.checkMethodImplementations();
        } catch (ModelDefinitionException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (MissingImplementationException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
