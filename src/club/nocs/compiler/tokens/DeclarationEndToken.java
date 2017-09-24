package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;

// represents single line declarations
// like name or desc
public class DeclarationEndToken extends DeclarationToken
{
	
	public DeclarationEndToken(TokenType type, String identifier, int depth, Compiler compiler)
	{
		super(type, identifier, depth, compiler);
	}
	
	@Override
	public boolean collect()
	{
		// declaration end is single-line
		return false;
	}
	
	@Override
	public boolean collect(String line)
	{
		return false;
	}
}
