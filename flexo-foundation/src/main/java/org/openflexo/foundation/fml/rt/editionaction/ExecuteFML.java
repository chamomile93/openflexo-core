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

package org.openflexo.foundation.fml.rt.editionaction;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificActionDefiningReceiver;
import org.openflexo.foundation.fml.rt.AbstractFMLRTModelSlot;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Getter.Cardinality;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

/**
 * This action is used to execute arbitrary FML code on a {@link FlexoConceptInstance}
 *
 * @author sylvain
 */
@ModelEntity
@ImplementationClass(ExecuteFML.ExecuteFMLImpl.class)
@XMLElement
@FML("ExecuteFML")
public interface ExecuteFML<T, VMI extends VirtualModelInstance<VMI, ?>>
        extends TechnologySpecificActionDefiningReceiver<AbstractFMLRTModelSlot<VMI, ?, ?>, VMI, T> {

    @PropertyIdentifier(type = Vector.class)
    public static final String PARAMETERS_KEY = "parameters";

    @Getter(value = PARAMETERS_KEY, cardinality = Cardinality.LIST, inverse = CreateFlexoConceptInstanceParameter.ACTION_KEY)
    @XMLElement
    @Embedded
    @CloningStrategy(StrategyType.CLONE)
    public List<CreateFlexoConceptInstanceParameter> getParameters();

    @Setter(PARAMETERS_KEY)
    public void setParameters(List<CreateFlexoConceptInstanceParameter> parameters);

    @Adder(PARAMETERS_KEY)
    public void addToParameters(CreateFlexoConceptInstanceParameter aParameter);

    @Remover(PARAMETERS_KEY)
    public void removeFromParameters(CreateFlexoConceptInstanceParameter aParameter);

    public static abstract class ExecuteFMLImpl<T, VMI extends VirtualModelInstance<VMI, ?>>
            extends TechnologySpecificActionDefiningReceiverImpl<AbstractFMLRTModelSlot<VMI, ?, ?>, VMI, T> implements ExecuteFML<T, VMI> {

        @Override
        public T execute(RunTimeEvaluationContext evaluationContext) {
            return null;
        }

        @Override
        public Type getAssignableType() {
            return Object.class;
        }

    }
}
