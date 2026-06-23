/**
 *
 * Copyright (c) 2014, Openflexo
 * <p>
 * This file is part of Gina-core, a component of the software infrastructure
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

package org.openflexo.foundation.fml.cli.command;

import org.openflexo.foundation.fml.cli.AbstractCommandSemanticsAnalyzer;
import org.openflexo.foundation.fml.cli.ScriptSemanticsAnalyzer;
import org.openflexo.foundation.fml.cli.command.directive.*;
import org.openflexo.foundation.fml.cli.command.fml.*;
import org.openflexo.foundation.fml.parser.node.*;
import org.openflexo.pamela.PamelaMetaModelLibrary;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.PamelaModelFactory;

public class FMLScriptModelFactory extends PamelaModelFactory {

    public FMLScriptModelFactory() throws ModelDefinitionException {
        super(PamelaMetaModelLibrary.retrieveMetaModel(FMLScript.class));
    }

    public FMLScript newFMLScript(Node node, ScriptSemanticsAnalyzer scriptSemanticsAnalyzer) {
        return newInstance(FMLScript.class, node, scriptSemanticsAnalyzer);
    }

    public ActivateTA newActivateTA(AActivateTaDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(ActivateTA.class, node, semanticsAnalyzer);
    }

    public CdDirective newCdDirective(ACdDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(CdDirective.class, node, semanticsAnalyzer);
    }

    public EnterDirective newEnterDirective(AEnterDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(EnterDirective.class, node, semanticsAnalyzer);
    }

    public ExitDirective newExitDirective(AExitDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(ExitDirective.class, node, semanticsAnalyzer);
    }

    public HelpDirective newHelpDirective(AHelpDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(HelpDirective.class, node, semanticsAnalyzer);
    }

    public HistoryDirective newHistoryDirective(AHistoryDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(HistoryDirective.class, node, semanticsAnalyzer);
    }

    public LoadResource newLoadResource(ALoadDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(LoadResource.class, node, semanticsAnalyzer);
    }

    public LsDirective newLsDirective(ALsDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(LsDirective.class, node, semanticsAnalyzer);
    }

    public ExecuteDirective newExecuteDirective(AExecuteDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(ExecuteDirective.class, node, semanticsAnalyzer);
    }

    public MoreDirective newMoreDirective(AMoreDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(MoreDirective.class, node, semanticsAnalyzer);
    }

    public OpenProject newOpenProject(AOpenDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(OpenProject.class, node, semanticsAnalyzer);
    }

    public PwdDirective newPwdDirective(APwdDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(PwdDirective.class, node, semanticsAnalyzer);
    }

    public QuitDirective newQuitDirective(AQuitDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(QuitDirective.class, node, semanticsAnalyzer);
    }

    public ResourcesDirective newResourcesDirective(AResourcesDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(ResourcesDirective.class, node, semanticsAnalyzer);
    }

    public ServiceDirective<?> newServiceDirective(AServiceDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(ServiceDirective.class, node, semanticsAnalyzer);
    }

    public ServicesDirective newServicesDirective(AServicesDirective node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(ServicesDirective.class, node, semanticsAnalyzer);
    }

    public FMLExpression newFMLExpression(AExpressionFmlCommand node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(FMLExpression.class, node, semanticsAnalyzer);
    }

    public FMLAssignation newFMLAssignation(AAssignmentExpression node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(FMLAssignation.class, node, semanticsAnalyzer);
    }

    public FMLAssertExpression newFMLAssertExpression(AAssertFmlCommand node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(FMLAssertExpression.class, node, semanticsAnalyzer);
    }

    public FMLContextCommand newFMLContextCommand(AContextFmlCommand node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(FMLContextCommand.class, node, semanticsAnalyzer);
    }

    public FMLActionCommand newFMLActionCommand(AFmlActionFmlCommand node, AbstractCommandSemanticsAnalyzer semanticsAnalyzer) {
        return newInstance(FMLActionCommand.class, node, semanticsAnalyzer);
    }

}
