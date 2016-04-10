package com.sylvanaar.appcode.swift.highlighter;

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.jetbrains.swift.codeinsight.resolve.binary.BinaryNode;
import com.jetbrains.swift.codeinsight.resolve.binary.Node;
import com.jetbrains.swift.psi.*;

/**
 * Created by sylvanaar on 3/15/16.
 */
public class SwiftReadWriteAccessDetector extends ReadWriteAccessDetector {
    @Override
    public boolean isReadWriteAccessible(PsiElement psiElement) {
        return psiElement instanceof SwiftVariable || psiElement instanceof SwiftOperatorReferenceExpression;
    }

    @Override
    public boolean isDeclarationWriteAccess(PsiElement psiElement) {
        return true;
    }

    @Override
    public Access getReferenceAccess(PsiElement psiElement, PsiReference psiReference) {
        if (psiReference == null) return null;
        assert (psiReference instanceof SwiftReferenceExpression);
        SwiftPsiElement parent = (SwiftPsiElement) ((SwiftReferenceExpression) psiReference).getParent();
        if (parent instanceof SwiftOptionalChainingExpression || parent instanceof SwiftForcedValueExpression) {
            parent = (SwiftPsiElement) parent.getParent();
        }

        if (parent instanceof SwiftReferenceExpression) {
            return Access.Read;
        }

        if (parent instanceof SwiftArgument) {
            return Access.Read;
        }

        if (parent instanceof SwiftInitializer) {
            return Access.Read;
        }

        if (parent instanceof SwiftBinaryExpression) {
            return getBinaryExpressionAccess((SwiftBinaryExpression) parent, psiReference.getElement());
        }

        if (parent instanceof SwiftStatement) {
            return Access.Read;
        }
        return null;
    }

    @Override
    public Access getExpressionAccess(PsiElement psiElement) {
        return Access.Read;  // UNUSED BY SWIFT
    }

    private Access getBinaryExpressionAccess(SwiftBinaryExpression expr, PsiElement element) {
        Node root = expr.getBinaryTree().getRoot();
        Access result = Access.Read;

        if (root instanceof BinaryNode) {
            SwiftBinaryOperator operatorPsi = ((BinaryNode) root).getOperatorPsi();
            if (operatorPsi.getText().equals("=")) {
                result = Access.Write;
            } else if (operatorPsi.getText().length() == 2 && operatorPsi.getText().charAt(1) == '=' && !(operatorPsi.getText().charAt(0) == '=')) {
                result = Access.ReadWrite;
            }

            Node left = ((BinaryNode) root).getLeft();

            Node elementNode = expr.getBinaryTree().findNode(element);
            if (elementNode == null) return Access.Read;

            do {
                if (elementNode == left || elementNode.getParent() == left) {
                    return result;
                }
                elementNode = elementNode.getParent();
            } while (elementNode != null);
        }

        return Access.Read;
    }
}
