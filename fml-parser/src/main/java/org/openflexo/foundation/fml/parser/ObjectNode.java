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

package org.openflexo.foundation.fml.parser;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.expr.Constant;
import org.openflexo.connie.type.CustomType;
import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.fml.*;
import org.openflexo.foundation.fml.FMLModelContext.FMLProperty;
import org.openflexo.foundation.fml.controlgraph.*;
import org.openflexo.foundation.fml.editionaction.*;
import org.openflexo.foundation.fml.md.*;
import org.openflexo.foundation.fml.parser.fmlnodes.*;
import org.openflexo.foundation.fml.parser.fmlnodes.controlgraph.*;
import org.openflexo.foundation.fml.parser.fmlnodes.expr.DataBindingNode;
import org.openflexo.foundation.fml.parser.node.*;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceFactory;
import org.openflexo.foundation.fml.rt.editionaction.*;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.PrettyPrintContext;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.p2pp.RawSource.RawSourcePosition;
import org.openflexo.toolbox.ChainedCollection;
import org.openflexo.toolbox.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Maintains consistency between the model (represented by an {@link FMLObject}) and source code represented in FML language
 *
 * Works
 *
 * @author sylvain
 *
 */
public abstract class ObjectNode<N extends Node, T, A extends FMLSemanticsAnalyzer> extends P2PPNode<N, T>
        implements FMLPrettyPrintDelegate<T> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ObjectNode.class.getPackage().getName());

    private final FMLSemanticsAnalyzer semanticsAnalyzer;

    public ObjectNode(N astNode, FMLSemanticsAnalyzer analyzer) {
        super(null, astNode, analyzer != null ? analyzer.getFragmentManager() : null);
        this.semanticsAnalyzer = analyzer;

		/*if (analyzer == null) {
			System.out.println("Tiens qui me cree sans analyzer ???");
			Thread.dumpStack();
			System.exit(-1);
		}*/

        modelObject = buildModelObjectFromAST(astNode);
    }

    public ObjectNode(T aFMLObject, FMLSemanticsAnalyzer analyzer) {
        super(aFMLObject, null, null);
        this.semanticsAnalyzer = analyzer;

		/*if (analyzer == null) {
			System.out.println("Tiens qui me cree sans analyzer ???");
			Thread.dumpStack();
			System.exit(-1);
		}*/
    }

    public FMLModelFactory getFactory() {
        return semanticsAnalyzer.getModelFactory();
    }

    @Override
    public boolean hasSource() {
        return getASTNode() != null;
    }

    /**
     * Return fragment matching AST node
     *
     * @return
     */
    @Override
    public RawSourceFragment getFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode());
        }
        return null;
    }

    @Override
    public RawSourcePosition getStartLocation() {
        RawSourceFragment fragment = getFragment();
        if (fragment != null) {
            return fragment.getStartPosition();
        }
        return null;
    }

    @Override
    public RawSourcePosition getEndLocation() {
        RawSourceFragment fragment = getFragment();
        if (fragment != null) {
            return fragment.getEndPosition();
        }
        return null;
    }

    public FMLSemanticsAnalyzer getSemanticsAnalyzer() {
        return semanticsAnalyzer;
    }

	/*public TypeFactory getTypeFactory() {
		return getanalyzer().getTypeFactory();
	}*/

    public FMLFactory getFMLFactory() {
        if (getSemanticsAnalyzer() instanceof FMLCompilationUnitSemanticsAnalyzer) {
            return ((FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer()).getFMLFactory();
        }
        return null;
    }

    protected FMLCompilationUnit getCompilationUnit() {
        return getSemanticsAnalyzer().getCompilationUnit();
    }

    // Make this method visible
    @Override
    public void addToChildren(P2PPNode<?, ?> child) {
        super.addToChildren(child);
    }

    // Make this method visible
    @Override
    public void preparePrettyPrint(boolean hasParsedVersion) {
        super.preparePrettyPrint(hasParsedVersion);
    }

	/*protected void handleToken(Token token) {
	
		// System.out.println("Receiving Token " + token.getLine() + ":" + token.getPos() + ":" + token.getText() + " tokenEnd=" + tokenEnd
		// + " endPosition=" + endPosition);
	
		RawSourcePosition tokenStart = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos());
		RawSourcePosition tokenEnd = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos() + token.getText().length());
	
		if (startPosition == null || tokenStart.compareTo(startPosition) < 0) {
			startPosition = tokenStart;
			parsedFragment = null;
		}
		if (endPosition == null || tokenEnd.compareTo(endPosition) > 0) {
			endPosition = tokenEnd;
			parsedFragment = null;
		}
	
		if (getParent() instanceof FMLObjectNode) {
			((FMLObjectNode<?, ?>) getParent()).handleToken(token);
		}
	}*/

    /**
     * Return original version of last serialized raw source, FOR THE ENTIRE compilation unit
     *
     * @return
     */
    @Override
    public RawSource getRawSource() {
        return getSemanticsAnalyzer().getRawSource();
    }

    @Override
    public String getRepresentation(PrettyPrintContext context) {
        return getTextualRepresentation(context);
    }

    @Override
    public String getNormalizedRepresentation(PrettyPrintContext context) {
        return getNormalizedTextualRepresentation(context);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <C> P2PPNode<?, C> makeObjectNode(C object) {
        if (getSemanticsAnalyzer() instanceof FMLCompilationUnitSemanticsAnalyzer) {
            if (object instanceof NamespaceDeclaration) {
                return (P2PPNode<?, C>) new NamespaceDeclarationNode((NamespaceDeclaration) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof UseModelSlotDeclaration) {
                return (P2PPNode<?, C>) new UseDeclarationNode((UseModelSlotDeclaration) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof JavaImportDeclaration) {
                return (P2PPNode<?, C>) new JavaImportNode((JavaImportDeclaration) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ElementImportDeclaration) {
                return (P2PPNode<?, C>) new ElementImportNode((ElementImportDeclaration) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof TypeDeclaration) {
                return (P2PPNode<?, C>) new TypeDeclarationNode((TypeDeclaration) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof BasicMetaData) {
                return (P2PPNode<?, C>) new BasicMetaDataNode((BasicMetaData) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof SingleMetaData) {
                return (P2PPNode<?, C>) new SingleMetaDataNode((SingleMetaData) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof MultiValuedMetaData) {
                return (P2PPNode<?, C>) new MultiValuedMetaDataNode((MultiValuedMetaData) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof MetaDataKeyValue) {
                return (P2PPNode<?, C>) new MetaDataKeyValueNode((MetaDataKeyValue) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ListMetaData) {
                return (P2PPNode<?, C>) new ListMetaDataNode((ListMetaData) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof VirtualModel) {
                return (P2PPNode<?, C>) new VirtualModelNode((VirtualModel) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoEnumValue) {
                return (P2PPNode<?, C>) new FlexoEnumValueNode((FlexoEnumValue) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoEnum) {
                return (P2PPNode<?, C>) new FlexoEnumNode((FlexoEnum) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoEvent) {
                return (P2PPNode<?, C>) new FlexoEventNode((FlexoEvent) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoConcept) {
                return (P2PPNode<?, C>) new FlexoConceptNode((FlexoConcept) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof SingleMetaData) {
                return (P2PPNode<?, C>) new SingleMetaDataNode((SingleMetaData) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof PrimitiveRole) {
                return (P2PPNode<?, C>) new PrimitiveRoleNode((PrimitiveRole) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof JavaRole) {
                return (P2PPNode<?, C>) new JavaRoleNode((JavaRole) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof AbstractProperty) {
                return (P2PPNode<?, C>) new AbstractPropertyNode((AbstractProperty) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ExpressionProperty) {
                return (P2PPNode<?, C>) new ExpressionPropertyNode((ExpressionProperty) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof GetSetProperty) {
                return (P2PPNode<?, C>) new GetSetPropertyNode((GetSetProperty) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof AbstractProperty) {
                return (P2PPNode<?, C>) new AbstractPropertyNode((AbstractProperty) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ModelSlot) {
                return new ModelSlotPropertyNode((ModelSlot) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoRole) {
                return new FlexoRolePropertyNode((FlexoRole) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ActionScheme) {
                return (P2PPNode<?, C>) new ActionSchemeNode((ActionScheme) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof CreationScheme) {
                return (P2PPNode<?, C>) new CreationSchemeNode((CreationScheme) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof DeletionScheme) {
                return (P2PPNode<?, C>) new DeletionSchemeNode((DeletionScheme) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoBehaviour) {
                return new FMLBehaviourNode((FlexoBehaviour) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FlexoBehaviourParameter) {
                return (P2PPNode<?, C>) new BehaviourParameterNode((FlexoBehaviourParameter) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof EmptyControlGraph) {
                return (P2PPNode<?, C>) new EmptyControlGraphNode((EmptyControlGraph) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof Sequence) {
                return (P2PPNode<?, C>) new SequenceNode((Sequence) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof AssignationAction) {
                return (P2PPNode<?, C>) new AssignationActionNode((AssignationAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof DeclarationAction) {
                return (P2PPNode<?, C>) new DeclarationActionNode((DeclarationAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ExpressionAction) {
                return (P2PPNode<?, C>) new ExpressionActionNode((ExpressionAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ReturnStatement) {
                return (P2PPNode<?, C>) new ReturnStatementNode((ReturnStatement) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ConditionalAction) {
                return (P2PPNode<?, C>) new ConditionalNode((ConditionalAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof IterationAction) {
                return (P2PPNode<?, C>) new IterationActionNode((IterationAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ExpressionIterationAction) {
                return (P2PPNode<?, C>) new ExpressionIterationActionNode((ExpressionIterationAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
			/*if (object instanceof AddFlexoConceptInstance) {
			return (P2PPNode<?, C>) new AddFlexoConceptInstanceNode((AddFlexoConceptInstance) object, getanalyzer());
			}
			if (object instanceof AddVirtualModelInstance) {
			return (P2PPNode<?, C>) new AddVirtualModelInstanceNode((AddVirtualModelInstance) object, getanalyzer());
			}
			if (object instanceof AddClassInstance) {
			return (P2PPNode<?, C>) new AddClassInstanceNode((AddClassInstance) object, getanalyzer());
			}*/
            if (object instanceof FireEvent) {
                return (P2PPNode<?, C>) new FireEventNode((FireEvent) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof LogAction) {
                return (P2PPNode<?, C>) new LogActionNode((LogAction) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof ConnectAction) {
                return (P2PPNode<?, C>) new ConnectActionNode((ConnectAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof NotifyPropertyChangedAction) {
                return (P2PPNode<?, C>) new NotifyActionNode((NotifyPropertyChangedAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof DeleteAction) {
                return (P2PPNode<?, C>) new DeleteActionNode((DeleteAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof AbstractFetchRequest) {
                return new FetchRequestNode((AbstractFetchRequest) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof BehaviourCallArgument) {
                return (P2PPNode<?, C>) new BehaviourCallArgumentNode((BehaviourCallArgument) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof InitiateMatching) {
                return (P2PPNode<?, C>) new BeginMatchActionNode((InitiateMatching) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof MatchFlexoConceptInstance) {
                return (P2PPNode<?, C>) new MatchActionNode((MatchFlexoConceptInstance) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FinalizeMatching) {
                return (P2PPNode<?, C>) new EndMatchActionNode((FinalizeMatching) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof TechnologySpecificAction) {
                return new FMLEditionActionNode((TechnologySpecificAction) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FMLSimplePropertyValue) {
                return new FMLSimplePropertyValueNode((FMLSimplePropertyValue) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FMLTypePropertyValue) {
                return new FMLTypePropertyValueNode((FMLTypePropertyValue) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FMLInstancePropertyValue) {
                return new FMLInstancePropertyValueNode((FMLInstancePropertyValue) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof FMLInstancesListPropertyValue) {
                return new FMLInstancesListPropertyValueNode((FMLInstancesListPropertyValue) object,
                        (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
            if (object instanceof WrappedFMLObject) {
                return new WrappedFMLObjectNode((WrappedFMLObject) object, (FMLCompilationUnitSemanticsAnalyzer) getSemanticsAnalyzer());
            }
        }
        System.err.println("Not supported: " + object);
        Thread.dumpStack();
        return null;
    }

	/*public ControlGraphFactory getControlGraphFactory() {
		if (getAbstractanalyzer() instanceof ControlGraphFactory) {
			return (ControlGraphFactory) getAbstractanalyzer();
		}
		if (getParent() instanceof FMLObjectNode) {
			return ((FMLObjectNode) getParent()).getControlGraphFactory();
		}
		return null;
	}*/

    /**
     * Return fragment matching supplied node in AST
     *
     * @param token
     * @return
     */
    public RawSourceFragment getFragment(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof Token) {
            Token token = (Token) node;
            return getRawSource().makeFragment(getRawSource().makePositionBeforeChar(token.getLine(), token.getPos()),
                    getRawSource().makePositionBeforeChar(token.getLine(), token.getPos() + token.getText().length()));
        } else {
            return getSemanticsAnalyzer().getFragmentManager().retrieveFragment(node);
        }
    }

    /**
     * Return fragment matching supplied nodes in AST
     *
     * @param token
     * @return
     */
    public RawSourceFragment getFragment(Node node, Node otherNode) {
        return getFragment(node, Collections.singletonList(otherNode));
    }

    /**
     * Return fragment matching supplied nodes in AST
     *
     * @param token
     * @return
     */
    public RawSourceFragment getFragment(Node node, List<? extends Node> otherNodes) {
        ChainedCollection<Node> collection = new ChainedCollection<>();
        collection.add(node);
        collection.add(otherNodes);
        return getSemanticsAnalyzer().getFragmentManager().getFragment(collection);
    }

    public String getText(Node node) {
        return getFragment(node).getRawText();
    }

    public List<String> makeFullQualifiedIdentifierList(List<PIdentifierPrefix> prefixes, TLidentifier identifier) {
        return semanticsAnalyzer.makeFullQualifiedIdentifierList(prefixes, identifier);
    }

    public String makeFullQualifiedIdentifier(List<PIdentifierPrefix> prefixes, TLidentifier identifier) {
        return semanticsAnalyzer.makeFullQualifiedIdentifier(prefixes, identifier);
    }

    public String makeFullQualifiedIdentifier(PCompositeIdent compositeIdentifier) {
        return semanticsAnalyzer.makeFullQualifiedIdentifier(compositeIdentifier);
    }

    public String makeFullQualifiedIdentifier(PAnnotationTag annotationTag) {
        return semanticsAnalyzer.makeFullQualifiedIdentifier(annotationTag);
    }

    public List<String> makeFullQualifiedIdentifierList(List<PIdentifierPrefix> prefixes, TUidentifier identifier) {
        return semanticsAnalyzer.makeFullQualifiedIdentifierList(prefixes, identifier);
    }

    public String makeFullQualifiedIdentifier(List<PIdentifierPrefix> prefixes, TUidentifier identifier) {
        return semanticsAnalyzer.makeFullQualifiedIdentifier(prefixes, identifier);
    }

    public String makeFullQualifiedIdentifier(PCompositeTident compositeIdentifier) {
        return semanticsAnalyzer.makeFullQualifiedIdentifier(compositeIdentifier);
    }

    public int getLiteralValue(TLitInteger node) {
        String f = node.getText();
        try {
            return Integer.parseInt(f);
        } catch (NumberFormatException e) {
            throwIssue("Cannot parse as integer: " + f, getFragment(node));
            return -1;
        }
    }

    public Object getLiteralValue(PLiteral node) throws ParseException {
        if (node instanceof ACharacterLiteral) {
            return ((ACharacterLiteral) node).getLitCharacter().getText().charAt(1);
        } else if (node instanceof AFalseLiteral) {
            return false;
        } else if (node instanceof ATrueLiteral) {
            return true;
        } else if (node instanceof AStringLiteral) {
            String t = ((AStringLiteral) node).getLitString().getText();
            return t.substring(1, t.length() - 2);
        } else if (node instanceof AFloatingPointLiteral) {
            String f = ((AFloatingPointLiteral) node).getLitFloat().getText();
            try {
                return Double.parseDouble(f);
            } catch (NumberFormatException e) {
                throwIssue("Cannot parse as double: " + f, getFragment(node));
                return null;
            }
        } else if (node instanceof AIntegerLiteral) {
            String f = ((AIntegerLiteral) node).getLitInteger().getText();
            try {
                return Long.parseLong(f);
            } catch (NumberFormatException e) {
                throwIssue("Cannot parse as long: " + f, getFragment(node));
                return null;
            }
        } else if (node instanceof ANullLiteral) {
            return null;
        }
        RawSourceFragment fragment = getFragment(node);
        throwIssue("Unexpected " + node, fragment);
        return null;
    }

	/*
	// We should parse expression instead
	@Deprecated
	protected <T> DataBinding<T> makeBinding(Node node, Type type, BindingDefinitionType bindingType, Bindable bindable) {
		return new DataBinding(getText(node), bindable, type, bindingType);
	}
	
	// We should parse expression instead
	@Deprecated
	protected <T> DataBinding<T> makeBinding(Node node, Bindable bindable) {
		return new DataBinding(getText(node), bindable, Object.class, BindingDefinitionType.GET);
	}
	
	// We should parse expression instead
	@Deprecated
	protected <T> DataBinding<T> makeBinding(PCompositeIdent compositeIdentifier, Type type, BindingDefinitionType bindingType,
			Bindable bindable) {
		// TODO: implement this
		logger.warning("Un truc a faire la pour " + compositeIdentifier);
		return new DataBinding(analyzer.makeFullQualifiedIdentifier(compositeIdentifier), bindable, type, bindingType);
	}
	
	// We should parse expression instead
	@Deprecated
	protected <T> DataBinding<T> makeBinding(PCompositeIdent compositeIdentifier, Bindable bindable) {
		// TODO: implement this
		logger.warning("Un truc a faire la pour " + compositeIdentifier);
		return new DataBinding(analyzer.makeFullQualifiedIdentifier(compositeIdentifier), bindable, Object.class,
				BindingDefinitionType.GET);
	}*/

    protected String getVisibilityAsString(Visibility visibility) {
        if (visibility != null) {
            switch (visibility) {
                case Default:
                    return "";
                case Public:
                    return "public";
                case Protected:
                    return "protected";
                case Private:
                    return "private";
            }
        }
        return "";
    }

    protected Visibility getVisibility(PVisibility visibility) {
        if (visibility == null) {
            return Visibility.Default;
        } else if (visibility instanceof APublicVisibility) {
            return Visibility.Public;
        } else if (visibility instanceof AProtectedVisibility) {
            return Visibility.Protected;
        } else if (visibility instanceof APrivateVisibility) {
            return Visibility.Private;
        }
        return null;
    }

    protected PropertyCardinality getCardinality(PCardinality cardinality) {
        if (cardinality == null) {
            return PropertyCardinality.ZeroOne;
        }
        Integer upperBounds = null;
        Integer lowerBounds = null;
        if (cardinality instanceof AWithExplicitBoundsCardinality) {
            lowerBounds = getLiteralValue(((AWithExplicitBoundsCardinality) cardinality).getLower());
            upperBounds = getLiteralValue(((AWithExplicitBoundsCardinality) cardinality).getUpper());
        } else if (cardinality instanceof AWithLowerBoundsCardinality) {
            lowerBounds = getLiteralValue(((AWithLowerBoundsCardinality) cardinality).getLower());
            upperBounds = null;
        } else if (cardinality instanceof AWithUpperBoundsCardinality) {
            lowerBounds = null;
            upperBounds = getLiteralValue(((AWithUpperBoundsCardinality) cardinality).getUpper());
        } else if (cardinality instanceof AMultiple1Cardinality) {
            lowerBounds = null;
            upperBounds = null;
        } else if (cardinality instanceof AMultiple2Cardinality) {
            lowerBounds = null;
            upperBounds = null;
        }
        if (lowerBounds != null && lowerBounds == 0) {
            if (upperBounds != null && upperBounds == 1) {
                return PropertyCardinality.ZeroOne;
            } else {
                return PropertyCardinality.ZeroMany;
            }
        }
        if (lowerBounds != null && lowerBounds == 1) {
            if (upperBounds != null && upperBounds == 1) {
                return PropertyCardinality.One;
            } else {
                return PropertyCardinality.OneMany;
            }
        }
        return PropertyCardinality.ZeroMany;
    }

    protected final String serializeCardinality(PropertyCardinality cardinality) {
        if (cardinality == null) {
            return "";
        }
        switch (cardinality) {
            case One:
                return "";
            case ZeroOne:
                return "";
            case ZeroMany:
                return "[0,*]";
            case OneMany:
                return "[1,*]";
            default:
                return "";
        }

    }

    protected TLidentifier getName(PVariableDeclarator variableDeclarator) {
        if (variableDeclarator instanceof AIdentifierVariableDeclarator) {
            return ((AIdentifierVariableDeclarator) variableDeclarator).getLidentifier();
        }
        if (variableDeclarator instanceof AInitializerExpressionVariableDeclarator) {
            return ((AInitializerExpressionVariableDeclarator) variableDeclarator).getLidentifier();
        }
        if (variableDeclarator instanceof AInitializerFmlActionVariableDeclarator) {
            return ((AInitializerFmlActionVariableDeclarator) variableDeclarator).getLidentifier();
        }
        return null;
    }

    protected PExpression getInitializerExpression(PVariableDeclarator variableDeclarator) {
        if (variableDeclarator instanceof AIdentifierVariableDeclarator) {
            return null;
        }
        if (variableDeclarator instanceof AInitializerExpressionVariableDeclarator) {
            return ((AInitializerExpressionVariableDeclarator) variableDeclarator).getExpression();
        }
        if (variableDeclarator instanceof AInitializerFmlActionVariableDeclarator) {
            return null;
        }
        return null;
    }

    protected PFmlActionExp getInitializerFMLAction(PVariableDeclarator variableDeclarator) {
        if (variableDeclarator instanceof AIdentifierVariableDeclarator) {
            return null;
        }
        if (variableDeclarator instanceof AInitializerExpressionVariableDeclarator) {
            return null;
        }
        if (variableDeclarator instanceof AInitializerFmlActionVariableDeclarator) {
            return ((AInitializerFmlActionVariableDeclarator) variableDeclarator).getFmlActionExp();
        }
        return null;
    }

    protected final String serializeTypeEscapeVoid(Type type, boolean escapeVoid) {
        if (escapeVoid && ((Void.class.equals(type)) || (Void.TYPE.equals(type)))) {
            return "";
        }
        return serializeType(type);
    }

    protected final String serializeType(Type type) {
        return serializeType(type, true);
    }

    protected final String serializeType(Type type, boolean useTypeDefinitions) {
        // TODO: generate required imports !
		/*if (type != null) {
			if (type.equals(Boolean.class)) {
				return "boolean";
			}
		}*/
        if (type instanceof CustomType) {
            if (!((CustomType) type).isResolved()) {
                ((CustomType) type).resolve();
            }
        }

		/*if (abbrevWhenPossible && semanticsAnalyzer.getCompilationUnit() != null) {
			for (TypeDeclaration typeDeclaration : semanticsAnalyzer.getCompilationUnit().getTypeDeclarations()) {
				if (typeDeclaration.getReferencedType() == type) {
					return typeDeclaration.getAbbrev();
				}
			}
		}*/

        if (type instanceof VirtualModelInstanceType && ((VirtualModelInstanceType) type).getVirtualModel() == null) {
            String uri = ((VirtualModelInstanceType) type).getConceptURI();
            if (StringUtils.isNotEmpty(uri)) {
                if (uri.contains("/")) {
                    uri = uri.substring(uri.lastIndexOf("/") + 1);
                }
                if (uri.endsWith(CompilationUnitResourceFactory.FML_SUFFIX)) {
                    uri = uri.substring(0, uri.length() - CompilationUnitResourceFactory.FML_SUFFIX.length());
                }
                return uri;
            }
            return AbstractFMLTypingSpace.MODEL_INSTANCE;
        }

        if (type instanceof FlexoConceptInstanceType && ((FlexoConceptInstanceType) type).getFlexoConcept() == null) {
            String uri = ((FlexoConceptInstanceType) type).getConceptURI();
            if (StringUtils.isNotEmpty(uri)) {
                if (uri.contains("/")) {
                    uri = uri.substring(uri.lastIndexOf("/") + 1);
                }
                return uri;
            }
            return AbstractFMLTypingSpace.CONCEPT_INSTANCE;
        }

        if (type instanceof TechnologySpecificType) {
            TechnologyAdapter ta = ((TechnologySpecificType) type).getSpecificTechnologyAdapter();
            if (ta != null) {
                return ta.serializeType((TechnologySpecificType) type, getCompilationUnit(), useTypeDefinitions);
            } else {
                logger.warning("No technology adapter for type " + type);
            }
        }

        String returned = TypeUtils.simpleRepresentation(type);
		/*if (returned.startsWith("#")) {
			System.out.println("Nimporte quoi: " + type);
			System.out.println("type: " + type.getClass());
			if (type instanceof FlexoConceptInstanceType) {
				FlexoConceptInstanceType fciType = (FlexoConceptInstanceType) type;
				System.out.println("uri: " + fciType.getConceptURI());
				System.out.println("concept: " + fciType.getFlexoConcept());
				if (fciType.getFlexoConcept() != null) {
					System.out.println("concept.name: " + fciType.getFlexoConcept().getName());
					System.out.println("resolved: " + fciType.isResolved());
					System.out.println("simpleRepresentation: " + fciType.simpleRepresentation());
					System.out.println("simpleRepresentation: " + TypeUtils.simpleRepresentation(type));
				}
			}
			Thread.dumpStack();
		}*/
        return returned;

    }

    protected final String serializeType(FlexoConcept type) {
        // TODO: generate required imports !
        if (type == null) {
            return "UndefinedConcept";
        }
        return type.getName();

    }

    protected String serializeFlexoBehaviour(FlexoBehaviour behaviour) {
        if (behaviour != null) {
            return behaviour.getName();
        }
        return "undefinedBehaviour";
    }

    protected DataBindingNode makeDataBinding(PExpression expression, Bindable bindable) {

        DataBindingNode dataBindingNode = getSemanticsAnalyzer().retrieveFMLNode(expression,
                n -> new DataBindingNode(n, bindable, BindingDefinitionType.GET, Object.class, getSemanticsAnalyzer()));
        addToChildren(dataBindingNode);

        ExpressionFactory._makeExpression(expression, bindable, getSemanticsAnalyzer(), dataBindingNode);

        return dataBindingNode;
    }

    protected void throwIssue(String errorMessage) {
        throwIssue(errorMessage, null);
    }

    protected final void throwIssue(String errorMessage, RawSourceFragment fragment) {
        getSemanticsAnalyzer().throwIssue(getModelObject(), errorMessage, fragment, getStartPosition());
    }

    @Override
    public List<SemanticAnalysisIssue> getSemanticAnalysisIssues() {
        return getSemanticsAnalyzer().getSemanticAnalysisIssues();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getASTNode();
    }

    // MUST be overriden to be usefull
    @Override
    public RawSourceFragment getFragment(FragmentContext context) {
        return getFragment();
    }

    protected List<FMLPropertyValue> decodeFMLProperties(PFmlParameters properties, FMLObject modelObject) {
        if (properties == null) {
            return null;
        }
        if (!modelObject.hasFMLProperties(getFactory())) {
            return null;
        }
        if (properties instanceof AFullQualifiedFmlParameters) {
            List<FMLPropertyValue> propertyValues = new ArrayList<>();
            PQualifiedArgumentList qualifiedArgumentList = ((AFullQualifiedFmlParameters) properties).getQualifiedArgumentList();
            decodeFMLProperties(qualifiedArgumentList, modelObject, propertyValues);
            // modelObject.decodeFMLProperties(serializedMap);
            return propertyValues;
        }
        return null;

    }

    private void decodeFMLProperties(PQualifiedArgumentList argList, FMLObject modelObject, List<FMLPropertyValue> propertyValues) {
        if (argList instanceof AOneQualifiedArgumentList) {
            AOneQualifiedArgumentList one = (AOneQualifiedArgumentList) argList;
            // decodeFMLProperty(one.getArgName(), one.getExpression(), modelObject, propertyValues);
            decodeFMLProperty(one.getQualifiedArgument(), modelObject, propertyValues);
        } else if (argList instanceof AManyQualifiedArgumentList) {
            AManyQualifiedArgumentList many = (AManyQualifiedArgumentList) argList;
            decodeFMLProperty(many.getQualifiedArgument(), modelObject, propertyValues);
            // decodeFMLProperty(many.getArgName(), many.getExpression(), modelObject, propertyValues);
            decodeFMLProperties(many.getQualifiedArgumentList(), modelObject, propertyValues);
        }
    }

    private void decodeFMLProperty(PQualifiedArgument qualifiedArg, FMLObject modelObject, List<FMLPropertyValue> propertyValues) {
        if (qualifiedArg instanceof ASimpleQualifiedArgument) {
            decodeSimpleFMLProperty(((ASimpleQualifiedArgument) qualifiedArg).getArgName(),
                    ((ASimpleQualifiedArgument) qualifiedArg).getExpression(), modelObject, propertyValues);
        } else if (qualifiedArg instanceof AInstanceQualifiedArgument) {
            decodeInstanceFMLProperty(((AInstanceQualifiedArgument) qualifiedArg).getArgName(),
                    ((AInstanceQualifiedArgument) qualifiedArg).getQualifiedInstance(), modelObject, propertyValues);
        } else if (qualifiedArg instanceof AListInstancesQualifiedArgument) {
            System.out.println("TODO for AListInstancesQualifiedArgument: " + qualifiedArg);
        }
    }

    private void decodeSimpleFMLProperty(TLidentifier propertyName, PExpression expressionValue, FMLObject modelObject,
                                         List<FMLPropertyValue> propertyValues) {

        // logger.info("Decoding " + propertyName.getText() + "=" + expressionValue);
        DataBinding<?> value = makeDataBinding(expressionValue, modelObject).getModelObject();
        FMLProperty fmlProperty = modelObject.getFMLProperty(propertyName.getText(), getFactory());
        if (fmlProperty == null) {
            logger.warning("Cannot retrieve FMLProperty " + propertyName + " for " + modelObject);
            FMLSimplePropertyValue newSimplePropertyValue = getFactory().newSimplePropertyValue();
            newSimplePropertyValue.setUnresolvedPropertyName(propertyName.getText());
            newSimplePropertyValue.setValue(value);
            propertyValues.add(0, newSimplePropertyValue);
            return;
        }

        // System.out.println("FMLProperty=" + fmlProperty + " type=" + fmlProperty.getType());
        if (DataBinding.class.equals(TypeUtils.getBaseClass(fmlProperty.getType()))) {
            // logger.info("Set " + fmlProperty.getName() + " = " + value);
            fmlProperty.set(value, modelObject);
            propertyValues.add(0, fmlProperty.makeFMLPropertyValue(modelObject, getFactory()));
        } else if (value.isConstant()) {
            Object constantValue = ((Constant) value.getExpression()).getValue();
            if (constantValue != null) {
                if (TypeUtils.isTypeAssignableFrom(fmlProperty.getType(), constantValue.getClass())) {
                    // logger.info("Set " + fmlProperty.getName() + " = " + constantValue);
                    fmlProperty.set(constantValue, modelObject);
                    propertyValues.add(0, fmlProperty.makeFMLPropertyValue(modelObject, getFactory()));
                } else {
                    logger.warning("Invalid value for property " + fmlProperty.getName() + " expected type: " + fmlProperty.getType()
                            + " value: " + constantValue);
                    Thread.dumpStack();
                }
            }
        } else {
            if (getCompilationUnit() != null) {
                for (ElementImportDeclaration elementImportDeclaration : getCompilationUnit().getElementImports()) {
                    // System.out.println(
                    // "> J'ai deja: " + elementImportDeclaration.getAbbrev() + "=" + elementImportDeclaration.getReferencedObject());
                    if (elementImportDeclaration.getAbbrev().equals(value.toString())) {
                        // System.out.println("Trouve !!!");
                        fmlProperty.set(elementImportDeclaration.getReferencedObject(), modelObject);
                        propertyValues.add(0, fmlProperty.makeFMLPropertyValue(modelObject, getFactory()));
                        break;
                    }
                }
            }

            logger.warning("Unexpected value for property " + fmlProperty.getName() + " expected type: " + fmlProperty.getType()
                    + " value: " + value);
        }

    }

    private <O extends FMLObject> O decodeInstanceFMLProperty(TLidentifier propertyName, PQualifiedInstance qualifiedInstance,
                                                              FMLObject modelObject, List<FMLPropertyValue> propertyValues) {
        Class<O> objectClass = null;
        if (qualifiedInstance instanceof ASimpleQualifiedInstance) {
            TUidentifier instanceType = ((ASimpleQualifiedInstance) qualifiedInstance).getArgType();
            objectClass = (Class<O>) getFMLFactory().getFMLObjectClass(instanceType);
        } else if (qualifiedInstance instanceof AFullQualifiedQualifiedInstance) {
            TCidentifier taID = ((AFullQualifiedQualifiedInstance) qualifiedInstance).getTaId();
            TUidentifier instanceType = ((AFullQualifiedQualifiedInstance) qualifiedInstance).getArgType();
            objectClass = (Class<O>) getFMLFactory().getFMLObjectClass(taID, instanceType);
        }

        O returned = getFactory().newInstance(objectClass);
        return returned;
    }

    public Object getObjectAtLocation(int row, int col) {
        if (getLastParsedFragment() == null) {
            return null;
        }
        RawSourcePosition position = getRawSource().new RawSourcePosition(row, col);
        ObjectNode<?, ?, ?> node = searchObjectNodeAtPosition(position);
        if (node != null) {
            return node.getModelObject();
        }

        return null;
    }

    private ObjectNode<?, ?, ?> searchObjectNodeAtPosition(RawSourcePosition position) {
        if (position.isInside(getLastParsedFragment())) {
            for (P2PPNode<?, ?> p2ppNode : getChildren()) {
                if (p2ppNode instanceof ObjectNode) {
                    if (position.isInside(((ObjectNode<?, ?, ?>) p2ppNode).getLastParsedFragment())) {
                        return ((ObjectNode<?, ?, ?>) p2ppNode).searchObjectNodeAtPosition(position);
                    }
                }
            }
            return this;
        }
        return null;

    }

}
