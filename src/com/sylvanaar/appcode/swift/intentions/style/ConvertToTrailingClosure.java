package com.sylvanaar.appcode.swift.intentions.style;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.swift.psi.*;
import com.sylvanaar.appcode.swift.intentions.SwiftIntentionsBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by jon on 4/9/16.
 */
public class ConvertToTrailingClosure extends BaseIntentionAction {
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return SwiftIntentionsBundle.message("convert.to.trailing.closure.family.name");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        if(file instanceof SwiftFile) {
            SwiftClosureExpression closure = PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), SwiftClosureExpression.class);
            if (closure == null) {
                return false;
            }

            SwiftArgument argument = PsiTreeUtil.getParentOfType(closure, SwiftArgument.class);
            if (argument == null) {
                return false;
            }

            SwiftArgumentList argumentList = PsiTreeUtil.getParentOfType(argument, SwiftArgumentList.class);
            if (argumentList == null) {
                return false;
            }

            List<SwiftArgument> arguments = argumentList.getArgumentList();
            if (arguments.isEmpty() || arguments.get(arguments.size()-1) != argument) {
                return false;
            }

            SwiftCallExpression call = PsiTreeUtil.getParentOfType(argument, SwiftCallExpression.class);
            if (call == null || PsiTreeUtil.hasErrorElements(call)) {
                return false;
            }

            setText(getFamilyName());
            return true;
        }
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        SwiftClosureExpression closure = PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), SwiftClosureExpression.class);
        if (closure == null) {
            throw new IncorrectOperationException("Not a closure");
        }

        SwiftArgument argument = PsiTreeUtil.getParentOfType(closure, SwiftArgument.class);
        if (argument == null) {
            throw new IncorrectOperationException("Not an argument");
        }

        SwiftArgumentList argumentList = PsiTreeUtil.getParentOfType(closure, SwiftArgumentList.class);
        if (argumentList == null) {
            throw new IncorrectOperationException("No argument list");
        }

        boolean removeParens = argumentList.getArgumentList().size() == 1;

        SwiftCallExpression call = PsiTreeUtil.getParentOfType(argumentList, SwiftCallExpression.class);
        if (call == null) {
            throw new IncorrectOperationException("Not in function call");
        }

        PsiElement lastChild = call.getLastChild();

        if (!lastChild.getText().equals(")")) {
            throw new IncorrectOperationException("Expected trailing parenthesis");
        }

        call.addAfter(closure, lastChild);

        // Need to remove the last argument, and place the closure after the closing paren, but inside the call
        PsiElement element = argument.getPrevSibling();
        while (element != null) {
            if (element.getText().equals(",")) {
                break;
            }
            element = element.getPrevSibling();
        }
        if (element != null) {
            argumentList.deleteChildRange(element, argument.getPrevSibling());
        }
        
        argument.delete();

        // Finally, if there are no arguments in the call, remove the parens
        if (removeParens) {
            element = call.getFirstChild();
            PsiElement openParen = null;
            PsiElement closeParen = null;
            while (element != null) {
                if (element.getText().equals("(")) {
                    openParen = element;
                }
                if (element.getText().equals(")")) {
                    closeParen = element;
                }
                element = element.getNextSibling();
            }

            call.deleteChildRange(openParen, closeParen);
        }

    }
}
