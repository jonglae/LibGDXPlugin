package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.util.PathUtil
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.intentions.calleeName
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

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
internal fun PsiElement.findParentWhichIsChildOf(childOf: PsiElement): PsiElement? {
  var element: PsiElement? = this
  while (element != null && element.parent != childOf) {
    element = element.parent
  }
  return element
}

internal fun String.removeDollarFromClassName(): String = split(".", "$").joinToString(".")


internal fun PsiLiteralExpression.asString(): String? = (value as? String)?.toString()

internal fun PsiClass.putDollarInInnerClassName(): String =
        containingClass?.let {
          it.putDollarInInnerClassName() + "$" + name
        } ?: qualifiedName ?: ""

internal fun Project.getPsiFile(filename: String): PsiFile? {

  getProjectBaseDir()?.findFileByRelativePath(PathUtil.toSystemIndependentName(filename))?.let { virtualFile ->
    return PsiManager.getInstance(this).findFile(virtualFile)
  }

  return null

}

internal fun PsiClass.allStaticInnerClasses(): List<PsiClass> {
  val result = mutableListOf(this)

  for (innerClass in innerClasses) {
    if (innerClass.hasModifierProperty(PsiModifier.STATIC) && (innerClass !is KtLightClass || innerClass.kotlinOrigin !is KtObjectDeclaration)) {
      result.addAll(innerClass.allStaticInnerClasses())
    }
  }

  return result
}

internal fun PsiMethodCallExpression.resolveCall(): Pair<PsiClass, PsiMethod>? {

  methodExpression.qualifierExpression?.let { qualifierExpression ->

    (qualifierExpression.type as? PsiClassType)?.resolve().let { it ->

      (it ?: (qualifierExpression as? PsiReference)?.resolve() as? PsiClass)?.let { clazz ->

        resolveMethod()?.let { method ->
          return Pair(clazz, method)
        }

      }

    }

  }

  return null

}

internal fun PsiMethodCallExpression.resolveCallToStrings(): Pair<String, String>? =
        resolveCall()?.let {
          it.first.qualifiedName?.let { className -> Pair(className, it.second.name) }
        }

internal fun KtQualifiedExpression.resolveCall(): Pair<DeclarationDescriptor, String>? {

  var receiverType: DeclarationDescriptor? = analyze().getType(receiverExpression)?.constructor?.declarationDescriptor

  if (receiverType == null) {
    // static method call?
    receiverType = receiverExpression.getReferenceTargets(analyze()).firstOrNull() ?: return null
  }

  val methodName = calleeName ?: return null

  return Pair(receiverType, methodName)

}

internal fun KtQualifiedExpression.resolveCallToStrings(): Pair<String, String>? =
        resolveCall()?.let {
          Pair(it.first.fqNameSafe.asString(), it.second)
        }

internal fun KtCallExpression.resolveCall(): Pair<ClassDescriptor, KtNameReferenceExpression>? {

  (context as? KtQualifiedExpression)?.let { dotExpression ->

    var receiverType: ClassDescriptor? = dotExpression.analyze().getType(dotExpression.receiverExpression)?.constructor?.declarationDescriptor as? ClassDescriptor

    if (receiverType == null) {
      // static method call?
      receiverType = dotExpression.receiverExpression.getReferenceTargets(dotExpression.analyze()).firstOrNull() as? ClassDescriptor ?: return null
    }

    val methodName = calleeExpression as? KtNameReferenceExpression ?: return null

    return Pair(receiverType, methodName)

  }

  return null

}

internal fun KtCallExpression.resolveCallToStrings(): Pair<String, String>? =
        resolveCall()?.let {
          Pair(it.first.fqNameSafe.asString(), it.second.getReferencedName())
        }