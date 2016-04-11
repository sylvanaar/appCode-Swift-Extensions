package com.sylvanaar.appcode.swift.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.jetbrains.swift.psi.*;
import com.jetbrains.swift.psi.types.SwiftImplicitlyUnwrappedType;
import com.jetbrains.swift.psi.types.SwiftOptionalType;
import com.jetbrains.swift.psi.types.SwiftType;
import org.jetbrains.annotations.NotNull;

import static com.sylvanaar.appcode.swift.highlighter.SwiftHighlightingData.*;

/**
 * Created by Sylvanaar on 3/25/16.
 */
public class SwiftOptionalAnnotator extends SwiftVisitor implements Annotator {
    private static final Logger log = Logger.getInstance(SwiftOptionalAnnotator.class);

    private AnnotationHolder myHolder = null;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof SwiftPsiElement) {
            myHolder = holder;
            element.accept(this);
            myHolder = null;
        }
    }

    @Override
    public void visitReferenceExpression(@NotNull SwiftReferenceExpression re) {
        SwiftType type = re.getType().resolveType();

        TextRange range = re.getTextRange().cutOut(re.getRangeInElement());

        if (type instanceof  SwiftImplicitlyUnwrappedType) {
            addSemanticHighlight(range, IMPLICIT_OPTIONAL);
        } else if (type instanceof SwiftOptionalType) {
            addSemanticHighlight(range, OPTIONAL);
        }
    }

    @Override
    public void visitIdentifierPattern(@NotNull SwiftIdentifierPattern id) {
        SwiftType type = id.getSwiftType();

        if (type instanceof SwiftImplicitlyUnwrappedType) {
            addSemanticHighlight(id.getTextRange(), IMPLICIT_OPTIONAL);
        } else if (type instanceof SwiftOptionalType) {
            addSemanticHighlight(id.getTextRange(), OPTIONAL);
        }
    }


    @Override
    public void visitOptionalChainingExpression(@NotNull SwiftOptionalChainingExpression o) {
        addSemanticHighlight(o.getLastChild().getTextRange(), OPTIONAL_CHAIN_OPERATOR);
    }

    @Override
    public void visitForcedValueExpression(@NotNull SwiftForcedValueExpression o) {
        addSemanticHighlight(o.getLastChild().getTextRange(), FORCE_UNWRAP_OPERATOR);
    }

    private void addSemanticHighlight(TextRange range, TextAttributesKey key) {
        myHolder.createInfoAnnotation(range, null).setTextAttributes(key);
    }
}
