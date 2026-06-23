/**
 *
 * Copyright (c) 2019, Openflexo
 * <p>
 * This file is part of FML-parser, a component of the software infrastructure
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

package org.openflexo.foundation.fml.parser.fmlnodes.expr;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.binding.FunctionPathElement;
import org.openflexo.foundation.fml.parser.FMLSemanticsAnalyzer;
import org.openflexo.foundation.fml.parser.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author sylvain
 *
 */
public abstract class AbstractCallBindingPathElementNode<N extends Node, BPE extends FunctionPathElement<?>>
        extends AbstractBindingPathElementNode<N, BPE> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AbstractCallBindingPathElementNode.class.getPackage().getName());

    private List<DataBinding<?>> args;

    public AbstractCallBindingPathElementNode(N astNode, FMLSemanticsAnalyzer analyzer, Bindable bindable) {
        super(astNode, analyzer, bindable);
    }

    public AbstractCallBindingPathElementNode(BPE bindingPathElement, FMLSemanticsAnalyzer analyzer, Bindable bindable) {
        super(bindingPathElement, analyzer, bindable);
    }

    public List<DataBinding<?>> getArguments() {
        return args;
    }

    protected void handleArguments(PArgumentList argumentList) {
        if (argumentList instanceof AManyArgumentList) {
            AManyArgumentList l = (AManyArgumentList) argumentList;
            handleArguments(l.getArgumentList());
            handleArgument(l.getExpression());
        } else if (argumentList instanceof AOneArgumentList) {
            handleArgument(((AOneArgumentList) argumentList).getExpression());
        }
    }

    protected void handleArgument(PExpression expression) {

        DataBindingNode dataBindingNode = makeDataBinding(expression, getBindable());
        if (args == null) {
            args = new ArrayList<>();
        }
        args.add(dataBindingNode.getModelObject());
    }

    protected String serializeArguments(List<DataBinding<?>> arguments) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arguments.size(); i++) {
            sb.append((i > 0 ? "," : "") + arguments.get(i).toString());
        }
        return sb.toString();
    }

}
