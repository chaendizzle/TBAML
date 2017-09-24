package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLCompileError;
import club.nocs.compiler.Token;

public class AndToken extends Token
{
	String rawA;
	String rawB;
	
	Token A;
	Token B;
	
	public AndToken(String rawA, String rawB, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.AND;
		statement = StatementType.BOOLEAN;
		this.rawA = rawA.trim();
		this.rawB = rawB.trim();
		
		// lex left and right
		A = Token.createToken(this.rawA, depth, compiler);
		B = Token.createToken(this.rawB, depth, compiler);
		// both must be boolean expressions
		if (A.statement != StatementType.BOOLEAN)
		{
			throw new TBAMLCompileError(
					"left statement of 'and' expression '" + rawA + "' is a '" + A.statement.name()
							+ "' expression, not a boolean expression.");
		}
		if (B.statement != StatementType.BOOLEAN)
		{
			throw new TBAMLCompileError(
					"right statement of 'and' expression '" + rawB + "' is a '" + B.statement.name()
							+ "' expression, not a boolean expression.");
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
		// and
		return (boolean) A.evaluate(interpreter) && (boolean) B.evaluate(interpreter);
	}
}
