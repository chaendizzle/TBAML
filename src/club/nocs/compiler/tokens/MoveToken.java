package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class MoveToken extends Token
{
	String move;
	
	public MoveToken(String move, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.MOVE;
		this.move = move;
		// no lexing needed
	}
	
	@Override
	public boolean collect()
	{
		// event is single-line
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
		// no parsing needed
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		interpreter.movePlayer(move);
		return null;
	}
}
