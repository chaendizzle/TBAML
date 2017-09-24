package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class EventToken extends Token
{
	String eventKey;
	
	public EventToken(String eventKey, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.EVENT;
		this.eventKey = eventKey;
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
		interpreter.runCustomEvent(eventKey);
		return null;
	}
}
