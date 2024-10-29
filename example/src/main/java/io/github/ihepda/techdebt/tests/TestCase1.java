package io.github.ihepda.techdebt.tests;

import java.util.Map;

import io.github.ihepda.techdebt.TechDebt;
import io.github.ihepda.techdebt.TechDebt.Effort;
import io.github.ihepda.techdebt.TechDebt.Type;

@TechDebt(comment = "This is a test case")
public class TestCase1 {

	@TechDebt(comment = "field a bad", author = "me", severity = TechDebt.Severity.MAJOR)
	private int a = 10;

	@TechDebt(comment = "useless constructor", author = "me", severity = TechDebt.Severity.MINOR, type = Type.REMOVE)
	public TestCase1() {
		super();
		// TODO Auto-generated constructor stub
	}

	@TechDebt(comment = "bad constructor", author = "me", severity = TechDebt.Severity.MINOR, type = Type.PERFORMANCE)
	public TestCase1(int a) {
		super();
		this.a = a;
	}

	@TechDebt(comment = "strange method", author = "CDA", severity = TechDebt.Severity.TRIVIAL, type = Type.MAINTAINABILITY, effort = Effort.MASSIVE)
	public void execute(Map<String, Object> params) {
		@TechDebt(comment = "bad variable", author = "me", severity = TechDebt.Severity.MAJOR)
		var a = 10;
		var b = 20;

		/*
		 * @TD-1 
		 * This is a test comment
		 * 
		 * to check
		 */
		this.execute(null);
		// #TD-1
		this.execute(null);

	}

	@TechDebt(
			comment = "strange method2", 
			author = "CDA", 
			severity = TechDebt.Severity.TRIVIAL, 
			type = Type.MAINTAINABILITY, 
			effort = Effort.MASSIVE,
			refComment = "AX")
	public void execute2(Map<String, Object> params) {
		@TechDebt(comment = "bad variable", author = "me", severity = TechDebt.Severity.MAJOR)
		var a = 10;
		var b = 20;

		/*
		 * @TD-AX 
		 * This is a test comment for AX
		 * 
		 * to check
		 */
		this.execute(null);
		// #TD-AX
		this.execute(null);

	}

	@TechDebt(comment = "strange method3", author = "CDA", severity = TechDebt.Severity.TRIVIAL, type = Type.MAINTAINABILITY, effort = Effort.MASSIVE, allElement = true)
	public void execute3(Map<String, Object> params) {
		this.execute(null);
		//pippo
		this.execute(null);

	}
	
	@TechDebt(author = "CDA", severity = TechDebt.Severity.TRIVIAL, type = Type.MAINTAINABILITY, effort = Effort.MASSIVE, refComment = "CC")
	public void execute4(Map<String, Object> params) {
		
		/*
		 * @TD-CC
		 * My comment
		 * about 
		 * technical debt
		 */
		this.execute(null);
		// pippo
		this.execute(null);
		
		// #TD-CC

	}
	
	public void execute5(@TechDebt(author = "CDA", type = Type.SECURITY, effort = Effort.SMALL, comment = "To review with ConcurrentHashMap") Map<String, Object> params) {
		this.execute(null);
		// pippo
		this.execute(null);


	}

}
