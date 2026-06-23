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

import org.openflexo.connie.BindingVariable;
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.NamespaceDeclaration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * A {@link BindingVariable} representing access to a {@link NamespaceDeclaration} in a {@link FMLCompilationUnit} instance context.
 *
 * @author sylvain
 *
 */
public class NamespaceBindingVariable extends BindingVariable implements PropertyChangeListener {
    static final Logger logger = Logger.getLogger(NamespaceBindingVariable.class.getPackage().getName());

    private final NamespaceDeclaration nsDeclaration;

    public NamespaceBindingVariable(NamespaceDeclaration nsDeclaration) {
        super(nsDeclaration.getAbbrev(), String.class, false);
        this.nsDeclaration = nsDeclaration;
        if (nsDeclaration.getPropertyChangeSupport() != null) {
            nsDeclaration.getPropertyChangeSupport().addPropertyChangeListener(this);
        }
    }

    @Override
    public void delete() {
        if (nsDeclaration != null && nsDeclaration.getPropertyChangeSupport() != null) {
            nsDeclaration.getPropertyChangeSupport().removePropertyChangeListener(this);
        }
        super.delete();
    }

    @Override
    public String getVariableName() {
        return getNamespaceDeclaration().getAbbrev();
    }

    @Override
    public Type getType() {
        return String.class;
    }

    public NamespaceDeclaration getNamespaceDeclaration() {
        return nsDeclaration;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getSource() == getNamespaceDeclaration()) {
            if (evt.getPropertyName().equals(NamespaceDeclaration.ABBREV_KEY)) {
                // System.out.println("Notify name changing for " + getFlexoProperty() + " new=" + getVariableName());
                if (getPropertyChangeSupport() != null) {
                    getPropertyChangeSupport().firePropertyChange(VARIABLE_NAME_PROPERTY, evt.getOldValue(), getVariableName());
                }
            }
        }
    }
}
