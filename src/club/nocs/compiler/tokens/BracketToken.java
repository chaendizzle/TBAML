package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class BracketToken extends Token
{
	
	public BracketToken(TokenType type, int depth, Compiler compiler)
	{
		super(depth, compiler);
		this.type = type;
	}
	
	@Override
	public boolean collect()
	{
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
		return null;
	}
	
}
