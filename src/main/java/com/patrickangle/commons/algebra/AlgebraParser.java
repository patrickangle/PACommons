/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.algebra;

import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * An {@code AlgebraParser} provides a framework around Java's native
 * scripting engine support to specifically handle mathematical equations
 * and expressions.
 * @author Patrick Angle
 * @since 07/06/18
 * @version 07/06/18
 */
public class AlgebraParser {
    private static final ScriptEngineManager manager = new ScriptEngineManager();
    private static final ScriptEngine engine = manager.getEngineByName("nashorn");
    
    static {
        try {
            engine.eval("E = Math.E");              // Euler's constant and the base of natural logarithms, approximately 2.718.
            engine.eval("LN2 = Math.LN2");          // Natural logarithm of 2, approximately 0.693.
            engine.eval("LN10 = Math.LN10");        // Natural logarithm of 10, approximately 2.303.
            engine.eval("LOG2E = Math.LOG2E");      // Base 2 logarithm of E, approximately 1.443.
            engine.eval("LOG10E = Math.LOG10E");    // Base 10 logarithm of E, approximately 0.434.
            engine.eval("PI = Math.PI");            // Ratio of the circumference of a circle to its diameter, approximately 3.14159.
            engine.eval("SQRT1_2 = Math.SQRT1_2");  // Square root of 1/2; equivalently, 1 over the square root of 2, approximately 0.707.
            engine.eval("SQRT2 = Math.SQRT2");      // Square root of 2, approximately 1.414.
            
            engine.eval("abs = Math.abs");          // Returns the absolute value of a number.
            engine.eval("acos = Math.acos");        // Returns the arccosine of a number.
            engine.eval("acosh = Math.acosh");      // Returns the hyperbolic arccosine of a number.
            engine.eval("asin = Math.asin");        // Returns the arcsine of a number.
            engine.eval("asinh = Math.asinh");      // Returns the hyperbolic arcsine of a number.
            engine.eval("atan = Math.atan");        // Returns the arctangent of a number.
            engine.eval("atanh = Math.atanh");      // Returns the hyperbolic arctangent of a number.
            engine.eval("atan2 = Math.atan2");      // Returns the arctangent of the quotient of its arguments.
            engine.eval("cbrt = Math.cbrt");        // Returns the cube root of a number.
            engine.eval("ceil = Math.ceil");        // Returns the smallest integer greater than or equal to a number.
            engine.eval("clz32 = Math.clz32");      // Returns the number of leading zeroes of a 32-bit integer.
            engine.eval("cos = Math.cos");          // Returns the cosine of a number.
            engine.eval("cosh = Math.cosh");        // Returns the hyperbolic cosine of a number.
            engine.eval("exp = Math.exp");          // Returns E^x, where x is the argument, and E is Euler's constant (2.718…), the base of the natural logarithm.
            engine.eval("expm1 = Math.expm1");      // Returns subtracting 1 from exp(x).
            engine.eval("floor = Math.floor");      // Returns the largest integer less than or equal to a number.
            engine.eval("fround = Math.fround");    // Returns the nearest single precision float representation of a number.
            engine.eval("hypot = Math.hypot");      // Returns the square root of the sum of squares of its arguments.
            engine.eval("imul = Math.imul");        // Returns the result of a 32-bit integer multiplication.
            engine.eval("log = Math.log");          // Returns the natural logarithm (log sub e, also ln) of a number.
            engine.eval("log1p = Math.log1p");      // Returns the natural logarithm (log sub e, also ln) of 1 + x for a number x.
            engine.eval("log10 = Math.log10");      // Returns the base 10 logarithm of a number.
            engine.eval("log2 = Math.log2");        // Returns the base 2 logarithm of a number.
            engine.eval("max = Math.max");          // Returns the largest of zero or more numbers.
            engine.eval("min = Math.min");          // Returns the smallest of zero or more numbers.
            engine.eval("pow = Math.pow");          // Returns x to the y power, that is, x^y.
            engine.eval("random = Math.random");    // Returns a pseudo-random number between 0 and 1.
            engine.eval("round = Math.round");      // Returns the value of a number rounded to the nearest integer.
            engine.eval("sign = Math.sign");        // Returns the sign of the x, indicating whether x is positive, negative or zero.
            engine.eval("sin = Math.sin");          // Returns the sine of a number.
            engine.eval("sinh = Math.sinh");        // Returns the hyperbolic sine of a number.
            engine.eval("sqrt = Math.sqrt");        // Returns the positive square root of a number.
            engine.eval("tan = Math.tan");          // Returns the tangent of a number.
            engine.eval("tanh = Math.tanh");        // Returns the hyperbolic tangent of a number.
            engine.eval("trunc = Math.trunc");      // Returns the integer part of the number x, removing any fractional digits.
            
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Evaluate the given expression (in ECMAScript 2015) with the
     * provided variables. Math functions and constants are mapped into
     * the global context for convenience.
     * 
     * @param formula {@code String} the ECMAScript 2015 expression to
     * be evaluated
     * @param variables {@code Map<String, Object>} a map of values as
     * {@code Object}s to assign to variables with the same name as
     * their {@code String} keys.
     * 
     * @return {@code Object} the result of the evaluation
     * 
     * @throws ScriptException If the script could not be executed, a
     * {@code ScriptException} will be thrown providing details of why
     * the script could not be executed.
     */
    public static Number evaluate(String expression, Map<String, Object> variables) throws ScriptException {
        Bindings bindings = engine.createBindings();
        bindings.putAll(variables);
        return (Number) engine.eval(expression, bindings);
    }
    
//    private static String convertInferredMultiplication(String expression) {
//        return expression.replaceAll(" ", "*").replaceAll("\\)\\(", "\\)*\\(");
//    }
//    
//    private static String convertMathmaticalOperations(String expression) {
//        // Function replacement
//        String returnExpression = expression.replaceAll("abs", "Math.abs");
//        returnExpression = returnExpression.replaceAll("acos", "Math.acos");
//        returnExpression = returnExpression.replaceAll("asin", "Math.asin");
//        returnExpression = returnExpression.replaceAll("atan", "Math.atan");
//        returnExpression = returnExpression.replaceAll("atan2", "Math.atan2");
//        returnExpression = returnExpression.replaceAll("ceil", "Math.ceil");
//        returnExpression = returnExpression.replaceAll("cos", "Math.cos");
//        returnExpression = returnExpression.replaceAll("exp", "Math.exp");
//        returnExpression = returnExpression.replaceAll("floor", "Math.floor");
//        returnExpression = returnExpression.replaceAll("log", "Math.log");
//        returnExpression = returnExpression.replaceAll("max", "Math.max");
//        returnExpression = returnExpression.replaceAll("min", "Math.min");
//        returnExpression = returnExpression.replaceAll("pow", "Math.pow");
//        returnExpression = returnExpression.replaceAll("random", "Math.random");
//        returnExpression = returnExpression.replaceAll("round", "Math.round");
//        returnExpression = returnExpression.replaceAll("sin", "Math.sin");
//        returnExpression = returnExpression.replaceAll("sqrt", "Math.sqrt");
//        returnExpression = returnExpression.replaceAll("tan", "Math.tan");
//        
//        // Constant replacement
//        returnExpression = returnExpression.replaceAll("LN2", "Math.LN2");
//        returnExpression = returnExpression.replaceAll("LN10", "Math.LN10");
//        returnExpression = returnExpression.replaceAll("LOG2E", "Math.LOG2E");
//        returnExpression = returnExpression.replaceAll("LOG10E", "Math.LOG10E");
//        returnExpression = returnExpression.replaceAll("PI", "Math.PI");
//        returnExpression = returnExpression.replaceAll("SQRT1_2", "Math.SQRT1_2");
//        returnExpression = returnExpression.replaceAll("SQRT2", "Math.SQRT2");
//        returnExpression = returnExpression.replaceAll("E", "Math.E");
//        
//        return returnExpression;
//    }
}
