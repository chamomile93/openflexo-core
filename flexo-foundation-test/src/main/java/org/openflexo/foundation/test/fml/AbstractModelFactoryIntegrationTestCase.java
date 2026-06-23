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

package org.openflexo.foundation.test.fml;

import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.technologyadapter.DefaultTechnologyAdapterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.exceptions.MissingImplementationException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;

import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Abstract test case for FMLModelFactory<br>
 *
 */
public class AbstractModelFactoryIntegrationTestCase extends OpenflexoTestCase {

    private static final Logger logger = FlexoLogger.getLogger(AbstractModelFactoryIntegrationTestCase.class.getPackage().getName());

    protected void testVirtualModelModelFactoryWithTechnologyAdapter(TechnologyAdapter<?> ta) {
        assertNotNull(ta);
        try {
            log("Instanciating FMLModelFactory integrating technology adapter " + ta);
            TechnologyAdapterService taService = DefaultTechnologyAdapterService.getNewInstance(null);
            taService.addToTechnologyAdapters(ta);

            FMLModelFactory factory = new FMLModelFactory(null, serviceManager);
            for (Class<?> modelSlotClass : ta.getAvailableModelSlotTypes()) {
                log("Check: " + modelSlotClass);
                assertNotNull(factory.getModelContext().getModelEntity(modelSlotClass));
				/* stacktrace:
				 * checkAlloyFMLTechnologyAdapter()
******************************************************************************
[org.openflexo.foundation.test.OpenflexoTestCase.log]
Instanciating FMLModelFactory integrating technology adapter org.openflexo.ta.alloy.AlloyTechnologyAdapter@60cf62ad
Check: interface org.openflexo.ta.alloy.AlloyModelSlot
]]></system-out>
  <system-err><![CDATA[Can't load log handler "java.util.logging.FileHandler"
java.nio.file.NoSuchFileException: /home/user/Library/Logs/OpenFlexo/Openflexo0.log.0.lck
java.nio.file.NoSuchFileException: /home/user/Library/Logs/OpenFlexo/Openflexo0.log.0.lck
        at java.base/sun.nio.fs.UnixException.translateToIOException(UnixException.java:92)
        at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:106)
        at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:111)
        at java.base/sun.nio.fs.UnixFileSystemProvider.newFileChannel(UnixFileSystemProvider.java:181)
        at java.base/java.nio.channels.FileChannel.open(FileChannel.java:298)
        at java.base/java.nio.channels.FileChannel.open(FileChannel.java:357)
        at java.logging/java.util.logging.FileHandler.openFiles(FileHandler.java:512)
        at java.logging/java.util.logging.FileHandler.<init>(FileHandler.java:279)
        at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
        at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:500)
        at java.base/java.lang.reflect.ReflectAccess.newInstance(ReflectAccess.java:128)
        at java.base/jdk.internal.reflect.ReflectionFactory.newInstance(ReflectionFactory.java:347)
        at java.base/java.lang.Class.newInstance(Class.java:647)
        at java.logging/java.util.logging.LogManager.createLoggerHandlers(LogManager.java:1005)
        at java.logging/java.util.logging.LogManager$4.run(LogManager.java:975)
        at java.logging/java.util.logging.LogManager$4.run(LogManager.java:971)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:318)
        at java.logging/java.util.logging.LogManager.loadLoggerHandlers(LogManager.java:971)
        at java.logging/java.util.logging.LogManager.initializeGlobalHandlers(LogManager.java:2424)
        at java.logging/java.util.logging.LogManager$RootLogger.accessCheckedHandlers(LogManager.java:2526)
        at java.logging/java.util.logging.Logger.getHandlers(Logger.java:2090)
        at java.logging/java.util.logging.Logger.log(Logger.java:977)
        at java.logging/java.util.logging.Logger.doLog(Logger.java:1007)
        at java.logging/java.util.logging.Logger.log(Logger.java:1030)
        at java.logging/java.util.logging.Logger.info(Logger.java:1803)
        at org.openflexo.foundation.test.OpenflexoTestCase.log(OpenflexoTestCase.java:440)
				 */
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
