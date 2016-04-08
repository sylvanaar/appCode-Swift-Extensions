package com.sylvanaar.appcode.swift.intentions.style;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.swift.psi.SwiftConditionClause;
import com.jetbrains.swift.psi.SwiftFile;
import com.jetbrains.swift.psi.SwiftParenthesizedExpression;
import com.sylvanaar.appcode.swift.intentions.SwiftIntentionsBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static com.jetbrains.swift.SwiftParserTypes.ARGUMENT;
import static com.jetbrains.swift.SwiftParserTypes.ARGUMENT_LIST;

/**
 * Created by jon on 4/8/16.
 */
public class RemoveUneededParenthesis extends BaseIntentionAction {
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return SwiftIntentionsBundle.message("remove.unecessary.parenthesis.family.name");
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        if(file instanceof SwiftFile) {
            SwiftParenthesizedExpression parens = PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), SwiftParenthesizedExpression.class);
            if (parens == null) {
                return false;
            }

            PsiElement conditional = parens.getParent();
            if (conditional == null) {
                return false;
            }

            ASTNode argumentList = parens.getNode().findChildByType(ARGUMENT_LIST);
            if (argumentList == null) {
                return false;
            }

            ASTNode argument = argumentList.findChildByType(ARGUMENT);
            if (argument == null) {
                return false;
            }

            if (conditional instanceof SwiftConditionClause) {
                setText(SwiftIntentionsBundle.message("remove.unecessary.parenthesis"));
                return true;
            }
        }
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        SwiftParenthesizedExpression parens = PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), SwiftParenthesizedExpression.class);
        if (parens == null) {
            throw new IncorrectOperationException("No Parenthesized Expression Found");
        }

        PsiElement conditional = parens.getParent();
        if (conditional == null) {
            throw new IncorrectOperationException("Conditional Clause Not Found");
        }

        ASTNode argumentList = parens.getNode().findChildByType(ARGUMENT_LIST);
        if (argumentList == null) {
            throw new IncorrectOperationException("Argument List Not Found");
        }

        ASTNode argument = argumentList.findChildByType(ARGUMENT);
        if (argument == null) {
            throw new IncorrectOperationException("Argument Not Found");
        }

        ASTNode condition = argument.getFirstChildNode();

        PsiElement conditionPsi = condition.getPsi();

        parens.replace(conditionPsi);
    }

}
