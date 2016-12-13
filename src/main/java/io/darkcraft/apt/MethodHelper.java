package io.darkcraft.apt;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import com.google.common.base.Joiner;

public class MethodHelper
{
	public static String getParametersNames(ExecutableElement method)
	{
		List<String> variables = new ArrayList<>();
		for(VariableElement v : method.getParameters())
			variables.add(v.getSimpleName().toString());
		return Joiner.on(", ").join(variables);
	}
	
	public static String getFirstParameter(ExecutableElement method)
	{
		for(VariableElement v : method.getParameters())
			return v.getSimpleName().toString();
		throw new RuntimeException("No first parameter could be found");
	}
}
