package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.Token;

public class InvToken extends Token
{
	String inv;
	
	public InvToken(String inv, int depth, Compiler compiler)
	{
		super(depth, compiler);
		type = TokenType.INV;
		if (inv.trim() != "")
		{
			this.inv = inv.trim();
		}
	}
	
	@Override
	public boolean collect()
	{
		// inventory is single-line
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
		if (inv == null)
		{
			return interpreter.playerInv;
		}
		return interpreter.rooms.get(inv).inventory;
	}
	
}
