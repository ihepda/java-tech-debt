package io.github.ihepda.techdebt.javaparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import io.github.ihepda.techdebt.TechDebt;
import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.utils.InternalLogger;
import io.github.ihepda.techdebt.utils.InternalLoggerAware;

public class SourcesNavigator implements InternalLoggerAware {

	private InternalLogger logger;
	
    private Pattern commentIdpattern = Pattern.compile("@TD-([^\\s]+)");
    private Pattern endCommentIdpattern = Pattern.compile("#TD-([^\\s]+)");

	
	@Override
	public void setInternalLogger(InternalLogger logger) {
        this.logger = logger;
    }
	
	public void navigate(Path... paths) {
		
	}
	
	private void logProblems(ParseResult<CompilationUnit> parseResult) {
        parseResult.getProblems().forEach(problem -> logger.error("Error parsing file: {}", problem));
    }
	
	private String resolveElementAt(Node n) {
		if(n instanceof ClassOrInterfaceDeclaration cid) {
			return cid.isInterface() ? "INTERFACE" : "CLASS" + " ==> " + cid.getFullyQualifiedName().orElse("");
		} else if(n instanceof FieldDeclaration fd) {
            return "FIELD" + " ==> " + resolveElementAt(fd);
		} else if (n instanceof ConstructorDeclaration cd) {
			return "CONSTRUCTOR" + " ==> " + cd.getDeclarationAsString(true, true, true);
		} else if (n instanceof MethodDeclaration md) {
			return "METHOD" + " ==> " + md.getDeclarationAsString(true, true, true);
		} else if (n instanceof VariableDeclarationExpr vde) {
			return "LOCAL VARIABLE" + " ==> " + resolveElementAt(vde);
		} else if (n instanceof PackageDeclaration pd) {
			return "PACKAGE" + " ==> " + pd.getNameAsString();
		}
		logger.warn("Unknown node type: {}", n.getClass().getName());
		
		return "";
	}
	
	private String resolveElementAt(VariableDeclarationExpr vde) {
		StringBuilder sb = new StringBuilder();
		VariableDeclarator variable = vde.getVariable(0);
		sb.append(variable.getTypeAsString());
		sb.append(" ");
		sb.append(variable.getNameAsString());
		Optional<Expression> initializer = variable.getInitializer();
		if(initializer.isPresent()) {
            sb.append(" = ");
            sb.append(initializer.get());
		}
		return sb.toString();
	}
	
	private String resolveElementAt(FieldDeclaration fd) {
		StringBuilder sb = new StringBuilder();
		if (fd.isPublic()) {
			sb.append("public ");
		} else if (fd.isPrivate()) {
			sb.append("private ");
		} else if (fd.isProtected()) {
			sb.append("protected ");
		}
		if (fd.isStatic()) {
			sb.append("static ");
		}
		if (fd.isFinal()) {
			sb.append("final ");
		}
		sb.append(fd.getElementType().asString());
		sb.append(" ");
		VariableDeclarator variable = fd.getVariable(0);
		sb.append(variable.getNameAsString());
		Optional<Expression> initializer = variable.getInitializer();
		if(initializer.isPresent()) {
            sb.append(" = ");
            sb.append(initializer.get());
		}
		return sb.toString();
	}
	
	private void registerTechDebt(NormalAnnotationExpr n, TechDebtDatas techDebts) {
		TechDebtElement techDebtElement = new TechDebtElement();
		
		Optional<Position> begin = n.getBegin();
		techDebtElement.setLineNumber(begin.orElse(new Position(-1,-1)).line);
		Optional<Node> parentNode = n.getParentNode();
		if(parentNode.isPresent()) {
            Node node = parentNode.get();
            
			techDebtElement.setElementAt(resolveElementAt(node));
        }
		
		n.getPairs().forEach(p -> {
			String name = p.getName().asString();
			Expression value = p.getValue();
			if (name.equals("severity")) {
				techDebtElement.setSeverity(TechDebt.Severity.valueOf(((FieldAccessExpr)value).getName().getIdentifier()));
			} else if (name.equals("type")) {
				techDebtElement.setType(TechDebt.Type.valueOf(((FieldAccessExpr)value).getName().getIdentifier()));
			} else if (name.equals("comment")) {
				techDebtElement.setComment(value.toString());
			} else if (name.equals("effort")) {
				techDebtElement.setEffort(TechDebt.Effort.valueOf(((FieldAccessExpr)value).getName().getIdentifier()));
			} else if (name.equals("author")) {
				techDebtElement.setAuthor(value.toString());
			} else if (name.equals("date")) {
				techDebtElement.setDate(value.toString());
			} else if (name.equals("solution")) {
				techDebtElement.setSolution(value.toString());
			} else if (name.equals("refComment")) {
				techDebtElement.setRefComment(value.toString());
			}
		});
		techDebts.elements().add(techDebtElement);
	}
	
