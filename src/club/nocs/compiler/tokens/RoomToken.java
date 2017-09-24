package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;

public class RoomToken extends DeclarationToken
{
	String name;
	String desc;
	
	public RoomToken(String identifier, int depth, Compiler compiler)
	{
		super(TokenType.ROOM, identifier, depth, compiler);
	}
}
