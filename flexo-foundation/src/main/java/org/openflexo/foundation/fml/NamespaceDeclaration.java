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

import org.openflexo.connie.BindingModel;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;

/**
 *
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(NamespaceDeclaration.NamespaceDeclarationImpl.class)
@XMLElement
public interface NamespaceDeclaration extends FlexoObject, FMLPrettyPrintable {

    @PropertyIdentifier(type = FMLCompilationUnit.class)
    public static final String COMPILATION_UNIT_KEY = "compilationUnit";
    @PropertyIdentifier(type = String.class)
    public static final String VALUE_KEY = "value";
    @PropertyIdentifier(type = String.class)
    public static final String ABBREV_KEY = "abbrev";

    @Getter(value = VALUE_KEY)
    @XMLAttribute
    public String getValue();

    @Setter(VALUE_KEY)
    public void setValue(String nsValue);

    @Getter(value = COMPILATION_UNIT_KEY, inverse = FMLCompilationUnit.NAMESPACES_KEY)
    @CloningStrategy(StrategyType.IGNORE)
    public FMLCompilationUnit getCompilationUnit();

    @Setter(COMPILATION_UNIT_KEY)
    public void setCompilationUnit(FMLCompilationUnit compilationUnit);

    @Getter(value = ABBREV_KEY)
    @XMLAttribute
    public String getAbbrev();

    @Setter(ABBREV_KEY)
    public void setAbbrev(String abbrev);

    public abstract class NamespaceDeclarationImpl extends FMLObjectImpl implements NamespaceDeclaration {

        @Override
        public BindingModel getBindingModel() {
            if (getCompilationUnit() != null) {
                return getCompilationUnit().getBindingModel();
            }
            return null;
        }

        @Override
        public FMLCompilationUnit getResourceData() {
            return getCompilationUnit();
        }

    }

}
