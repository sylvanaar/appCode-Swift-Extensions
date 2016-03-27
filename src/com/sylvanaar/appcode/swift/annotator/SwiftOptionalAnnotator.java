package com.sylvanaar.appcode.swift.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
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

        if (type instanceof  SwiftImplicitlyUnwrappedType) {
            addSemanticHighlight(re, IMPLICIT_OPTIONAL);
        } else if (type instanceof SwiftOptionalType) {
            addSemanticHighlight(re, OPTIONAL);
        }
    }

    @Override
    public void visitIdentifierPattern(@NotNull SwiftIdentifierPattern id) {
        SwiftType type = id.getSwiftType();

        if (type instanceof SwiftImplicitlyUnwrappedType) {
            addSemanticHighlight(id, IMPLICIT_OPTIONAL);
        } else if (type instanceof SwiftOptionalType) {
            addSemanticHighlight(id, OPTIONAL);
        }
    }


    @Override
    public void visitOptionalChainingExpression(@NotNull SwiftOptionalChainingExpression o) {
        addSemanticHighlight(o.getLastChild(), OPTIONAL_CHAIN_OPERATOR);
    }

    @Override
    public void visitForcedValueExpression(@NotNull SwiftForcedValueExpression o) {
        addSemanticHighlight(o.getLastChild(), FORCE_UNWRAP_OPERATOR);
    }

    private void addSemanticHighlight(PsiElement id, TextAttributesKey key) {
        myHolder.createInfoAnnotation(id.getTextRange(), null).setTextAttributes(key);
    }
}
