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

package org.openflexo.foundation.fml;

import org.openflexo.connie.expr.*;
import org.openflexo.foundation.fml.expr.*;

/**
 * Represents FML expression language grammar
 *
 * @author sylvain
 *
 */
public class FMLGrammar implements ExpressionGrammar {

    private static final BinaryOperator[] ALL_SUPPORTED_BINARY_OPERATORS = {FMLBooleanBinaryOperator.AND, FMLBooleanBinaryOperator.OR,
            FMLBooleanBinaryOperator.EQUALS, FMLBooleanBinaryOperator.NOT_EQUALS, FMLBooleanBinaryOperator.LESS_THAN,
            FMLBooleanBinaryOperator.LESS_THAN_OR_EQUALS, FMLBooleanBinaryOperator.GREATER_THAN,
            FMLBooleanBinaryOperator.GREATER_THAN_OR_EQUALS, FMLArithmeticBinaryOperator.ADDITION, FMLArithmeticBinaryOperator.SUBSTRACTION,
            FMLArithmeticBinaryOperator.MULTIPLICATION, FMLArithmeticBinaryOperator.DIVISION, FMLArithmeticBinaryOperator.MOD,
            FMLArithmeticBinaryOperator.SHIFT_LEFT, FMLArithmeticBinaryOperator.SHIFT_RIGHT, FMLArithmeticBinaryOperator.SHIFT_RIGHT_2,
            FMLArithmeticBinaryOperator.BITWISE_AND, FMLArithmeticBinaryOperator.BITWISE_OR, FMLArithmeticBinaryOperator.BITWISE_XOR,
            FMLAssignOperator.ASSIGN, FMLAssignOperator.PLUS_ASSIGN};

    private static final UnaryOperator[] ALL_SUPPORTED_UNARY_OPERATORS = {FMLBooleanUnaryOperator.NOT,
            FMLArithmeticUnaryOperator.UNARY_PLUS, FMLArithmeticUnaryOperator.UNARY_MINUS, FMLArithmeticUnaryOperator.PRE_INCREMENT,
            FMLArithmeticUnaryOperator.PRE_DECREMENT, FMLArithmeticUnaryOperator.POST_INCREMENT, FMLArithmeticUnaryOperator.POST_DECREMENT,
            FMLArithmeticUnaryOperator.BITWISE_COMPLEMENT};

    private static final Operator[] logicalOperators = {FMLBooleanBinaryOperator.AND, FMLBooleanBinaryOperator.OR,
            FMLBooleanUnaryOperator.NOT};
    private static final Operator[] comparisonOperators = {FMLBooleanBinaryOperator.EQUALS, FMLBooleanBinaryOperator.NOT_EQUALS,
            FMLBooleanBinaryOperator.LESS_THAN, FMLBooleanBinaryOperator.LESS_THAN_OR_EQUALS, FMLBooleanBinaryOperator.GREATER_THAN,
            FMLBooleanBinaryOperator.GREATER_THAN_OR_EQUALS};
    private static final Operator[] arithmeticOperators = {FMLArithmeticBinaryOperator.ADDITION, FMLArithmeticBinaryOperator.SUBSTRACTION,
            FMLArithmeticBinaryOperator.MULTIPLICATION, FMLArithmeticBinaryOperator.DIVISION, FMLArithmeticUnaryOperator.UNARY_MINUS};

    private static String getSymbol(UnaryOperator operator) throws OperatorNotSupportedException {
        if (operator == FMLBooleanUnaryOperator.NOT) {
            return "!";
        }
        if (operator == FMLArithmeticUnaryOperator.UNARY_MINUS) {
            return "-";
        }
        if (operator == FMLArithmeticUnaryOperator.PRE_INCREMENT) {
            return "++";
        }
        if (operator == FMLArithmeticUnaryOperator.PRE_DECREMENT) {
            return "--";
        }
        if (operator == FMLArithmeticUnaryOperator.POST_INCREMENT) {
            return "++";
        }
        if (operator == FMLArithmeticUnaryOperator.POST_DECREMENT) {
            return "--";
        }
        if (operator == FMLArithmeticUnaryOperator.BITWISE_COMPLEMENT) {
            return "~";
        }
        throw new OperatorNotSupportedException();
    }

