/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * <p>
 * This file is part of Connie-core, a component of the software infrastructure
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

package org.openflexo.foundation.fml.expr;

import org.openflexo.connie.exception.TransformException;
import org.openflexo.connie.expr.ConditionalExpression;
import org.openflexo.connie.expr.Expression;
import org.openflexo.connie.expr.ExpressionPrettyPrinter;
import org.openflexo.connie.expr.ExpressionTransformer;
import org.openflexo.foundation.fml.expr.FMLConstant.BooleanConstant;

public class FMLConditionalExpression extends ConditionalExpression {

    public FMLConditionalExpression(Expression condition, Expression thenExpression, Expression elseExpression) {
        super(condition, thenExpression, elseExpression);
    }

    @Override
    public ExpressionPrettyPrinter getPrettyPrinter() {
        return FMLPrettyPrinter.getInstance();
    }

    @Override
    public int getPriority() {
        return 14;
    }

    @Override
    public Expression transform(ExpressionTransformer transformer) throws TransformException {

        Expression expression = this;
        Expression transformedCondition = getCondition().transform(transformer);

        // Lazy evaluation
        // special case if condition has been evaluated
        if (transformedCondition == BooleanConstant.TRUE) {
            // No need to analyze further
            return getThenExpression().transform(transformer);
        } else if (transformedCondition == BooleanConstant.FALSE) {
            // No need to analyze further
            return getElseExpression().transform(transformer);
        }

        Expression transformedThenExpression = getThenExpression().transform(transformer);
        Expression transformedElseExpression = getElseExpression().transform(transformer);

        if (!transformedCondition.equals(getCondition()) || !transformedThenExpression.equals(getThenExpression())
                || !transformedElseExpression.equals(getElseExpression())) {
            expression = new FMLConditionalExpression(transformedCondition, transformedThenExpression, transformedElseExpression);
        }

        return transformer.performTransformation(expression);
    }

}
