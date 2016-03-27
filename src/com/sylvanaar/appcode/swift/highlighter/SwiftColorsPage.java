package com.sylvanaar.appcode.swift.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.jetbrains.swift.SwiftLanguage;
import com.sylvanaar.appcode.swift.SwiftBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Created by jon on 3/27/16.
 */
public class SwiftColorsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
            new AttributesDescriptor(SwiftBundle.message("color.settings.optional"), SwiftHighlightingData.OPTIONAL),
            new AttributesDescriptor(SwiftBundle.message("color.settings.implicity.unwrapped.optional"), SwiftHighlightingData.IMPLICIT_OPTIONAL),
            new AttributesDescriptor(SwiftBundle.message("color.settings.optional.chain.operator"), SwiftHighlightingData.OPTIONAL_CHAIN_OPERATOR),
            new AttributesDescriptor(SwiftBundle.message("color.settings.optional.force.unwrap.operator"), SwiftHighlightingData.FORCE_UNWRAP_OPERATOR)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return SyntaxHighlighterFactory.getSyntaxHighlighter(SwiftLanguage.INSTANCE, null, null);
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "class Foo {}";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return new ColorDescriptor[0];
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Swift-Ext";
    }
}
