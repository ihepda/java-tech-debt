package io.github.ihepda.techdebt.filter;

import java.util.ArrayDeque;
import java.util.Deque;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import io.github.ihepda.techdebt.filter.parser.FilterLexer;
import io.github.ihepda.techdebt.filter.parser.FilterParser;
import io.github.ihepda.techdebt.filter.parser.FilterParser.ExprContext;
import io.github.ihepda.techdebt.filter.parser.FilterParser.IdentifierContext;
import io.github.ihepda.techdebt.filter.parser.FilterParser.ProgramContext;
import io.github.ihepda.techdebt.filter.parser.FilterParser.StringLiteralContext;
import io.github.ihepda.techdebt.filter.parser.FilterParserBaseVisitor;
import io.github.ihepda.techdebt.utils.StringUtils;

public class FilterParserVisitor extends FilterParserBaseVisitor<FilterOperation> {
	
	private Deque<FilterOperation> stack = new ArrayDeque<>();
	
	private FilterOperation result;
	
	public static FilterOperation parse(String filter) {
		FilterLexer lexer = new FilterLexer(CharStreams.fromString(filter));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FilterParser parser = new FilterParser(tokens);
		FilterParser.ProgramContext program = parser.program();
		FilterParserVisitor visitor = new FilterParserVisitor();
		FilterOperation filterOperation = visitor.visitProgram(program);
		return filterOperation;
	}
	
	@Override
	public FilterOperation visitProgram(ProgramContext ctx) {
		FilterOperation visitProgram = super.visitProgram(ctx);
		this.result = visitProgram;
		return visitProgram;
	}
	
	public FilterOperation getResult() {
		return result;
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
		Token neg = ctx.neg;
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
		case FilterParser.LIKE:
			result = new LikeOperation();
			break;
			
		case FilterParser.IN:
			result = new InOperation();
			break;
		default:
		}
		
		if (result != null ) {
			result.setNot(neg != null);
		}
		
		
		return result;
	}
	
	@Override
	protected FilterOperation defaultResult() {
		
		FilterOperation peek = this.stack.peek();
		if(result == null) {
			result = peek;
		}
		if(peek == null) {
			peek = result;
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
		FilterElement element = FilterElement.fromString(ctx.getText());
		FilterOperation operation = stack.peek();
		if(operation instanceof CompareOperation co) {
			co.setFilterElement(element);
		}
		
		return super.visitIdentifier(ctx);
	}

	@Override
	public FilterOperation visitStringLiteral(StringLiteralContext ctx) {
		String text = ctx.getText();
		text = StringUtils.decode(text);
		FilterOperation operation = stack.peek();
		if(operation instanceof CompareOperation co) {
			co.setLiteral(text);
		}
		return super.visitStringLiteral(ctx);
	}
}
