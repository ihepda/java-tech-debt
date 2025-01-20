package io.github.ihepda.techdebt.maven.plugin.filter;

import java.util.ArrayDeque;
import java.util.Deque;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;
import io.github.ihepda.techdebt.maven.plugin.filter.FilterParser.ExprContext;
import io.github.ihepda.techdebt.maven.plugin.filter.FilterParser.IdentifierContext;
import io.github.ihepda.techdebt.maven.plugin.filter.FilterParser.LiteralContext;
import io.github.ihepda.techdebt.maven.plugin.filter.FilterParser.ProgramContext;


public class FilterParserVisitorImpl extends FilterParserBaseVisitor<FilterOperation> {
	
	private Deque<FilterOperation> stack = new ArrayDeque<>();
	
	private FilterOperation filterOperationResult;
	
	public static FilterOperation parse(String filter) {
		FilterLexer lexer = new FilterLexer(CharStreams.fromString(filter));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FilterParser parser = new FilterParser(tokens);
		FilterParser.ProgramContext program = parser.program();
		FilterParserVisitorImpl visitor = new FilterParserVisitorImpl();
		FilterOperation filterOperation = visitor.visitProgram(program);
		return filterOperation;
	}
	
	@Override
	public FilterOperation visitProgram(ProgramContext ctx) {
		FilterOperation visitProgram = super.visitProgram(ctx);
		this.filterOperationResult = visitProgram;
		return visitProgram;
	}
	
	public FilterOperation getResult() {
		return filterOperationResult;
	}
	
	private LogicOperation resolveLogicOperation(ExprContext ctx) {
		Token op = ctx.op;
		LogicOperation result = null;
		if (op == null) {
			return null;
		}

		switch (op.getType()) {
		case FilterParser.AND:
			result = new AndLogicOperation();
			break;
		case FilterParser.OR:
			result = new OrLogicOperation();
			break;
		default:
		}

		return result;
	}
	
	private CompareOperation resolveCompareOperation(ExprContext ctx) {
		Token op = ctx.op;
		CompareOperation result = null;
		if(op == null) {
			return null;
		}
		
		switch (op.getType()) {
		case FilterParser.EQUAL:
			result = new EqualsOperation();
			
			break;
		case FilterParser.NEQUAL:
			result = new NotEqualsOperation();
			break;
		case FilterParser.GT:
			result = new GreaterThanOperation();
			break;
		case FilterParser.GE:
			result = new GreaterEqualsOperation();
			break;
		case FilterParser.LT:
			result = new LessThanOperation();
			break;
		case FilterParser.LE:
			result = new LessEqualsOperation();
			break;
		default:
		}
		
		return result;
	}
	
	@Override
	protected FilterOperation defaultResult() {
		
		FilterOperation peek = this.stack.peek();
		if(filterOperationResult == null) {
			filterOperationResult = peek;
		}
		if(peek == null) {
			peek = filterOperationResult;
		}
		return peek;
	}
	
	@Override
	public FilterOperation visitExpr(ExprContext ctx) {
		FilterOperation currentExpression = resolveCompareOperation(ctx);
		if (currentExpression == null) {
			currentExpression = resolveLogicOperation(ctx);
		}
		if (currentExpression == null) {
			return super.visitExpr(ctx);
		}
		FilterOperation peek = this.stack.peek();
		if(peek instanceof LogicOperation lo) {
            lo.addOperation(currentExpression);
		}
		this.stack.push(currentExpression);
		FilterOperation visitExpr = super.visitExpr(ctx);
		this.stack.pop();
		return visitExpr;
	}
	
	@Override
	public FilterOperation visitIdentifier(IdentifierContext ctx) {
		String firstPattern = ctx.getChild(0).getText();
		Enum<?> enumValue = null;
		if(ALL.ALL.name().equalsIgnoreCase(firstPattern)) {
			enumValue = ALL.ALL;
		} else if("severity".equalsIgnoreCase(firstPattern)) {
			String lastPattern = ctx.getChild(2).getText();
			enumValue = Severity.valueOf(lastPattern.toUpperCase());
		} else if ("type".equalsIgnoreCase(firstPattern)) {
			String lastPattern = ctx.getChild(2).getText();
			enumValue = Type.valueOf(lastPattern.toUpperCase());
		}
		FilterOperation operation = stack.peek();
		if (operation instanceof CompareOperation co) {
			co.setFilterElement(enumValue);
		}
		return super.visitIdentifier(ctx);
	}

	@Override
	public FilterOperation visitLiteral(LiteralContext ctx) {
		String text = ctx.getText();
		int value = Integer.parseInt(text);
		FilterOperation operation = stack.peek();
		if(operation instanceof CompareOperation co) {
			co.setLiteral(value);
		}
		return super.visitLiteral(ctx);
	}
}
