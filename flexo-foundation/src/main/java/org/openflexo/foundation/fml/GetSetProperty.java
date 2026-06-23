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

import org.openflexo.foundation.fml.controlgraph.FMLControlGraph;
import org.openflexo.foundation.fml.controlgraph.FMLControlGraphOwner;
import org.openflexo.foundation.fml.controlgraph.FMLControlGraphVisitor;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;

/**
 * A {@link GetSetProperty} is a particular implementation of a {@link FlexoProperty} allowing to access data for reading and writing using
 * a typed control graph<br>
 * Access to data is read-write
 *
 *
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(GetSetProperty.GetSetPropertyImpl.class)
@XMLElement
public abstract interface GetSetProperty<T> extends GetProperty<T> {

    @PropertyIdentifier(type = String.class)
    public static final String VALUE_VARIABLE_NAME_KEY = "valueVariableName";

    @PropertyIdentifier(type = FMLControlGraph.class)
    public static final String SET_CONTROL_GRAPH_KEY = "setControlGraph";

    @Getter(value = VALUE_VARIABLE_NAME_KEY, defaultValue = "value")
    @XMLAttribute
    public String getValueVariableName();

    @Setter(VALUE_VARIABLE_NAME_KEY)
    public void setValueVariableName(String valueVariableName);

    @Getter(value = SET_CONTROL_GRAPH_KEY, inverse = FMLControlGraph.OWNER_KEY)
    @CloningStrategy(StrategyType.CLONE)
    @XMLElement(context = "SetControlGraph_")
    @Embedded
    public FMLControlGraph getSetControlGraph();

    @Setter(SET_CONTROL_GRAPH_KEY)
    public void setSetControlGraph(FMLControlGraph aControlGraph);

    public static abstract class GetSetPropertyImpl<T> extends GetPropertyImpl<T> implements GetSetProperty<T> {

        // private static final Logger logger = Logger.getLogger(FlexoRole.class.getPackage().getName());

        @Override
        public boolean isReadOnly() {
            return getSetControlGraph() == null;
        }

        @Override
        public void setSetControlGraph(FMLControlGraph aControlGraph) {
            if (aControlGraph != null) {
                aControlGraph.setOwnerContext(SET_CONTROL_GRAPH_KEY);
            }
            performSuperSetter(SET_CONTROL_GRAPH_KEY, aControlGraph);
        }

        @Override
        public FMLControlGraph getControlGraph(String ownerContext) {
            if (SET_CONTROL_GRAPH_KEY.equals(ownerContext)) {
                return getSetControlGraph();
            }
            return super.getControlGraph(ownerContext);
        }

        @Override
        public void setControlGraph(FMLControlGraph controlGraph, String ownerContext) {

            if (SET_CONTROL_GRAPH_KEY.equals(ownerContext)) {
                setSetControlGraph(controlGraph);
            } else {
                super.setControlGraph(controlGraph, ownerContext);
            }
        }

        @Override
        public void reduce() {
            super.reduce();
            if (getSetControlGraph() instanceof FMLControlGraphOwner) {
                ((FMLControlGraphOwner) getSetControlGraph()).reduce();
            }
        }

        @Override
        public void finalizeDeserialization() {

            if (getSetControlGraph() != null) {
                getSetControlGraph().accept(new FMLControlGraphVisitor() {
                    @Override
                    public void visit(FMLControlGraph controlGraph) {
                        controlGraph.finalizeDeserialization();
                    }
                });
            }

            super.finalizeDeserialization();

        }

    }

}
