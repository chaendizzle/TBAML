package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class StartRoomToken extends Token
{
	String room;
	
	public StartRoomToken(String room, int depth, Compiler compiler)
	{
		super(depth, compiler);
		this.room = room;
		compiler.start = this.room;
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
		return interpreter.start = room;
	}
	
}
