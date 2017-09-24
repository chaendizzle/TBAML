package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;

public class ParseTree extends DeclarationToken
{
	public ParseTree(Compiler compiler)
	{
		super("", 0, compiler);
	}
	
	@Override
	public boolean collect()
	{
		return true;
	}
}
