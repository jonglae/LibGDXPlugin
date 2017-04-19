// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResources;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkinResourcesImpl extends SkinElementImpl implements SkinResources {

  public SkinResourcesImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitResources(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SkinResource> getResourceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SkinResource.class);
  }

}
