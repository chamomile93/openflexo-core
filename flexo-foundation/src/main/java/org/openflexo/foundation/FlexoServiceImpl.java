/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

import org.openflexo.localization.LocalizedDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base implementation of a {@link FlexoService}
 *
 * @author sylvain
 *
 */
public abstract class FlexoServiceImpl extends FlexoObservable implements FlexoService {

    protected static final Logger logger = Logger.getLogger(FlexoServiceImpl.class.getPackage().getName());
    protected Status status = Status.Registered;
    private FlexoServiceManager serviceManager;
    private Collection<ServiceOperation<?>> availableServiceOperations = null;

    @Override
    public void receiveNotification(FlexoService caller, ServiceNotification notification) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(getClass().getSimpleName() + " service received notification " + notification + " from " + caller);
        }
    }

    @Override
    public void register(FlexoServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        status = Status.Registered;
    }

    @Override
    public FlexoServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    /**
     * Return indicating general status of this FlexoService<br>
     * This is the display value of 'service <service> status' as given in FML command-line interpreter
     *
     * @return
     */
    @Override
    public String getDisplayableStatus() {
        return getServiceName() + " " + getStatus();
    }

    public LocalizedDelegate getLocales() {
        return getServiceManager().getLocalizationService().getFlexoLocalizer();
    }

    @Override
    public String getDeletedProperty() {
        return null;
    }

    /**
     * Called to stop the service
     */
    @Override
    public void stop() {
        logger.warning("STOP Method for service should be overriden in each service [" + this.getClass().getCanonicalName() + "]");
        status = Status.Stopped;
    }

    /**
     * Return collection of all available {@link ServiceOperation} available for this {@link FlexoService}
     *
     * @return
     */
    @Override
    public final Collection<ServiceOperation<?>> getAvailableServiceOperations() {
        if (availableServiceOperations == null) {
            availableServiceOperations = makeAvailableServiceOperations();
        }
        return availableServiceOperations;
    }

    protected Collection<ServiceOperation<?>> makeAvailableServiceOperations() {
        availableServiceOperations = new ArrayList<>();
        availableServiceOperations.add(HELP_ON_SERVICE);
        availableServiceOperations.add(DISPLAY_SERVICE_STATUS);
        availableServiceOperations.add(START_SERVICE);
        availableServiceOperations.add(STOP_SERVICE);
        return availableServiceOperations;
    }

    @Override
    public void addToAvailableServiceOperations(ServiceOperation<?> serviceOperation) {
        getAvailableServiceOperations().add(serviceOperation);
    }

}
