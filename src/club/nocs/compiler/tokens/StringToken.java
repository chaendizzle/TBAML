package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class StringToken extends Token
{
	String value;
	
	public StringToken(String value, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.STRING;
		statement = StatementType.VALUE;
		this.value = value;
		// no lexing needed
	}
	
	@Override
	public boolean collect()
	{
		// var set is single-line
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
		
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		return value;
	}
	
}
