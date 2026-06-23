/**
 *
 * Copyright (c) 2014-2015, Openflexo
 * <p>
 * This file is part of Flexodiagram, a component of the software infrastructure
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

package org.openflexo.foundation.action.transformation;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InvalidNameException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;

import java.util.Vector;
import java.util.logging.Logger;

/**
 * This abstract class is a base action which can be used to perform a transformation<br>
 * One or more strategies should be implemented in one or some {@link TransformationStrategy}
 *
 * @author Sylvain
 *
 * @param <A>
 *            type of action
 * @param <T1>
 *            type of focused object (technology object beeing used in operation)
 */
public abstract class TransformationAction<A extends TransformationAction<A, T1, T2>, T1 extends FlexoObject, T2 extends FlexoObject>
        extends FlexoAction<A, T1, T2> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TransformationAction.class.getPackage().getName());

    /**
     * Constructor for this class
     *
     * @param actionType
     * @param focusedObject
     * @param globalSelection
     * @param editor
     */
    protected TransformationAction(FlexoActionFactory<A, T1, T2> actionType, T1 focusedObject, Vector<T2> globalSelection,
                                   FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    /**
     * Return the strategy beeing choosen and that should apply
     */
    public abstract TransformationStrategy<A> getTransformationStrategy();

    /**
     * Indicates if a transformation strategy has been choosen, is valid and might be applied
     *
     * @see #performStrategy()
     * @return
     */
    @Override
    public boolean isValid() {
        if (getTransformationStrategy() == null) {
            return false;
        }
        return getTransformationStrategy().isValid();
    }

    @Override
    protected final void doAction(Object context) throws InvalidNameException {

        if (isValid()) {
            getTransformationStrategy().performStrategy();
            performPostProcessings();
        }
    }

}
