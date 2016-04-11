package com.sylvanaar.appcode.swift.inspections.style;

import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.swift.psi.SwiftClosureExpression;
import com.jetbrains.swift.psi.SwiftReturnStatement;
import com.jetbrains.swift.psi.SwiftStatement;
import com.jetbrains.swift.psi.SwiftVisitor;
import com.sylvanaar.appcode.swift.inspections.SwiftInspectionsBundle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by sylvanaar on 4/11/16.
 */
public class SwiftUnneededReturnInspection extends LocalInspectionTool {
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new SwiftVisitor() {
            @Override
            public void visitReturnStatement(@NotNull SwiftReturnStatement returnStatement) {
                PsiElement closure = returnStatement.getParent();

                if (closure instanceof SwiftClosureExpression) {
                    List<SwiftStatement> statements = ((SwiftClosureExpression) closure).getStatements();
                    if (statements.size() == 1) {
                        PsiElement returnKeyword = returnStatement.getFirstChild();

                        holder.registerProblem(returnKeyword, SwiftInspectionsBundle.message("unneeded.return"), new ReturnQuickFix());
                    }
                }
            }
        };
    }

    private class ReturnQuickFix extends LocalQuickFixBase {
        ReturnQuickFix() {
            super("SwiftUnneededReturn", SwiftInspectionsBundle.message("unneeded.return.quickfix.family.name"));
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            descriptor.getPsiElement().delete();
        }
    }
}
