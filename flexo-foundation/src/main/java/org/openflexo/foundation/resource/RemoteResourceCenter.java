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

package org.openflexo.foundation.resource;

import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.PamelaModelFactory;

import java.util.logging.Logger;

/**
 * Implementation for a {@link FlexoResourceCenter} accessed via an URL
 *
 * @author sylvain
 *
 */
// TODO
@ModelEntity
@ImplementationClass(RemoteResourceCenter.RemoteResourceCenterImpl.class)
public interface RemoteResourceCenter extends FlexoResourceCenter<Object> {

    @ModelEntity
    @XMLElement
    public static interface RemoteResourceCenterEntry extends ResourceCenterEntry<RemoteResourceCenter> {
        @PropertyIdentifier(type = String.class)
        public static final String URL_KEY = "url";

        @Getter(URL_KEY)
        @XMLAttribute
        public String getURL();

        @Setter(URL_KEY)
        public void setURL(String aURL);

        @Implementation
        public static abstract class RemoteResourceCenterEntryImpl implements RemoteResourceCenterEntry {

            private boolean isSystem = false;

            @Override
            public RemoteResourceCenter makeResourceCenter(FlexoResourceCenterService rcService) {
                return null;
            }

            @Override
            public boolean isSystemEntry() {
                return false;
            }

            @Override
            public void setIsSystemEntry(boolean isSystemEntry) {
                // Does Nothing
            }
        }

    }

    public static abstract class RemoteResourceCenterImpl extends ResourceRepositoryImpl<FlexoResource<?>, Object>
            implements RemoteResourceCenter {

        protected static final Logger logger = Logger.getLogger(RemoteResourceCenter.class.getPackage().getName());

		/*public RemoteResourceCenterImpl(FlexoResourceCenter<Object> resourceCenter) {
			super(resourceCenter, null);
		}*/
        private RemoteResourceCenterEntry entry;

        public String getURL() {
            return null;
        }

        public void setURL(String aURL) {

        }

        @Override
        public RemoteResourceCenterEntry getResourceCenterEntry() {
            if (entry == null) {
                try {
                    PamelaModelFactory factory = new PamelaModelFactory(RemoteResourceCenterEntry.class);
                    entry = factory.newInstance(RemoteResourceCenterEntry.class);
                    entry.setURL(getURL());
                } catch (ModelDefinitionException e) {
                    e.printStackTrace();
                }
            }
            return entry;
        }

        /**
         * Stops the Resource Center (When needed)
         */
        @Override
        public void stop() {
            logger.warning("STOP method needs to be implemented for RemoteResourceCenters");
        }

        @Override
        public boolean containsArtefact(Object serializationArtefact) {
            // TODO
            return false;
        }

        @Override
        public String relativePath(Object serializationArtefact) {
            // TODO
            return null;
        }

        @Override
        public String getDisplayableStatus() {
            return "[uri=\"" + getDefaultBaseURI() + "\"] with " + getAllResources().size() + " resources";
        }

    }

}
