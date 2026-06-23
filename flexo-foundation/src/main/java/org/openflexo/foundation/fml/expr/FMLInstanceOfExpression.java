/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.expr.*;

import java.lang.reflect.Type;
import java.util.Vector;

public class FMLInstanceOfExpression extends Expression {

    private Type type;
    private Expression argument;

    public FMLInstanceOfExpression(Expression argument, Type type) {
        super();
        this.argument = argument;
        this.type = type;
    }

    @Override
    public int getDepth() {
        return argument.getDepth() + 1;
    }

    @Override
    public ExpressionPrettyPrinter getPrettyPrinter() {
        return FMLPrettyPrinter.getInstance();
    }

    @Override
    public int getPriority() {
        return 2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Expression getArgument() {
        return argument;
    }

    public void setArgument(Expression argument) {
        this.argument = argument;
    }

    @Override
    public Expression transform(ExpressionTransformer transformer) throws TransformException {

        Expression expression = this;
        Expression transformedArgument = getArgument().transform(transformer);

        if (!transformedArgument.equals(getArgument())) {
            expression = new FMLInstanceOfExpression(transformedArgument, getType());
        }

        return transformer.performTransformation(expression);
    }

    @Override
    public void visit(ExpressionVisitor visitor) throws VisitorException {
        argument.visit(visitor);
        visitor.visit(this);
    }

    @Override
    public EvaluationType getEvaluationType() throws TypeMismatchException {
        return EvaluationType.BOOLEAN;
    }

    @Override
    protected Vector<Expression> getChilds() {
        Vector<Expression> returned = new Vector<>();
        returned.add(getArgument());
        return returned;
    }

    @Override
    public boolean isSettable() {
        return false;
    }

    @Override
    public Type getAccessedType() {
        return Boolean.class;
    }

}
