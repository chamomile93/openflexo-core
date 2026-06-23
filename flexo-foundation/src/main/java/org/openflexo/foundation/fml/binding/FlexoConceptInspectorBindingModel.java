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

package org.openflexo.foundation.fml.binding;

import org.openflexo.connie.BindingModel;
import org.openflexo.foundation.fml.inspector.FlexoConceptInspector;

import java.beans.PropertyChangeEvent;

/**
 * This is the {@link BindingModel} exposed by a FlexoConceptInspector<br>
 *
 * @author sylvain
 *
 */
public class FlexoConceptInspectorBindingModel extends BindingModel {

    private final FlexoConceptInspector inspector;

    public FlexoConceptInspectorBindingModel(FlexoConceptInspector inspector) {
        super(inspector.getFlexoConcept() != null ? inspector.getFlexoConcept().getBindingModel() : null);
        this.inspector = inspector;
        if (inspector.getPropertyChangeSupport() != null) {
            inspector.getPropertyChangeSupport().addPropertyChangeListener(this);
        }
    }

    /**
     * Delete this {@link BindingModel}
     */
    @Override
    public void delete() {
        if (inspector != null && inspector.getPropertyChangeSupport() != null) {
            inspector.getPropertyChangeSupport().removePropertyChangeListener(this);
        }
        super.delete();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getSource() == inspector) {
            if (evt.getPropertyName().equals(FlexoConceptInspector.FLEXO_CONCEPT_KEY)) {
                setBaseBindingModel(inspector.getFlexoConcept() != null ? inspector.getFlexoConcept().getBindingModel() : null);
            }
        }
    }

    public FlexoConceptInspector getInspector() {
        return inspector;
    }
}
