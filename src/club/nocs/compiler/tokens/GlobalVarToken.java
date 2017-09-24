package club.nocs.compiler.tokens;

import club.nocs.compiler.Compiler;
import club.nocs.compiler.Interpreter;
import club.nocs.compiler.TBAMLRuntimeError;

public class GlobalVarToken extends VarToken
{
	public GlobalVarToken(String var, int depth, Compiler compiler)
	{
		super(var, depth, compiler);
		type = TokenType.GLOB_VAR;
		// no lexing needed
	}
	
	@Override
	public Object evaluate(Interpreter interpreter)
	{
		if (interpreter.global.containsKey(var))
		{
			return interpreter.global.get(var);
		}
		throw new TBAMLRuntimeError("Global variable '" + var + "' was not initialized.");
	}
	
	// assigns a global variable
	@Override
	public void assign(Interpreter interpreter, String value)
	{
		interpreter.global.put(var, value);
	}
}
