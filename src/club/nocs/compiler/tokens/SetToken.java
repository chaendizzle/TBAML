package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLCompileError;
import club.nocs.compiler.Token;

public class SetToken extends Token
{
	
	String rawA;
	String rawB;
	
	Token A;
	Token B;
	
	public SetToken(String rawA, String rawB, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.SET;
		this.rawA = rawA.trim();
		this.rawB = rawB.trim();
		
		// lex left and right
		A = Token.createToken(this.rawA, depth, compiler);
		B = Token.createToken(this.rawB, depth, compiler);
		// var or glob var on left
		if (A.type != TokenType.VAR && A.type != TokenType.GLOB_VAR)
		{
			throw new TBAMLCompileError(
					"left statement of '=' expression '" + rawA + "' is a '" + A.type.name()
							+ "' expression, not a var or global var.");
		}
		// value on right
		if (B.statement != StatementType.VALUE)
		{
			throw new TBAMLCompileError(
					"right statement of '=' expression '" + rawB + "' is a '" + B.statement.name()
							+ "' expression, not a value expression.");
		}
	}
	
	@Override
	public boolean collect()
	{
		// set is single-line
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
		// assign value of B to A
		((VarToken) A).assign(interpreter, (String) B.evaluate(interpreter));
		return null;
	}
	
}
