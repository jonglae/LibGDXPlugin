package com.gmail.blueboxware.libgdxplugin.inspections.kotlin

import com.gmail.blueboxware.libgdxplugin.filetypes.properties.GDXPropertyReference
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.psiUtil.plainContent


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
class KotlinInvalidPropertyKeyInspection: LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("invalid.property.key.inspection.html.description")

  override fun getID() = "LibGDXInvalidPropertyKey"

  override fun getDisplayName() = message("invalid.property.key.inspection")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitStringTemplateExpression(expression: KtStringTemplateExpression) {

      expression.references.filter { it is GDXPropertyReference }.let { references ->
        if (references.isEmpty()) {
          return
        }
        references.forEach { reference ->
          if ((reference as? GDXPropertyReference)?.multiResolve(false)?.isEmpty() != true) {
            return
          }
        }
      }

      holder.registerProblem(expression, message("invalid.property.key.inspection.problem.descriptor", expression.plainContent))

    }

  }

}