    private static String getSymbol(BinaryOperator operator) throws OperatorNotSupportedException {
        if (operator == FMLBooleanBinaryOperator.AND) {
            return "&&";
        }
        if (operator == FMLBooleanBinaryOperator.OR) {
            return "||";
        }
        if (operator == FMLBooleanBinaryOperator.EQUALS) {
            return "==";
        }
        if (operator == FMLBooleanBinaryOperator.NOT_EQUALS) {
            return "!=";
        }
        if (operator == FMLBooleanBinaryOperator.LESS_THAN) {
            return "<";
        }
        if (operator == FMLBooleanBinaryOperator.LESS_THAN_OR_EQUALS) {
            return "<=";
        }
        if (operator == FMLBooleanBinaryOperator.GREATER_THAN) {
            return ">";
        }
        if (operator == FMLBooleanBinaryOperator.GREATER_THAN_OR_EQUALS) {
            return ">=";
        }
        if (operator == FMLArithmeticBinaryOperator.ADDITION) {
            return "+";
        }
        if (operator == FMLArithmeticBinaryOperator.SUBSTRACTION) {
            return "-";
        }
        if (operator == FMLArithmeticBinaryOperator.MULTIPLICATION) {
            return "*";
        }
        if (operator == FMLArithmeticBinaryOperator.DIVISION) {
            return "/";
        }
        if (operator == FMLArithmeticBinaryOperator.MOD) {
            return "%";
        }
        if (operator == FMLArithmeticBinaryOperator.SHIFT_LEFT) {
            return "<<";
        }
        if (operator == FMLArithmeticBinaryOperator.SHIFT_RIGHT) {
            return ">>";
        }
        if (operator == FMLArithmeticBinaryOperator.SHIFT_RIGHT_2) {
            return ">>>";
        }
        if (operator == FMLArithmeticBinaryOperator.BITWISE_AND) {
            return "&";
        }
        if (operator == FMLArithmeticBinaryOperator.BITWISE_OR) {
            return "|";
        }
        if (operator == FMLArithmeticBinaryOperator.BITWISE_XOR) {
            return "^";
        }
        if (operator == FMLAssignOperator.ASSIGN) {
            return "=";
        }
        throw new OperatorNotSupportedException();
    }

    @Override
    public BinaryOperator[] getAllSupportedBinaryOperators() {
        return ALL_SUPPORTED_BINARY_OPERATORS;
    }

    @Override
    public UnaryOperator[] getAllSupportedUnaryOperators() {
        return ALL_SUPPORTED_UNARY_OPERATORS;
    }

    @Override
    public String getAlternativeSymbol(Operator operator) throws OperatorNotSupportedException {
        return null;
    }

    @Override
    public String getSymbol(Operator operator) throws OperatorNotSupportedException {
        if (operator instanceof UnaryOperator) {
            return getSymbol((UnaryOperator) operator);
        }
        if (operator instanceof BinaryOperator) {
            return getSymbol((BinaryOperator) operator);
        }
        throw new OperatorNotSupportedException();
    }

    @Override
    public Operator[] getLogicalOperators() {
        return logicalOperators;
    }

    @Override
    public Operator[] getComparisonOperators() {
        return comparisonOperators;
    }

    @Override
    public Operator[] getArithmeticOperators() {
        return arithmeticOperators;
    }

    @Override
    public Operator[] getScientificOperators() {
        return null;
    }

    @Override
    public Operator[] getTrigonometricOperators() {
        return null;
    }

    @Override
    public FMLBinaryOperatorExpression makeBinaryOperatorExpression(BinaryOperator operator, Expression leftArgument,
                                                                    Expression rightArgument) {
        return new FMLBinaryOperatorExpression(operator, leftArgument, rightArgument);
    }

    @Override
    public FMLUnaryOperatorExpression makeUnaryOperatorExpression(UnaryOperator operator, Expression argument) {
        return new FMLUnaryOperatorExpression(operator, argument);
    }

    @Override
    public FMLConditionalExpression makeConditionalExpression(Expression condition, Expression thenExpression, Expression elseExpression) {
        return new FMLConditionalExpression(condition, thenExpression, elseExpression);
    }

    @Override
    public <O> Constant<O> getConstant(O value) {
        return FMLConstant.makeConstant(value);
    }

}
