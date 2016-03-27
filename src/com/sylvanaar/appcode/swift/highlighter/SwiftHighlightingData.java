package com.sylvanaar.appcode.swift.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NonNls;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.OPERATION_SIGN;

/**
 * Created by sylvanaar on 3/27/16.
 */
public class SwiftHighlightingData  {
    @NonNls
    private static final String OPTIONAL_ID            = "SWIFTEXT_OPTIONAL";
    @NonNls
    private static final String IMPLICIT_OPTIONAL_ID   = "SWIFTEXT_IMPLICIT_OPTIONAL";
    @NonNls
    private static final String OPTIONAL_CHAIN_ID      = "SWIFTEXT_OPTIONAL_CHAINING_OPERATOR";
    @NonNls
    private static final String FORCE_UNWRAP_ID        = "SWIFTEXT_FORCE_UNWRAP_OPERATOR";


    public static final TextAttributesKey OPTIONAL = TextAttributesKey.createTextAttributesKey(OPTIONAL_ID);
    public static final TextAttributesKey IMPLICIT_OPTIONAL = TextAttributesKey.createTextAttributesKey(IMPLICIT_OPTIONAL_ID, OPTIONAL);
    public static final TextAttributesKey OPTIONAL_CHAIN_OPERATOR = TextAttributesKey.createTextAttributesKey(OPTIONAL_CHAIN_ID, OPERATION_SIGN);
    public static final TextAttributesKey FORCE_UNWRAP_OPERATOR = TextAttributesKey.createTextAttributesKey(FORCE_UNWRAP_ID, OPERATION_SIGN);
}
