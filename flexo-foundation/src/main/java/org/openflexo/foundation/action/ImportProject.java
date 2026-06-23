/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoProjectObject;
import org.openflexo.foundation.project.FlexoProjectReference;

import java.util.Vector;

public class ImportProject extends FlexoAction<ImportProject, FlexoProjectObject<?>, FlexoProjectObject<?>> {

    public static final FlexoActionFactory<ImportProject, FlexoProjectObject<?>, FlexoProjectObject<?>> actionType = new FlexoActionFactory<ImportProject, FlexoProjectObject<?>, FlexoProjectObject<?>>(
            "import_project") {

        @Override
        public ImportProject makeNewAction(FlexoProjectObject<?> focusedObject, Vector<FlexoProjectObject<?>> globalSelection,
                                           FlexoEditor editor) {
            return new ImportProject(focusedObject, globalSelection, editor);
        }

        @Override
        public boolean isVisibleForSelection(FlexoProjectObject<?> object, Vector<FlexoProjectObject<?>> globalSelection) {
            return object != null && object.getProject() != null;
        }

        @Override
        public boolean isEnabledForSelection(FlexoProjectObject<?> object, Vector<FlexoProjectObject<?>> globalSelection) {
            return object != null && object.getProject() != null;
        }
    };

    static {
        FlexoObjectImpl.addActionForClass(actionType, FlexoProject.class);
    }

    private FlexoProject<?> projectToImport;

    private ImportProject(FlexoProjectObject<?> focusedObject, Vector<FlexoProjectObject<?>> globalSelection, FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    protected void doAction(Object context) throws FlexoException {
        FlexoProject<?> project = getImportingProject();

        FlexoProjectReference<?> projectReference = project.getModelFactory().newInstance(FlexoProjectReference.class)
                .init(projectToImport);
        project.addToImportedProjects(projectReference);
    }

    public FlexoProject<?> getImportingProject() {
        return getFocusedObject().getProject();
    }

    public FlexoProject<?> getProjectToImport() {
        return projectToImport;
    }

    public void setProjectToImport(FlexoProject<?> projectToImport) {
        this.projectToImport = projectToImport;
    }

}
