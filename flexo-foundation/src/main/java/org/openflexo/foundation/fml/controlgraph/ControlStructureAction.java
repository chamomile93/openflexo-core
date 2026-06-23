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

package org.openflexo.foundation.fml.controlgraph;

import org.openflexo.foundation.fml.binding.ControlGraphBindingModel;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;

import java.util.logging.Logger;

@ModelEntity(isAbstract = true)
@ImplementationClass(ControlStructureAction.ControlStructureActionImpl.class)
@Imports({@Import(ConditionalAction.class), @Import(IterationAction.class), @Import(ExpressionIterationAction.class),
        @Import(WhileAction.class), @Import(IncrementalIterationAction.class)})
public abstract interface ControlStructureAction extends EditionAction {

    public static abstract class ControlStructureActionImpl extends EditionActionImpl implements ControlStructureAction {

        @SuppressWarnings("unused")
        private static final Logger logger = Logger.getLogger(ControlStructureAction.class.getPackage().getName());

        private ControlGraphBindingModel<?> inferedBindingModel;

        @Override
        public ControlGraphBindingModel<?> getInferedBindingModel() {
            if (inferedBindingModel == null) {
                inferedBindingModel = makeInferedBindingModel();
            }
            return inferedBindingModel;
        }

        protected abstract ControlGraphBindingModel<?> makeInferedBindingModel();

    }
}
