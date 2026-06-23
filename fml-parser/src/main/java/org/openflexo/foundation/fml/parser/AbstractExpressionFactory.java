package org.openflexo.foundation.fml.parser;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.expr.Expression;
import org.openflexo.foundation.fml.*;
import org.openflexo.foundation.fml.parser.fmlnodes.expr.BindingPathNode;
import org.openflexo.foundation.fml.parser.fmlnodes.expr.ConstantNode;
import org.openflexo.foundation.fml.parser.fmlnodes.expr.DataBindingNode;
import org.openflexo.foundation.fml.parser.node.*;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.p2pp.RawSource.RawSourcePosition;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * A factory based on {@link FMLSemanticsAnalyzer}, used to instantiate a {@link DataBinding} or an {@link Expression} from AST
 * <p>
 * Such a factory works with a parent {@link FMLSemanticsAnalyzer}
 *
 * @author sylvain
 *
 */
public abstract class AbstractExpressionFactory extends FMLSemanticsAnalyzer {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AbstractExpressionFactory.class.getPackage().getName());

    private final Map<Node, Expression> expressionNodes;
    private final DataBindingNode dataBindingNode;
    private final FMLSemanticsAnalyzer parentAnalyzer;
    private final AbstractFMLTypingSpace typingSpace;
    private final FMLBindingFactory bindingFactory;
    protected int depth = -1;
    int ident = 0;
    private Node topLevel = null;
    private Bindable bindable;
    private Map<Node, ObjectNode<?, ?, ?>> nodesForAST = new HashMap<>();

    protected AbstractExpressionFactory(Node rootNode, Bindable aBindable, FMLSemanticsAnalyzer parentAnalyzer,
                                        DataBindingNode dataBindingNode) {
        super(parentAnalyzer.getModelFactory(), rootNode);
        expressionNodes = new Hashtable<>();
        this.parentAnalyzer = parentAnalyzer;
        this.bindable = aBindable;
        this.typingSpace = parentAnalyzer.getTypingSpace();
        this.bindingFactory = parentAnalyzer.getFMLBindingFactory();
        this.dataBindingNode = dataBindingNode;
    }

    protected AbstractExpressionFactory(Node rootNode, Bindable aBindable, FMLModelFactory modelFactory, AbstractFMLTypingSpace typingSpace,
                                        FMLBindingFactory bindingFactory, DataBindingNode dataBindingNode) {
        super(modelFactory, rootNode);
        expressionNodes = new Hashtable<>();
        this.bindable = aBindable;
        this.typingSpace = typingSpace;
        this.bindingFactory = bindingFactory;
        this.dataBindingNode = dataBindingNode;
        parentAnalyzer = null;
    }

    @Override
    public <N extends Node, FMLN extends ObjectNode<?, ?, ?>> FMLN retrieveFMLNode(N astNode, Function<N, FMLN> function) {
        FMLN returned = (FMLN) nodesForAST.get(astNode);
        if (returned == null) {
            returned = function.apply(astNode);
            nodesForAST.put(astNode, returned);
        }
        return returned;
    }

    protected boolean weAreDealingWithTheRightBindingPath() {
        return depth == 0;
    }

    public DataBindingNode getDataBindingNode() {
        return dataBindingNode;
    }

    public Expression getExpression() {
        if (topLevel != null) {
            return expressionNodes.get(topLevel);
        }
        return null;
    }

    public Bindable getBindable() {
        return bindable;
    }

    @Override
    public FMLCompilationUnitSemanticsAnalyzer getCompilationUnitAnalyzer() {
        if (getParentAnalyzer() instanceof FMLCompilationUnitSemanticsAnalyzer) {
            return (FMLCompilationUnitSemanticsAnalyzer) getParentAnalyzer();
        }
        return null;
    }

    public FMLSemanticsAnalyzer getParentAnalyzer() {
        return parentAnalyzer;
    }

    @Override
    public FMLCompilationUnit getCompilationUnit() {
        if (getParentAnalyzer() != null) {
            return getParentAnalyzer().getCompilationUnit();
        }
        return null;
    }

    @Override
    public AbstractFMLTypingSpace getTypingSpace() {
        if (getParentAnalyzer() != null) {
            return getParentAnalyzer().getTypingSpace();
        }
        return typingSpace;
    }

    @Override
    public FMLBindingFactory getFMLBindingFactory() {
        if (getParentAnalyzer() != null) {
            return getParentAnalyzer().getFMLBindingFactory();
        }
        return bindingFactory;
    }

    @Override
    public FragmentManager getFragmentManager() {
        if (getParentAnalyzer() != null) {
            return getParentAnalyzer().getFragmentManager();
        }
        return null;
    }

    @Override
    public RawSource getRawSource() {
        if (getParentAnalyzer() != null) {
            return getParentAnalyzer().getRawSource();
        }
        return null;
    }

    @Override
    public void throwIssue(Object modelObject, String errorMessage, RawSourceFragment fragment, RawSourcePosition startPosition) {
        if (getParentAnalyzer() != null) {
            getParentAnalyzer().throwIssue(modelObject, errorMessage, fragment, startPosition);
        }
    }

    @Override
    public List<SemanticAnalysisIssue> getSemanticAnalysisIssues() {
        if (getParentAnalyzer() != null) {
            return getParentAnalyzer().getSemanticAnalysisIssues();
        }
        return null;
    }

    @Override
    protected void push(ObjectNode<?, ?, ?> fmlNode) {
        super.push(fmlNode);
        if (fmlNode.getModelObject() instanceof Expression) {
            registerExpressionNode(fmlNode.getASTNode(), (Expression) fmlNode.getModelObject());
        }
    }

    private void registerExpressionNode(Node n, Expression e) {
        // System.out.println("REGISTER in " + this + " / " + e + "(" + e.getClass() + ")" + " for node " + n + " as " + n.getClass());

		/*if (e instanceof FMLBinaryOperatorExpression) {
			System.out.println("1er arg : " + ((FMLBinaryOperatorExpression) e).getLeftArgument() + " of "
					+ ((FMLBinaryOperatorExpression) e).getLeftArgument().getClass());
			System.out.println("2eme arg : " + ((FMLBinaryOperatorExpression) e).getRightArgument() + " of "
					+ ((FMLBinaryOperatorExpression) e).getRightArgument().getClass());
		}*/
        expressionNodes.put(n, e);
        if (topLevel == null) {
            topLevel = n;
        }
    }

    public Expression getExpression(Node n) {
        if (n != null) {
            Expression returned = expressionNodes.get(n);

            if (returned == null) {
                if (n instanceof AConditionalExpression) {
                    return getExpression(((AConditionalExpression) n).getConditionalExp());
                }
                if (n instanceof ASimpleConditionalExp) {
                    return getExpression(((ASimpleConditionalExp) n).getConditionalOrExp());
                }
                if (n instanceof ASimpleConditionalOrExp) {
                    return getExpression(((ASimpleConditionalOrExp) n).getConditionalAndExp());
                }
                if (n instanceof ASimpleConditionalAndExp) {
                    return getExpression(((ASimpleConditionalAndExp) n).getInclusiveOrExp());
                }
                if (n instanceof ASimpleInclusiveOrExp) {
                    return getExpression(((ASimpleInclusiveOrExp) n).getExclusiveOrExp());
                }
                if (n instanceof ASimpleExclusiveOrExp) {
                    return getExpression(((ASimpleExclusiveOrExp) n).getAndExp());
                }
                if (n instanceof ASimpleAndExp) {
                    return getExpression(((ASimpleAndExp) n).getEqualityExp());
                }
                if (n instanceof ASimpleEqualityExp) {
                    return getExpression(((ASimpleEqualityExp) n).getRelationalExp());
                }
                if (n instanceof ASimpleRelationalExp) {
                    return getExpression(((ASimpleRelationalExp) n).getShiftExp());
                }
                if (n instanceof ASimpleShiftExp) {
                    return getExpression(((ASimpleShiftExp) n).getAddExp());
                }
                if (n instanceof ASimpleAddExp) {
                    return getExpression(((ASimpleAddExp) n).getMultExp());
                }
                if (n instanceof ASimpleMultExp) {
                    return getExpression(((ASimpleMultExp) n).getUnaryExp());
                }
                if (n instanceof AUnaryUnaryExp) {
                    return getExpression(((AUnaryUnaryExp) n).getUnaryExpNotPlusMinus());
                }
                if (n instanceof APostfixUnaryExpNotPlusMinus) {
                    return getExpression(((APostfixUnaryExpNotPlusMinus) n).getPostfixExp());
                }
                if (n instanceof APrimaryPostfixExp) {
                    return getExpression(((APrimaryPostfixExp) n).getPrimary());
                }
                if (n instanceof APrimaryNoIdPrimary) {
                    return getExpression(((APrimaryNoIdPrimary) n).getPrimaryNoId());
                }
                if (n instanceof ALiteralPrimaryNoId) {
                    return getExpression(((ALiteralPrimaryNoId) n).getLiteral());
                }
                if (n instanceof AExpressionPrimaryNoId) {
                    return getExpression(((AExpressionPrimaryNoId) n).getExpression());
                }
                if (n instanceof APreIncrementUnaryExp) {
                    return getExpression(((APreIncrementUnaryExp) n).getPreIncrExp());
                }
                if (n instanceof APreDecrementUnaryExp) {
                    return getExpression(((APreDecrementUnaryExp) n).getPreDecrExp());
                }
                if (n instanceof APostIncrementPostfixExp) {
                    return getExpression(((APostIncrementPostfixExp) n).getPostIncrExp());
                }
                if (n instanceof APostDecrementPostfixExp) {
                    return getExpression(((APostDecrementPostfixExp) n).getPostDecrExp());
                }
                if (n instanceof APrimaryUriExpression) {
                    return getExpression(((APrimaryUriExpression) n).getUriExpressionPrimary());
                }
				/*if (n instanceof ALitteralUriExpressionPrimary) {
					return getExpression(((ALitteralUriExpressionPrimary) n).getLitString());
				}*/

                // This may be NOT an issue
                // logger.warning("In expressionFactory: " + this + " : no expression registered for " + n + " of " + n.getClass());
            }
            return returned;
        }
        return null;
    }

    @Override
    public void defaultIn(Node node) {
        super.defaultIn(node);
        ident++;
        // System.out.println(StringUtils.buildWhiteSpaceIndentation(ident) + " > " + node.getClass().getSimpleName() + " : " + node);
    }

    @Override
    public void defaultOut(Node node) {
        super.defaultOut(node);
        ident--;
    }

    protected BindingPathNode pushBindingPathNode(Node node) {
        depth++;
        if (weAreDealingWithTheRightBindingPath()) {
            BindingPathNode returned;
            push(returned = retrieveFMLNode(node, n -> new BindingPathNode(n, this)));
            return returned;
        }
        return null;
    }

    protected BindingPathNode popBindingPathNode(Node node) {
        try {
            if (weAreDealingWithTheRightBindingPath()) {
                // BindingPathNode bindingPathNode = peek();
                // BindingPathFactory.makeBindingPath(node, this, bindingPathNode, true);
                BindingPathNode bindingPathNode = pop();
                expressionNodes.put(node, bindingPathNode.getModelObject());
                return bindingPathNode;
            }
            return null;
        } finally {
            depth--;
        }
    }

    protected <N extends Node, FMLN extends ConstantNode<?, ?>> FMLN pushConstantNode(N astNode, Function<N, FMLN> function) {
        depth++;
        if (weAreDealingWithTheRightBindingPath()) {
            FMLN newNode = retrieveFMLNode(astNode, function);
            push(newNode);
            return newNode;
        }
        return null;
    }

    protected BindingPathNode popConstantNode(Node node) {
        try {
            if (weAreDealingWithTheRightBindingPath()) {
                pop();
            }
            return null;
        } finally {
            depth--;
        }
    }

}
