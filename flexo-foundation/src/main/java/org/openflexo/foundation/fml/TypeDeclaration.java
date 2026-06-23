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

import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.BindingModel;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;

import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 *
 *
 * @author sylvain
 *
 */
@ModelEntity
@XMLElement
@ImplementationClass(TypeDeclaration.TypeDeclarationImpl.class)
public interface TypeDeclaration extends FMLPrettyPrintable {

    @PropertyIdentifier(type = FMLCompilationUnit.class)
    public static final String COMPILATION_UNIT_KEY = "compilationUnit";
    @PropertyIdentifier(type = Type.class)
    public static final String REFERENCED_TYPE_KEY = "referencedType";
    @PropertyIdentifier(type = String.class)
    public static final String ABBREV_KEY = "abbrev";

    @Getter(value = REFERENCED_TYPE_KEY, ignoreType = true)
    public Type getReferencedType();

    @Setter(REFERENCED_TYPE_KEY)
    public void setReferencedType(Type referencedType);

    @Getter(value = COMPILATION_UNIT_KEY, inverse = FMLCompilationUnit.TYPE_DECLARATIONS_KEY)
    @CloningStrategy(StrategyType.IGNORE)
    public FMLCompilationUnit getCompilationUnit();

    @Setter(COMPILATION_UNIT_KEY)
    public void setCompilationUnit(FMLCompilationUnit compilationUnit);

    @Getter(value = ABBREV_KEY)
    @XMLAttribute
    public String getAbbrev();

    @Setter(ABBREV_KEY)
    public void setAbbrev(String abbrev);

    public static abstract class TypeDeclarationImpl extends FMLObjectImpl implements TypeDeclaration {

        private static final Logger logger = Logger.getLogger(TypeDeclarationImpl.class.getPackage().getName());

        @Override
        public FMLCompilationUnit getResourceData() {
            return getCompilationUnit();
        }

        @Override
        public BindingModel getBindingModel() {
            if (getCompilationUnit() != null) {
                return getCompilationUnit().getBindingModel();
            }
            return null;
        }

        @Override
        public BindingFactory getBindingFactory() {
            if (getCompilationUnit() != null) {
                return getCompilationUnit().getBindingFactory();
            }
            return null;
        }

    }

}
