package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.removeDollarFromClassName
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

/*
 * Copyright 2017 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class SkinDuplicateResourceNameInspection: SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.duplicate.resource.description")

  override fun getID() = "LibGDXSkinDuplicateResource"

  override fun getDisplayName() = message("skin.inspection.duplicate.resource.display.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object: SkinElementVisitor() {

    override fun visitResource(skinResource: SkinResource) {
      val className = skinResource.classSpecification?.classNameAsString ?: return

      (skinResource.containingFile as? SkinFile)?.getResources(className, skinResource.name)?.let { resources ->
        if (resources.size > 1) {
          holder.registerProblem(skinResource.resourceName, message("skin.inspection.duplicate.resource.message", skinResource.name, className.removeDollarFromClassName()))
        }
      }

    }

  }

}