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

package org.openflexo.foundation.action;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProjectObject;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.toolbox.ToolBox;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

public class OpenFileInExplorer extends FlexoAction<OpenFileInExplorer, FlexoProjectObject<?>, FlexoProjectObject<?>> {

    @SuppressWarnings("unused")
    private static final Logger logger = FlexoLogger.getLogger(OpenFileInExplorer.class.getPackage().getName());

    public static FlexoActionFactory<OpenFileInExplorer, FlexoProjectObject<?>, FlexoProjectObject<?>> actionType = new FlexoActionFactory<OpenFileInExplorer, FlexoProjectObject<?>, FlexoProjectObject<?>>(
            ToolBox.isMacOS() ? "open_in_finder" : "open_in_explorer", FlexoActionFactory.defaultGroup,
            FlexoActionFactory.NORMAL_ACTION_TYPE) {

        /**
         * Factory method
         */
        @Override
        public OpenFileInExplorer makeNewAction(FlexoProjectObject<?> focusedObject, Vector<FlexoProjectObject<?>> globalSelection,
                                                FlexoEditor editor) {
            return new OpenFileInExplorer(focusedObject, globalSelection, editor);
        }

        @Override
        public boolean isVisibleForSelection(FlexoProjectObject<?> object, Vector<FlexoProjectObject<?>> globalSelection) {
            return true;
        }

        @Override
        public boolean isEnabledForSelection(FlexoProjectObject<?> object, Vector<FlexoProjectObject<?>> globalSelection) {
            return true;
        }

    };

    private File fileToOpen;

    private OpenFileInExplorer(FlexoProjectObject<?> focusedObject, Vector<FlexoProjectObject<?>> globalSelection, FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    protected void doAction(Object context) throws FlexoException {

        if (fileToOpen != null && fileToOpen.exists()) {
            try {
                ToolBox.showFileInExplorer(fileToOpen);
            } catch (IOException e) {
                throw new IOFlexoException(e);
            }
        }
    }

    public File getFileToOpen() {
        return fileToOpen;
    }

    public void setFileToOpen(File fileToOpen) {
        this.fileToOpen = fileToOpen;
    }

}
