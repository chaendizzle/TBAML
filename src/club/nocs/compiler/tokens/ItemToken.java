package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;

public class ItemToken extends DeclarationToken
{
	String name;
	String desc;
	
	public ItemToken(String identifier, int depth, Compiler compiler)
	{
		super(TokenType.ITEM, identifier, depth, compiler);
	}
}
