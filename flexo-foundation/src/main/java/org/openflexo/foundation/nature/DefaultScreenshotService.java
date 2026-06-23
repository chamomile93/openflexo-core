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

package org.openflexo.foundation.nature;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoService;
import org.openflexo.foundation.FlexoServiceImpl;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.PamelaModelFactory;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Default implementation for {@link ScreenshotService}
 *
 * @author sylvain
 *
 */
public abstract class DefaultScreenshotService extends FlexoServiceImpl implements ScreenshotService {

    private static final Logger logger = Logger.getLogger(DefaultScreenshotService.class.getPackage().getName());
    private Map<Class<? extends ScreenshotableNature<?>>, ScreenshotServiceDelegate<?, ?>> registeredDelegates;

    public static ScreenshotService createInstance() {
        try {
            PamelaModelFactory factory = new PamelaModelFactory(ScreenshotService.class);
            factory.setImplementingClassForInterface(DefaultScreenshotService.class, ScreenshotService.class);
            ScreenshotService returned = factory.newInstance(ScreenshotService.class);
            return returned;
        } catch (ModelDefinitionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void receiveNotification(FlexoService caller, ServiceNotification notification) {
        super.receiveNotification(caller, notification);
    }

    @Override
    public String getServiceName() {
        return "ScreenshotService";
    }

    @Override
    public void initialize() {
        registeredDelegates = new HashMap<>();
        status = Status.Started;
    }

    @Override
    public void registerDelegate(ScreenshotServiceDelegate<?, ?> delegate) {
        registeredDelegates.put(delegate.getNatureClass(), delegate);
    }

    @Override
    public void unregisterDelegate(ScreenshotServiceDelegate<?, ?> delegate) {
        registeredDelegates.remove(delegate.getNatureClass());
    }

    @Override
    public <T extends FlexoObject, N extends ScreenshotableNature<T>> ScreenshotServiceDelegate<T, N> getDelegate(Class<N> natureClass) {
        return (ScreenshotServiceDelegate<T, N>) registeredDelegates.get(natureClass);
    }

    @Override
    public <T extends FlexoObject> BufferedImage generateScreenshot(T object, Class<? extends ScreenshotableNature<T>> natureClass)
            throws CouldNotGenerateScreenshotException {

        if (object == null) {
            throw new CouldNotGenerateScreenshotException();
        }

        ScreenshotServiceDelegate<T, ? extends ScreenshotableNature<T>> delegate = getDelegate(natureClass);

        if (delegate != null) {
            return delegate.generateScreenshot(object);
        }

        throw new CouldNotGenerateScreenshotException();
    }

}
