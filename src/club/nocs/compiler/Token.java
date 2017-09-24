package club.nocs.compiler;

import club.nocs.compiler.tokens.AndToken;
import club.nocs.compiler.tokens.BracketToken;
import club.nocs.compiler.tokens.DeclarationEndToken;
import club.nocs.compiler.tokens.DeclarationToken;
import club.nocs.compiler.tokens.ElseToken;
import club.nocs.compiler.tokens.EqualsToken;
import club.nocs.compiler.tokens.EventToken;
import club.nocs.compiler.tokens.ExitToken;
import club.nocs.compiler.tokens.GlobalVarToken;
import club.nocs.compiler.tokens.IfToken;
import club.nocs.compiler.tokens.InvAddToken;
import club.nocs.compiler.tokens.InvHasToken;
import club.nocs.compiler.tokens.InvRemoveToken;
import club.nocs.compiler.tokens.InvToken;
import club.nocs.compiler.tokens.ItemToken;
import club.nocs.compiler.tokens.MoveToken;
import club.nocs.compiler.tokens.NotEqualsToken;
import club.nocs.compiler.tokens.NotToken;
import club.nocs.compiler.tokens.OrToken;
import club.nocs.compiler.tokens.PrintToken;
import club.nocs.compiler.tokens.RoomToken;
import club.nocs.compiler.tokens.SetToken;
import club.nocs.compiler.tokens.StartRoomToken;
import club.nocs.compiler.tokens.StringToken;
import club.nocs.compiler.tokens.VarToken;

public abstract class Token
{
	// which type the token is
	public TokenType type = TokenType.NONE;
	public StatementType statement = StatementType.NONE;
	
	public int depth = 0;
	
	public Compiler compiler;
	
	public enum TokenType
	{
		NONE, PRINT, GLOB_VAR, VAR, IF, ELSE, INV, INV_HAS, INV_ADD, INV_REMOVE, NOT, EQUAL, NOTEQUAL, EVENT, AND, OR, STRING, SET, NOUN, VERB, ADV, ADJ, PREP, ROOM, ITEM, MOVE, OP_BRACKET, CL_BRACKET, NAME, DESC, EXIT;
	}
	
	public enum StatementType
	{
		NONE, BOOLEAN, VALUE, DECL;
	}
	
	public Token(int depth, Compiler compiler)
	{
		this.depth = depth;
		this.compiler = compiler;
	}
	
	// pass in lines using collect() until the Token decides it's time to stop
	// will return false when it's time to stop
	// some one-liners are already collected(), so check this first right after
	// createToken()
	public abstract boolean collect();
	
	// collect more lines until collect() is false
	// returns the number of lines to go back up if we overshot
	public abstract boolean collect(String line);
	
	// parses the collection of strings passed in, initializing the token
	public abstract void parse();
	
	// evaluate the given expression
	public abstract Object evaluate(Interpreter interpreter);
	
	// pass in the line (or the remaining bit) to determine token type.
	// it will return a token of the correct type for collect(line) and parse()
	public static Token createToken(String line, int depth, Compiler compiler)
	{
		String trimmed = line.trim();
		// check for string
		if (trimmed.charAt(0) == '"' && trimmed.charAt(trimmed.length() - 1) == '"')
		{
			// string value without quotes
			return new StringToken(trimmed.substring(1, trimmed.length() - 1), depth, compiler);
		}
		String[] parts = line.split(" ", 2);
		switch (parts[0].trim())
		{
			case "if":
				return new IfToken(parts[1], depth, compiler);
		}
		if (parts[0].trim() == "not")
		{
			return new NotToken(parts[1], depth, compiler);
		}
		// check for and, or, == and has
		if (line.contains(" and "))
		{
			// split by first and
			parts = line.split(" and ", 2);
			return new AndToken(parts[0], parts[1], depth, compiler);
		}
		if (line.contains(" or "))
		{
			parts = line.split(" or ", 2);
			return new OrToken(parts[0], parts[1], depth, compiler);
		}
		if (line.contains("=="))
		{
			parts = line.split("==", 2);
			return new EqualsToken(parts[0], parts[1], depth, compiler);
		}
		if (line.contains("!="))
		{
			parts = line.split("!=", 2);
			return new NotEqualsToken(parts[0], parts[1], depth, compiler);
		}
		if (line.contains(" has "))
		{
			parts = line.split(" has ", 2);
			return new InvHasToken(parts[0], parts[1], depth, compiler);
		}
		if (line.contains("="))
		{
			parts = line.split("=", 2);
			return new SetToken(parts[0], parts[1], depth, compiler);
		}
		// take the first word
		switch (parts[0].trim())
		{
			case "start":
				String[] start = parts[1].split(" ", 2);
				if (start[0].equals("room"))
				{
					return new StartRoomToken(start[1], depth, compiler);
				}
				break;
			case "exits":
				return new ExitToken(parts.length > 1 ? parts[1].trim() : "", depth, compiler);
			case "{":
				return new BracketToken(TokenType.OP_BRACKET, depth, compiler);
			case "}":
				return new BracketToken(TokenType.CL_BRACKET, depth, compiler);
			case "name":
				return new DeclarationEndToken(TokenType.NAME, parts[1].trim(), depth, compiler);
			case "desc":
				return new DeclarationEndToken(TokenType.DESC, parts[1].trim(), depth, compiler);
			case "room":
				return new RoomToken(parts[1].trim(), depth, compiler);
			case "item":
				return new ItemToken(parts[1].trim(), depth, compiler);
			case "verb":
				return new DeclarationToken(TokenType.VERB, parts[1].trim().split(","), depth, compiler);
			case "noun":
				return new DeclarationToken(TokenType.NOUN, parts[1].trim().split(","), depth, compiler);
			case "adj":
				return new DeclarationToken(TokenType.ADJ, parts[1].trim().split(","), depth, compiler);
			case "prep":
				return new DeclarationToken(TokenType.PREP, parts[1].trim().split(","), depth, compiler);
			case "adv":
				return new DeclarationToken(TokenType.ADV, parts[1].trim().split(","), depth, compiler);
			case "move":
				return new MoveToken(parts[1], depth, compiler);
			case "print":
				return new PrintToken(parts[1], depth, compiler);
			case "glob":
				return new GlobalVarToken(parts[1], depth, compiler);
			case "else":
				return new ElseToken(parts.length > 1 ? parts[1].trim() : "", depth, compiler);
			case "inventory":
				return new InvToken(parts.length > 1 ? parts[1].trim() : "", depth, compiler);
			case "add":
				// add ITEM to inventory A
				String[] partsAdd = parts[1].split("to", 2);
				return new InvAddToken(partsAdd[1], partsAdd[0], depth, compiler);
			case "remove":
				// remove ITEM from inventory A
				String[] partsRemove = parts[1].split("from", 2);
				return new InvRemoveToken(partsRemove[1], partsRemove[0], depth, compiler);
			case "event":
				return new EventToken(parts[1], depth, compiler);
		}
		// if one word, then var or string
		line = line.trim();
		if (!line.contains(" "))
		{
			// if depth is 0, then we're global
			if (depth == 0)
			{
				return new GlobalVarToken(line, depth, compiler);
			}
			// var name
			return new VarToken(line, depth, compiler);
		}
		// worst case, it's not recognized
		throw new TBAMLCompileError("Invalid syntax: '" + line + "'.");
	}
}