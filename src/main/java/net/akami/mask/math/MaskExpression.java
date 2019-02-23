package net.akami.mask.math;

import net.akami.mask.exception.MaskException;

import java.util.ArrayList;

/**
 * MaskExpression is the core object of the mask library. It handles a String, which corresponds to the expression,
 * and an array of variables, used to solve the expression for values, or to provide images of the function.
 *
 * It is a mutable class, hence the expression can be modified. When doing some calculations with an expression, you
 * will be asked to choose the original expression (in) plus the expression that will be affected by the
 * calculation(out).
 */
public class MaskExpression {

    /**
     * Temporary instance that can be used when only the result of an expression is needed, to avoid multiple instances.
     * <br/>
     * Be aware that once you did a calculation with it by setting it as the out parameter, if you
     * want the int value of TEMP for instance, you need to call {@link MaskOperator#asExpression(MaskExpression)}
     * and not {@link MaskOperator#asExpression()}, otherwise you'll get the non-temporary expression
     * you based yourself on for the calculation.
     * <br/>
     * Here is an example :
     *
     * <pre>
     * MaskExpression base = new MaskExpression(2x);
     * MaskOperator operator = MaskOperator.begin(base);
     * String exp = operator.imageFor(MaskExpression.TEMP, false, 5).asExpression();
     * System.out.println(exp);
     *
     * Output : "2x"
     *
     * -----------------
     *
     * MaskExpression base = new MaskExpression(2x);
     * MaskOperator operator = MaskOperator.begin(base);
     * String exp = operator.imageFor(MaskExpression.TEMP, false, 5).asExpression(MaskExpression.TEMP);
     * System.out.println(exp);
     *
     * Output : "10"
     * </pre>
     */
    public static final MaskExpression TEMP = new MaskExpression();

    private String expression;
    private char[] variables;

    /**
     * Constructs a new MaskExpression without any string expression by default.
     */
    public MaskExpression() {
        this(null);
    }

    /**
     * Constructs a new MaskExpression from the given string.
     * @param expression the given string
     */
    public MaskExpression(String expression) {
        reload(expression);
    }

    private char[] createVariables() {
        String letters = expression.replaceAll("[\\d.+\\-*\\/^]+", "");
        ArrayList<Character> chars = new ArrayList<>();
        for(char c : letters.toCharArray()) {
            if(!chars.contains(c) && !MaskOperator.NON_VARIABLES.contains(""+c))
                chars.add(c);
        }
        char[] vars = new char[chars.size()];
        for(Character c : chars) {
            vars[chars.indexOf(c)] = c;
        }
        return vars;
    }

    public int getVariablesAmount() { return variables.length; }
    public String getExpression()   { return expression;       }
    public char[] getVariables()    { return variables;        }

    public void reload(String newExp) {
        if(newExp == null) {
            this.expression = "undefined";
            this.variables = new char[]{};
        } else {
            this.expression = newExp.replaceAll("\\s", "");
            checkExpressionValidity();
            this.variables = createVariables();
        }
    }

    private void checkExpressionValidity() {

        if(".*/^".contains(String.valueOf(expression.charAt(0)))
                || ".+-*/^".contains(String.valueOf(expression.charAt(expression.length()-1))))
            throw new MaskException("Expression not valid", this);
    }

    /**
     * @return the expression of the mask
     */
    @Override
    public String toString() {
        return expression;
    }
}
