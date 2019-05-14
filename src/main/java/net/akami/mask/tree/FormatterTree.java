package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;

public class FormatterTree extends CalculationTree<FormatterBranch> {

    public FormatterTree(String initial) {
        super(FormatterFactory.addMultiplicationSigns(
                ExpressionUtils.removeEdgeBrackets(initial, false), true), null);
    }

    @Override
    protected FormatterBranch generate(String origin) {
        return new FormatterBranch(origin);
    }

    @Override
    protected void evalBranch(FormatterBranch self) {

        String left = null;self.getLeftValue();
        String right = null;self.getRightValue();

        if(evalTrigonometry(self, left, right)) return;

        if(ExpressionUtils.hasHigherPriority(String.valueOf(self.getOperation()), right))
            right = addRequiredBrackets(right);

        if(ExpressionUtils.hasHigherPriority(String.valueOf(self.getOperation()), left))
            left = addRequiredBrackets(left);

        char operation = self.getOperation();

        if(operation == '*') {
            //self.setReducedValue(left + right);
        } else if(operation == '-' && left.equals("0")){
            //self.setReducedValue(operation + right);
        } else {
            //self.setReducedValue(left + operation + right);
        }
    }

    private String addRequiredBrackets(String self) {
        if(ExpressionUtils.isReduced(self))
            return self;
        return "(" + self + ")";
    }

    private boolean evalTrigonometry(Branch self, String left, String right) {
        char trigonometricSign = '$';
        String value = null;

        if(ExpressionUtils.isTrigonometricShortcut(left)) {
            trigonometricSign = left.charAt(0);
            value = right;
        } else if(ExpressionUtils.isTrigonometricShortcut(right)) {
            trigonometricSign = right.charAt(0);
            value = left;
        }

        if(trigonometricSign != '$') {
            switch (trigonometricSign) {
                case '@':
                    //self.setReducedValue("sin("+value+")");
                    break;
                case '#':
                    //self.setReducedValue("cos("+value+")");
                    break;
                case '§':
                    //self.setReducedValue("tan("+value+")");
            }
            return true;
        }
        return false;
    }
}
