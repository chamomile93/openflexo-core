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

package org.openflexo.foundation.fml.cli.command.directive;

import org.openflexo.foundation.fml.cli.AbstractCommandSemanticsAnalyzer;
import org.openflexo.foundation.fml.cli.command.Directive;
import org.openflexo.foundation.fml.cli.command.DirectiveDeclaration;
import org.openflexo.foundation.fml.cli.command.FMLCommandExecutionException;
import org.openflexo.foundation.fml.parser.node.AActivateTaDirective;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;

import java.util.logging.Logger;

/**
 * Represents activate TA directive in FML command-line interpreter
 *
 * Allows to activate TechnologyAdapter
 *
 * Usage: activate <ta>
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(ActivateTA.ActivateTAImpl.class)
@DirectiveDeclaration(keyword = "activate", usage = "activate <ta>", description = "Activate technology adapter", syntax = "activate <ta>")
public interface ActivateTA extends Directive<AActivateTaDirective> {

    public TechnologyAdapter<?> getTechnologyAdapter();

    public static abstract class ActivateTAImpl extends DirectiveImpl<AActivateTaDirective> implements ActivateTA {

        @SuppressWarnings("unused")
        private static final Logger logger = Logger.getLogger(ActivateTA.class.getPackage().getName());

        private TechnologyAdapter<?> technologyAdapter;

        @Override
        public void create(AActivateTaDirective node, AbstractCommandSemanticsAnalyzer commandSemanticsAnalyzer) {
            performSuperInitializer(node, commandSemanticsAnalyzer);
            technologyAdapter = getTechnologyAdapter(getText(node.getTechnologyAdapter()));
        }

        @Override
        public String toString() {
            return "activate " + technologyAdapter.getIdentifier();
        }

        @Override
        public TechnologyAdapter<?> getTechnologyAdapter() {
            return technologyAdapter;
        }

        @Override
        public boolean isSyntaxicallyValid() {
            return technologyAdapter != null;
        }

        @Override
        public String invalidCommandReason() {
            if (technologyAdapter == null) {
                return "Technology adapter not found";
            }
            return null;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public TechnologyAdapter<?> execute() throws FMLCommandExecutionException {
            super.execute();
            output.clear();
            String cmdOutput;

            if (!getTechnologyAdapter().isActivated()) {
                getCommandInterpreter().getServiceManager().getTechnologyAdapterService()
                        .activateTechnologyAdapter((TechnologyAdapter) getTechnologyAdapter(), true);

                cmdOutput = "Technology adapter " + getTechnologyAdapter().getIdentifier() + " has been activated";
                output.add(cmdOutput);
                getOutStream().println(cmdOutput);
            } else {
                cmdOutput = "Technology adapter " + getTechnologyAdapter().getIdentifier() + " is already activated";
                output.add(cmdOutput);
                getOutStream().println(cmdOutput);
            }
            return getTechnologyAdapter();
        }
    }
}
