package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLCompileError;
import club.nocs.compiler.Token;

public class EqualsToken extends Token
{
	String rawA;
	String rawB;
	
	Token A;
	Token B;
	
	public EqualsToken(String rawA, String rawB, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.EQUAL;
		statement = StatementType.BOOLEAN;
		this.rawA = rawA.trim();
		this.rawB = rawB.trim();
		
		// lex left and right
		A = Token.createToken(this.rawA, depth, compiler);
		B = Token.createToken(this.rawB, depth, compiler);
		// both must be the same type of expression
		if (A.statement != B.statement)
		{
			throw new TBAMLCompileError("Error in 'equals' statement: "
					+ "left statement '" + rawA + "' is of type '" + A.statement.name() + "' but "
					+ "right statement '" + rawB + "' is of type '" + B.statement.name() + "'.");
		}
		if (A.statement != StatementType.BOOLEAN && A.statement != StatementType.VALUE)
		{
			throw new TBAMLCompileError(
					"left statement of '==' expression '" + rawA + "' is a '" + A.statement.name()
							+ "' expression, not a boolean or value expression.");
		}
		if (B.statement != StatementType.BOOLEAN && B.statement != StatementType.VALUE)
		{
			throw new TBAMLCompileError(
					"right statement of '==' expression '" + rawB + "' is a '" + B.statement.name()
							+ "' expression, not a boolean or value expression.");
		}
	}
	
	@Override
	public boolean collect()
	{
		// and is single-line
		return false;
	}
	
	@Override
	public boolean collect(String line)
	{
		return false;
	}
	
	@Override
	public void parse()
	{
		// parse both
		A.parse();
		B.parse();
		// free strings
		rawA = null;
		rawB = null;
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		// equals
		return A.evaluate(interpreter).equals(B.evaluate(interpreter));
	}
}