	private void resolveComment(BlockComment n, TechDebtDatas techDebts) {
		String input = n.getContent();
		if (input == null || input.trim().length() == 0 || !input.contains("@TD-")) {
			return;
		}
		AtomicInteger i = new AtomicInteger(0);
		String first = input.lines().filter(l-> { //NOSONAR
			i.incrementAndGet();
			return !l.trim().isEmpty();
		}).findFirst().get();//NOSONAR
		
		first = first.replaceAll("[\\s\\*]+", "");
		Matcher matcher = commentIdpattern.matcher(first);
		String code = null; 
		if (matcher.matches()) {
            code = matcher.group(1);
        } else {
        	return;
        }
		long count = input.lines().count()-i.intValue();
		input = input.lines().skip(i.intValue()).limit(count-1)
                .map(line -> line.replaceAll("^[\\s\\*]+", ""))
                .reduce((line1, line2) -> line1 + "\n" + line2)
                .orElse("");
		Optional<Position> begin = n.getEnd();
		int line = begin.orElse(new Position(-1,-1)).line;
		line = line == -1? line: line+1;
		CommentInfo ci = CommentInfo.of(input, line);
		techDebts.refComments().put(code, ci);
	}
	
	public void resolveLineComment(LineComment n, TechDebtDatas techDebts) {
		String content = n.getContent();
		content = content.replaceAll("^[\\s\\/]+", "");
		if (content.startsWith("#TD-")) {
			Matcher matcher = endCommentIdpattern.matcher(content);
			String code = null;
			if (matcher.matches()) {
				code = matcher.group(1);
			} else {
				return;
			}
			CommentInfo ci = techDebts.refComments().get(code);
			if (ci != null) {
				Optional<Position> begin = n.getBegin();
				int line = begin.orElse(new Position(-1,-1)).line;
				line = line == -1 ? line : line-1;
				ci.endLine().set(line);
			}
		}
	}
	
	public void navigate(Path path) throws IOException {
		SourceRoot sr = new SourceRoot(path);
		sr.getParserConfiguration().setDoNotAssignCommentsPrecedingEmptyLines(true);
		List<ParseResult<CompilationUnit>> tryToParse = sr.tryToParse();
        TechDebtDatas techDebtDatas = TechDebtDatas.newInstance();	
		for (ParseResult<CompilationUnit> parseResult : tryToParse) {
			if(!parseResult.isSuccessful()) {
				this.logProblems(parseResult);
				continue;
			}
            CompilationUnit result = parseResult.getResult().get(); //NOSONAR
            VoidVisitorAdapter<TechDebtDatas> va = new VoidVisitorAdapter<>() {
            	@Override
            	public void visit(NormalAnnotationExpr n, TechDebtDatas arg) {
            		registerTechDebt(n, arg);
            		super.visit(n, arg);
            	}
            	@Override
            	public void visit(BlockComment n, TechDebtDatas arg) {
            		resolveComment(n, arg);
            		super.visit(n, arg);
            	}
            	@Override
            	public void visit(LineComment n, TechDebtDatas arg) {
            		resolveLineComment(n, arg);
            		super.visit(n, arg);
            	}
            	
            		
            };
            va.visit(result, techDebtDatas);
            Optional<CommentsCollection> commentsCollection = parseResult.getCommentsCollection();
            if (commentsCollection.isPresent()) {
				commentsCollection.get().getLineComments().forEach(lc -> {
					if(!lc.isOrphan()) {
						return;
					}
					if(lc.isBlockComment()) {
						resolveComment(lc.asBlockComment(), techDebtDatas);
					}
					if(lc.isLineComment()) {
						resolveLineComment(lc.asLineComment(), techDebtDatas);
					}
				});
            }
            this.adjustComments(techDebtDatas, path);
            
		}
		
	    techDebtDatas.elements().forEach(td -> logger.info(td.toString()));
	    techDebtDatas.refComments().entrySet().forEach(td -> logger.info(td.toString()));
	}
	
    public static String extractSourceTextBetweenLines(Path path, int startLine, int endLine) throws IOException {
    	if(endLine < startLine) {
    		endLine = startLine;
    	}
        List<String> lines = Files.readAllLines(path);
        StringBuilder sb = new StringBuilder();

        for (int i = startLine - 1; i < endLine; i++) {
            sb.append(lines.get(i)).append(System.lineSeparator());
        }

        return sb.toString();
    }

	
	private void adjustComments(TechDebtDatas techDebts, Path path) throws IOException {
		
		Map<String, CommentInfo> refComments = techDebts.refComments();
		techDebts.elements().forEach(td -> {
			String refComment = td.getComment();
			if(refComment != null && !refComment.isEmpty()) {
                CommentInfo ci = refComments.get(refComment);
                if(ci != null) {
                	td.setLineNumber(ci.startline());
                	try {
						td.setElementAt(extractSourceTextBetweenLines(path, ci.startline(), ci.endLine().get()));
					} catch (IOException e) {
						logger.error("Error reading file: {}", path, e);
					}
                }
		}
		});
	}
}
