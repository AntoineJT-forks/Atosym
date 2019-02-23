package net.akami.mask.utils;

import java.util.List;

import net.akami.mask.math.OperationSign;
import net.akami.mask.math.Tree;
import net.akami.mask.math.Tree.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReducerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReducerFactory.class.getName());
    public static final OperationSign[] OPERATIONS;

    static {
        OPERATIONS = new OperationSign[]{
                OperationSign.SUM, OperationSign.SUBTRACT,
                OperationSign.MULT, OperationSign.DIVIDE,
                OperationSign.POW, OperationSign.NONE
        };
    }

    public static String reduce(String exp) {
        long time = System.nanoTime();

        Tree tree = new Tree();

        // deletes all the spaces
        String localExp = exp.replaceAll("\\s", "");

        tree.new Branch(localExp);
        LOGGER.debug("Initial branch added : {}", tree.getBranches().get(0));

        TreeUtils.printBranches(tree);
        LOGGER.debug("Now merging branches");
        String result;
        try {
            result = TreeUtils.mergeBranches(tree);
        } catch (ArithmeticException | NumberFormatException e) {
            if(e instanceof ArithmeticException)
                LOGGER.error("Non solvable mathematical expression given : {}", exp);
            else
                LOGGER.error("Wrong format in the expression {}", exp);
            result = "undefined";
        }

        float deltaTime = (System.nanoTime() - time) / 1000000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return result;
    }

    public static boolean isSurroundedByParentheses(int index, String exp) {

        int leftParenthesis = 0;

        for(int i = 0; i < exp.length(); i++) {
            if(exp.charAt(i) == '(') {
                leftParenthesis++;
            }

            if(exp.charAt(i) == ')') {
                leftParenthesis--;
            }
            if(leftParenthesis > 0 && i == index) {
                LOGGER.debug("- Indeed surrounded");
                return true;
            }
        }
        LOGGER.debug("- Not surrounded");
        return false;
    }

}
