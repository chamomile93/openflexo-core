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

package org.openflexo.foundation.fml;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.ParseException;
import org.openflexo.connie.expr.Expression;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;

import java.util.logging.Logger;

/**
 * This is the FML binding factory, which allow to define how to browse inside FML compilation unit<br>
 * A {@link CompilationUnitBindingFactory} should be build using a {@link FMLCompilationUnit} which defines the scope of types beeing
 * managed in this BindingFactory
 *
 * @author sylvain
 *
 */
public class CompilationUnitBindingFactory extends AbstractFMLBindingFactory {
    static final Logger logger = Logger.getLogger(CompilationUnitBindingFactory.class.getPackage().getName());

    private FMLCompilationUnit compilationUnit;

    public CompilationUnitBindingFactory(FMLCompilationUnit compilationUnit) {
        super();
        this.compilationUnit = compilationUnit;
    }

    public FMLCompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    @Override
    public FMLModelFactory getFMLModelFactory() {
        if (compilationUnit != null) {
            return compilationUnit.getFMLModelFactory();
        }
        return super.getFMLModelFactory();
    }

    @Override
    public Expression parseExpression(String expressionAsString, Bindable bindable) throws ParseException {
        if (compilationUnit.getResource() != null) {
            return ((CompilationUnitResource) compilationUnit.getResource()).parseExpression(expressionAsString, bindable);
        }
        return null;
    }

}
