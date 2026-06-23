/**
 *
 * Copyright (c) 2022, Openflexo
 * <p>
 * This file is part of FML-CLI, a component of the software infrastructure
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
package org.openflexo.foundation.fml.cli;

import org.openflexo.foundation.FlexoService.ServiceOperation;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;

import java.io.PrintStream;
import java.util.Map;

public class CdResourceCenter implements ServiceOperation<FlexoResourceCenterService> {

    @Override
    public String getOperationName() {
        return "cd_rc";
    }

    @Override
    public String usage(FlexoResourceCenterService service) {
        return "service " + service.getServiceName() + " cd_rc -rc <rc>";
    }

    @Override
    public String description() {
        return "change working directory to the path denoted by supplied resource center";
    }

    @Override
    public String getArgument() {
        return "<rc>";
    }

    @Override
    public String getSyntax(FlexoResourceCenterService service) {
        return "service " + service.getServiceName() + " " + getOperationName() + " -rc " + getArgument();
    }

    @Override
    public void execute(FlexoResourceCenterService service, PrintStream out, PrintStream err, Object argument, Map<String, ?> options) {
        AbstractCommandInterpreter commandInterpreter = (AbstractCommandInterpreter) options.get("commandInterpreter");
        if (argument instanceof DirectoryResourceCenter) {
            // System.out.println("On change pour [" + ((DirectoryResourceCenter) argument).getRootDirectory().getAbsolutePath() + "]");
            commandInterpreter.setWorkingDirectory(((DirectoryResourceCenter) argument).getRootDirectory());
            // System.out.println("Hop: [" + commandInterpreter.getWorkingDirectory().getAbsolutePath() + "]");
        } else {
            err.println("Cannot cd to " + argument);
        }
    }

    @Override
    public String getStringRepresentation(Object argumentValue) {
        return getOperationName() + "-rc [\"" + ((FlexoResourceCenter<?>) argumentValue).getDefaultBaseURI() + "\"]";
    }

}
